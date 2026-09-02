package com.wangyiheng.vcamsx

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
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
import android.view.Surface
import android.view.SurfaceHolder
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
    private val nonPreviewSurfaces: MutableSet<Surface> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedDeviceClasses: MutableSet<String>  = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedCallbackClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedReaderClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

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
        @Volatile @JvmField var data_buffer: ByteArray = byteArrayOf()

        fun makeFakeST(old: SurfaceTexture?): SurfaceTexture {
            old?.release()
            return if (Build.VERSION.SDK_INT >= 26) SurfaceTexture(false)
            else SurfaceTexture(10)
        }
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.wangyiheng.vcamsx") return
        val proc = lpparam.processName
        if (proc.endsWith(":push") || proc.endsWith(":remote") ||
            proc.endsWith(":nfc")  || proc.endsWith(":work")) return

        XposedBridge.log("$TAG hook: ${lpparam.packageName}")
        hookAppInit(lpparam)
        hookCamera1(lpparam)
        hookCamera2(lpparam)
        hookImageReader(lpparam)
        hookMediaCodecSurface(lpparam)
        hookMediaRecorder(lpparam)
    }

    private fun filterAndSwapSurfaceList(original: List<Surface>): List<Surface> {
        val virt = c2_virtual_surface ?: return original
        val result = ArrayList<Surface>(original.size)
        var previewReplaced = false
        for (s in original) {
            when {
                nonPreviewSurfaces.contains(s) -> {
                    result.add(s)
                }
                !previewReplaced -> {
                    original_preview_Surface = s
                    result.add(virt)
                    previewReplaced = true
                }
                else -> result.add(s)
            }
        }
        if (!previewReplaced) result.add(0, virt)
        return result
    }

    private fun hookAppInit(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("android.app.Instrumentation", lpparam.classLoader,
                "callApplicationOnCreate", Application::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val app = param.args?.firstOrNull() as? Application ?: return
                        val ctx = app.applicationContext
                        if (context == ctx) return
                        try {
                            context = ctx; VideoControlReceiver.register(ctx)
                            if (!isPlaying) { isPlaying = true; VideoPlayer.initializeTheStateAsWellAsThePlayer() }
                        } catch (e: Throwable) { XposedBridge.log("$TAG init: $e") }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookAppInit install: $e") }
    }

    private fun hookCamera1(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader
        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "setPreviewTexture", SurfaceTexture::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        val incomingST = param.args[0] as? SurfaceTexture ?: return
                        if (incomingST == fake_SurfaceTexture) return
                        if (origin_preview_camera != null && origin_preview_camera == param.thisObject) {
                            param.args[0] = fake_SurfaceTexture; return
                        }
                        origin_preview_camera              = param.thisObject as? Camera
                        original_c1_preview_SurfaceTexture = incomingST
                        fake_SurfaceTexture                = makeFakeST(fake_SurfaceTexture)
                        param.args[0]                      = fake_SurfaceTexture
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.setPreviewTexture install: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "setPreviewDisplay", SurfaceHolder::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        mcamera1  = param.thisObject as? Camera
                        oriHolder = param.args[0] as? SurfaceHolder
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.setPreviewDisplay install: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "startPreview",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        val cam = param.thisObject as? Camera ?: return
                        if (oriHolder != null && cam == mcamera1 && origin_preview_camera == null) {
                            c1FakeTexture = makeFakeST(c1FakeTexture)
                            try {
                                origin_preview_camera = cam
                                fake_SurfaceTexture   = makeFakeST(fake_SurfaceTexture)
                                cam.setPreviewTexture(fake_SurfaceTexture)
                            } catch (e: Throwable) {
                                XposedBridge.log("$TAG setPreviewTexture before start: $e")
                                origin_preview_camera = null
                            }
                        }
                    }
                    override fun afterHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        VideoPlayer.c1_camera_play()
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.startPreview install: $e") }

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
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.setPreviewCallbackWithBuffer install: $e") }

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
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.addCallbackBuffer install: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "takePicture",
                Camera.ShutterCallback::class.java, Camera.PictureCallback::class.java,
                Camera.PictureCallback::class.java, Camera.PictureCallback::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val status = InfoProcesser.videoStatus
                        if (status?.isVideoEnable != true && !ImagePlayer.isActive.value) return
                        if (param.args[0] != null) hookYUVCb(param)
                        if (param.args[2] != null) hookJPEGCb(param, 2)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.takePicture install: $e") }
    }

    private fun hookCamera2(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader
        XposedHelpers.findAndHookMethod("android.hardware.camera2.CameraManager", cl,
            "openCamera", String::class.java, CameraDevice.StateCallback::class.java, Handler::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    intercept2(param.args[1] as? CameraDevice.StateCallback, lpparam)
                }
            }
        )
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod("android.hardware.camera2.CameraManager", cl,
                    "openCamera", String::class.java, java.util.concurrent.Executor::class.java,
                    CameraDevice.StateCallback::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            intercept2(param.args[2] as? CameraDevice.StateCallback, lpparam)
                        }
                    }
                )
            } catch (e: Throwable) { XposedBridge.log("$TAG openCamera(Exec): $e") }
        }

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

        XposedHelpers.findAndHookMethod(
            "android.hardware.camera2.CaptureRequest\$Builder", cl, "build",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    VideoPlayer.camera2Play()
                }
            }
        )
    }

    private fun intercept2(cb: CameraDevice.StateCallback?, lpparam: XC_LoadPackage.LoadPackageParam) {
        if (cb == null || cb == c2_state_callback) return
        c2_state_callback = cb; hookOnOpened(cb.javaClass, lpparam)
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
                        XposedBridge.log("$TAG session(List) filtered: " +
                            "${original.size} surfaces -> ${(param.args[0] as List<*>).size}")
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

                            val origOutputs = try { orig.outputConfigurations } catch (_: Throwable) { null }
                            val newOutputs = ArrayList<OutputConfiguration>()
                            var previewReplaced = false
                            if (origOutputs != null) {
                                for (oc in origOutputs) {
                                    val s = try { oc.surface } catch (_: Throwable) { null }
                                    when {
                                        s != null && nonPreviewSurfaces.contains(s) -> newOutputs.add(oc)
                                        !previewReplaced -> {
                                            newOutputs.add(OutputConfiguration(virt))
                                            previewReplaced = true
                                        }
                                        else -> newOutputs.add(oc)
                                    }
                                }
                            } else {
                                newOutputs.add(OutputConfiguration(virt))
                            }

                            val fake = SessionConfiguration(
                                orig.sessionType, newOutputs, orig.executor, orig.stateCallback)
                            fake_sessionConfiguration = fake; param.args[0] = fake
                            XposedBridge.log("$TAG session(SC) filtered: " +
                                "${origOutputs?.size ?: 0} outputs -> ${newOutputs.size}")
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
            } catch (e: Throwable) {
                XposedBridge.log("$TAG session(List,Executor,SC) overload not present on this ROM: $e")
            }
        }
    }

    private fun hookImageReader(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader
        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl,
                "newInstance",
                Int::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val w = param.args[0] as? Int ?: return
                        val h = param.args[1] as? Int ?: return
                        val fmt = param.args[2] as? Int ?: return
                        val reader = param.result ?: return
                        try {
                            val surf = reader.javaClass.getMethod("getSurface")
                                .invoke(reader) as? Surface ?: return
                            nonPreviewSurfaces.add(surf)
                            XposedBridge.log("$TAG IR fmt=$fmt ${w}x${h} — flagged non-preview")
                            VideoPlayer.addImageWriterTarget(surf, fmt, w, h)
                            hookAcquireOnReaderClass(reader.javaClass)
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
                        hookAcquireOnReaderClass(param.thisObject.javaClass)
                    }
                }
            )
        } catch (_: Throwable) {}
    }

    private fun hookAcquireOnReaderClass(cls: Class<*>) {
        if (!hookedReaderClasses.add(cls.name)) return
        val swapHook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (!ImagePlayer.isActive.value) return
                val bmp   = ImagePlayer.currentBitmapSnapshot() ?: return
                val image = param.result as? Image ?: return
                try {
                    if (image.format != android.graphics.ImageFormat.JPEG) return
                    val plane = image.planes.getOrNull(0) ?: return
                    val buf   = plane.buffer
                    val stream = ByteArrayOutputStream()
                    val scaled = if (bmp.width == image.width && bmp.height == image.height) bmp
                                 else Bitmap.createScaledBitmap(bmp, image.width, image.height, true)
                    scaled.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                    if (scaled !== bmp) scaled.recycle()
                    val jpeg = stream.toByteArray()
                    buf.clear()
                    buf.put(jpeg, 0, minOf(jpeg.size, buf.capacity()))
                    buf.rewind()
                    XposedBridge.log("$TAG acquireImage swapped JPEG " +
                        "${image.width}x${image.height} -> ${jpeg.size}b")
                } catch (e: Throwable) { XposedBridge.log("$TAG acquireImage swap: $e") }
            }
        }
        try { XposedHelpers.findAndHookMethod(cls, "acquireLatestImage", swapHook) }
            catch (e: Throwable) { XposedBridge.log("$TAG hook acquireLatest on $cls: $e") }
        try { XposedHelpers.findAndHookMethod(cls, "acquireNextImage", swapHook) }
            catch (e: Throwable) { XposedBridge.log("$TAG hook acquireNext on $cls: $e") }
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
                        val dst = p.args[0] as? ByteArray ?: return

                        // FIX (Bug 3): When image mode is active, generate NV21 from the
                        // bitmap directly — VideoToFrames.data_buffer is never populated in
                        // image mode because the reader pipeline isn't running yet at this
                        // point, so waiting on data_buffer always times out and injects nothing.
                        if (ImagePlayer.isActive.value) {
                            val bmp = ImagePlayer.currentBitmapSnapshot()
                            if (bmp != null) {
                                // Derive output size from the destination buffer (app-allocated)
                                // Typical NV21: W*H*3/2 bytes; guess dimensions from buffer size
                                val totalPixels = dst.size * 2 / 3
                                // Find W and H that match the buffer — prefer 720x1280 or fallback
                                val (outW, outH) = when {
                                    totalPixels == 720 * 1280 -> Pair(720, 1280)
                                    totalPixels == 1280 * 720 -> Pair(1280, 720)
                                    totalPixels == 640 * 480  -> Pair(640, 480)
                                    totalPixels == 480 * 640  -> Pair(480, 640)
                                    totalPixels == 1920 * 1080 -> Pair(1920, 1080)
                                    else -> {
                                        // Best-effort: try to find W as sqrt of aspect-close value
                                        val sqr = Math.sqrt(totalPixels.toDouble()).toInt()
                                        Pair(sqr, totalPixels / sqr)
                                    }
                                }
                                try {
                                    val scaled = if (bmp.width == outW && bmp.height == outH) bmp
                                                 else Bitmap.createScaledBitmap(bmp, outW, outH, true)
                                    val nv21 = bitmapToNv21(scaled, outW, outH)
                                    if (scaled !== bmp) scaled.recycle()
                                    System.arraycopy(nv21, 0, dst, 0, minOf(nv21.size, dst.size))
                                } catch (e: Throwable) {
                                    XposedBridge.log("$TAG onPreviewFrame imgNV21: $e")
                                }
                            }
                            return  // Do NOT fall through to VideoToFrames path in image mode
                        }

                        // Video mode: spin up VideoToFrames decoder if camera changed
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
                        System.arraycopy(data_buffer, 0, dst, 0, minOf(data_buffer.size, dst.size))
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookPreviewCallback install: $e") }
    }

    /** NV21 conversion used by Camera1 preview callback in image mode */
    private fun bitmapToNv21(src: Bitmap, w: Int, h: Int): ByteArray {
        val argb  = IntArray(w * h)
        src.getPixels(argb, 0, w, 0, 0, w, h)
        val nv21  = ByteArray(w * h * 3 / 2)
        var yIdx  = 0; var uvIdx = w * h
        for (row in 0 until h) {
            for (col in 0 until w) {
                val px = argb[row * w + col]
                val r  = (px shr 16) and 0xFF
                val g  = (px shr  8) and 0xFF
                val b  =  px         and 0xFF
                val y  = ((66 * r + 129 * g + 25 * b + 128) shr 8) + 16
                nv21[yIdx++] = y.coerceIn(16, 235).toByte()
                if (row % 2 == 0 && col % 2 == 0) {
                    val cb = ((-38 * r - 74 * g + 112 * b + 128) shr 8) + 128
                    val cr = ((112 * r - 94 * g - 18 * b + 128) shr 8) + 128
                    nv21[uvIdx++] = cr.coerceIn(16, 240).toByte()
                    nv21[uvIdx++] = cb.coerceIn(16, 240).toByte()
                }
            }
        }
        return nv21
    }

    private fun hookJPEGCb(param: XC_MethodHook.MethodHookParam, idx: Int) {
        try {
            XposedHelpers.findAndHookMethod(param.args[idx].javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        val realJpeg = p.args[0] as? ByteArray ?: return
                        val sourceBmp = ImagePlayer.currentBitmapSnapshot() ?: return
                        try {
                            val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                            BitmapFactory.decodeByteArray(realJpeg, 0, realJpeg.size, opts)
                            val targetW = opts.outWidth.takeIf { it > 0 } ?: sourceBmp.width
                            val targetH = opts.outHeight.takeIf { it > 0 } ?: sourceBmp.height

                            val scaled = centerCropBitmap(sourceBmp, targetW, targetH)
                            val out = ByteArrayOutputStream(realJpeg.size)
                            scaled.compress(Bitmap.CompressFormat.JPEG, 95, out)
                            if (scaled !== sourceBmp) scaled.recycle()

                            p.args[0] = out.toByteArray()
                            XposedBridge.log("$TAG JPEG replaced: ${targetW}x${targetH}")
                        } catch (e: Throwable) {
                            XposedBridge.log("$TAG hookJPEGCb: ${e.message}")
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookJPEGCb install: $e") }
    }

    private fun hookYUVCb(param: XC_MethodHook.MethodHookParam) {
        try {
            XposedHelpers.findAndHookMethod(param.args[0].javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        val realJpeg = p.args[0] as? ByteArray ?: return
                        val sourceBmp = ImagePlayer.currentBitmapSnapshot() ?: return
                        try {
                            val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                            BitmapFactory.decodeByteArray(realJpeg, 0, realJpeg.size, opts)
                            val targetW = opts.outWidth.takeIf { it > 0 } ?: sourceBmp.width
                            val targetH = opts.outHeight.takeIf { it > 0 } ?: sourceBmp.height

                            val scaled = centerCropBitmap(sourceBmp, targetW, targetH)
                            val out = ByteArrayOutputStream(realJpeg.size)
                            scaled.compress(Bitmap.CompressFormat.JPEG, 95, out)
                            if (scaled !== sourceBmp) scaled.recycle()

                            p.args[0] = out.toByteArray()
                            XposedBridge.log("$TAG YUV replaced: ${targetW}x${targetH}")
                        } catch (e: Throwable) {
                            XposedBridge.log("$TAG hookYUVCb: ${e.message}")
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookYUVCb install: $e") }
    }
    private fun hookYUVCb(param: XC_MethodHook.MethodHookParam) {
        try {
            XposedHelpers.findAndHookMethod(param.args[0].javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        val bmp = ImagePlayer.currentBitmapSnapshot() ?: return
                        val stream = ByteArrayOutputStream()
                        bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                        p.args[0] = stream.toByteArray()
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookYUVCb install: $e") }
    }
}
