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
import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.Toast
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
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class MainHook : IXposedHookLoadPackage {

    private var c2_state_callback: CameraDevice.StateCallback? = null
    private val nonPreviewSurfaces: MutableSet<Surface> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedDeviceClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedCallbackClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

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
            return null
        }

        fun shouldInjectCamera(): Boolean {
            InfoProcesser.initStatus()
            val videoOn = InfoProcesser.videoStatus?.isVideoEnable == true
            val imageOn = ImagePlayer.isActive.value
            return videoOn || imageOn
        }

        fun compositeOnCapture(ourBmp: Bitmap, captureW: Int, captureH: Int): Bitmap {
            val outW = if (captureW > 0) captureW else ourBmp.width
            val outH = if (captureH > 0) captureH else ourBmp.height

            val result = Bitmap.createBitmap(outW, outH, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(result)
            canvas.drawColor(android.graphics.Color.BLACK)

            val scale = minOf(
                outW.toFloat() / ourBmp.width.toFloat(),
                outH.toFloat() / ourBmp.height.toFloat(),
                1.0f
            )
            val drawW = (ourBmp.width  * scale).toInt()
            val drawH = (ourBmp.height * scale).toInt()
            val left  = (outW - drawW) / 2
            val top   = (outH - drawH) / 2

            canvas.drawBitmap(ourBmp, null, android.graphics.Rect(left, top, left + drawW, top + drawH), null)
            return result
        }

        fun showToast(ctx: Context, msg: String) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
            }
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
                            param.args[0] = fake_SurfaceTexture; return
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
                        if (ImagePlayer.isActive.value) {
                            val st = original_c1_preview_SurfaceTexture
                            if (st != null) {
                                try { ImagePlayer.attachC1Surface(Surface(st)) } catch (_: Throwable) {}
                            }
                        } else if (InfoProcesser.videoStatus?.isVideoEnable == true) {
                            VideoPlayer.c1_camera_play()
                        }
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
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "takePicture",
                Camera.ShutterCallback::class.java, Camera.PictureCallback::class.java,
                Camera.PictureCallback::class.java, Camera.PictureCallback::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
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
        try {
            XposedHelpers.findAndHookMethod("android.hardware.camera2.CameraManager", cl,
                "openCamera", String::class.java, CameraDevice.StateCallback::class.java, Handler::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        intercept2(param.args[1] as? CameraDevice.StateCallback, lpparam)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG openCamera: $e") }

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
                        if (ImagePlayer.isActive.value) {
                            ImagePlayer.attachSurface(c2_virtual_surface ?: return)
                        } else if (InfoProcesser.videoStatus?.isVideoEnable == true) {
                            VideoPlayer.camera2Play()
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG build: $e") }
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
                    }
                }
            )
        } catch (_: Throwable) {}

        // Class-level acquire hooks — fires regardless of WHEN the ImageReader was created
        val acquireHook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (!ImagePlayer.isActive.value) return
                val bmp = ImagePlayer.currentBitmapSnapshot() ?: return
                val image = param.result as? Image ?: return

                val readerW = try { param.thisObject.javaClass.getMethod("getWidth") .invoke(param.thisObject) as? Int ?: 0 } catch (_: Throwable) { 0 }
                val readerH = try { param.thisObject.javaClass.getMethod("getHeight").invoke(param.thisObject) as? Int ?: 0 } catch (_: Throwable) { 0 }

                try {
                    val fmt  = image.format
                    val capW = image.width.takeIf  { it > 0 } ?: readerW.takeIf { it > 0 } ?: bmp.width
                    val capH = image.height.takeIf { it > 0 } ?: readerH.takeIf { it > 0 } ?: bmp.height
                    XposedBridge.log("$TAG acquireImage FIRED fmt=$fmt ${capW}x${capH} bmp=${bmp.width}x${bmp.height}")

                    when (fmt) {
                        android.graphics.ImageFormat.JPEG -> {
                            val plane = image.planes.getOrNull(0) ?: return
                            val buf   = plane.buffer
                            val composite = compositeOnCapture(bmp, capW, capH)
                            val stream = ByteArrayOutputStream()
                            composite.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                            if (composite !== bmp) composite.recycle()
                            val jpeg = stream.toByteArray()
                            buf.clear()
                            buf.put(jpeg, 0, minOf(jpeg.size, buf.capacity()))
                            buf.rewind()
                            resolveCtx()?.let { showToast(it, "Captured: ${capW}×${capH} ← ${bmp.width}×${bmp.height}") }
                            XposedBridge.log("$TAG acquireImage JPEG: ${jpeg.size}b")
                        }
                        android.graphics.ImageFormat.YUV_420_888,
                        android.graphics.ImageFormat.NV21,
                        17 -> {
                            val composite = compositeOnCapture(bmp, capW, capH)
                            val nv21 = VideoPlayer.bitmapToNv21Public(composite, capW, capH)
                            if (composite !== bmp) composite.recycle()
                            writeNV21ToImage(image, nv21, capW, capH)
                            XposedBridge.log("$TAG acquireImage YUV: ${capW}x${capH}")
                        }
                        else -> XposedBridge.log("$TAG acquireImage: unhandled fmt=$fmt")
                    }
                } catch (e: Throwable) { XposedBridge.log("$TAG acquireImage swap: $e") }
            }
        }
        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl, "acquireLatestImage", acquireHook)
            XposedBridge.log("$TAG hooked IR.acquireLatestImage")
        } catch (e: Throwable) { XposedBridge.log("$TAG IR.acquireLatestImage: $e") }
        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl, "acquireNextImage", acquireHook)
            XposedBridge.log("$TAG hooked IR.acquireNextImage")
        } catch (e: Throwable) { XposedBridge.log("$TAG IR.acquireNextImage: $e") }
    }

    private fun writeNV21ToImage(img: Image, nv21: ByteArray, w: Int, h: Int) {
        val planes = img.planes
        val ySize  = w * h
        val yBuf   = planes[0].buffer
        val yStr   = planes[0].rowStride
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
                    original_preview_Surface = s; result.add(virt); previewReplaced = true
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
                        resolveCtx()?.let { showToast(it, "Captured: ${camW}×${camH} ← ${bmp.width}×${bmp.height}") }
                        XposedBridge.log("$TAG hookJPEGCb: done ${bmp.width}×${bmp.height} → ${camW}×${camH}")
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
}
