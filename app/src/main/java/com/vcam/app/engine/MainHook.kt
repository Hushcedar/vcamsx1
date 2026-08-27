package com.vcam.app.engine

import android.app.Application
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.view.Surface
import android.view.SurfaceHolder
import com.vcam.app.engine.InfoProcesser
import com.vcam.app.engine.OutputImageFormat
import com.vcam.app.engine.VideoPlayer
import com.vcam.app.engine.VideoToFrames
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class MainHook : IXposedHookLoadPackage {

    private var c2_state_callback: CameraDevice.StateCallback? = null
    private var c2_virtual_surface: Surface?                   = null
    private val nonPreviewSurfaces: MutableSet<Surface> = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedDeviceClasses: MutableSet<String>  = Collections.newSetFromMap(ConcurrentHashMap())
    private val hookedCallbackClasses: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

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
        if (lpparam.packageName == "com.vcam.app") return
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

    private fun hookAppInit(lpparam: XC_LoadPackage.LoadPackageParam) {
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
                    } catch (e: Exception) { XposedBridge.log("$TAG init: $e") }
                }
            }
        )
    }

    private fun hookCamera1(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

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
                        } catch (e: Exception) {
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

        XposedHelpers.findAndHookMethod("android.hardware.Camera", cl,
            "takePicture",
            Camera.ShutterCallback::class.java, Camera.PictureCallback::class.java,
            Camera.PictureCallback::class.java, Camera.PictureCallback::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                    if (param.args[0] != null) hookYUVCb(param)
                    if (param.args[2] != null) hookJPEGCb(param, 2)
                }
            }
        )
    }

    private fun hookCamera2(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        XposedHelpers.findAndHookMethod("android.hardware.camera2.CameraManager", cl,
            "openCamera",
            String::class.java, CameraDevice.StateCallback::class.java, Handler::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    intercept2(param.args[1] as? CameraDevice.StateCallback, lpparam)
                }
            }
        )

        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod("android.hardware.camera2.CameraManager", cl,
                    "openCamera",
                    String::class.java, java.util.concurrent.Executor::class.java,
                    CameraDevice.StateCallback::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            intercept2(param.args[2] as? CameraDevice.StateCallback, lpparam)
                        }
                    }
                )
            } catch (e: Exception) { XposedBridge.log("$TAG openCamera(Exec): $e") }
        }

        XposedHelpers.findAndHookMethod(
            "android.hardware.camera2.CaptureRequest\$Builder", cl,
            "addTarget", Surface::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val surface = param.args[0] as? Surface ?: return
                    if (surface == c2_virtual_surface) return
                    if (nonPreviewSurfaces.contains(surface)) {
                        param.setResult(null); return
                    }
                    val virt = c2_virtual_surface ?: return
                    original_preview_Surface = surface
                    param.args[0] = virt
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            "android.hardware.camera2.CaptureRequest\$Builder", cl, "build",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) { VideoPlayer.camera2Play() }
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
                    needRecreate = true; createVirtualSurface()
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
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val virt = c2_virtual_surface ?: return
                        param.args[0] = listOf(virt)
                        XposedBridge.log("$TAG session(List) replaced")
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG session(List): $e") }

        if (Build.VERSION.SDK_INT >= 28) {
            try {
                XposedHelpers.findAndHookMethod(devCls, "createCaptureSession",
                    SessionConfiguration::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            val orig = param.args[0] as? SessionConfiguration ?: return
                            sessionConfiguration = orig
                            val virt = c2_virtual_surface ?: return
                            val fakeOut = OutputConfiguration(virt)
                            outputConfiguration = fakeOut
                            val fake = SessionConfiguration(
                                orig.sessionType, listOf(fakeOut), orig.executor, orig.stateCallback)
                            fake_sessionConfiguration = fake; param.args[0] = fake
                            XposedBridge.log("$TAG session(SC) replaced")
                        }
                    }
                )
            } catch (e: Exception) { XposedBridge.log("$TAG session(SC): $e") }

            try {
                XposedHelpers.findAndHookMethod(devCls, "createCaptureSession",
                    List::class.java, java.util.concurrent.Executor::class.java,
                    CameraCaptureSession.StateCallback::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            val virt = c2_virtual_surface ?: return
                            param.args[0] = listOf(virt)
                        }
                    }
                )
            } catch (_: Exception) {}
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
                            val surf = reader.javaClass.getMethod("getSurface").invoke(reader) as? Surface ?: return
                            nonPreviewSurfaces.add(surf)
                            XposedBridge.log("$TAG IR fmt=$fmt ${w}x${h}")
                            VideoPlayer.addImageWriterTarget(surf, fmt, w, h)
                        } catch (e: Exception) { XposedBridge.log("$TAG IR.newInstance: $e") }
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG IR hook: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl, "getSurface",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        (param.result as? Surface)?.let { nonPreviewSurfaces.add(it) }
                    }
                }
            )
        } catch (_: Exception) {}
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
        } catch (e: Exception) { XposedBridge.log("$TAG MC surface: $e") }
    }

    private fun hookMediaRecorder(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("android.media.MediaRecorder", lpparam.classLoader,
                "setCamera", Camera::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (InfoProcesser.videoStatus?.isVideoEnable != true) return
                        val cam = param.args[0] as? Camera ?: return
                        val st = makeFakeST(null)
                        try { cam.setPreviewTexture(st) } catch (_: Exception) {}
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG MR.setCamera: $e") }

        try {
            XposedHelpers.findAndHookMethod("android.media.MediaRecorder", lpparam.classLoader,
                "getSurface",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        (param.result as? Surface)?.let { nonPreviewSurfaces.add(it) }
                    }
                }
            )
        } catch (_: Exception) {}
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
                                "content://com.vcam.app.videoprovider"))
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
    }

    private fun hookJPEGCb(param: XC_MethodHook.MethodHookParam, idx: Int) {
        XposedHelpers.findAndHookMethod(param.args[idx].javaClass, "onPictureTaken",
            ByteArray::class.java, Camera::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(p: MethodHookParam) { p.args[0] = ByteArray(0) }
            }
        )
    }

    private fun hookYUVCb(param: XC_MethodHook.MethodHookParam) {
        XposedHelpers.findAndHookMethod(param.args[0].javaClass, "onPictureTaken",
            ByteArray::class.java, Camera::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(p: MethodHookParam) { p.args[0] = ByteArray(0) }
            }
        )
    }
}
