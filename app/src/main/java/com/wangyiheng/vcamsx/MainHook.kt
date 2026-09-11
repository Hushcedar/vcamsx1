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
        @Volatile @JvmField var lastCaptureW = 1920
        @Volatile @JvmField var lastCaptureH = 1080

        fun showToast(ctx: Context, msg: String) {
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        fun isInjectionActive(status: com.wangyiheng.vcamsx.data.models.VideoStatues?): Boolean =
            status?.isVideoEnable == true || status?.isImageEnabled == true || ImagePlayer.isActive.value

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
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
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
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
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
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
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
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
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
                        if (!ImagePlayer.isActive.value && InfoProcesser.videoStatus?.isImageEnabled != true) return
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

        // Messenger/Facebook fix: hook CameraDeviceImpl directly
        hookCameraDeviceImpl(cl, lpparam)
    }

    /**
     * Messenger fix: CameraDeviceImpl.createCaptureSession intercept.
     * Messenger never calls addTarget() on CaptureRequest.Builder during session setup,
     * so original_preview_Surface stays null and camera2Play() does nothing → blank screen.
     * We intercept at CameraDeviceImpl level, save the preview surface, wrap the StateCallback
     * so camera2Play() fires after onConfigured() with original_preview_Surface set.
     */
    private fun hookCameraDeviceImpl(cl: ClassLoader, lpparam: XC_LoadPackage.LoadPackageParam) {
        val impl = "android.hardware.camera2.impl.CameraDeviceImpl"

        // List variant (older API / some Messenger paths)
        try {
            XposedHelpers.findAndHookMethod(impl, cl,
                "createCaptureSession",
                List::class.java, CameraCaptureSession.StateCallback::class.java, Handler::class.java,
                object : XC_MethodHook() {
                    @Suppress("UNCHECKED_CAST")
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (!isInjectionActive(InfoProcesser.videoStatus)) return
                        createVirtualSurface()
                        val original = param.args[0] as? List<Surface> ?: return
                        // Pick first non-ImageWriter surface as preview
                        val previewSurf = original.firstOrNull { !nonPreviewSurfaces.contains(it) }
                        if (previewSurf != null) {
                            original_preview_Surface = previewSurf
                            c2_reader_Surfcae        = previewSurf
                        }
                        param.args[0] = filterAndSwapSurfaceList(original)
                        val origCb = param.args[1] as? CameraCaptureSession.StateCallback
                        param.args[1] = makeSessionCb(origCb, previewSurf)
                        XposedBridge.log("$TAG impl.CS(List) pkg=${lpparam.packageName} preview=$previewSurf")
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG impl.CS(List): $e") }

        // SessionConfiguration variant (API 28+, most modern Messenger/Facebook)
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod(impl, cl,
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
                            outputs?.forEach { oc ->
                                val s = try { oc.surface } catch (_: Throwable) { null }
                                when {
                                    s != null && nonPreviewSurfaces.contains(s) -> newOuts.add(oc)
                                    !replaced -> {
                                        if (s != null) { previewSurf = s; original_preview_Surface = s; c2_reader_Surfcae = s }
                                        newOuts.add(OutputConfiguration(virt)); replaced = true
                                    }
                                    else -> newOuts.add(oc)
                                }
                            }
                            if (!replaced) newOuts.add(0, OutputConfiguration(virt))
                            val pSurf   = previewSurf
                            val fake    = SessionConfiguration(orig.sessionType, newOuts, orig.executor, makeSessionCb(orig.stateCallback, pSurf))
                            fake_sessionConfiguration = fake
                            param.args[0] = fake
                            XposedBridge.log("$TAG impl.CS(SC) pkg=${lpparam.packageName} preview=$pSurf")
                        }
                    }
                )
            } catch (e: Throwable) { XposedBridge.log("$TAG impl.CS(SC): $e") }
        }
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
        val cl   = lpparam.classLoader
        val self = this
        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl,
                "newInstance",
                Int::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val w      = param.args[0] as? Int ?: return
                        val h      = param.args[1] as? Int ?: return
                        val fmt    = param.args[2] as? Int ?: return
                        val reader = param.result as? android.media.ImageReader ?: return
                        try {
                            val surf = reader.surface ?: return
                            nonPreviewSurfaces.add(surf)
                            XposedBridge.log("$TAG IR fmt=$fmt ${w}x${h}")
                            VideoPlayer.addImageWriterTarget(surf, fmt, w, h)
                            // MYCAM K0 style: hook the specific reader instance
                            self.hookAcquireOnReader(reader, w, h)
                            context?.let { showToast(it, "VCamSX: Capture ${w}x${h}") }
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
     * MYCAM K0() style: hook acquire on a specific ImageReader instance.
     * KEY: uses reader.width/reader.height — NOT image.width/image.height.
     * Only replaces JPEG (format 256) — that's the still capture reader.
     * YUV preview readers are handled by VideoPlayer.startWriterLoop().
     */
    private fun hookAcquireOnReader(reader: android.media.ImageReader, rW: Int, rH: Int) {
        val key = "${reader.javaClass.name}_${rW}x${rH}"
        if (!hookedReaderClasses.add(key)) return

        val hook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (!ImagePlayer.isActive.value && InfoProcesser.videoStatus?.isImageEnabled != true) return
                val image = param.result as? Image ?: return
                try {
                    val fmt = try { image.format } catch (_: Throwable) { 0 }
                    if (fmt != 256) return   // 256 = JPEG — only replace still captures

                    val plane = image.planes.getOrNull(0) ?: return
                    val buf   = plane.buffer
                    if (buf.isReadOnly) return

                    // MYCAM K0: use reader dims, not image dims
                    val w   = try { reader.width  } catch (_: Throwable) { rW }
                    val h   = try { reader.height } catch (_: Throwable) { rH }
                    if (w <= 0 || h <= 0) return
                    val cap = buf.capacity()

                    val jpeg = buildJpeg(w, h, cap) ?: return
                    if (jpeg.size > cap) { XposedBridge.log("$TAG JPEG ${jpeg.size}>$cap skip"); return }

                    buf.clear(); buf.put(jpeg); buf.flip()   // MYCAM pattern
                    val bmp = ImagePlayer.currentBitmapSnapshot()
                    context?.let { showToast(it, "VCamSX: ${bmp?.width}x${bmp?.height} -> ${w}x${h}") }
                    XposedBridge.log("$TAG K0 JPEG ${w}x${h} bytes=${jpeg.size}")
                } catch (e: Throwable) { XposedBridge.log("$TAG acquire: ${e.message}") }
            }
        }
        try { XposedHelpers.findAndHookMethod(reader.javaClass, "acquireLatestImage", hook) }
            catch (e: Throwable) { XposedBridge.log("$TAG acquireLatest: $e") }
        try { XposedHelpers.findAndHookMethod(reader.javaClass, "acquireNextImage",   hook) }
            catch (e: Throwable) { XposedBridge.log("$TAG acquireNext: $e") }
    }

    /** Generate JPEG at exact dimensions, scaled-to-fit with black bars (no stretch) */
    private fun buildJpeg(w: Int, h: Int, cap: Int): ByteArray? {
        val bmp   = ImagePlayer.currentBitmapSnapshot() ?: return null
        val scale = minOf(w.toFloat()/bmp.width, h.toFloat()/bmp.height)
        val dW    = (bmp.width  * scale).toInt().coerceAtLeast(1)
        val dH    = (bmp.height * scale).toInt().coerceAtLeast(1)
        val out   = android.graphics.Bitmap.createBitmap(w, h, android.graphics.Bitmap.Config.ARGB_8888)
        android.graphics.Canvas(out).apply {
            drawColor(android.graphics.Color.BLACK)
            val sc = if (dW == bmp.width && dH == bmp.height) bmp
                     else android.graphics.Bitmap.createScaledBitmap(bmp, dW, dH, true)
            drawBitmap(sc, ((w-dW)/2).toFloat(), ((h-dH)/2).toFloat(),
                android.graphics.Paint().apply { isFilterBitmap = true })
            if (sc !== bmp) sc.recycle()
        }
        val stream = java.io.ByteArrayOutputStream(cap.coerceAtLeast(4096))
        out.compress(android.graphics.Bitmap.CompressFormat.JPEG, 95, stream)
        out.recycle()
        return stream.toByteArray()
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
        } catch (e: Throwable) { XposedBridge.log("$TAG hookPreviewCallback install: $e") }
    }

    private fun hookJPEGCb(param: XC_MethodHook.MethodHookParam, idx: Int) {
        val cbObj = param.args[idx] ?: return
        try {
            XposedHelpers.findAndHookMethod(cbObj.javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        if (!ImagePlayer.isActive.value && InfoProcesser.videoStatus?.isImageEnabled != true) return
                        val cam  = p.args[1] as? Camera
                        val sz   = try { cam?.parameters?.pictureSize } catch (_: Exception) { null }
                        val capW = sz?.width  ?: lastCaptureW
                        val capH = sz?.height ?: lastCaptureH
                        val jpeg = buildJpeg(capW, capH, capW * capH / 4) ?: return
                        p.args[0] = jpeg
                        context?.let { showToast(it, "VCamSX: Snap ${capW}x${capH}") }
                        XposedBridge.log("$TAG C1 JPEG ${capW}x${capH} bytes=${jpeg.size}")
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
                        if (!ImagePlayer.isActive.value && InfoProcesser.videoStatus?.isImageEnabled != true) return
                        val cam  = p.args[1] as? Camera
                        val sz   = try { cam?.parameters?.pictureSize } catch (_: Exception) { null }
                        val capW = sz?.width  ?: lastCaptureW
                        val capH = sz?.height ?: lastCaptureH
                        val jpeg = buildJpeg(capW, capH, capW * capH / 4) ?: return
                        p.args[0] = jpeg
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookYUVCb: $e") }
    }

    // ── Messenger/Facebook: makeSessionCb triggers playback after session configured
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
                        original_preview_Surface = s
                        VideoPlayer.camera2Play()
                        XposedBridge.log("$TAG sessionCb.onConfigured s=$s")
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
}
