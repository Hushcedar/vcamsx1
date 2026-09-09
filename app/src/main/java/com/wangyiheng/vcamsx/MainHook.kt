package com.wangyiheng.vcamsx

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.Toast
import com.crossbowffs.remotepreferences.RemotePreferences
import com.google.gson.Gson
import com.wangyiheng.vcamsx.VideoControlReceiver
import com.wangyiheng.vcamsx.data.models.VideoStatues
import com.wangyiheng.vcamsx.utils.AudioInjector
import com.wangyiheng.vcamsx.utils.ImagePlayer
import com.wangyiheng.vcamsx.utils.InfoProcesser
import com.wangyiheng.vcamsx.utils.OutputImageFormat
import com.wangyiheng.vcamsx.utils.VideoPlayer
import com.wangyiheng.vcamsx.utils.VideoToFrames
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class MainHook : IXposedHookLoadPackage {

    private var c2_state_callback: CameraDevice.StateCallback? = null
    private val nonPreviewSurfaces:    MutableSet<Surface>  = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedDeviceClasses:   MutableSet<String>   = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedCallbackClasses: MutableSet<String>   = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedReaderClasses:   MutableSet<String>   = Collections.newSetFromMap(ConcurrentHashMap())

    // Track ImageReader → (width, height) for MYCAM-style correct sizing
    private val readerSizes: MutableMap<ImageReader, Pair<Int,Int>> = ConcurrentHashMap()

    companion object {
        const val TAG = "vcamsx"

        @JvmField var context:                            Context?        = null
        @JvmField var original_preview_Surface:           Surface?        = null
        @JvmField var original_c1_preview_SurfaceTexture: SurfaceTexture? = null
        @JvmField var fake_SurfaceTexture:                SurfaceTexture? = null
        @JvmField var c1FakeTexture:                      SurfaceTexture? = null
        @JvmField var c1FakeSurface:                      Surface?        = null
        @JvmField var oriHolder:                          SurfaceHolder?  = null
        @JvmField var origin_preview_camera:              Camera?         = null
        @JvmField var c2_reader_Surfcae:                  Surface?        = null
        @JvmField var c2VirtualSurfaceTexture:            SurfaceTexture? = null
        @JvmField var c2_virtual_surface:                 Surface?        = null
        @JvmField var sessionConfiguration:               SessionConfiguration? = null
        @JvmField var outputConfiguration:                OutputConfiguration?  = null
        @JvmField var fake_sessionConfiguration:          SessionConfiguration? = null
        @JvmField var isPlaying    = false
        @JvmField var needRecreate = false
        @JvmField var hw_decode_obj:          VideoToFrames? = null
        @JvmField var mcamera1:               Camera?        = null
        @JvmField var camera_onPreviewFrame:  Camera?        = null
        @JvmField var camera_callback_calss:  Class<*>?      = null
        @Volatile @JvmField var data_buffer:  ByteArray      = byteArrayOf()
        @Volatile @JvmField var audioEnabled  = false

        // MYCAM-style: track last known capture dims from ImageReader (not Image)
        @Volatile @JvmField var lastReaderW   = 1920
        @Volatile @JvmField var lastReaderH   = 1080

        fun makeFakeST(old: SurfaceTexture?): SurfaceTexture {
            old?.release()
            return if (Build.VERSION.SDK_INT >= 26) SurfaceTexture(false)
            else SurfaceTexture(10)
        }

        /** Injection active if video OR image is enabled */
        fun isInjectionActive(status: VideoStatues?): Boolean =
            status?.isVideoEnable == true || status?.isImageEnabled == true || ImagePlayer.isActive.value

        fun readStatusDirect(ctx: Context): VideoStatues? {
            return try {
                val prefs = RemotePreferences(
                    ctx, "com.wangyiheng.vcamsx.preferences", "main_prefs", true)
                val json = prefs.getString("videoStatus", null) ?: return null
                Gson().fromJson(json, VideoStatues::class.java)
            } catch (e: Throwable) { XposedBridge.log("$TAG readStatus: $e"); null }
        }

        fun getContextFromActivityThread(): Context? = try {
            val at = Class.forName("android.app.ActivityThread")
            (at.getMethod("currentApplication").invoke(null) as? Application)?.applicationContext
        } catch (_: Throwable) { context }

        fun showToast(ctx: Context, msg: String) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * MYCAM-style JPEG generation:
         * Scales our image to exact capture dimensions, returns JPEG bytes.
         * Uses ImageReader.getWidth()/getHeight() — same as MYCAM's K0().
         * No stretching — fits inside dimensions, centered on black.
         */
        fun generateJpeg(captureW: Int, captureH: Int, bufferCapacity: Int): ByteArray? {
            val bmp = ImagePlayer.currentBitmapSnapshot() ?: return null

            // Scale to fit without stretching — IPMAN/MYCAM approach
            val scale  = minOf(captureW.toFloat() / bmp.width, captureH.toFloat() / bmp.height)
            val dstW   = (bmp.width  * scale).toInt().coerceAtLeast(1)
            val dstH   = (bmp.height * scale).toInt().coerceAtLeast(1)
            val left   = (captureW - dstW) / 2
            val top    = (captureH - dstH) / 2

            val output = Bitmap.createBitmap(captureW, captureH, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            canvas.drawColor(Color.BLACK)  // black background to preserve aspect ratio

            val scaled = if (dstW == bmp.width && dstH == bmp.height) bmp
                         else Bitmap.createScaledBitmap(bmp, dstW, dstH, true)
            canvas.drawBitmap(scaled, left.toFloat(), top.toFloat(),
                Paint().apply { isFilterBitmap = true })
            if (scaled !== bmp) scaled.recycle()

            val out = ByteArrayOutputStream(bufferCapacity.coerceAtLeast(1024))
            output.compress(Bitmap.CompressFormat.JPEG, 95, out)
            output.recycle()
            return out.toByteArray()
        }

        /**
         * MYCAM-style in-place JPEG replace.
         * Uses imageReader.getWidth()/getHeight() — NOT image.getWidth()/getHeight().
         * This is the key fix MYCAM uses to get correct dimensions.
         */
        fun replaceJpegInPlace(imageReader: ImageReader, image: Image): Boolean {
            if (!ImagePlayer.isActive.value) return false
            try {
                val plane = image.planes.getOrNull(0) ?: return false
                val buf   = plane.buffer
                if (buf.isReadOnly) return false
                // MYCAM KEY: use imageReader.width/height, NOT image.width/height
                val w   = imageReader.width.takeIf  { it > 0 } ?: return false
                val h   = imageReader.height.takeIf { it > 0 } ?: return false
                val cap = buf.capacity()
                val bmp = ImagePlayer.currentBitmapSnapshot() ?: return false
                var jpeg = generateJpeg(w, h, cap) ?: return false
                // If too large, try 70% quality
                if (jpeg.size > cap) {
                    val scale   = minOf(w.toFloat()/bmp.width, h.toFloat()/bmp.height)
                    val dstW    = (bmp.width  * scale).toInt().coerceAtLeast(1)
                    val dstH    = (bmp.height * scale).toInt().coerceAtLeast(1)
                    val left    = (w - dstW) / 2
                    val top     = (h - dstH) / 2
                    val outBmp  = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                    val c       = Canvas(outBmp)
                    c.drawColor(Color.BLACK)
                    val sc = Bitmap.createScaledBitmap(bmp, dstW, dstH, true)
                    c.drawBitmap(sc, left.toFloat(), top.toFloat(), null)
                    if (sc !== bmp) sc.recycle()
                    val out70 = ByteArrayOutputStream(cap)
                    outBmp.compress(Bitmap.CompressFormat.JPEG, 70, out70)
                    outBmp.recycle()
                    jpeg = out70.toByteArray()
                    if (jpeg.size > cap) {
                        XposedBridge.log("$TAG JPEG still too large ${jpeg.size}>$cap skip")
                        return false
                    }
                }
                // MYCAM pattern exactly: clear, put, flip
                buf.clear(); buf.put(jpeg); buf.flip()
                XposedBridge.log("$TAG K0 JPEG ${w}x${h} bytes=${jpeg.size} cap=$cap")
                context?.let { showToast(it, "VCamSX: ${bmp.width}×${bmp.height} → ${w}×${h}") }
                return true
            } catch (e: Throwable) {
                XposedBridge.log("$TAG replaceJpeg: ${e.message}")
                return false
            }
        }

        /**
         * Camera1 shutter JPEG replace — uses Camera.Parameters to get picture size
         * Same approach as MYCAM: exact dimensions, no stretch.
         */
        fun replacePictureJpeg(realJpeg: ByteArray?, cam: Camera?): ByteArray? {
            if (!ImagePlayer.isActive.value) return null
            val sz   = try { cam?.parameters?.pictureSize } catch (_: Exception) { null }
            val capW = sz?.width  ?: lastReaderW
            val capH = sz?.height ?: lastReaderH
            val cap  = realJpeg?.size ?: (capW * capH / 4)
            val jpeg = generateJpeg(capW, capH, cap) ?: return null
            XposedBridge.log("$TAG C1 JPEG replaced ${capW}x${capH} bytes=${jpeg.size}")
            context?.let { showToast(it, "VCamSX: Snap ${capW}×${capH}") }
            return jpeg
        }
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.wangyiheng.vcamsx") return
        val proc = lpparam.processName
        if (proc.endsWith(":push")   || proc.endsWith(":remote") ||
            proc.endsWith(":nfc")    || proc.endsWith(":work")) return
        XposedBridge.log("$TAG hook: ${lpparam.packageName} proc=$proc")
        hookAppInit(lpparam)
        hookCamera1(lpparam)
        hookCamera2(lpparam)
        hookImageReader(lpparam)
        hookMediaCodecSurface(lpparam)
        hookMediaRecorder(lpparam)
        hookAudioRecord(lpparam)
    }

    private fun filterAndSwapSurfaceList(original: List<Surface>): List<Surface> {
        val virt   = c2_virtual_surface ?: return original
        val result = ArrayList<Surface>(original.size)
        var replaced = false
        for (s in original) {
            when {
                nonPreviewSurfaces.contains(s) -> result.add(s)
                !replaced -> {
                    original_preview_Surface = s
                    result.add(virt); replaced = true
                }
                else -> result.add(s)
            }
        }
        if (!replaced) result.add(0, virt)
        return result
    }

    // ══════════════════════════════════════════════════════════════════════════
    // APP INIT
    // ══════════════════════════════════════════════════════════════════════════

    private fun hookAppInit(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod(
                "android.app.Instrumentation", lpparam.classLoader,
                "callApplicationOnCreate", Application::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val app = param.args?.firstOrNull() as? Application ?: return
                        val ctx = app.applicationContext
                        if (context == ctx) return
                        try {
                            context = ctx
                            VideoControlReceiver.register(ctx)
                            if (!isPlaying) {
                                isPlaying = true
                                VideoPlayer.initializeTheStateAsWellAsThePlayer()
                            }
                            val status = readStatusDirect(ctx)
                            XposedBridge.log("$TAG appInit proc=${lpparam.processName} " +
                                "video=${status?.isVideoEnable} image=${status?.isImageEnabled}")
                            if (status?.isVideoEnable == true && status.volume) audioEnabled = true
                        } catch (e: Throwable) { XposedBridge.log("$TAG init: $e") }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookAppInit: $e") }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CAMERA 1
    // Fix: isInjectionActive() covers image-only mode
    // ══════════════════════════════════════════════════════════════════════════

    private fun hookCamera1(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "setPreviewTexture", SurfaceTexture::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
                        val st = param.args[0] as? SurfaceTexture ?: return
                        if (st == fake_SurfaceTexture) return
                        if (origin_preview_camera != null && origin_preview_camera == param.thisObject) {
                            param.args[0] = fake_SurfaceTexture; return
                        }
                        origin_preview_camera              = param.thisObject as? Camera
                        original_c1_preview_SurfaceTexture = st
                        fake_SurfaceTexture                = makeFakeST(fake_SurfaceTexture)
                        param.args[0]                      = fake_SurfaceTexture
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.SPT: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "setPreviewDisplay", SurfaceHolder::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
                        mcamera1  = param.thisObject as? Camera
                        oriHolder = param.args[0] as? SurfaceHolder
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.SPD: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "startPreview",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
                        val cam = param.thisObject as? Camera ?: return
                        if (oriHolder != null && cam == mcamera1 && origin_preview_camera == null) {
                            try {
                                origin_preview_camera = cam
                                fake_SurfaceTexture   = makeFakeST(fake_SurfaceTexture)
                                cam.setPreviewTexture(fake_SurfaceTexture)
                            } catch (e: Throwable) {
                                XposedBridge.log("$TAG SPT before start: $e")
                                origin_preview_camera = null
                            }
                        }
                    }
                    override fun afterHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
                        VideoPlayer.c1_camera_play()
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.startPreview: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "setPreviewCallbackWithBuffer", Camera.PreviewCallback::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (InfoProcesser.videoStatus?.isVideoEnable == true && param.args[0] != null)
                            hookPreviewCallback(param)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.PCWB: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "addCallbackBuffer", ByteArray::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        val buf = param.args[0] as? ByteArray ?: return
                        param.args[0] = ByteArray(buf.size)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.ACB: $e") }

        // takePicture — IPMAN/MYCAM style: replace at picture callback level
        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "takePicture",
                Camera.ShutterCallback::class.java, Camera.PictureCallback::class.java,
                Camera.PictureCallback::class.java, Camera.PictureCallback::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        if (param.args[0] != null) hookYUVCb(param)
                        if (param.args[2] != null) hookJPEGCb(param, 2)
                        if (param.args[3] != null) hookJPEGCb(param, 3)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.takePicture: $e") }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CAMERA 2
    // ══════════════════════════════════════════════════════════════════════════

    private fun hookCamera2(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        try {
            XposedHelpers.findAndHookMethod(
                "android.hardware.camera2.CameraManager", cl,
                "openCamera", String::class.java,
                CameraDevice.StateCallback::class.java, Handler::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        intercept2(param.args[1] as? CameraDevice.StateCallback, lpparam)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG openCamera(H): $e") }

        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod(
                    "android.hardware.camera2.CameraManager", cl,
                    "openCamera", String::class.java,
                    java.util.concurrent.Executor::class.java,
                    CameraDevice.StateCallback::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            intercept2(param.args[2] as? CameraDevice.StateCallback, lpparam)
                        }
                    }
                )
            } catch (e: Throwable) { XposedBridge.log("$TAG openCamera(E): $e") }
        }

        try {
            XposedHelpers.findAndHookMethod(
                "android.hardware.camera2.CaptureRequest\$Builder", cl,
                "addTarget", Surface::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val surface = param.args[0] as? Surface ?: return
                        if (surface == c2_virtual_surface) return
                        if (nonPreviewSurfaces.contains(surface)) return
                        val virt = c2_virtual_surface ?: return
                        original_preview_Surface = surface
                        param.args[0] = virt
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG addTarget: $e") }

        try {
            XposedHelpers.findAndHookMethod(
                "android.hardware.camera2.CaptureRequest\$Builder", cl, "build",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        VideoPlayer.camera2Play()
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG build: $e") }

        // Messenger/Facebook fix
        hookCameraDeviceImpl(cl, lpparam)
    }

    private fun hookCameraDeviceImpl(cl: ClassLoader, lpparam: XC_LoadPackage.LoadPackageParam) {
        val implClass = "android.hardware.camera2.impl.CameraDeviceImpl"

        try {
            XposedHelpers.findAndHookMethod(implClass, cl,
                "createCaptureSession",
                List::class.java, CameraCaptureSession.StateCallback::class.java, Handler::class.java,
                object : XC_MethodHook() {
                    @Suppress("UNCHECKED_CAST")
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
                        createVirtualSurface()
                        val original = param.args[0] as? List<Surface> ?: return
                        val previewSurf = original.firstOrNull { !nonPreviewSurfaces.contains(it) }
                        if (previewSurf != null) {
                            original_preview_Surface = previewSurf
                            c2_reader_Surfcae        = previewSurf
                        }
                        param.args[0] = filterAndSwapSurfaceList(original)
                        val origCb = param.args[1] as? CameraCaptureSession.StateCallback
                        param.args[1] = makeSessionCb(origCb, previewSurf)
                        XposedBridge.log("$TAG impl.CS(List) pkg=${lpparam.packageName}")
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG impl.CS(List): $e") }

        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod(implClass, cl,
                    "createCaptureSession", SessionConfiguration::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            InfoProcesser.initStatus()
                            if (!isInjectionActive(InfoProcesser.videoStatus)) return
                            createVirtualSurface()
                            val orig    = param.args[0] as? SessionConfiguration ?: return
                            val virt    = c2_virtual_surface ?: return
                            val outputs = try { orig.outputConfigurations } catch (_: Throwable) { null }
                            val newOuts = ArrayList<OutputConfiguration>()
                            var previewSurf: Surface? = null
                            var replaced = false
                            if (outputs != null) {
                                for (oc in outputs) {
                                    val s = try { oc.surface } catch (_: Throwable) { null }
                                    when {
                                        s != null && nonPreviewSurfaces.contains(s) -> newOuts.add(oc)
                                        !replaced -> {
                                            if (s != null) {
                                                previewSurf              = s
                                                original_preview_Surface = s
                                                c2_reader_Surfcae        = s
                                            }
                                            newOuts.add(OutputConfiguration(virt)); replaced = true
                                        }
                                        else -> newOuts.add(oc)
                                    }
                                }
                            }
                            if (!replaced) newOuts.add(0, OutputConfiguration(virt))
                            val pSurf   = previewSurf
                            val wrapped = makeSessionCb(orig.stateCallback, pSurf)
                            val fake    = SessionConfiguration(orig.sessionType, newOuts, orig.executor, wrapped)
                            fake_sessionConfiguration = fake
                            param.args[0] = fake
                            XposedBridge.log("$TAG impl.CS(SC) pkg=${lpparam.packageName}")
                        }
                    }
                )
            } catch (e: Throwable) { XposedBridge.log("$TAG impl.CS(SC): $e") }
        }
    }

    private fun makeSessionCb(
        original: CameraCaptureSession.StateCallback?,
        previewSurface: Surface? = null
    ): CameraCaptureSession.StateCallback {
        return object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                try { original?.onConfigured(session) } catch (_: Throwable) {}
                try {
                    val s = previewSurface ?: c2_reader_Surfcae ?: original_preview_Surface
                    if (s != null && s.isValid) {
                        // Set original_preview_Surface so camera2Play() works
                        original_preview_Surface = s
                        VideoPlayer.camera2Play()
                        XposedBridge.log("$TAG sessionCb.onConfigured camera2Play s=$s")
                    }
                } catch (e: Throwable) { XposedBridge.log("$TAG sessionCb: $e") }
            }
            override fun onConfigureFailed(session: CameraCaptureSession) {
                try { original?.onConfigureFailed(session) } catch (_: Throwable) {}
            }
            override fun onClosed(session: CameraCaptureSession) {
                try { original?.onClosed(session) } catch (_: Throwable) {}
            }
        }
    }

    private fun intercept2(cb: CameraDevice.StateCallback?, lpparam: XC_LoadPackage.LoadPackageParam) {
        if (cb == null || cb == c2_state_callback) return
        c2_state_callback = cb
        hookOnOpened(cb.javaClass, lpparam)
    }

    private fun hookOnOpened(cls: Class<*>, lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!hookedDeviceClasses.add(cls.name)) return
        XposedHelpers.findAndHookMethod(cls, "onOpened", CameraDevice::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    VideoPlayer.onCameraSwitch(); needRecreate = true; createVirtualSurface()
                    c2_reader_Surfcae = null; original_preview_Surface = null
                    nonPreviewSurfaces.clear()
                    val devCls = (param.args[0] ?: return).javaClass
                    hookSessionCreation(devCls, lpparam)
                }
            }
        )
    }

    private fun hookSessionCreation(devCls: Class<*>, lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod(devCls, "createCaptureSession",
                List::class.java, CameraCaptureSession.StateCallback::class.java, Handler::class.java,
                object : XC_MethodHook() {
                    @Suppress("UNCHECKED_CAST")
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val original = param.args[0] as? List<Surface> ?: return
                        param.args[0] = filterAndSwapSurfaceList(original)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG session(List): $e") }

        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod(devCls, "createCaptureSession",
                    SessionConfiguration::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            val orig = param.args[0] as? SessionConfiguration ?: return
                            sessionConfiguration = orig
                            val virt = c2_virtual_surface ?: return
                            val outs = try { orig.outputConfigurations } catch (_: Throwable) { null }
                            val newOuts = ArrayList<OutputConfiguration>()
                            var replaced = false
                            if (outs != null) {
                                for (oc in outs) {
                                    val s = try { oc.surface } catch (_: Throwable) { null }
                                    when {
                                        s != null && nonPreviewSurfaces.contains(s) -> newOuts.add(oc)
                                        !replaced -> { newOuts.add(OutputConfiguration(virt)); replaced = true }
                                        else -> newOuts.add(oc)
                                    }
                                }
                            } else { newOuts.add(OutputConfiguration(virt)) }
                            val fake = SessionConfiguration(orig.sessionType, newOuts, orig.executor, orig.stateCallback)
                            fake_sessionConfiguration = fake; param.args[0] = fake
                        }
                    }
                )
            } catch (e: Throwable) { XposedBridge.log("$TAG session(SC): $e") }

            try {
                XposedHelpers.findAndHookMethod(devCls, "createCaptureSession",
                    List::class.java, java.util.concurrent.Executor::class.java,
                    CameraCaptureSession.StateCallback::class.java,
                    object : XC_MethodHook() {
                        @Suppress("UNCHECKED_CAST")
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            val original = param.args[0] as? List<Surface> ?: return
                            param.args[0] = filterAndSwapSurfaceList(original)
                        }
                    }
                )
            } catch (e: Throwable) { XposedBridge.log("$TAG session(List,E): $e") }
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // IMAGE READER — MYCAM-style K0() approach
    // Key: use imageReader.getWidth()/getHeight(), NOT image.getWidth()/getHeight()
    // ══════════════════════════════════════════════════════════════════════════

    private fun hookImageReader(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader
        val self = this

        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl,
                "newInstance", Int::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val w   = param.args[0] as? Int ?: return
                        val h   = param.args[1] as? Int ?: return
                        val fmt = param.args[2] as? Int ?: return
                        val reader = param.result as? ImageReader ?: return
                        try {
                            val surf = reader.surface ?: return
                            nonPreviewSurfaces.add(surf)
                            // Store reader → dims (MYCAM approach)
                            self.readerSizes[reader] = Pair(w, h)
                            if (w >= 480 || h >= 480) { lastReaderW = w; lastReaderH = h }
                            XposedBridge.log("$TAG IR fmt=$fmt ${w}x${h}")
                            VideoPlayer.addImageWriterTarget(surf, fmt, w, h)
                            hookAcquireOnReader(reader, w, h, lpparam.classLoader)
                            context?.let { showToast(it, "VCamSX: Capture ${w}×${h}") }
                        } catch (e: Throwable) { XposedBridge.log("$TAG IR.newInstance: $e") }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG IR hook: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl, "getSurface",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        (param.result as? Surface)?.let { nonPreviewSurfaces.add(it) }
                    }
                }
            )
        } catch (_: Throwable) {}
    }

    /**
     * Hook acquireLatestImage/acquireNextImage on a SPECIFIC ImageReader instance.
     * We keep a reference to the reader so we can call reader.getWidth()/getHeight()
     * exactly like MYCAM's K0() does — NOT image.getWidth()/getHeight().
     */
    private fun hookAcquireOnReader(reader: ImageReader, rdrW: Int, rdrH: Int, cl: ClassLoader) {
        val cls = reader.javaClass
        if (!hookedReaderClasses.add("${cls.name}_${rdrW}x${rdrH}")) return

        val swapHook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (!ImagePlayer.isActive.value) return
                val image = param.result as? Image ?: return
                try {
                    // MYCAM K0() style: check imageFormat from READER not image
                    val readerFmt = try { reader.imageFormat } catch (_: Throwable) { 0 }
                    val imageFmt  = try { image.format } catch (_: Throwable) { 0 }

                    if (readerFmt == 256 || imageFmt == 256) {
                        // JPEG — MYCAM K0() path: use reader.width/reader.height
                        val w = try { reader.width } catch (_: Throwable) { rdrW }
                        val h = try { reader.height } catch (_: Throwable) { rdrH }
                        if (w <= 0 || h <= 0) return
                        val plane = image.planes.getOrNull(0) ?: return
                        val buf   = plane.buffer
                        if (buf.isReadOnly) return
                        val cap  = buf.capacity()
                        val jpeg = generateJpeg(w, h, cap) ?: return
                        if (jpeg.size > cap) {
                            XposedBridge.log("$TAG JPEG too large ${jpeg.size}>$cap skip")
                            return
                        }
                        // MYCAM pattern exactly
                        buf.clear(); buf.put(jpeg); buf.flip()
                        val bmp = ImagePlayer.currentBitmapSnapshot()
                        context?.let { showToast(it, "VCamSX: ${bmp?.width}×${bmp?.height} → ${w}×${h}") }
                        XposedBridge.log("$TAG K0 JPEG ${w}x${h} bytes=${jpeg.size}")
                    }
                    // YUV handled by ImageWriter loop in VideoPlayer
                } catch (e: Throwable) { XposedBridge.log("$TAG acquireImg: ${e.message}") }
            }
        }
        try { XposedHelpers.findAndHookMethod(cls, "acquireLatestImage", swapHook) }
            catch (e: Throwable) { XposedBridge.log("$TAG acquireLatest: $e") }
        try { XposedHelpers.findAndHookMethod(cls, "acquireNextImage",   swapHook) }
            catch (e: Throwable) { XposedBridge.log("$TAG acquireNext: $e") }
    }

    private fun hookMediaCodecSurface(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("android.media.MediaCodec", lpparam.classLoader,
                "createInputSurface",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        (param.result as? Surface)?.let { nonPreviewSurfaces.add(it) }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG MC surface: $e") }
    }

    private fun hookMediaRecorder(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("android.media.MediaRecorder", lpparam.classLoader,
                "setCamera", Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        val cam = param.args[0] as? Camera ?: return
                        try { cam.setPreviewTexture(makeFakeST(null)) } catch (_: Throwable) {}
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG MR.setCamera: $e") }
        try {
            XposedHelpers.findAndHookMethod("android.media.MediaRecorder", lpparam.classLoader,
                "getSurface",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        (param.result as? Surface)?.let { nonPreviewSurfaces.add(it) }
                    }
                }
            )
        } catch (_: Throwable) {}
    }

    // ══════════════════════════════════════════════════════════════════════════
    // AUDIO INJECTION
    // ══════════════════════════════════════════════════════════════════════════

    private fun hookAudioRecord(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl   = lpparam.classLoader
        val proc = lpparam.processName

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl, "startRecording",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val ctx    = getContextFromActivityThread()
                        val status = if (ctx != null) readStatusDirect(ctx) else InfoProcesser.videoStatus
                        XposedBridge.log("$TAG AR.startRecording proc=$proc " +
                            "video=${status?.isVideoEnable} volume=${status?.volume}")
                        if (status?.isVideoEnable == true && status.volume) {
                            audioEnabled = true; AudioInjector.enabled = true; AudioInjector.start()
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.startRecording: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl, "stop",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (AudioInjector.enabled) {
                            audioEnabled = false; AudioInjector.enabled = false; AudioInjector.stop()
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.stop: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteArray::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf = param.args[0] as? ByteArray ?: return
                        val off = (param.args[1] as? Int) ?: 0
                        val sz  = (param.args[2] as? Int) ?: (buf.size - off)
                        val n   = AudioInjector.read(buf, off, sz)
                        if (n > 0) param.result = n
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read[]: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteArray::class.java, Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf = param.args[0] as? ByteArray ?: return
                        val off = (param.args[1] as? Int) ?: 0
                        val sz  = (param.args[2] as? Int) ?: (buf.size - off)
                        val n   = AudioInjector.read(buf, off, sz)
                        if (n > 0) param.result = n
                    }
                }
            )
        } catch (_: Throwable) {}

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteBuffer::class.java, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf = param.args[0] as? ByteBuffer ?: return
                        val sz  = (param.args[1] as? Int) ?: return
                        val n   = AudioInjector.read(buf, sz)
                        if (n > 0) param.result = n
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read(BB): $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteBuffer::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf = param.args[0] as? ByteBuffer ?: return
                        val sz  = (param.args[1] as? Int) ?: return
                        val n   = AudioInjector.read(buf, sz)
                        if (n > 0) param.result = n
                    }
                }
            )
        } catch (_: Throwable) {}
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    private fun createVirtualSurface() {
        if (!needRecreate && c2_virtual_surface?.isValid == true) return
        c2VirtualSurfaceTexture?.release(); c2_virtual_surface?.release()
        c2VirtualSurfaceTexture = SurfaceTexture(10).also { it.setDefaultBufferSize(720, 1280) }
        c2_virtual_surface      = Surface(c2VirtualSurfaceTexture!!)
        needRecreate            = false
        XposedBridge.log("$TAG virtual surface created")
    }

    private fun hookPreviewCallback(param: XC_MethodHook.MethodHookParam) {
        val cls = param.args[0].javaClass
        if (!hookedCallbackClasses.add(cls.name)) return
        try {
            XposedHelpers.findAndHookMethod(cls, "onPreviewFrame",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        val cam = p.args[1] as? Camera ?: return
                        if (cam != camera_onPreviewFrame) {
                            camera_callback_calss = cls; camera_onPreviewFrame = cam
                            hw_decode_obj?.stopDecode()
                            hw_decode_obj = VideoToFrames().also {
                                it.setSaveFrames(OutputImageFormat.NV21)
                                it.decode(android.net.Uri.parse(
                                    "content://com.wangyiheng.vcamsx.videoprovider"))
                            }
                        }
                        val deadline = System.currentTimeMillis() + 2000L
                        while (data_buffer.size <= 1 && System.currentTimeMillis() < deadline) {
                            try { Thread.sleep(10) } catch (_: InterruptedException) { break }
                        }
                        if (data_buffer.size <= 1) return
                        val dst = p.args[0] as? ByteArray ?: return
                        System.arraycopy(data_buffer, 0, dst, 0, minOf(data_buffer.size, dst.size))
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookPreviewCb: $e") }
    }

    // Camera1 shutter — MYCAM/IPMAN style
    private fun hookJPEGCb(param: XC_MethodHook.MethodHookParam, idx: Int) {
        val cbObj = param.args[idx] ?: return
        try {
            XposedHelpers.findAndHookMethod(cbObj.javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val realJpeg  = p.args[0] as? ByteArray
                        val cam       = p.args[1] as? Camera
                        val composite = replacePictureJpeg(realJpeg, cam) ?: return
                        p.args[0]     = composite
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookJPEGCb: $e") }
    }

    private fun hookYUVCb(param: XC_MethodHook.MethodHookParam) {
        val cbObj = param.args[0] ?: return
        try {
            XposedHelpers.findAndHookMethod(cbObj.javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val realJpeg  = p.args[0] as? ByteArray
                        val cam       = p.args[1] as? Camera
                        val composite = replacePictureJpeg(realJpeg, cam) ?: return
                        p.args[0]     = composite
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookYUVCb: $e") }
    }
}
