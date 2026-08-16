package com.axiom.vcam.hook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.Image
import android.media.ImageReader
import android.media.ImageWriter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

/**
 * CameraHook — injected into every cloned app process by BXposed/BlackBox.
 *
 * Hooks both Camera1 (android.hardware.Camera) and Camera2
 * (android.hardware.camera2) paths to replace the real camera feed with
 * either a video (MediaPlayer → surface) or a static image (NV21 / ImageWriter).
 *
 * IPC back to the host app (com.axiom.vcam) uses a shared ContentProvider URI
 * to fetch the current video/image MP4.
 */
class CameraHook : IXposedHookLoadPackage {

    companion object {
        private const val TAG          = "VCamHook"
        private const val PROVIDER_URI = "content://com.axiom.vcam.videoprovider"

        // Shared state visible across hook callbacks in this process
        @Volatile var isActive         = false
        @Volatile var isImageMode      = false

        // Camera2 surfaces captured during session setup
        @Volatile var previewSurface:  Surface? = null
        @Volatile var readerSurface:   Surface? = null
        @Volatile var virtualSurface:  Surface? = null

        // Camera1 surfaces
        @Volatile var c1SurfaceTex:    SurfaceTexture? = null
        @Volatile var c1Holder:        SurfaceHolder?  = null

        // Image mode NV21 buffer refreshed at 30 fps from bitmap in host process
        @Volatile var nv21Buffer:      ByteArray = ByteArray(1)

        // Running MediaPlayer / decoder
        @Volatile var mediaPlayer:     MediaPlayer? = null
        @Volatile var isInitialising   = false

        // ImageWriter targets (Camera2 apps that expose ImageReader surfaces)
        private val imageWriters = ConcurrentHashMap<ImageWriter, Triple<Int,Int,Int>>()
        private val writerRunning = AtomicBoolean(false)
        private val writerExecutor = Executors.newSingleThreadExecutor()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Entry point
    // ─────────────────────────────────────────────────────────────────────────

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "android") return
        Log.d(TAG, "hooked into ${lpparam.packageName}")
        hookCamera2(lpparam.classLoader)
        hookCamera1(lpparam.classLoader)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Camera2 hooks
    // ─────────────────────────────────────────────────────────────────────────

    private fun hookCamera2(cl: ClassLoader) {
        // Hook CameraDevice.createCaptureSession (legacy path)
        hookCreateCaptureSessionLegacy(cl)
        // Hook CameraDevice.createCaptureSession (SessionConfiguration path, API 28+)
        hookCreateCaptureSessionNew(cl)
        // Hook CaptureSession.setRepeatingRequest — intercept preview requests
        hookSetRepeatingRequest(cl)
        // Hook ImageReader.acquireLatestImage — intercept still capture
        hookImageReaderAcquire(cl)
    }

