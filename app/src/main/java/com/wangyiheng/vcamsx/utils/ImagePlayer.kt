package com.wangyiheng.vcamsx.utils

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
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ImagePlayer {
    private const val TAG    = "VCamSX-ImagePlayer"
    private const val NV21_W = 720
    private const val NV21_H = 1280

    val isActive  = mutableStateOf(false)
    val hasImage  = mutableStateOf(false)

    @Volatile var currentBitmap: Bitmap? = null
        private set

    private val _loadResult = MutableStateFlow<Boolean?>(null)
    val loadResult = _loadResult.asStateFlow()
    fun clearLoadResult() { _loadResult.value = null }

    fun loadImage(context: Context, uri: Uri) {
        val bytes: ByteArray? = try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (t: Throwable) {
            Log.e(TAG, "readBytes: ${t.message}"); null
        }
        if (bytes == null || bytes.isEmpty()) {
            _loadResult.value = false; return
        }

        Thread({
            try {
                val probe = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, probe)
                if (probe.outWidth <= 0 || probe.outHeight <= 0) {
                    _loadResult.value = false; return@Thread
                }

                var sample = 1
                while (probe.outWidth / sample > 1080 || probe.outHeight / sample > 1920) sample *= 2

                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size,
                    BitmapFactory.Options().apply {
                        inSampleSize      = sample
                        inPreferredConfig = Bitmap.Config.ARGB_8888
                    }
                ) ?: run { _loadResult.value = false; return@Thread }

                currentBitmap?.recycle()
                currentBitmap = bmp
                Log.d(TAG, "decoded ${bmp.width}x${bmp.height} sample=$sample")

                // Encode image into copied_video.mp4 — same file video uses.
                // VideoToFrames + content provider picks it up automatically.
                val outDir  = context.getExternalFilesDir(null) ?: context.filesDir
                val outFile = File(outDir, "copied_video.mp4")
                Log.d(TAG, "encoding image→mp4...")
                bitmapToMp4Loop(bmp, outFile)
                Log.d(TAG, "image→mp4 done: ${outFile.length()}b")

                hasImage.value    = true
                isActive.value    = false
                _loadResult.value = true

            } catch (t: Throwable) {
                Log.e(TAG, "decode thread: ${t.message}", t)
                _loadResult.value = false
            }
        }, "VCamSX-ImgLoad").apply { isDaemon = true; start() }
    }

    fun activateInjection() {
        if (!hasImage.value) return
        isActive.value = true
        // Trigger the video player to reload with the new mp4 (which is our image)
        VideoPlayer.reload()
        // Also restart camera feeds so they pick up the new file
        VideoPlayer.camera2Play()
    }

    fun stop() {
        isActive.value = false
    }

    fun reset() {
        stop()
        currentBitmap?.recycle()
        currentBitmap = null
        hasImage.value = false
    }

    fun currentBitmapSnapshot(): Bitmap? = currentBitmap

    // These are no-ops now — video path handles transforms
    fun attachSurface(surface: Surface) {}
    fun rotate()  { VideoPlayer.rotate() }
    fun flip()    { VideoPlayer.flip() }
    fun zoomIn()  { VideoPlayer.zoomIn() }
    fun zoomOut() { VideoPlayer.zoomOut() }
    fun adjustOffset(dx: Int, dy: Int) { VideoPlayer.adjustOffset(dx, dy) }

    // ── EGL MP4 encoder ───────────────────────────────────────────────────────
    private fun bitmapToMp4Loop(src: Bitmap, outFile: File) {
        val W = 720; val H = 1280; val FPS = 10
        val TOTAL_FRAMES = 100; val FRAME_US = 100_000L

        val scaled = if (src.width == W && src.height == H) src
                     else Bitmap.createScaledBitmap(src, W, H, true)

        val mf = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, W, H).apply {
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            setInteger(MediaFormat.KEY_BIT_RATE,         2_000_000)
            setInteger(MediaFormat.KEY_FRAME_RATE,       FPS)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
        }
        val codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        codec.configure(mf, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        val inputSurface = codec.createInputSurface()
        codec.start()

        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        EGL14.eglInitialize(eglDisplay, IntArray(1), 0, IntArray(1), 0)
        val cfgAttr = intArrayOf(
            EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8, EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
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

        val VERT = "attribute vec4 aPos;attribute vec2 aTex;varying vec2 vTex;void main(){gl_Position=aPos;vTex=aTex;}"
        val FRAG = "precision mediump float;uniform sampler2D uTex;varying vec2 vTex;void main(){gl_FragColor=texture2D(uTex,vTex);}"
        fun shader(type: Int, src: String) = GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, src); GLES20.glCompileShader(it)
        }
        val prog = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, shader(GLES20.GL_VERTEX_SHADER, VERT))
            GLES20.glAttachShader(it, shader(GLES20.GL_FRAGMENT_SHADER, FRAG))
            GLES20.glLinkProgram(it)
        }
        val texIds = IntArray(1)
        GLES20.glGenTextures(1, texIds, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIds[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, scaled, 0)

        fun fb(d: FloatArray) = ByteBuffer.allocateDirect(d.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().also { it.put(d); it.position(0) }
        val POS = fb(floatArrayOf(-1f,-1f, 1f,-1f, -1f,1f, 1f,1f))
        val UV  = fb(floatArrayOf( 0f, 1f, 1f, 1f,  0f,0f, 1f,0f))

        val aPosLoc = GLES20.glGetAttribLocation(prog, "aPos")
        val aTexLoc = GLES20.glGetAttribLocation(prog, "aTex")
        val uTexLoc = GLES20.glGetUniformLocation(prog, "uTex")
        GLES20.glUseProgram(prog)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIds[0])
        GLES20.glUniform1i(uTexLoc, 0)
        GLES20.glEnableVertexAttribArray(aPosLoc)
        GLES20.glEnableVertexAttribArray(aTexLoc)
        GLES20.glVertexAttribPointer(aPosLoc, 2, GLES20.GL_FLOAT, false, 0, POS)
        GLES20.glVertexAttribPointer(aTexLoc, 2, GLES20.GL_FLOAT, false, 0, UV)

        val muxer = MediaMuxer(outFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        var trackIdx = -1; var muxerStarted = false
        val info = MediaCodec.BufferInfo()

        fun drain(eos: Boolean) {
            val deadline = System.currentTimeMillis() + if (eos) 3000L else 150L
            while (System.currentTimeMillis() < deadline) {
                val idx = codec.dequeueOutputBuffer(info, if (eos) 100_000L else 5_000L)
                when {
                    idx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                        trackIdx = muxer.addTrack(codec.outputFormat); muxer.start(); muxerStarted = true
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

    fun bitmapToNV21(src: Bitmap, outW: Int, outH: Int): ByteArray {
        val bmp = if (src.width != outW || src.height != outH)
            Bitmap.createScaledBitmap(src, outW, outH, true) else src
        val argb  = IntArray(outW * outH)
        bmp.getPixels(argb, 0, outW, 0, 0, outW, outH)
        val nv21  = ByteArray(outW * outH * 3 / 2)
        val frame = outW * outH
        var yIdx  = 0; var uvIdx = frame
        for (row in 0 until outH) {
            for (col in 0 until outW) {
                val p = argb[yIdx]
                val r = (p shr 16) and 0xFF
                val g = (p shr  8) and 0xFF
                val b =  p         and 0xFF
                val y = (( 66*r + 129*g +  25*b + 128) shr 8) + 16
                val u = ((-38*r -  74*g + 112*b + 128) shr 8) + 128
                val v = ((112*r -  94*g -  18*b + 128) shr 8) + 128
                nv21[yIdx++] = y.coerceIn(0, 255).toByte()
                if (row % 2 == 0 && col % 2 == 0) {
                    nv21[uvIdx++] = v.coerceIn(0, 255).toByte()
                    nv21[uvIdx++] = u.coerceIn(0, 255).toByte()
                }
            }
        }
        return nv21
    }
}
