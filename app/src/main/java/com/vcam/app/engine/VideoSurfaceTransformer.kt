package com.vcam.app.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.opengl.*
import android.util.Log
import android.view.Surface
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VideoSurfaceTransformer(
    private val outputSurface: Surface,
    private val width:  Int = 1280,
    private val height: Int = 720
) {
    companion object {
        private const val TAG = "VCam-Transformer"

        private val VERTEX_SHADER = """
            uniform mat4 uMVPMatrix;
            uniform mat4 uTexMatrix;
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            varying vec2 vTextureCoord;
            void main() {
                gl_Position   = uMVPMatrix * aPosition;
                vTextureCoord = (uTexMatrix * aTextureCoord).xy;
            }
        """.trimIndent()

        private val FRAGMENT_SHADER = """
            #extension GL_OES_EGL_image_external : require
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform samplerExternalOES sTexture;
            void main() {
                gl_FragColor = texture2D(sTexture, vTextureCoord);
            }
        """.trimIndent()

        private val QUAD_COORDS = floatArrayOf(
            -1f, -1f, 0f,
             1f, -1f, 0f,
            -1f,  1f, 0f,
             1f,  1f, 0f
        )

        private val QUAD_TEXCOORDS = floatArrayOf(
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
        )

        private val IDENTITY = floatArrayOf(
            1f,0f,0f,0f, 0f,1f,0f,0f, 0f,0f,1f,0f, 0f,0f,0f,1f
        )
        private const val COORDS_PER_VERTEX    = 3
        private const val TEXCOORDS_PER_VERTEX = 2
    }

    @Volatile var rotationDeg:    Int     = 0
    @Volatile var flipHorizontal: Boolean = false
    @Volatile var glScale:        Float   = 1f
    @Volatile var offsetX:        Float   = 0f
    @Volatile var offsetY:        Float   = 0f

    private val inputST: SurfaceTexture = SurfaceTexture(0).also {
        it.setDefaultBufferSize(width, height)
    }
    val inputSurface: Surface = Surface(inputST)

    private var eglDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext: EGLContext = EGL14.EGL_NO_CONTEXT
    private var eglSurface: EGLSurface = EGL14.EGL_NO_SURFACE
    private var program    = 0
    private var oesTexId   = 0
    private var uMVP = 0; private var uTex = 0
    private var aPos = 0; private var aTex = 0
    private lateinit var vertBuf: FloatBuffer
    private lateinit var texBuf:  FloatBuffer
    private val pixelBuf   = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder())
    private val outputRect = Rect(0, 0, width, height)

    @Volatile private var running = false
    private var thread: Thread? = null

    fun start() {
        if (running) return
        running = true
        thread = Thread(::renderLoop, "VCamTransformer").also { it.start() }
    }

    fun release() {
        running = false; thread?.join(2000); thread = null
        runCatching { inputSurface.release() }
        runCatching { inputST.release() }
    }

    fun setRotation(deg: Int)         { rotationDeg    = ((deg % 360) + 360) % 360 }
    fun setFlip(flip: Boolean)        { flipHorizontal = flip }
    fun setScale(s: Float)            { glScale        = s.coerceIn(0.1f, 5f) }
    fun setOffset(x: Float, y: Float) { offsetX = x; offsetY = y }

    private fun renderLoop() {
        try {
            setupEgl(); setupGl(); setupBuffers()
            inputST.detachFromGLContext()
            inputST.attachToGLContext(oesTexId)
            var frameAvailable = false
            inputST.setOnFrameAvailableListener { frameAvailable = true }
            while (running) {
                if (frameAvailable) {
                    frameAvailable = false
                    inputST.updateTexImage()
                    drawFrame(); pushCanvas()
                } else Thread.sleep(4)
            }
        } catch (e: Exception) {
            Log.e(TAG, "renderLoop: ${e.message}", e)
        } finally { teardown() }
    }

    private fun drawFrame() {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(program)

        val mvp = FloatArray(16)
        android.opengl.Matrix.setIdentityM(mvp, 0)

        android.opengl.Matrix.scaleM(mvp, 0, 1f, -1f, 1f)
        android.opengl.Matrix.scaleM(mvp, 0, glScale, glScale, 1f)
        if (rotationDeg != 0)
            android.opengl.Matrix.rotateM(mvp, 0, rotationDeg.toFloat(), 0f, 0f, 1f)
        if (flipHorizontal)
            android.opengl.Matrix.scaleM(mvp, 0, -1f, 1f, 1f)
        android.opengl.Matrix.translateM(mvp, 0, offsetX, offsetY, 0f)

        GLES20.glUniformMatrix4fv(uMVP, 1, false, mvp, 0)
        GLES20.glUniformMatrix4fv(uTex, 1, false, IDENTITY, 0)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, oesTexId)

        GLES20.glEnableVertexAttribArray(aPos)
        GLES20.glVertexAttribPointer(aPos, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
            false, COORDS_PER_VERTEX * 4, vertBuf)
        GLES20.glEnableVertexAttribArray(aTex)
        GLES20.glVertexAttribPointer(aTex, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT,
            false, TEXCOORDS_PER_VERTEX * 4, texBuf)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(aPos)
        GLES20.glDisableVertexAttribArray(aTex)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        GLES20.glUseProgram(0)
    }

    private fun pushCanvas() {
        try {
            pixelBuf.rewind()
            GLES20.glReadPixels(0, 0, width, height,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuf)
            pixelBuf.rewind()
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bmp.copyPixelsFromBuffer(pixelBuf)
            val canvas: Canvas = try { outputSurface.lockHardwareCanvas() }
                                  catch (_: Exception) { outputSurface.lockCanvas(outputRect) }
            canvas.drawBitmap(bmp, null, outputRect, null)
            outputSurface.unlockCanvasAndPost(canvas)
            bmp.recycle()
        } catch (e: Exception) { Log.e(TAG, "pushCanvas: ${e.message}") }
    }

    private fun setupEgl() {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        check(eglDisplay != EGL14.EGL_NO_DISPLAY)
        val ver = IntArray(2); check(EGL14.eglInitialize(eglDisplay, ver, 0, ver, 1))
        val att = intArrayOf(EGL14.EGL_RED_SIZE,8, EGL14.EGL_GREEN_SIZE,8,
            EGL14.EGL_BLUE_SIZE,8, EGL14.EGL_ALPHA_SIZE,8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT, EGL14.EGL_NONE)
        val cfgs = arrayOfNulls<EGLConfig>(1); val num = IntArray(1)
        check(EGL14.eglChooseConfig(eglDisplay, att, 0, cfgs, 0, 1, num, 0))
        val ctx = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
        eglContext = EGL14.eglCreateContext(eglDisplay, cfgs[0], EGL14.EGL_NO_CONTEXT, ctx, 0)
        check(eglContext != EGL14.EGL_NO_CONTEXT)
        val pb = intArrayOf(EGL14.EGL_WIDTH, width, EGL14.EGL_HEIGHT, height, EGL14.EGL_NONE)
        eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, cfgs[0], pb, 0)
        check(eglSurface != EGL14.EGL_NO_SURFACE)
        check(EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext))
    }

    private fun setupGl() {
        program = createProgram(VERTEX_SHADER, FRAGMENT_SHADER); check(program != 0)
        aPos = GLES20.glGetAttribLocation(program, "aPosition")
        aTex = GLES20.glGetAttribLocation(program, "aTextureCoord")
        uMVP = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        uTex = GLES20.glGetUniformLocation(program, "uTexMatrix")
        val t = IntArray(1); GLES20.glGenTextures(1, t, 0); oesTexId = t[0]
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, oesTexId)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
    }

    private fun setupBuffers() {
        vertBuf = ByteBuffer.allocateDirect(QUAD_COORDS.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .also { it.put(QUAD_COORDS); it.position(0) }
        texBuf  = ByteBuffer.allocateDirect(QUAD_TEXCOORDS.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .also { it.put(QUAD_TEXCOORDS); it.position(0) }
    }

    private fun teardown() {
        runCatching { inputST.detachFromGLContext() }
        if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
            if (eglSurface != EGL14.EGL_NO_SURFACE) EGL14.eglDestroySurface(eglDisplay, eglSurface)
            if (eglContext != EGL14.EGL_NO_CONTEXT) EGL14.eglDestroyContext(eglDisplay, eglContext)
            EGL14.eglTerminate(eglDisplay)
        }
        if (program != 0) { GLES20.glDeleteProgram(program); program = 0 }
    }

    private fun createProgram(vs: String, fs: String): Int {
        val v = compileShader(GLES20.GL_VERTEX_SHADER, vs)
        val f = compileShader(GLES20.GL_FRAGMENT_SHADER, fs)
        val p = GLES20.glCreateProgram()
        GLES20.glAttachShader(p, v); GLES20.glAttachShader(p, f); GLES20.glLinkProgram(p)
        val st = IntArray(1); GLES20.glGetProgramiv(p, GLES20.GL_LINK_STATUS, st, 0)
        if (st[0] == 0) { Log.e(TAG, "link: ${GLES20.glGetProgramInfoLog(p)}"); GLES20.glDeleteProgram(p); return 0 }
        GLES20.glDeleteShader(v); GLES20.glDeleteShader(f); return p
    }

    private fun compileShader(type: Int, src: String): Int {
        val s = GLES20.glCreateShader(type)
        GLES20.glShaderSource(s, src); GLES20.glCompileShader(s)
        val st = IntArray(1); GLES20.glGetShaderiv(s, GLES20.GL_COMPILE_STATUS, st, 0)
        if (st[0] == 0) { Log.e(TAG, "compile: ${GLES20.glGetShaderInfoLog(s)}"); GLES20.glDeleteShader(s); return 0 }
        return s
    }
}
