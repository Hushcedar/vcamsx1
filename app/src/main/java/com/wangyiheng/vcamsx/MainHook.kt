package com.wangyiheng.vcamsx

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.Image
import android.media.ImageReader
import android.media.MediaRecorder
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
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class MainHook : IXposedHookLoadPackage {

    private var c2_state_callback: CameraDevice.StateCallback? = null
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
        val captureOutputFiles: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

        fun makeFakeST(old: SurfaceTexture?): SurfaceTexture {
            old?.release()
            return if (Build.VERSION.SDK_INT >= 26) SurfaceTexture(false)
            else SurfaceTexture(10)
        }

        fun injectJpeg(bmp: Bitmap, w: Int, h: Int): ByteArray {
            val stream = ByteArrayOutputStream()
            val scaled = if (bmp.width == w && bmp.height == h) bmp
                         else Bitmap.createScaledBitmap(bmp, w, h, true)
            scaled.compress(Bitmap.CompressFormat.JPEG, 95, stream)
            if (scaled !== bmp) scaled.recycle()
            return stream.toByteArray()
        }

        fun injectIntoBuffer(bmp: Bitmap, buf: ByteBuffer, w: Int, h: Int) {
            val jpeg = injectJpeg(bmp, w, h)
            buf.clear()
            buf.put(jpeg, 0, minOf(jpeg.size, buf.capacity()))
            buf.rewind()
        }

        fun drawBitmapToSurface(bmp: Bitmap, surface: Surface) {
            if (!surface.isValid) return
            try {
                val canvas = surface.lockCanvas(null)
                canvas.drawBitmap(bmp, null,
                    RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()), null)
                surface.unlockCanvasAndPost(canvas)
            } catch (e: Exception) { XposedBridge.log("$TAG drawToSurface: $e") }
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
        hookAcquireImage(lpparam)
        hookPixelCopy(lpparam)
        hookGlReadPixels(lpparam)
        hookCaptureSessionCapture(lpparam)
        hookFileRename(lpparam)
        hookFileWrite(lpparam)
        hookBitmapCompress(lpparam)
    }

    private fun hookBitmapCompress(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("android.graphics.Bitmap",
                lpparam.classLoader, "compress",
                Bitmap.CompressFormat::class.java, Int::class.java,
                java.io.OutputStream::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val injected = ImagePlayer.currentBitmapSnapshot() ?: return
                        val fmt = param.args[0] as? Bitmap.CompressFormat ?: return
                        if (fmt != Bitmap.CompressFormat.JPEG &&
                            fmt != Bitmap.CompressFormat.WEBP) return
                        val thisBmp = param.thisObject as? Bitmap ?: return
                        if (thisBmp.width < 100 || thisBmp.height < 100) return
                        if (thisBmp === injected) return
                        val out = param.args[2] as? java.io.OutputStream ?: return
                        try {
                            val scaled = if (injected.width == thisBmp.width &&
                                            injected.height == thisBmp.height) injected
                                         else Bitmap.createScaledBitmap(
                                             injected, thisBmp.width, thisBmp.height, true)
                            scaled.compress(fmt, param.args[1] as Int, out)
                            if (scaled !== injected) scaled.recycle()
                            param.setResult(true)
                            XposedBridge.log("$TAG Bitmap.compress intercepted " +
                                "${thisBmp.width}x${thisBmp.height}")
                        } catch (e: Exception) {
                            XposedBridge.log("$TAG compress: $e")
                        }
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG hookBitmapCompress: $e") }
    }

    private fun hookAcquireImage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader
        val hook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (!ImagePlayer.isActive.value) return
                val bmp   = ImagePlayer.currentBitmapSnapshot() ?: return
                val image = param.result as? Image ?: return
                if (image.format != ImageFormat.JPEG) return
                try {
                    val plane = image.planes[0]
                    injectIntoBuffer(bmp, plane.buffer, image.width, image.height)
                    XposedBridge.log("$TAG acquireImage JPEG swapped ${image.width}x${image.height}")
                } catch (e: Exception) { XposedBridge.log("$TAG acquireImage: $e") }
            }
        }
        try { XposedHelpers.findAndHookMethod("android.media.ImageReader", cl,
            "acquireLatestImage", hook) } catch (e: Exception) { XposedBridge.log("$TAG acqLatest: $e") }
        try { XposedHelpers.findAndHookMethod("android.media.ImageReader", cl,
            "acquireNextImage", hook) } catch (e: Exception) { XposedBridge.log("$TAG acqNext: $e") }
    }

    private fun hookPixelCopy(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (Build.VERSION.SDK_INT < 26) return
        try {
            XposedHelpers.findAndHookMethod("android.view.PixelCopy",
                lpparam.classLoader, "request",
                Surface::class.java, android.graphics.Rect::class.java,
                Bitmap::class.java,
                android.view.PixelCopy.OnPixelCopyFinishedListener::class.java,
                Handler::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val bmp  = ImagePlayer.currentBitmapSnapshot() ?: return
                        val dest = param.args[2] as? Bitmap ?: return
                        val listener = param.args[3]
                            as? android.view.PixelCopy.OnPixelCopyFinishedListener ?: return
                        val handler = param.args[4] as? Handler ?: return
                        try {
                            val canvas = Canvas(dest)
                            canvas.drawBitmap(bmp, null,
                                RectF(0f, 0f, dest.width.toFloat(), dest.height.toFloat()), null)
                            handler.post { listener.onPixelCopyFinished(android.view.PixelCopy.SUCCESS) }
                            param.setResult(null)
                            XposedBridge.log("$TAG PixelCopy intercepted")
                        } catch (e: Exception) { XposedBridge.log("$TAG PixelCopy: $e") }
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG hookPixelCopy: $e") }
    }

    private fun hookGlReadPixels(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("android.opengl.GLES20",
                lpparam.classLoader, "glReadPixels",
                Int::class.java, Int::class.java, Int::class.java, Int::class.java,
                Int::class.java, Int::class.java, java.nio.Buffer::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val bmp = ImagePlayer.currentBitmapSnapshot() ?: return
                        val w   = param.args[2] as? Int ?: return
                        val h   = param.args[3] as? Int ?: return
                        if (w < 100 || h < 100) return
                        val buf = param.args[6] as? ByteBuffer ?: return
                        try {
                            val scaled = if (bmp.width == w && bmp.height == h) bmp
                                         else Bitmap.createScaledBitmap(bmp, w, h, true)
                            val rgba = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                            val m = Matrix().apply { setScale(1f, -1f); postTranslate(0f, h.toFloat()) }
                            Canvas(rgba).drawBitmap(scaled, m, null)
                            rgba.copyPixelsToBuffer(buf)
                            rgba.recycle()
                            if (scaled !== bmp) scaled.recycle()
                            param.setResult(null)
                            XposedBridge.log("$TAG glReadPixels ${w}x${h}")
                        } catch (e: Exception) { XposedBridge.log("$TAG glReadPixels: $e") }
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG hookGLRead: $e") }
    }

    private fun hookCaptureSessionCapture(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader
        val captureHook = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (!ImagePlayer.isActive.value) return
                val bmp = ImagePlayer.currentBitmapSnapshot() ?: return
                c2_virtual_surface?.let { drawBitmapToSurface(bmp, it) }
                XposedBridge.log("$TAG capture() — bitmap drawn to virtual surface")
            }
        }
        listOf("android.hardware.camera2.impl.CameraCaptureSessionImpl",
                "android.hardware.camera2.impl.CameraAdvancedSessionImpl",
                "android.hardware.camera2.CameraCaptureSession").forEach { cls ->
            try {
                XposedHelpers.findAndHookMethod(cls, cl, "capture",
                    android.hardware.camera2.CaptureRequest::class.java,
                    CameraCaptureSession.CaptureCallback::class.java,
                    Handler::class.java, captureHook)
            } catch (_: Exception) {}
            try {
                XposedHelpers.findAndHookMethod(cls, cl, "captureBurst",
                    List::class.java,
                    CameraCaptureSession.CaptureCallback::class.java,
                    Handler::class.java, captureHook)
            } catch (_: Exception) {}
        }
    }

    private fun hookFileRename(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("java.io.File", lpparam.classLoader,
                "renameTo", File::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val bmp  = ImagePlayer.currentBitmapSnapshot() ?: return
                        val dest = param.args[0] as? File ?: return
                        val path = dest.absolutePath.lowercase()
                        if (!path.endsWith(".jpg") && !path.endsWith(".jpeg") &&
                            !path.endsWith(".png")  && !path.endsWith(".webp")) return
                        if (!dest.exists() || dest.length() < 100) return
                        try {
                            FileOutputStream(dest).use { bmp.compress(
                                Bitmap.CompressFormat.JPEG, 95, it) }
                            XposedBridge.log("$TAG renameTo overwrite: ${dest.absolutePath}")
                        } catch (e: Exception) { XposedBridge.log("$TAG renameTo: $e") }
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG hookRename: $e") }
    }

    private fun hookFileWrite(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod("java.io.FileOutputStream",
                lpparam.classLoader, "write",
                ByteArray::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (!ImagePlayer.isActive.value) return
                        val bmp   = ImagePlayer.currentBitmapSnapshot() ?: return
                        val bytes = param.args[0] as? ByteArray ?: return
                        val off   = param.args[1] as? Int ?: return
                        val len   = param.args[2] as? Int ?: return
                        if (len < 3 || off + 2 >= bytes.size) return
                        if (bytes[off]     != 0xFF.toByte() ||
                            bytes[off + 1] != 0xD8.toByte() ||
                            bytes[off + 2] != 0xFF.toByte()) return
                        if (len < 10_000) return
                        try {
                            val stream = ByteArrayOutputStream()
                            bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                            val jpeg = stream.toByteArray()
                            param.args[0] = jpeg
                            param.args[1] = 0
                            param.args[2] = jpeg.size
                            XposedBridge.log("$TAG FOS.write JPEG swapped ${jpeg.size}b")
                        } catch (e: Exception) { XposedBridge.log("$TAG fosWrite: $e") }
                    }
                }
            )
        } catch (e: Exception) { XposedBridge.log("$TAG hookFileWrite: $e") }
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
            } catch (e: Exception) { XposedBridge.log("$TAG openCamera(Exec): $e") }
        }
        XposedHelpers.findAndHookMethod(
            "android.hardware.camera2.CaptureRequest\$Builder", cl,
            "addTarget", Surface::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val surface = param.args[0] as? Surface ?: return
                    if (surface == c2_virtual_surface) return
                    if (nonPreviewSurfaces.contains(surface)) { param.setResult(null); return }
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
                            val fake = SessionConfiguration(orig.sessionType,
                                listOf(fakeOut), orig.executor, orig.stateCallback)
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
            XposedHelpers.findAndHookMethod("android.media.ImageReader", cl, "newInstance",
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
                        try { cam.setPreviewTexture(makeFakeST(null)) } catch (_: Exception) {}
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
    }

    private fun hookJPEGCb(param: XC_MethodHook.MethodHookParam, idx: Int) {
        XposedHelpers.findAndHookMethod(param.args[idx].javaClass, "onPictureTaken",
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
    }

    private fun hookYUVCb(param: XC_MethodHook.MethodHookParam) {
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
    }
}
