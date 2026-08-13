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

class ImageSurfaceRenderer(
    private val targetSurface: Surface,
    private val bitmap: Bitmap,
    @Volatile var rotationDeg: Int  = 0,
    @Volatile var flipH: Boolean    = false,
    @Volatile var scaleValue: Float = 1.0f,
    @Volatile var offsetX: Float    = 0f,
    @Volatile var offsetY: Float    = 0f
) {
    companion object {
        private const val TAG = "VCamSX-ImgGL"

        private val VERT = """
            attribute vec4 aPos;
            attribute vec2 aTex;
            varying   vec2 vTex;
            uniform   mat4 uMVP;
            void main() { gl_Position = uMVP * aPos; vTex = aTex; }
        """.trimIndent()

        private val FRAG = """
            precision mediump float;
            uniform sampler2D uTex;
            varying vec2 vTex;
            void main() { gl_FragColor = texture2D(uTex, vTex); }
        """.trimIndent()

        private val POS = floatArrayOf(-1f,-1f,  1f,-1f,  -1f,1f,  1f,1f)
        private val UV  = floatArrayOf( 0f, 1f,  1f, 1f,   0f,0f,  1f,0f)
    }

    @Volatile var needsRedraw = false

    private val running   = AtomicBoolean(false)
    private val initLatch = CountDownLatch(1)
    private var thread: Thread? = null

    private var eglDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext = EGL14.EGL_NO_CONTEXT
    private var eglSurface = EGL14.EGL_NO_SURFACE
    private var texId   = 0
    private var program = 0
    private var surfW   = 0
    private var surfH   = 0

    fun start(): Boolean {
        if (!running.compareAndSet(false, true)) return true
        thread = Thread(::glLoop, "VCamSX-ImgGL").apply { isDaemon = true; start() }
        return initLatch.await(3, TimeUnit.SECONDS)
    }

    fun stop() {
        running.set(false)
        thread?.interrupt()
        thread = null
    }

    private fun glLoop() {
        try {
            initEGL()
            program = buildProgram()
            texId   = uploadBitmap(bitmap)
            initLatch.countDown()

            while (running.get()) {
                querySize()
                if (surfW > 0 && surfH > 0) {
                    drawFrame()
                    EGL14.eglSwapBuffers(eglDisplay, eglSurface)
                }
                try { Thread.sleep(33) } catch (_: InterruptedException) { break }
                needsRedraw = false
            }
        } catch (_: InterruptedException) {
        } catch (e: Exception) {
            Log.e(TAG, "glLoop: ${e.message}", e)
            initLatch.countDown()
        } finally {
            teardown()
        }
    }

    private fun initEGL() {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        EGL14.eglInitialize(eglDisplay, IntArray(1), 0, IntArray(1), 0)

        val attr = intArrayOf(
            EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8, EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
            EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        EGL14.eglChooseConfig(eglDisplay, attr, 0, configs, 0, 1, IntArray(1), 0)
        val cfg = configs[0]!!

        eglContext = EGL14.eglCreateContext(eglDisplay, cfg, EGL14.EGL_NO_CONTEXT,
            intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE), 0)
        eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, cfg, targetSurface,
            intArrayOf(EGL14.EGL_NONE), 0)
        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    private fun querySize() {
        val w = IntArray(1); val h = IntArray(1)
        EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_WIDTH,  w, 0)
        EGL14.eglQuerySurface(eglDisplay, eglSurface, EGL14.EGL_HEIGHT, h, 0)
        if (w[0] > 0 && h[0] > 0) { surfW = w[0]; surfH = h[0] }
    }

    private fun uploadBitmap(bmp: Bitmap): Int {
        val ids = IntArray(1)
        GLES20.glGenTextures(1, ids, 0)
        val id = ids[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
        Log.d(TAG, "bitmap uploaded as GL_TEXTURE_2D id=$id size=${bmp.width}x${bmp.height}")
        return id
    }

    private fun drawFrame() {
        GLES20.glViewport(0, 0, surfW, surfH)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(program)

        val mvp = buildMVP()

        val aPos = GLES20.glGetAttribLocation(program,  "aPos")
        val aTex = GLES20.glGetAttribLocation(program,  "aTex")
        val uMVP = GLES20.glGetUniformLocation(program, "uMVP")
        val uTex = GLES20.glGetUniformLocation(program, "uTex")

        GLES20.glUniformMatrix4fv(uMVP, 1, false, mvp, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId)
        GLES20.glUniform1i(uTex, 0)

        GLES20.glEnableVertexAttribArray(aPos)
        GLES20.glVertexAttribPointer(aPos, 2, GLES20.GL_FLOAT, false, 0, fb(POS))
        GLES20.glEnableVertexAttribArray(aTex)
        GLES20.glVertexAttribPointer(aTex, 2, GLES20.GL_FLOAT, false, 0, fb(UV))
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

    private fun buildProgram(): Int {
        fun shader(type: Int, src: String) = GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, src); GLES20.glCompileShader(it)
            val ok = IntArray(1); GLES20.glGetShaderiv(it, GLES20.GL_COMPILE_STATUS, ok, 0)
            if (ok[0] == 0) Log.e(TAG, "shader[$type]: ${GLES20.glGetShaderInfoLog(it)}")
        }
        return GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, shader(GLES20.GL_VERTEX_SHADER,   VERT))
            GLES20.glAttachShader(it, shader(GLES20.GL_FRAGMENT_SHADER, FRAG))
            GLES20.glLinkProgram(it)
        }
    }

    private fun fb(d: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(d.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
            .also { it.put(d); it.position(0) }

    private fun teardown() {
        try {
            if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
                EGL14.eglMakeCurrent(eglDisplay,
                    EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
                if (eglSurface != EGL14.EGL_NO_SURFACE) EGL14.eglDestroySurface(eglDisplay, eglSurface)
                if (eglContext != EGL14.EGL_NO_CONTEXT) EGL14.eglDestroyContext(eglDisplay, eglContext)
                EGL14.eglTerminate(eglDisplay)
            }
        } catch (e: Exception) { Log.e(TAG, "teardown: $e") }
    }
}
