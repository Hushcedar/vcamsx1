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

    // Track ImageReader dimensions per surface for IPMAN trick
    private val readerDimensions: MutableMap<String, Pair<Int,Int>> = ConcurrentHashMap()

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

        // Last known capture dimensions from ImageReader — used for IPMAN sizing
        @Volatile @JvmField var lastCaptureW  = 1920
        @Volatile @JvmField var lastCaptureH  = 1080

        fun makeFakeST(old: SurfaceTexture?): SurfaceTexture {
            old?.release()
            return if (Build.VERSION.SDK_INT >= 26) SurfaceTexture(false)
            else SurfaceTexture(10)
        }

        fun readStatusDirect(ctx: Context): VideoStatues? {
            return try {
                val prefs = RemotePreferences(
                    ctx, "com.wangyiheng.vcamsx.preferences", "main_prefs", true)
                val json = prefs.getString("videoStatus", null) ?: return null
                Gson().fromJson(json, VideoStatues::class.java)
            } catch (e: Throwable) {
                XposedBridge.log("$TAG readStatusDirect: $e"); null
            }
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
         * IPMAN composite trick:
         * - Take real captured JPEG as base
         * - Scale our selected image to fit capture dims WITHOUT stretching
         * - Center and draw our image ON TOP of real capture
         * - Result: real capture underneath, our image covering it
         */
        fun ipmanComposite(captureW: Int, captureH: Int, realJpeg: ByteArray?): ByteArray? {
            val bmp = ImagePlayer.currentBitmapSnapshot() ?: return null

            // Create output canvas at exact capture dimensions
            val output = Bitmap.createBitmap(captureW, captureH, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            // Layer 1: real capture as background (so dimensions match perfectly)
            if (realJpeg != null && realJpeg.isNotEmpty()) {
                try {
                    val real = BitmapFactory.decodeByteArray(realJpeg, 0, realJpeg.size)
                    if (real != null) {
                        canvas.drawBitmap(real,
                            Rect(0, 0, real.width, real.height),
                            Rect(0, 0, captureW, captureH), null)
                        real.recycle()
                    }
                } catch (_: Exception) { canvas.drawColor(Color.BLACK) }
            } else {
                canvas.drawColor(Color.BLACK)
            }

            // Layer 2: our image on top, scaled to fit WITHOUT stretching, centered
            val scaleW = captureW.toFloat() / bmp.width
            val scaleH = captureH.toFloat() / bmp.height
            val scale  = minOf(scaleW, scaleH)      // fit inside, never stretch
            val dstW   = (bmp.width  * scale).toInt().coerceAtLeast(1)
            val dstH   = (bmp.height * scale).toInt().coerceAtLeast(1)
            val left   = (captureW - dstW) / 2
            val top    = (captureH - dstH) / 2

            val scaled = if (dstW == bmp.width && dstH == bmp.height) bmp
                         else Bitmap.createScaledBitmap(bmp, dstW, dstH, true)
            canvas.drawBitmap(scaled, left.toFloat(), top.toFloat(), Paint().apply {
                isFilterBitmap = true
            })
            if (scaled !== bmp) scaled.recycle()

            val out = ByteArrayOutputStream()
            output.compress(Bitmap.CompressFormat.JPEG, 95, out)
            output.recycle()
            return out.toByteArray()
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

    // ══════════════════════════════════════════════════════════════════════════
    // ORIGINAL VIDEO INJECTION — surface swap logic untouched
    // ══════════════════════════════════════════════════════════════════════════

    private fun filterAndSwapSurfaceList(original: List<Surface>): List<Surface> {
        val virt   = c2_virtual_surface ?: return original
        val result = ArrayList<Surface>(original.size)
        var replaced = false
        for (s in original) {
            when {
                nonPreviewSurfaces.contains(s) -> result.add(s)
                !replaced -> {
                    original_preview_Surface = s
                    result.add(virt)
                    replaced = true
                }
                else -> result.add(s)
            }
        }
        if (!replaced) result.add(0, virt)
        return result
    }

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
                                "videoEnable=${status?.isVideoEnable} " +
                                "imageEnable=${status?.isImageEnabled} " +
                                "volume=${status?.volume}")
                            if (status?.isVideoEnable == true && status.volume) audioEnabled = true
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
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
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
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        val cam = param.thisObject as? Camera ?: return
                        if (oriHolder != null && cam == mcamera1 && origin_preview_camera == null) {
                            c1FakeTexture = makeFakeST(c1FakeTexture)
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
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
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

        // takePicture — IPMAN trick for Camera1 shutter
        try {
            XposedHelpers.findAndHookMethod("android.hardware.Camera", cl, "takePicture",
                Camera.ShutterCallback::class.java, Camera.PictureCallback::class.java,
                Camera.PictureCallback::class.java, Camera.PictureCallback::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val status = InfoProcesser.videoStatus
                        val imageActive = ImagePlayer.isActive.value
                        val videoActive = status?.isVideoEnable == true
                        // Fix: run if EITHER image OR video injection is active
                        if (!imageActive && !videoActive) return
                        if (param.args[0] != null) hookYUVCb(param)
                        if (param.args[2] != null) hookJPEGCb(param, 2)
                        if (param.args[3] != null) hookJPEGCb(param, 3)
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG C1.takePicture: $e") }
    }

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

        // ── Messenger fix ─────────────────────────────────────────────────────
        // Messenger uses CameraDeviceImpl.createCaptureSession directly and
        // sometimes doesn't call addTarget on the builder, so
        // original_preview_Surface stays null and camera2Play() does nothing.
        //
        // Fix: hook CameraDeviceImpl.createCaptureSession directly to:
        //   1. Swap preview surface in the surface list
        //   2. Set original_preview_Surface ourselves
        //   3. Call camera2Play() after session is created via StateCallback
        hookCameraDeviceImpl(cl, lpparam)
    }

    private fun hookCameraDeviceImpl(cl: ClassLoader, lpparam: XC_LoadPackage.LoadPackageParam) {
        val implClass = "android.hardware.camera2.impl.CameraDeviceImpl"

        // List variant — Messenger on older APIs
        try {
            XposedHelpers.findAndHookMethod(implClass, cl,
                "createCaptureSession",
                List::class.java, CameraCaptureSession.StateCallback::class.java, Handler::class.java,
                object : XC_MethodHook() {
                    @Suppress("UNCHECKED_CAST")
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        InfoProcesser.initStatus()
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        createVirtualSurface()
                        val original = param.args[0] as? List<Surface> ?: return
                        // Pick the first non-nonPreview surface as preview
                        val previewSurface = original.firstOrNull { !nonPreviewSurfaces.contains(it) }
                        if (previewSurface != null) original_preview_Surface = previewSurface
                        param.args[0] = filterAndSwapSurfaceList(original)
                        // Wrap StateCallback so we call camera2Play after session created
                        val origCb = param.args[1] as? CameraCaptureSession.StateCallback
                        if (origCb != null) {
                            param.args[1] = wrapSessionCallback(origCb)
                        }
                        XposedBridge.log("$TAG impl.createCS(List) preview=$previewSurface")
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG impl.createCS(List): $e") }

        // SessionConfiguration variant — Messenger on API 28+
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod(implClass, cl,
                    "createCaptureSession", SessionConfiguration::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            InfoProcesser.initStatus()
                            if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                            createVirtualSurface()
                            val orig    = param.args[0] as? SessionConfiguration ?: return
                            val virt    = c2_virtual_surface ?: return
                            val outputs = try { orig.outputConfigurations } catch (_: Throwable) { null }
                            val newOuts = ArrayList<OutputConfiguration>()
                            var replaced = false
                            if (outputs != null) {
                                for (oc in outputs) {
                                    val s = try { oc.surface } catch (_: Throwable) { null }
                                    when {
                                        s != null && nonPreviewSurfaces.contains(s) -> newOuts.add(oc)
                                        !replaced -> {
                                            if (s != null) original_preview_Surface = s
                                            newOuts.add(OutputConfiguration(virt))
                                            replaced = true
                                        }
                                        else -> newOuts.add(oc)
                                    }
                                }
                            }
                            if (!replaced) {
                                newOuts.add(0, OutputConfiguration(virt))
                            }
                            // Wrap the stateCallback so we trigger camera2Play after configured
                            val wrappedCb = wrapSessionStateCallbackForSC(orig.stateCallback)
                            val fake = SessionConfiguration(
                                orig.sessionType, newOuts, orig.executor, wrappedCb)
                            fake_sessionConfiguration = fake
                            param.args[0] = fake
                            XposedBridge.log("$TAG impl.createCS(SC) outputs=${outputs?.size}→${newOuts.size}")
                        }
                    }
                )
            } catch (e: Throwable) { XposedBridge.log("$TAG impl.createCS(SC): $e") }
        }
    }

    /**
     * Wrap CameraCaptureSession.StateCallback so that after onConfigured()
     * we call VideoPlayer.camera2Play(). This is what triggers actual playback
     * in Messenger — without it the surface is swapped but nothing plays.
     */
    private fun wrapSessionCallback(
        original: CameraCaptureSession.StateCallback
    ): CameraCaptureSession.StateCallback {
        return object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                original.onConfigured(session)
                try { VideoPlayer.camera2Play() } catch (e: Throwable) {
                    XposedBridge.log("$TAG wrapCb.onConfigured camera2Play: $e")
                }
            }
            override fun onConfigureFailed(session: CameraCaptureSession) {
                original.onConfigureFailed(session)
            }
            override fun onClosed(session: CameraCaptureSession) {
                try { original.onClosed(session) } catch (_: Throwable) {}
            }
        }
    }

    private fun wrapSessionStateCallbackForSC(
        original: CameraCaptureSession.StateCallback?
    ): CameraCaptureSession.StateCallback {
        return object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                try { original?.onConfigured(session) } catch (_: Throwable) {}
                try { VideoPlayer.camera2Play() } catch (e: Throwable) {
                    XposedBridge.log("$TAG wrapSC.onConfigured camera2Play: $e")
                }
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
                            val fake = SessionConfiguration(
                                orig.sessionType, newOuts, orig.executor, orig.stateCallback)
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
    // IMAGE READER — IPMAN trick with auto aspect ratio correction
    // ══════════════════════════════════════════════════════════════════════════

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
                        try {
                            val surf = reader.javaClass.getMethod("getSurface")
                                .invoke(reader) as? Surface ?: return
                            nonPreviewSurfaces.add(surf)
                            // Store dims for this reader surface
                            readerDimensions[surf.toString()] = Pair(w, h)
                            // Update last known capture dimensions
                            // Only update if this looks like a still capture (>= 720p)
                            if (w >= 720 || h >= 720) {
                                lastCaptureW = w; lastCaptureH = h
                            }
                            XposedBridge.log("$TAG IR fmt=$fmt ${w}x${h}")
                            VideoPlayer.addImageWriterTarget(surf, fmt, w, h)
                            hookAcquireOnReaderClass(reader.javaClass, w, h)
                            // IPMAN toast — show capture dimensions like his app
                            context?.let { ctx ->
                                showToast(ctx, "VCamSX: Capture size ${w}×${h}")
                            }
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

    private fun hookAcquireOnReaderClass(cls: Class<*>, captureW: Int = 0, captureH: Int = 0) {
        // Key by class + dims so we hook once per unique (class, resolution)
        val key = "${cls.name}_${captureW}x${captureH}"
        if (!hookedReaderClasses.add(key)) return

        val cW = captureW; val cH = captureH

        val swapHook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (!ImagePlayer.isActive.value) return
                val image = param.result as? Image ?: return
                try {
                    if (image.format != android.graphics.ImageFormat.JPEG) return
                    val plane = image.planes.getOrNull(0) ?: return
                    val buf   = plane.buffer

                    // Use actual image dimensions, fall back to registered dims
                    val actualW = if (image.width  > 0) image.width  else if (cW > 0) cW else lastCaptureW
                    val actualH = if (image.height > 0) image.height else if (cH > 0) cH else lastCaptureH

                    // Read real capture JPEG for composite base
                    val realBytes: ByteArray? = if (buf.remaining() > 0) {
                        val arr = ByteArray(buf.remaining())
                        buf.get(arr); buf.rewind(); arr
                    } else null

                    // IPMAN composite: our image centered on top of real capture
                    val composite = ipmanComposite(actualW, actualH, realBytes) ?: return

                    // IPMAN-style toast with corrected dimensions
                    val bmp = ImagePlayer.currentBitmapSnapshot()
                    context?.let { ctx ->
                        showToast(ctx, "VCamSX: ${bmp?.width}×${bmp?.height} → ${actualW}×${actualH}")
                    }

                    buf.clear()
                    buf.put(composite, 0, minOf(composite.size, buf.capacity()))
                    buf.rewind()
                    XposedBridge.log("$TAG IPMAN: img=${bmp?.width}x${bmp?.height} " +
                        "cap=${actualW}x${actualH} jpeg=${composite.size}b")
                } catch (e: Throwable) { XposedBridge.log("$TAG acquireImage: $e") }
            }
        }
        try { XposedHelpers.findAndHookMethod(cls, "acquireLatestImage", swapHook) }
            catch (e: Throwable) { XposedBridge.log("$TAG acquireLatest: $e") }
        try { XposedHelpers.findAndHookMethod(cls, "acquireNextImage", swapHook) }
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
    // AUDIO INJECTION — unchanged
    // ══════════════════════════════════════════════════════════════════════════

    private fun hookAudioRecord(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl   = lpparam.classLoader
        val proc = lpparam.processName

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl, "startRecording",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val ctx    = getContextFromActivityThread()
                        val status = if (ctx != null) readStatusDirect(ctx)
                                     else InfoProcesser.videoStatus
                        XposedBridge.log("$TAG AR.startRecording proc=$proc " +
                            "videoEnable=${status?.isVideoEnable} volume=${status?.volume}")
                        if (status?.isVideoEnable == true && status.volume) {
                            audioEnabled = true; AudioInjector.enabled = true; AudioInjector.start()
                            XposedBridge.log("$TAG AudioInjector STARTED proc=$proc")
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
                "read", ByteArray::class.java,
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
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read[]: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteArray::class.java,
                Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
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
                "read", ByteBuffer::class.java,
                Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
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
    // ORIGINAL HELPERS — UNTOUCHED
    // ══════════════════════════════════════════════════════════════════════════

    private fun createVirtualSurface() {
        if (!needRecreate && c2_virtual_surface?.isValid == true) return
        c2VirtualSurfaceTexture?.release(); c2_virtual_surface?.release()
        c2VirtualSurfaceTexture = SurfaceTexture(10)
            .also { it.setDefaultBufferSize(720, 1280) }
        c2_virtual_surface = Surface(c2VirtualSurfaceTexture!!)
        needRecreate       = false
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

    private fun hookJPEGCb(param: XC_MethodHook.MethodHookParam, idx: Int) {
        try {
            XposedHelpers.findAndHookMethod(param.args[idx].javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val realJpeg = p.args[0] as? ByteArray
                        // Get picture size from camera parameters
                        val cam  = p.args[1] as? Camera
                        val size = try { cam?.parameters?.pictureSize } catch (_: Exception) { null }
                        val capW = size?.width  ?: lastCaptureW
                        val capH = size?.height ?: lastCaptureH
                        val composite = ipmanComposite(capW, capH, realJpeg) ?: return
                        p.args[0] = composite
                        context?.let { ctx ->
                            showToast(ctx, "VCamSX: Snapshot ${capW}×${capH}")
                        }
                        XposedBridge.log("$TAG JPEG cb IPMAN ${capW}x${capH}")
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
                        if (!ImagePlayer.isActive.value) return
                        val realJpeg = p.args[0] as? ByteArray
                        val cam  = p.args[1] as? Camera
                        val size = try { cam?.parameters?.pictureSize } catch (_: Exception) { null }
                        val capW = size?.width  ?: lastCaptureW
                        val capH = size?.height ?: lastCaptureH
                        val composite = ipmanComposite(capW, capH, realJpeg) ?: return
                        p.args[0] = composite
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookYUVCb: $e") }
    }
}
