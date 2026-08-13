package com.wangyiheng.vcamsx.utils

import android.graphics.Bitmap
import android.opengl.*
import android.util.Log
import android.view.Surface
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class StaticImageRenderer(
    private val targetSurface: Surface,
    private val bitmap: Bitmap,
    @Volatile var rotationDeg: Int  = 0,
    @Volatile var flipH: Boolean    = false,
    @Volatile var offsetX: Float    = 0f,
    @Volatile var offsetY: Float    = 0f,
    @Volatile var scaleValue: Float = 1.0f,
    @Volatile var needsRedraw: Boolean = true
) {
    companion object {
        private const val TAG = "VCamSX-ImgRender"
        private const val MIN_DIM = 4

        private val VERT = """
            attribute vec4 aPosition;
            attribute vec4 aTexCoord;
            varying   vec2 vTexCoord;
            uniform   mat4 uMVP;
            void main() {
                gl_Position = uMVP * aPosition;
                vTexCoord   = aTexCoord.xy;
            }
        """.trimIndent()

        private val FRAG = """
            precision mediump float;
            uniform sampler2D uTexture;
            varying vec2 vTexCoord;
            void main() {
                gl_FragColor = texture2D(uTexture, vTexCoord);
            }
        """.trimIndent()

        private val QUAD_V = floatArrayOf(-1f, -1f,  1f, -1f, -1f, 1f,  1f, 1f)
        private val QUAD_T = floatArrayOf( 0f,  1f,  1f,  1f,  0f, 0f,  1f, 0f)
    }

    private val running   = AtomicBoolean(false)
    private val initLatch = CountDownLatch(1)
    private var thread: Thread? = null

    private var eglDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext = EGL14.EGL_NO_CONTEXT
    private var eglSurface = EGL14.EGL_NO_SURFACE
    private var texId      = 0
    private var glProgram  = 0
    private var surfW      = 0
    private var surfH      = 0

    fun start(): Boolean {
        if (!running.compareAndSet(false, true)) return false
        thread = Thread(::glLoop, "VCamSX-ImgGL").apply { isDaemon = true; start() }
        return initLatch.await(2, TimeUnit.SECONDS)
    }

    fun stop() {
        running.set(false)
        thread?.interrupt()
        thread = null
    }

    private fun glLoop() {
        try {
            setupEGL()
            setupProgram()
            uploadBitmap()
            initLatch.countDown()

            renderFrame()
            EGL14.eglSwapBuffers(eglDisplay, eglSurface)
            needsRedraw = false

            val sw = IntArray(1); val sh = IntArray(1)
            while (running.get()) {
                EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_WIDTH,  sw, 0)
                EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_HEIGHT, sh, 0)
                if (sw[0] > MIN_DIM && sh[0] > MIN_DIM && (sw[0] != surfW || sh[0] != surfH)) {
                    surfW = sw[0]; surfH = sh[0]; needsRedraw = true
                }
                if (needsRedraw) {
                    renderFrame()
                    EGL14.eglSwapBuffers(eglDisplay, eglSurface)
                    needsRedraw = false
                } else {
                    Thread.sleep(50)
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
        val sw = IntArray(1); val sh = IntArray(1)
        EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_WIDTH,  sw, 0)
        EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_HEIGHT, sh, 0)
        surfW = if (sw[0] > MIN_DIM) sw[0] else bitmap.width
        surfH = if (sh[0] > MIN_DIM) sh[0] else bitmap.height
    }

    private fun setupProgram() {
        val v = compile(GLES20.GL_VERTEX_SHADER,   VERT)
        val f = compile(GLES20.GL_FRAGMENT_SHADER, FRAG)
        glProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, v); GLES20.glAttachShader(it, f)
            GLES20.glLinkProgram(it)
        }
    }

    private fun uploadBitmap() {
        val ids = IntArray(1)
        GLES20.glGenTextures(1, ids, 0)
        texId = ids[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
    }

    private fun renderFrame() {
        GLES20.glViewport(0, 0, surfW, surfH)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(glProgram)

        val mvp = buildMVP()
        val pos  = GLES20.glGetAttribLocation(glProgram,  "aPosition")
        val tex  = GLES20.glGetAttribLocation(glProgram,  "aTexCoord")
        val uMVP = GLES20.glGetUniformLocation(glProgram, "uMVP")
        val uSmp = GLES20.glGetUniformLocation(glProgram, "uTexture")

        GLES20.glUniformMatrix4fv(uMVP, 1, false, mvp, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId)
        GLES20.glUniform1i(uSmp, 0)

        GLES20.glEnableVertexAttribArray(pos)
        GLES20.glVertexAttribPointer(pos, 2, GLES20.GL_FLOAT, false, 0, fb(QUAD_V))
        GLES20.glEnableVertexAttribArray(tex)
        GLES20.glVertexAttribPointer(tex, 2, GLES20.GL_FLOAT, false, 0, fb(QUAD_T))
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun buildMVP(): FloatArray {
        val m = FloatArray(16)
        Matrix.setIdentityM(m, 0)
        Matrix.translateM(m, 0, offsetX, offsetY, 0f)
        if (rotationDeg != 0) Matrix.rotateM(m, 0, rotationDeg.toFloat(), 0f, 0f, 1f)
        val sx = if (flipH) -scaleValue else scaleValue
        Matrix.scaleM(m, 0, sx, scaleValue, 1f)
        return m
    }

    private fun fb(data: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(data.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().also { it.put(data); it.position(0) }

    private fun compile(type: Int, src: String) =
        GLES20.glCreateShader(type).also { GLES20.glShaderSource(it, src); GLES20.glCompileShader(it) }

    private fun teardownEGL() {
        try {
            if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
                EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
                if (eglSurface != EGL14.EGL_NO_SURFACE) EGL14.eglDestroySurface(eglDisplay, eglSurface)
                if (eglContext != EGL14.EGL_NO_CONTEXT) EGL14.eglDestroyContext(eglDisplay, eglContext)
                EGL14.eglTerminate(eglDisplay)
            }
        } catch (e: Exception) { Log.e(TAG, "teardownEGL: $e") }
    }
}
