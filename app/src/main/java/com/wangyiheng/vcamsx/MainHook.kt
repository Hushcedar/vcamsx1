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
    private val nonPreviewSurfaces: MutableSet<Surface>  = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedDeviceClasses: MutableSet<String>  = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedCallbackClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedReaderClasses: MutableSet<String>  = Collections.newSetFromMap(ConcurrentHashMap())

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
        @JvmField var hw_decode_obj:         VideoToFrames? = null
        @JvmField var mcamera1:              Camera?        = null
        @JvmField var camera_onPreviewFrame: Camera?        = null
        @JvmField var camera_callback_calss: Class<*>?     = null
        @Volatile @JvmField var data_buffer: ByteArray     = byteArrayOf()

        @Volatile @JvmField var audioEnabled = false

        fun makeFakeST(old: SurfaceTexture?): SurfaceTexture {
            old?.release()
            return if (Build.VERSION.SDK_INT >= 26) SurfaceTexture(false)
            else SurfaceTexture(10)
        }

        fun readStatusDirect(ctx: Context): VideoStatues? {
            return try {
                val prefs = RemotePreferences(
                    ctx,
                    "com.wangyiheng.vcamsx.preferences",
                    "main_prefs",
                    true
                )
                val json = prefs.getString("videoStatus", null) ?: return null
                Gson().fromJson(json, VideoStatues::class.java)
            } catch (e: Throwable) {
                XposedBridge.log("$TAG readStatusDirect error: $e")
                null
            }
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
                                "videoEnable=${status?.isVideoEnable} volume=${status?.volume}")
                            audioEnabled = status?.isVideoEnable == true && status.volume
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
        XposedHelpers.findAndHookMethod(
            "android.hardware.camera2.CameraManager", cl,
            "openCamera", String::class.java, CameraDevice.StateCallback::class.java, Handler::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    intercept2(param.args[1] as? CameraDevice.StateCallback, lpparam)
                }
            }
        )
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod(
                    "android.hardware.camera2.CameraManager", cl,
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
                            val origOutputs = try { orig.outputConfigurations } catch (_: Throwable) { null }
                            val newOutputs  = ArrayList<OutputConfiguration>()
                            var replaced    = false
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
            } catch (e: Throwable) { XposedBridge.log("$TAG session(List,Exec): $e") }
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
                        try {
                            val surf = reader.javaClass.getMethod("getSurface").invoke(reader) as? Surface ?: return
                            nonPreviewSurfaces.add(surf)
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
                    val plane  = image.planes.getOrNull(0) ?: return
                    val buf    = plane.buffer
                    val stream = ByteArrayOutputStream()
                    val scaled = if (bmp.width == image.width && bmp.height == image.height) bmp
                                 else Bitmap.createScaledBitmap(bmp, image.width, image.height, true)
                    scaled.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                    if (scaled !== bmp) scaled.recycle()
                    val jpeg = stream.toByteArray()
                    buf.clear(); buf.put(jpeg, 0, minOf(jpeg.size, buf.capacity())); buf.rewind()
                } catch (e: Throwable) { XposedBridge.log("$TAG acquireImage swap: $e") }
            }
        }
        try { XposedHelpers.findAndHookMethod(cls, "acquireLatestImage", swapHook) } catch (_: Throwable) {}
        try { XposedHelpers.findAndHookMethod(cls, "acquireNextImage",   swapHook) } catch (_: Throwable) {}
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
    // AUDIO INJECTION — Bypasses MainHook.context entirely.
    // Uses ActivityThread.currentApplication() which works in ALL processes
    // including :voip/:calls, even before Application.onCreate fires.
    // ══════════════════════════════════════════════════════════════════════════

    private fun getContextFromActivityThread(): Context? {
        return try {
            val atClass  = Class.forName("android.app.ActivityThread")
            val atMethod = atClass.getMethod("currentApplication")
            (atMethod.invoke(null) as? Application)?.applicationContext
        } catch (_: Throwable) {
            context  // fallback to MainHook.context
        }
    }

    private fun getAudioStatus(): Boolean {
        // Try ActivityThread context first (works in :voip before onCreate)
        val ctx = getContextFromActivityThread()
        val status = if (ctx != null) readStatusDirect(ctx) else InfoProcesser.videoStatus
        return status?.isVideoEnable == true && status.volume
    }

    private fun hookAudioRecord(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl   = lpparam.classLoader
        val proc = lpparam.processName

        XposedBridge.log("$TAG hookAudioRecord installing proc=$proc")

        // ── startRecording ────────────────────────────────────────────────────
        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl, "startRecording",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val enabled = getAudioStatus()
                        XposedBridge.log("$TAG AR.startRecording proc=$proc enabled=$enabled")
                        if (enabled) {
                            audioEnabled          = true
                            AudioInjector.enabled = true
                            AudioInjector.start()
                            XposedBridge.log("$TAG AudioInjector STARTED proc=$proc")
                        } else {
                            XposedBridge.log("$TAG AudioInjector NOT started — status false")
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.startRecording install: $e") }

        // ── stop ──────────────────────────────────────────────────────────────
        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl, "stop",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (AudioInjector.enabled) {
                            audioEnabled          = false
                            AudioInjector.enabled = false
                            AudioInjector.stop()
                            XposedBridge.log("$TAG AudioInjector STOPPED proc=$proc")
                        }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.stop install: $e") }

        // ── read(byte[], int, int) ────────────────────────────────────────────
        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteArray::class.java,
                Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf    = param.args[0] as? ByteArray ?: return
                        val offset = (param.args[1] as? Int) ?: 0
                        val size   = (param.args[2] as? Int) ?: (buf.size - offset)
                        val n      = AudioInjector.read(buf, offset, size)
                        if (n > 0) { param.result = n; XposedBridge.log("$TAG AR.read[] $n bytes") }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read[] install: $e") }

        // ── read(byte[], int, int, int) ───────────────────────────────────────
        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteArray::class.java,
                Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf    = param.args[0] as? ByteArray ?: return
                        val offset = (param.args[1] as? Int) ?: 0
                        val size   = (param.args[2] as? Int) ?: (buf.size - offset)
                        val n      = AudioInjector.read(buf, offset, size)
                        if (n > 0) { param.result = n; XposedBridge.log("$TAG AR.read[4] $n bytes") }
                    }
                }
            )
        } catch (_: Throwable) {}

        // ── read(ByteBuffer, int) ─────────────────────────────────────────────
        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteBuffer::class.java, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf  = param.args[0] as? ByteBuffer ?: return
                        val size = (param.args[1] as? Int) ?: return
                        val n    = AudioInjector.read(buf, size)
                        if (n > 0) { param.result = n; XposedBridge.log("$TAG AR.read(BB) $n bytes") }
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG AR.read(BB) install: $e") }

        // ── read(ByteBuffer, int, int) ────────────────────────────────────────
        try {
            XposedHelpers.findAndHookMethod("android.media.AudioRecord", cl,
                "read", ByteBuffer::class.java,
                Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!AudioInjector.enabled) return
                        val buf  = param.args[0] as? ByteBuffer ?: return
                        val size = (param.args[1] as? Int) ?: return
                        val n    = AudioInjector.read(buf, size)
                        if (n > 0) param.result = n
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
        try {
            XposedHelpers.findAndHookMethod(param.args[idx].javaClass, "onPictureTaken",
                ByteArray::class.java, Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(p: MethodHookParam) {
                        val bmp    = ImagePlayer.currentBitmapSnapshot() ?: return
                        val stream = ByteArrayOutputStream()
                        bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                        p.args[0] = stream.toByteArray()
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
                        val bmp    = ImagePlayer.currentBitmapSnapshot() ?: return
                        val stream = ByteArrayOutputStream()
                        bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                        p.args[0] = stream.toByteArray()
                    }
                }
            )
        } catch (e: Throwable) { XposedBridge.log("$TAG hookYUVCb install: $e") }
    }
}
