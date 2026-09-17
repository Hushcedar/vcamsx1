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
import android.media.AudioRecord
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.view.Surface
import android.view.SurfaceHolder
import com.wangyiheng.vcamsx.NativeAudioBridge
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
    private val nonPreviewSurfaces: MutableSet<Surface> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedDeviceClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedCallbackClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedReaderClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

    companion object {
        const val TAG = "vcamsx"

        @JvmField var context: Context? = null
        @JvmField var original_preview_Surface: Surface? = null
        @JvmField var original_c1_preview_SurfaceTexture: SurfaceTexture? = null
        @JvmField var fake_SurfaceTexture: SurfaceTexture? = null
        @JvmField var c1FakeTexture: SurfaceTexture? = null
        @JvmField var c1FakeSurface: Surface? = null
        @JvmField var oriHolder: SurfaceHolder? = null
        @JvmField var origin_preview_camera: Camera? = null
        @JvmField var c2_reader_Surfcae: Surface? = null
        @JvmField var c2VirtualSurfaceTexture: SurfaceTexture? = null
        @JvmField var c2_virtual_surface: Surface? = null
        @JvmField var sessionConfiguration: SessionConfiguration? = null
        @JvmField var outputConfiguration: OutputConfiguration? = null
        @JvmField var fake_sessionConfiguration: SessionConfiguration? = null
        @JvmField var isPlaying = false
        @JvmField var needRecreate = false
        @JvmField var hw_decode_obj: VideoToFrames? = null
        @JvmField var mcamera1: Camera? = null
        @JvmField var camera_onPreviewFrame: Camera? = null
        @JvmField var camera_callback_calss: Class<*>? = null
        @Volatile @JvmField var data_buffer: ByteArray = byteArrayOf()

        fun makeFakeST(old: SurfaceTexture?): SurfaceTexture {
            old?.release()
            return if (Build.VERSION.SDK_INT >= 26) SurfaceTexture(false)
            else SurfaceTexture(10)
        }

        fun resolveContext(): Context? {
            if (context != null) return context
            return try {
                val atClass = Class.forName("android.app.ActivityThread")
                val app = atClass.getMethod("currentApplication").invoke(null) as? Application
                app?.applicationContext?.also { context = it }
            } catch (_: Throwable) { null }
        }

        fun resolveCtx(): Context? {
            if (context != null) return context
            val atCtx = try {
                val at = Class.forName("android.app.ActivityThread")
                at.getMethod("currentApplication").invoke(null) as? Context
            } catch (_: Throwable) { null }
            if (atCtx != null) { context = atCtx; return atCtx }
            return AudioInjector.getContext()
        }

        fun shouldInjectCamera(): Boolean {
            InfoProcesser.initStatus()
            val videoOn = InfoProcesser.videoStatus?.isVideoEnable == true
            val imageOn = ImagePlayer.isActive.value
            return videoOn || imageOn
        }
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.wangyiheng.vcamsx") return
        val proc = lpparam.processName
        if (proc.endsWith(":push") || proc.endsWith(":remote") ||
            proc.endsWith(":nfc") || proc.endsWith(":work")) return

        XposedBridge.log("$TAG hook pkg=${lpparam.packageName} proc=$proc")
        hookAppInit(lpparam)
        hookCamera1(lpparam)
        hookCamera2(lpparam)
        hookImageReader(lpparam)
        hookMediaCodecSurface(lpparam)
        hookMediaRecorder(lpparam)
        hookAudio(lpparam)
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
                            context = ctx
                            VideoControlReceiver.register(ctx)
                            if (!isPlaying) {
                                isPlaying = true
                                VideoPlayer.initializeTheStateAsWellAsThePlayer()
                            }
                            NativeAudioBridge.install()
                        } catch (e: Throwable) { XposedBridge.log("$TAG init: $e") }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookAppInit: $e") }
    }

    private fun hookCamera1(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "setPreviewTexture", SurfaceTexture::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!shouldInjectCamera()) return
                        val incomingST = param.args[0] as? SurfaceTexture ?: return
                        if (incomingST == fake_SurfaceTexture) return
                        if (origin_preview_camera != null && origin_preview_camera == param.thisObject) {
                            param.args[0] = fake_SurfaceTexture
                            return
                        }
                        origin_preview_camera = param.thisObject as? Camera
                        original_c1_preview_SurfaceTexture = incomingST
                        fake_SurfaceTexture = makeFakeST(fake_SurfaceTexture)
                        param.args[0] = fake_SurfaceTexture
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.setPreviewTexture: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "setPreviewDisplay", SurfaceHolder::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!shouldInjectCamera()) return
                        mcamera1 = param.thisObject as? Camera
                        oriHolder = param.args[0] as? SurfaceHolder
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.setPreviewDisplay: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "startPreview",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!shouldInjectCamera()) return
                        val cam = param.thisObject as? Camera ?: return
                        if (oriHolder != null && cam == mcamera1 && origin_preview_camera == null) {
                            c1FakeTexture = makeFakeST(c1FakeTexture)
                            try {
                                origin_preview_camera = cam
                                fake_SurfaceTexture = makeFakeST(fake_SurfaceTexture)
                                cam.setPreviewTexture(fake_SurfaceTexture)
                            } catch (e: Throwable) {
                                XposedBridge.log("$TAG setPreviewTexture before start: $e")
                                origin_preview_camera = null
                            }
                        }
                    }
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!shouldInjectCamera()) return
                        if (InfoProcesser.videoStatus?.isVideoEnable == true)
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
                        if (shouldInjectCamera() && param.args[0] != null)
                            hookPreviewCallback(param)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.setPreviewCallbackWithBuffer: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
                "addCallbackBuffer", ByteArray::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!shouldInjectCamera()) return
                        val buf = param.args[0] as? ByteArray ?: return
                        param.args[0] = ByteArray(buf.size)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.addCallbackBuffer: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera\$Parameters", cl,
                "setPictureSize", Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val bmp = ImagePlayer.currentBitmapSnapshot() ?: return
                        val reqW = param.args[0] as? Int ?: return
                        val reqH = param.args[1] as? Int ?: return
                        val imgRatio = bmp.width.toFloat() / bmp.height.toFloat()
                        val appRatio = reqW.toFloat() / reqH.toFloat()
                        if (kotlin.math.abs(imgRatio - appRatio) > 0.05f) {
                            val newH = (reqW / imgRatio).toInt()
                            param.args[1] = newH
                            XposedBridge.log("$TAG setPictureSize adjusted ${reqW}x${reqH} → ${reqW}x${newH} (imgRatio=$imgRatio)")
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.setPictureSize: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "takePicture",
                Camera.ShutterCallback::class.java, Camera.PictureCallback::class.java,
                Camera.PictureCallback::class.java, Camera.PictureCallback::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!shouldInjectCamera()) return
                        XposedBridge.log("$TAG takePicture FIRED imageActive=${ImagePlayer.isActive.value}")
                        if (param.args[0] != null) hookYUVCb(param)
                        if (param.args[2] != null) hookJPEGCb(param, 2)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.takePicture: $e") }
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
                    if (InfoProcesser.videoStatus?.isVideoEnable == true)
                        VideoPlayer.camera2Play()
                }
            }
        )
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
                    VideoPlayer.onCameraSwitch()
                    needRecreate = true
                    createVirtualSurface()
                    c2_reader_Surfcae = null
                    original_preview_Surface = null
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
                            val origOutputs = try { orig.outputConfigurations } catch (_: Throwable) { null }
                            val newOutputs = ArrayList<OutputConfiguration>()
                            var replaced = false
                            if (origOutputs != null) {
                                for (oc in origOutputs) {
                                    val s = try { oc.surface } catch (_: Throwable) { null }
                                    when {
                                        s != null && nonPreviewSurfaces.contains(s) -> newOutputs.add(oc)
                                        !replaced -> { newOutputs.add(OutputConfiguration(virt)); replaced = true }
                                        else -> newOutputs.add(oc)
                                    }
                                }
                            } else { newOutputs.add(OutputConfiguration(virt)) }
                            val fake = SessionConfiguration(orig.sessionType, newOutputs, orig.executor, orig.stateCallback)
                            fake_sessionConfiguration = fake
                            param.args[0] = fake
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
            } catch (_: Throwable) {}
        }
    }

    private fun hookImageReader(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader
        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl,
                "newInstance", Int::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val w   = param.args[0] as? Int ?: return
                        val h   = param.args[1] as? Int ?: return
                        val fmt = param.args[2] as? Int ?: return
                        val reader = param.result ?: return
                        XposedBridge.log("$TAG IR.newInstance ${w}x${h} fmt=$fmt")
                        try {
                            val surf = reader.javaClass.getMethod("getSurface").invoke(reader) as? Surface ?: return
                            nonPreviewSurfaces.add(surf)
                            VideoPlayer.addImageWriterTarget(surf, fmt, w, h)
                            hookAcquireOnReaderClass(reader.javaClass, w, h)
                        } catch (e: Throwable) { XposedBridge.log("$TAG IR.newInstance inner: $e") }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG IR hook: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl, "getSurface",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        (param.result as? Surface)?.let { nonPreviewSurfaces.add(it) }
                        hookAcquireOnReaderClass(param.thisObject.javaClass, 0, 0)
                    }
                }
            )
        } catch (_: Throwable) {}
    }

    private fun hookAcquireOnReaderClass(cls: Class<*>, readerW: Int, readerH: Int) {
        if (!hookedReaderClasses.add(cls.name)) return
        val swapHook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                XposedBridge.log("$TAG acquireImage FIRED imageActive=${ImagePlayer.isActive.value}")
                if (!ImagePlayer.isActive.value) return
                val bmp = ImagePlayer.currentBitmapSnapshot() ?: run {
                    XposedBridge.log("$TAG acquireImage: no bitmap snapshot"); return
                }
                val image = param.result as? Image ?: return
                try {
                    if (image.format != android.graphics.ImageFormat.JPEG) {
                        XposedBridge.log("$TAG acquireImage: format=${image.format} not JPEG, skipping")
                        return
                    }
                    val plane  = image.planes.getOrNull(0) ?: return
                    val buf    = plane.buffer
                    val capW   = image.width
                    val capH   = image.height
                    XposedBridge.log("$TAG acquireImage: cap=${capW}x${capH} bmp=${bmp.width}x${bmp.height}")

                    val composite = compositeOnCapture(bmp, capW, capH)
                    val stream    = ByteArrayOutputStream()
                    composite.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                    if (composite !== bmp) composite.recycle()
                    val jpeg = stream.toByteArray()
                    buf.clear()
                    buf.put(jpeg, 0, minOf(jpeg.size, buf.capacity()))
                    buf.rewind()
                    showToast("Image corrected: ${capW}×${capH} ← ${bmp.width}×${bmp.height}")
                    XposedBridge.log("$TAG acquireImage: composited ${bmp.width}×${bmp.height} → ${capW}×${capH}")
                } catch (e: Throwable) { XposedBridge.log("$TAG acquireImage swap: $e") }
            }
        }
        try { XposedHelpers.findAndHookMethod(cls, "acquireLatestImage", swapHook) } catch (_: Throwable) {}
        try { XposedHelpers.findAndHookMethod(cls, "acquireNextImage", swapHook) } catch (_: Throwable) {}
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
                        if (!shouldInjectCamera()) return
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

    private fun filterAndSwapSurfaceList(original: List<Surface>): List<Surface> {
        val virt = c2_virtual_surface ?: return original
        val result = ArrayList<Surface>(original.size)
        var previewReplaced = false
        for (s in original) {
            when {
                nonPreviewSurfaces.contains(s) -> result.add(s)
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

    private fun createVirtualSurface() {
        if (!needRecreate && c2_virtual_surface?.isValid == true) return
        c2VirtualSurfaceTexture?.release()
        c2_virtual_surface?.release()
        c2VirtualSurfaceTexture = SurfaceTexture(10).also { it.setDefaultBufferSize(720, 1280) }
        c2_virtual_surface = Surface(c2VirtualSurfaceTexture!!)
        needRecreate = false
        XposedBridge.log("$TAG virtual surface created")
    }

    private fun hookJPEGCb(param: XC_MethodHook.MethodHookParam, idx: Int) {
        try {
            XposedHelpers.findAndHookMethod(param.args[idx].javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        XposedBridge.log("$TAG hookJPEGCb FIRED imageActive=${ImagePlayer.isActive.value}")
                        val bmp = ImagePlayer.currentBitmapSnapshot() ?: run {
                            XposedBridge.log("$TAG hookJPEGCb: no bitmap"); return
                        }
                        val camW = try { (param.thisObject as Camera).parameters.pictureSize?.width  ?: bmp.width  } catch (_: Throwable) { bmp.width }
                        val camH = try { (param.thisObject as Camera).parameters.pictureSize?.height ?: bmp.height } catch (_: Throwable) { bmp.height }
                        val composite = compositeOnCapture(bmp, camW, camH)
                        val stream = ByteArrayOutputStream()
                        composite.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                        if (composite !== bmp) composite.recycle()
                        p.args[0] = stream.toByteArray()
                        showToast("Image corrected: ${camW}×${camH} ← ${bmp.width}×${bmp.height}")
                        XposedBridge.log("$TAG hookJPEGCb: composited ${bmp.width}×${bmp.height} → ${camW}×${camH}")
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookJPEGCb: $e") }
    }

    private fun hookYUVCb(param: XC_MethodHook.MethodHookParam) {
        try {
            XposedHelpers.findAndHookMethod(param.args[0].javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        val bmp = ImagePlayer.currentBitmapSnapshot() ?: return
                        val camW = try { (param.thisObject as Camera).parameters.pictureSize?.width  ?: bmp.width  } catch (_: Throwable) { bmp.width }
                        val camH = try { (param.thisObject as Camera).parameters.pictureSize?.height ?: bmp.height } catch (_: Throwable) { bmp.height }
                        val composite = compositeOnCapture(bmp, camW, camH)
                        val stream = ByteArrayOutputStream()
                        composite.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                        if (composite !== bmp) composite.recycle()
                        p.args[0] = stream.toByteArray()
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookYUVCb: $e") }
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
                            camera_callback_calss = cls
                            camera_onPreviewFrame = cam
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
        } catch (e: Throwable) { XposedBridge.log("$TAG hookPreviewCallback: $e") }
    }

    // IPMAN composite: our image letterboxed onto a black canvas at capture size.
    // Aspect ratio preserved — never stretches.
    private fun compositeOnCapture(ourBmp: Bitmap, captureW: Int, captureH: Int): Bitmap {
        val outW = if (captureW > 0) captureW else ourBmp.width
        val outH = if (captureH > 0) captureH else ourBmp.height

        val result = Bitmap.createBitmap(outW, outH, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(result)
        canvas.drawColor(android.graphics.Color.BLACK)

        val scaleX = outW.toFloat() / ourBmp.width.toFloat()
        val scaleY = outH.toFloat() / ourBmp.height.toFloat()
        val scale  = minOf(scaleX, scaleY)
        val drawW  = (ourBmp.width  * scale).toInt()
        val drawH  = (ourBmp.height * scale).toInt()
        val left   = (outW - drawW) / 2
        val top    = (outH - drawH) / 2

        val dst = android.graphics.Rect(left, top, left + drawW, top + drawH)
        canvas.drawBitmap(ourBmp, null, dst, null)

        return result
    }

    private fun showToast(msg: String) {
        val ctx = resolveCtx() ?: return
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun hookAudio(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        try {
            val ctx = resolveCtx()
            if (ctx != null) NativeAudioBridge.initReceiverIfNeeded(ctx)
        } catch (e: Throwable) { XposedBridge.log("$TAG audio receiver early reg: $e") }

        try {
            XposedHelpers.findAndHookConstructor(
                "android.media.AudioRecord", cl,
                Int::class.java, Int::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val ar = param.thisObject as? AudioRecord ?: return
                        AudioInjector.onJavaAudioRecord(ar)
                    }
                }
            )
            XposedBridge.log("$TAG hooked AR ctor(5) in ${lpparam.packageName}")
        } catch (e: Throwable) { XposedBridge.log("$TAG AR ctor(5): $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl, "startRecording",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val ar = param.thisObject as? AudioRecord ?: return
                        AudioInjector.onJavaAudioRecord(ar)
                    }
                }
            )
        } catch (_: Throwable) {}

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteArray::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.isToggleOn) return
                        val ar  = param.thisObject as? AudioRecord ?: return
                        val buf = param.args[0] as? ByteArray ?: return
                        val off = (param.args[1] as? Int) ?: 0
                        val sz  = (param.args[2] as? Int) ?: return
                        if (AudioInjector.injectBuffer(ar, ByteBuffer.wrap(buf, off, sz), sz)) param.result = sz
                    }
                }
            )
            XposedBridge.log("$TAG hooked AR.read(byte[]) in ${lpparam.packageName}")
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read(byte[]): $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteArray::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.isToggleOn) return
                        val ar  = param.thisObject as? AudioRecord ?: return
                        val buf = param.args[0] as? ByteArray ?: return
                        val off = (param.args[1] as? Int) ?: 0
                        val sz  = (param.args[2] as? Int) ?: return
                        if (AudioInjector.injectBuffer(ar, ByteBuffer.wrap(buf, off, sz), sz)) param.result = sz
                    }
                }
            )
        } catch (_: Throwable) {}

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ShortArray::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.isToggleOn) return
                        val ar  = param.thisObject as? AudioRecord ?: return
                        val buf = param.args[0] as? ShortArray ?: return
                        val off = (param.args[1] as? Int) ?: 0
                        val sz  = (param.args[2] as? Int) ?: return
                        if (AudioInjector.injectShorts(ar, buf, off, sz)) param.result = sz
                    }
                }
            )
            XposedBridge.log("$TAG hooked AR.read(short[]) in ${lpparam.packageName}")
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read(short[]): $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ShortArray::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.isToggleOn) return
                        val ar  = param.thisObject as? AudioRecord ?: return
                        val buf = param.args[0] as? ShortArray ?: return
                        val off = (param.args[1] as? Int) ?: 0
                        val sz  = (param.args[2] as? Int) ?: return
                        if (AudioInjector.injectShorts(ar, buf, off, sz)) param.result = sz
                    }
                }
            )
        } catch (_: Throwable) {}

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteBuffer::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.isToggleOn) return
                        val ar  = param.thisObject as? AudioRecord ?: return
                        val buf = param.args[0] as? ByteBuffer ?: return
                        val sz  = (param.args[1] as? Int) ?: return
                        if (AudioInjector.injectBuffer(ar, buf, sz)) param.result = sz
                    }
                }
            )
            XposedBridge.log("$TAG hooked AR.read(ByteBuffer) in ${lpparam.packageName}")
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read(ByteBuffer): $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteBuffer::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.isToggleOn) return
                        val ar  = param.thisObject as? AudioRecord ?: return
                        val buf = param.args[0] as? ByteBuffer ?: return
                        val sz  = (param.args[1] as? Int) ?: return
                        if (AudioInjector.injectBuffer(ar, buf, sz)) param.result = sz
                    }
                }
            )
        } catch (_: Throwable) {}

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl, "release",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val ar = param.thisObject as? AudioRecord ?: return
                        AudioInjector.onAudioRecordReleased(ar)
                    }
                }
            )
        } catch (_: Throwable) {}

        XposedBridge.log("$TAG audio hooks installed for ${lpparam.packageName}")
    }
}
