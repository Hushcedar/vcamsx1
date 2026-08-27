package com.vcam.app.engine

import android.graphics.SurfaceTexture
import android.opengl.*
import android.util.Log
import android.view.Surface
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * GL ES 2.0: MediaPlayer → OES input texture → MVP → target Surface.
 *
 * KEY INSIGHT — why all previous AR formulas were wrong:
 *
 *   SurfaceTexture.getTransformMatrix() returns a matrix that already maps
 *   UV [0,1]×[0,1] DIRECTLY to the video content pixels, compensating for
 *   any letterboxing MediaPlayer applied. Sampling the full quad [0,1]×[0,1]
 *   through texMatrix gives you exactly the video content filling the surface.
 *
 *   Adding COVER scaling on top of texMatrix = double-scaling = zoom.
 *   That was the zoom bug. Every AR calculation we applied made it worse.
 *
 * CORRECT approach:
 *   MVP = translate × rotate × scale(userZoom only)
 *   texMatrix handles content fitting automatically.
 *   We never touch AR in the MVP.
 *
 * Rotation:
 *   At 90°/270°, rotating the MVP rotates the content. A portrait video in a
 *   portrait screen appears as a landscape strip — exactly like tilting your
 *   phone. No extra scaling needed or wanted.
 */
class VideoSurfaceTransformer(
    private val targetSurface: Surface,
    @Volatile var rotationDeg: Int = 0,
    @Volatile var flipH: Boolean   = false,
    private val bufferWidth: Int   = 720,
    private val bufferHeight: Int  = 1280
) {
    companion object {
        private const val TAG     = "VCamSX-GL"
        private const val MIN_DIM = 4

        private val VERT_SRC = """
            attribute vec4 aPosition;
            attribute vec4 aTexCoord;
            varying   vec2 vTexCoord;
            uniform   mat4 uMVP;
            uniform   mat4 uTexMtx;
            void main() {
                gl_Position = uMVP * aPosition;
                vTexCoord   = (uTexMtx * aTexCoord).xy;
            }
        """.trimIndent()

        private val FRAG_SRC = """
            #extension GL_OES_EGL_image_external : require
            precision mediump float;
            uniform samplerExternalOES uTexture;
            varying vec2 vTexCoord;
            void main() {
                gl_FragColor = texture2D(uTexture, vTexCoord);
            }
        """.trimIndent()

        private val QUAD_V = floatArrayOf(-1f, -1f,  1f, -1f, -1f, 1f,  1f, 1f)
        private val QUAD_T = floatArrayOf( 0f,  0f,  1f,  0f,  0f, 1f,  1f, 1f)
    }

    @Volatile var offsetX: Float       = 0f
    @Volatile var offsetY: Float       = 0f
    @Volatile var scaleValue: Float    = 1.0f
    @Volatile var needsRedraw: Boolean = false

    var inputSurface: Surface? = null
        private set

    private val running    = AtomicBoolean(false)
    private val frameReady = AtomicBoolean(false)
    private val initLatch  = CountDownLatch(1)
    private var glThread: Thread? = null

    private var eglDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext = EGL14.EGL_NO_CONTEXT
    private var eglSurface = EGL14.EGL_NO_SURFACE
    private var oesTexId   = 0
    private var glProgram  = 0
    private var surfW      = 0
    private var surfH      = 0
    private var inputST: SurfaceTexture? = null

    fun start(): Boolean {
        if (!running.compareAndSet(false, true)) return false
        glThread = Thread(::glLoop, "VCamSX-GL").apply { isDaemon = true; start() }
        return initLatch.await(2, TimeUnit.SECONDS)
    }

    fun stop() {
        running.set(false)
        glThread?.interrupt()
        glThread = null
    }

    private fun glLoop() {
        try {
            setupEGL()
            setupProgram()
            setupOESTexture()

            var lastTexMtx = FloatArray(16).also { Matrix.setIdentityM(it, 0) }
            val texMatrix  = FloatArray(16)
            val sw = IntArray(1); val sh = IntArray(1)

            while (running.get()) {
                EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_WIDTH,  sw, 0)
                EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_HEIGHT, sh, 0)
                if (sw[0] > MIN_DIM && sh[0] > MIN_DIM) { surfW = sw[0]; surfH = sh[0] }
                if (surfW == 0 || surfH == 0) { Thread.sleep(8); continue }

                when {
                    frameReady.compareAndSet(true, false) -> {
                        inputST?.updateTexImage()
                        inputST?.getTransformMatrix(texMatrix)
                        lastTexMtx = texMatrix.copyOf()
                        renderFrame(texMatrix)
                        EGL14.eglSwapBuffers(eglDisplay, eglSurface)
                        needsRedraw = false
                    }
                    needsRedraw -> {
                        renderFrame(lastTexMtx)
                        EGL14.eglSwapBuffers(eglDisplay, eglSurface)
                        needsRedraw = false
                    }
                    else -> Thread.sleep(4)
                }
            }
        } catch (_: InterruptedException) {
        } catch (e: Exception) {
            Log.e(TAG, "glLoop: ${e.message}", e)
        } finally {
            teardownEGL()
        }
    }

    private fun setupEGL() {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        EGL14.eglInitialize(eglDisplay, IntArray(1), 0, IntArray(1), 0)
        val cfgAttr = intArrayOf(
            EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8, EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT, EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        EGL14.eglChooseConfig(eglDisplay, cfgAttr, 0, configs, 0, 1, IntArray(1), 0)
        val cfg = configs[0]!!
        eglContext = EGL14.eglCreateContext(eglDisplay, cfg, EGL14.EGL_NO_CONTEXT,
            intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE), 0)
        eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, cfg, targetSurface,
            intArrayOf(EGL14.EGL_NONE), 0)
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    private fun setupProgram() {
        val v = compileShader(GLES20.GL_VERTEX_SHADER,   VERT_SRC)
        val f = compileShader(GLES20.GL_FRAGMENT_SHADER, FRAG_SRC)
        glProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, v); GLES20.glAttachShader(it, f)
            GLES20.glLinkProgram(it)
        }
    }

    private fun setupOESTexture() {
        val ids = IntArray(1)
        GLES20.glGenTextures(1, ids, 0)
        oesTexId = ids[0]
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, oesTexId)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        inputST = SurfaceTexture(oesTexId).also {
            it.setDefaultBufferSize(bufferWidth, bufferHeight)
            it.setOnFrameAvailableListener { frameReady.set(true) }
        }
        inputSurface = Surface(inputST!!)
        initLatch.countDown()
    }

    private fun renderFrame(texMtx: FloatArray) {
        GLES20.glViewport(0, 0, surfW, surfH)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(glProgram)

        val mvp  = buildMVP()
        val pos  = GLES20.glGetAttribLocation(glProgram,  "aPosition")
        val tex  = GLES20.glGetAttribLocation(glProgram,  "aTexCoord")
        val uMVP = GLES20.glGetUniformLocation(glProgram, "uMVP")
        val uTex = GLES20.glGetUniformLocation(glProgram, "uTexMtx")
        val uSmp = GLES20.glGetUniformLocation(glProgram, "uTexture")

        GLES20.glUniformMatrix4fv(uMVP, 1, false, mvp,    0)
        GLES20.glUniformMatrix4fv(uTex, 1, false, texMtx, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, oesTexId)
        GLES20.glUniform1i(uSmp, 0)

        GLES20.glEnableVertexAttribArray(pos)
        GLES20.glVertexAttribPointer(pos, 2, GLES20.GL_FLOAT, false, 0, fb(QUAD_V))
        GLES20.glEnableVertexAttribArray(tex)
        GLES20.glVertexAttribPointer(tex, 2, GLES20.GL_FLOAT, false, 0, fb(QUAD_T))
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    /**
     * MVP = translate × rotate × scale(userZoom).
     *
     * NO aspect ratio math here. texMatrix already maps UV coords to the
     * video content correctly — applying AR scaling on top causes double-zoom.
     *
     * rotation: rotates content in clip space.
     *   At 90°/270° in a portrait screen: portrait video appears as landscape
     *   strip with black bars — exactly like tilting a phone on a real call.
     *
     * scaleValue: user-controlled zoom only (zoomIn/zoomOut buttons).
     *   Default 1.0 = no zoom, content fills screen via texMatrix.
     */
    private fun buildMVP(): FloatArray {
        val m = FloatArray(16)
        Matrix.setIdentityM(m, 0)
        Matrix.translateM(m, 0, offsetX, offsetY, 0f)
        if (rotationDeg != 0) Matrix.rotateM(m, 0, rotationDeg.toFloat(), 0f, 0f, 1f)
        val sx = (if (flipH) -scaleValue else scaleValue)
        Matrix.scaleM(m, 0, sx, scaleValue, 1f)
        return m
    }

    private fun fb(data: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(data.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
            .also { it.put(data); it.position(0) }

    private fun compileShader(type: Int, src: String): Int =
        GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, src); GLES20.glCompileShader(it)
        }

    private fun teardownEGL() {
        try {
            inputSurface?.release(); inputST?.release()
            if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
                EGL14.eglMakeCurrent(eglDisplay,
                    EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
                if (eglSurface != EGL14.EGL_NO_SURFACE) EGL14.eglDestroySurface(eglDisplay, eglSurface)
                if (eglContext != EGL14.EGL_NO_CONTEXT) EGL14.eglDestroyContext(eglDisplay, eglContext)
                EGL14.eglTerminate(eglDisplay)
            }
        } catch (e: Exception) { Log.e(TAG, "teardownEGL: $e") }
    }
}