    private fun hookCreateCaptureSessionLegacy(cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                CameraDevice::class.java,
                "createCaptureSession",
                List::class.java,
                CameraCaptureSession.StateCallback::class.java,
                Handler::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        @Suppress("UNCHECKED_CAST")
                        val surfaces = param.args[0] as? List<Surface> ?: return
                        captureSurfaces(surfaces)
                    }
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        startMediaPlayer(previewSurface)
                        readerSurface?.let { startReaderFeed(it) }
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookLegacySession: ${e.message}") }
    }

    private fun hookCreateCaptureSessionNew(cl: ClassLoader) {
        if (Build.VERSION.SDK_INT < 28) return
        try {
            XposedHelpers.findAndHookMethod(
                CameraDevice::class.java,
                "createCaptureSession",
                SessionConfiguration::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        val cfg = param.args[0] as? SessionConfiguration ?: return
                        val surfaces = cfg.outputConfigurations.mapNotNull { it.surface }
                        captureSurfaces(surfaces)
                    }
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        startMediaPlayer(previewSurface)
                        readerSurface?.let { startReaderFeed(it) }
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookNewSession: ${e.message}") }
    }

    private fun hookSetRepeatingRequest(cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                CameraCaptureSession::class.java,
                "setRepeatingRequest",
                CaptureRequest::class.java,
                CameraCaptureSession.CaptureCallback::class.java,
                Handler::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        // Ensure MediaPlayer is running on the preview surface
                        val surf = previewSurface?.takeIf { it.isValid } ?: return
                        if (mediaPlayer?.isPlaying != true && !isInitialising) {
                            startMediaPlayer(surf)
                        }
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookRepeating: ${e.message}") }
    }

    /**
     * Hook ImageReader.acquireLatestImage — called when the app does a still
     * capture. We return an Image populated with our bitmap NV21 data so the
     * shutter captures what's shown, not the real camera.
     *
     * The hook replaces the real Image data by writing NV21 into its planes
     * before returning — same approach as vcamsx's ImageWriter path.
     */
    private fun hookImageReaderAcquire(cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                ImageReader::class.java,
                "acquireLatestImage",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        val img = param.result as? Image ?: return
                        val buf = nv21Buffer.takeIf { it.size > 1 } ?: return
                        try { overwriteImageNv21(img, buf) } catch (_: Exception) {}
                    }
                }
            )
            // Also hook acquireNextImage
            XposedHelpers.findAndHookMethod(
                ImageReader::class.java,
                "acquireNextImage",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        val img = param.result as? Image ?: return
                        val buf = nv21Buffer.takeIf { it.size > 1 } ?: return
                        try { overwriteImageNv21(img, buf) } catch (_: Exception) {}
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookAcquire: ${e.message}") }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Camera1 hooks
    // ─────────────────────────────────────────────────────────────────────────

    private fun hookCamera1(cl: ClassLoader) {
        hookC1SetPreviewTexture(cl)
        hookC1SetPreviewDisplay(cl)
        hookC1PreviewCallback(cl)
        hookC1TakePicture(cl)
    }

    private fun hookC1SetPreviewTexture(cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                Camera::class.java, "setPreviewTexture", SurfaceTexture::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        val st = param.args[0] as? SurfaceTexture ?: return
                        c1SurfaceTex = st
                        startMediaPlayer(Surface(st))
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookC1Tex: ${e.message}") }
    }

    private fun hookC1SetPreviewDisplay(cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                Camera::class.java, "setPreviewDisplay", SurfaceHolder::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        val holder = param.args[0] as? SurfaceHolder ?: return
                        c1Holder = holder
                        holder.surface?.takeIf { it.isValid }?.let { startMediaPlayer(it) }
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookC1Display: ${e.message}") }
    }

    private fun hookC1PreviewCallback(cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                Camera::class.java, "setPreviewCallback", Camera.PreviewCallback::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val cb = param.args[0] as? Camera.PreviewCallback ?: return
                        // Wrap the callback so we can intercept NV21 data
                        param.args[0] = Camera.PreviewCallback { data, camera ->
                            if (isActive) {
                                val buf = nv21Buffer
                                if (buf.size > 1 && data != null)
                                    System.arraycopy(buf, 0, data, 0, minOf(buf.size, data.size))
                            }
                            cb.onPreviewFrame(data, camera)
                        }
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookC1Preview: ${e.message}") }
    }

    /**
     * Hook Camera1 takePicture — on shutter, inject our bitmap as the JPEG
     * by overwriting the raw/jpeg data bytes before delivering to the app.
     */
    private fun hookC1TakePicture(cl: ClassLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                Camera::class.java,
                "takePicture",
                Camera.ShutterCallback::class.java,
                Camera.PictureCallback::class.java,
                Camera.PictureCallback::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!isActive) return
                        val rawCb  = param.args[1] as? Camera.PictureCallback
                        val jpegCb = param.args[2] as? Camera.PictureCallback ?: return

                        // Replace jpeg callback with one that substitutes our bitmap bytes
                        param.args[2] = Camera.PictureCallback { _, camera ->
                            val jpeg = currentBitmapAsJpeg() ?: run { jpegCb.onPictureTaken(null, camera); return@PictureCallback }
                            jpegCb.onPictureTaken(jpeg, camera)
                        }
                    }
                }
            )
        } catch (e: Throwable) { Log.e(TAG, "hookC1TakePicture: ${e.message}") }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Media player
    // ─────────────────────────────────────────────────────────────────────────

    private fun startMediaPlayer(surface: Surface?) {
        if (surface == null || !surface.isValid) return
        if (isInitialising) return
        isInitialising = true

        Handler(Looper.getMainLooper()).post {
            try {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    isLooping = true
                    setSurface(surface)
                    setDataSource(surface.toString().let {
                        // Use the content URI — VideoProvider in host serves the MP4
                        PROVIDER_URI
                    })
                    setOnPreparedListener { player ->
                        isInitialising = false
                        player.start()
                        Log.d(TAG, "MediaPlayer playing on $surface")
                    }
                    setOnErrorListener { _, w, e ->
                        Log.e(TAG, "MP error $w/$e"); isInitialising = false; false
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                Log.e(TAG, "startMediaPlayer: ${e.message}"); isInitialising = false
            }
        }
    }

    private fun startReaderFeed(surface: Surface) {
        if (!surface.isValid) return
        if (Build.VERSION.SDK_INT < 23) return
        try {
            val writer = ImageWriter.newInstance(surface, 3)
            imageWriters[writer] = Triple(ImageFormat.YUV_420_888, 720, 1280)
            startWriterLoop()
        } catch (e: Exception) { Log.e(TAG, "startReaderFeed: ${e.message}") }
    }

    private fun startWriterLoop() {
        if (!writerRunning.compareAndSet(false, true)) return
        writerExecutor.submit {
            while (writerRunning.get()) {
                val buf = nv21Buffer
                if (isActive && buf.size > 1) {
                    imageWriters.forEach { (writer, info) ->
                        val (_, w, h) = info
                        try {
                            val img = writer.dequeueInputImage() ?: return@forEach
                            writeNv21ToImage(img, buf, w, h)
                            writer.queueInputImage(img)
                        } catch (_: Exception) {}
                    }
                }
                try { Thread.sleep(33) } catch (_: InterruptedException) { return@submit }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NV21 helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun overwriteImageNv21(img: Image, nv21: ByteArray) {
        val w = img.width; val h = img.height
        val planes = img.planes; val ySize = w * h
        val yBuf = planes[0].buffer; val yStr = planes[0].rowStride
        if (yStr == w) {
            yBuf.put(nv21, 0, minOf(ySize, yBuf.remaining(), nv21.size))
        } else {
            for (r in 0 until h) {
                yBuf.position(r * yStr)
                val off = r * w; val len = minOf(w, yBuf.remaining(), nv21.size - off)
                if (len <= 0) break; yBuf.put(nv21, off, len)
            }
        }
        if (planes.size < 3) return
        val uBuf = planes[1].buffer; val vBuf = planes[2].buffer
        val uStr = planes[1].rowStride; val uPix = planes[1].pixelStride
        for (r in 0 until h / 2) {
            for (c in 0 until w / 2) {
                val src = ySize + r * w + c * 2; if (src + 1 >= nv21.size) break
                val dst = r * uStr + c * uPix
                if (dst < uBuf.capacity()) { uBuf.position(dst); uBuf.put(nv21[src + 1]) }
                if (dst < vBuf.capacity()) { vBuf.position(dst); vBuf.put(nv21[src]) }
            }
        }
    }

    private fun writeNv21ToImage(img: Image, nv21: ByteArray, w: Int, h: Int) =
        overwriteImageNv21(img, nv21)

    private fun currentBitmapAsJpeg(): ByteArray? {
        // Fetch current bitmap NV21 snapshot — encoded as JPEG for Camera1 takePicture
        val buf = nv21Buffer.takeIf { it.size > 1 } ?: return null
        return try {
            val w = 720; val h = 1280
            val yuv = android.graphics.YuvImage(buf, ImageFormat.NV21, w, h, null)
            val out = java.io.ByteArrayOutputStream()
            yuv.compressToJpeg(android.graphics.Rect(0, 0, w, h), 95, out)
            out.toByteArray()
        } catch (e: Exception) { null }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Surface helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun captureSurfaces(surfaces: List<Surface>) {
        for (s in surfaces) {
            if (!s.isValid) continue
            // Heuristic: ImageReader surfaces are smaller, preview surfaces are larger
            // We treat the first valid surface as preview
            if (previewSurface == null) previewSurface = s
            else readerSurface = s
        }
        Log.d(TAG, "captured preview=$previewSurface reader=$readerSurface")
    }
}
