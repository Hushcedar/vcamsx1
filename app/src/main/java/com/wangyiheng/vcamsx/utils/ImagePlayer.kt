package com.wangyiheng.vcamsx.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLExt
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import android.view.Surface
import com.wangyiheng.vcamsx.data.models.VideoStatues
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ImagePlayer {
    private const val TAG = "VCamSX-ImagePlayer"
    
    // ── State ────────────────────────────────────────────────────────────────
    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive
    
    private var currentBitmap: Bitmap? = null
    private var currentVideoFile: File? = null
    private var mediaCodec: MediaCodec? = null
    var surfaceProvider: (() -> Surface?)? = null
    
    // ── Public API ────────────────────────────────────────────────────────────
    fun setSurfaceProvider(provider: () -> Surface?) {
        surfaceProvider = provider
    }
    
    fun loadImage(context: Context, uri: Uri): Boolean {
        try {
            val resolver: ContentResolver = context.contentResolver
            val inputStream = resolver.openInputStream(uri) ?: return false
            
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            
            if (bitmap == null) {
                Log.e(TAG, "Failed to decode bitmap")
                return false
            }
            
            currentBitmap = bitmap
            Log.d(TAG, "Image loaded: ${bitmap.width}x${bitmap.height}")
            
            val videoFile = File(context.cacheDir, "image_video.mp4")
            bitmapToMp4Loop(bitmap, videoFile)
            
            if (videoFile.exists()) {
                currentVideoFile = videoFile
                Log.d(TAG, "MP4 created: ${videoFile.length()} bytes")
                return true
            } else {
                Log.e(TAG, "Failed to create MP4 from image")
                return false
            }
        } catch (e: Exception) {
            Log.e(TAG, "loadImage error: ${e.message}", e)
            return false
        }
    }
    
    fun enable() {
        if (currentBitmap == null) {
            Log.e(TAG, "No image loaded")
            return
        }
        
        if (currentVideoFile == null || !currentVideoFile!!.exists()) {
            Log.e(TAG, "No video file available")
            return
        }
        
        _isActive.value = true
        triggerC2ReaderPlay()
        Log.d(TAG, "ImagePlayer enabled")
    }
    
    fun disable() {
        _isActive.value = false
        stopDecoder()
        Log.d(TAG, "ImagePlayer disabled")
    }
    
    fun attachSurface(surface: Surface): Boolean {
        if (!_isActive.value) {
            Log.d(TAG, "ImagePlayer not active, skipping attach")
            return false
        }
        
        return try {
            startDecoder(surface)
            true
        } catch (e: Exception) {
            Log.e(TAG, "attachSurface error: ${e.message}", e)
            false
        }
    }
    
    fun getCurrentBitmapSnapshot(): Bitmap? = currentBitmap
    
    fun activateInjection() {
        enable()
    }
    
    fun stop() {
        disable()
    }
    
    // ── Decoder ──────────────────────────────────────────────────────────────
    private fun startDecoder(surface: Surface) {
        val videoFile = currentVideoFile ?: return
        
        val format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 720, 1280)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 10)
        
        mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        mediaCodec?.configure(format, surface, null, 0)
        mediaCodec?.start()
        
        Log.d(TAG, "Decoder started")
    }
    
    private fun stopDecoder() {
        try {
            mediaCodec?.stop()
            mediaCodec?.release()
        } catch (_: Exception) {}
        mediaCodec = null
    }
    
    private fun triggerC2ReaderPlay() {
        Log.d(TAG, "triggerC2ReaderPlay called")
        VideoStatues.isImageMode = true
    }
    
    // ── MP4 Generation ──────────────────────────────────────────────────────
    private fun bitmapToMp4Loop(src: Bitmap, outFile: File) {
        val W            = 720
        val H            = 1280
        val FPS          = 10
        val TOTAL_FRAMES = 100          // 10 s at 10 fps
        val FRAME_US     = 100_000L     // 1 000 000 / 10

        val scaled = if (src.width == W && src.height == H) src
                     else Bitmap.createScaledBitmap(src, W, H, true)

        // ── Codec ────────────────────────────────────────────────────────────────
        val mf = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, W, H).apply {
            setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            setInteger(MediaFormat.KEY_BIT_RATE,         2_000_000)
            setInteger(MediaFormat.KEY_FRAME_RATE,       FPS)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
        }
        val codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        codec.configure(mf, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        val inputSurface = codec.createInputSurface()
        codec.start()

        // ── EGL ──────────────────────────────────────────────────────────────────
        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        EGL14.eglInitialize(eglDisplay, IntArray(1), 0, IntArray(1), 0)

        val cfgAttr = intArrayOf(
            EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8, EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE,    EGL14.EGL_WINDOW_BIT,
            EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        EGL14.eglChooseConfig(eglDisplay, cfgAttr, 0, configs, 0, 1, IntArray(1), 0)
        val cfg = configs[0]!!

        val eglContext = EGL14.eglCreateContext(eglDisplay, cfg, EGL14.EGL_NO_CONTEXT,
            intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE), 0)
        val eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, cfg, inputSurface,
            intArrayOf(EGL14.EGL_NONE), 0)
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)

        // ── GL program ───────────────────────────────────────────────────────────
        val VERT = "attribute vec4 aPos;attribute vec2 aTex;varying vec2 vTex;" +
                   "void main(){gl_Position=aPos;vTex=aTex;}"
        val FRAG = "precision mediump float;uniform sampler2D uTex;varying vec2 vTex;" +
                   "void main(){gl_FragColor=texture2D(uTex,vTex);}"

        fun compileShader(type: Int, src: String) = GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, src); GLES20.glCompileShader(it)
        }
        val prog = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, compileShader(GLES20.GL_VERTEX_SHADER,   VERT))
            GLES20.glAttachShader(it, compileShader(GLES20.GL_FRAGMENT_SHADER, FRAG))
            GLES20.glLinkProgram(it)
        }

        // Upload bitmap as GL texture once
        val texIds = IntArray(1)
        GLES20.glGenTextures(1, texIds, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIds[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,     GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,     GLES20.GL_CLAMP_TO_EDGE)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, scaled, 0)

        fun fb(d: FloatArray) = ByteBuffer.allocateDirect(d.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().also { it.put(d); it.position(0) }
        val POS = fb(floatArrayOf(-1f,-1f, 1f,-1f, -1f,1f, 1f,1f))
        val UV  = fb(floatArrayOf( 0f, 1f, 1f, 1f,  0f,0f, 1f,0f))

        val aPosLoc = GLES20.glGetAttribLocation(prog,  "aPos")
        val aTexLoc = GLES20.glGetAttribLocation(prog,  "aTex")
        val uTexLoc = GLES20.glGetUniformLocation(prog, "uTex")
        GLES20.glUseProgram(prog)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIds[0])
        GLES20.glUniform1i(uTexLoc, 0)
        GLES20.glEnableVertexAttribArray(aPosLoc)
        GLES20.glEnableVertexAttribArray(aTexLoc)
        GLES20.glVertexAttribPointer(aPosLoc, 2, GLES20.GL_FLOAT, false, 0, POS)
        GLES20.glVertexAttribPointer(aTexLoc, 2, GLES20.GL_FLOAT, false, 0, UV)

        // ── Muxer ────────────────────────────────────────────────────────────────
        val muxer = MediaMuxer(outFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        var trackIdx     = -1
        var muxerStarted = false
        val info         = MediaCodec.BufferInfo()

        fun drain(eos: Boolean) {
            val deadline = System.currentTimeMillis() + if (eos) 3000L else 150L
            while (System.currentTimeMillis() < deadline) {
                val idx = codec.dequeueOutputBuffer(info, if (eos) 100_000L else 5_000L)
                when {
                    idx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                        trackIdx = muxer.addTrack(codec.outputFormat)
                        muxer.start(); muxerStarted = true
                    }
                    idx >= 0 -> {
                        val buf = codec.getOutputBuffer(idx)
                        if (buf != null && muxerStarted && trackIdx >= 0 && info.size > 0 &&
                            (info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0)
                            muxer.writeSampleData(trackIdx, buf, info)
                        codec.releaseOutputBuffer(idx, false)
                        if ((info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) return
                        if (!eos) return
                    }
                    else -> if (!eos) return
                }
            }
        }

        // ── Encode loop ──────────────────────────────────────────────────────────
        for (i in 0 until TOTAL_FRAMES) {
            GLES20.glViewport(0, 0, W, H)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
            EGLExt.eglPresentationTimeANDROID(eglDisplay, eglSurface, i * FRAME_US * 1000L)
            EGL14.eglSwapBuffers(eglDisplay, eglSurface)
            drain(false)
        }

        codec.signalEndOfInputStream()
        drain(true)

        // ── Cleanup ──────────────────────────────────────────────────────────────
        try { EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT) } catch (_: Exception) {}
        try { EGL14.eglDestroySurface(eglDisplay, eglSurface) }  catch (_: Exception) {}
        try { EGL14.eglDestroyContext(eglDisplay, eglContext) }  catch (_: Exception) {}
        try { EGL14.eglTerminate(eglDisplay) }                   catch (_: Exception) {}
        try { codec.stop() }                                      catch (_: Exception) {}
        codec.release()
        if (muxerStarted) try { muxer.stop() } catch (_: Exception) {}
        muxer.release()
        inputSurface.release()
        if (scaled !== src) scaled.recycle()
    }
}
