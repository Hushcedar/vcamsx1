package com.wangyiheng.vcamsx.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import com.wangyiheng.vcamsx.MainHook

object ImagePlayer {

    private const val TAG    = "VCamSX-ImagePlayer"
    private const val NV21_W = 720
    private const val NV21_H = 1280

    @Volatile var isActive: Boolean = false
        private set

    @Volatile var currentBitmap: Bitmap? = null
        private set

    private var activeRenderer: StaticImageRenderer? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    fun loadImage(context: Context, uri: Uri, onResult: (Boolean) -> Unit) {
        val bytes: ByteArray? = try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) {
            Log.e(TAG, "readBytes failed: ${e.message}"); null
        }

        if (bytes == null || bytes.isEmpty()) {
            onResult(false); return
        }

        Thread({
            try {
                val probe = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, probe)

                var sample = 1
                while (probe.outWidth  / sample > 1080 ||
                       probe.outHeight / sample > 1920) sample *= 2

                val opts = BitmapFactory.Options().apply {
                    inSampleSize      = sample
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
                    ?: run {
                        Log.e(TAG, "decodeByteArray returned null")
                        mainHandler.post { onResult(false) }
                        return@Thread
                    }

                currentBitmap = bmp
                Log.d(TAG, "loaded ${bmp.width}x${bmp.height} (sample=$sample)")

                // Populate NV21 buffer so Camera1 callback path gets the frame too
                val nv21 = bitmapToNV21(bmp, NV21_W, NV21_H)
                VideoToFrames.data_buffer = nv21
                MainHook.data_buffer      = nv21

                isActive = true

                // Attach GL renderer to whatever surface is live right now
                MainHook.original_preview_Surface?.let { s ->
                    if (s.isValid) attachSurface(s)
                }

                mainHandler.post {
                    // Sync Compose-observable state ON the main thread
                    VideoControls.isImageEnabled.value = true
                    onResult(true)
                }

            } catch (e: Exception) {
                Log.e(TAG, "decode thread: ${e.message}")
                mainHandler.post { onResult(false) }
            }
        }, "VCamSX-ImgLoad").apply { isDaemon = true; start() }
    }

    fun attachSurface(surface: Surface) {
        val bmp = currentBitmap ?: return
        stopRenderer()
        try {
            val r = StaticImageRenderer(
                targetSurface = surface,
                bitmap        = bmp,
                rotationDeg   = VideoControls.rotation.value,
                flipH         = VideoControls.isFlipped.value,
                scaleValue    = VideoControls.scale.value
            )
            if (!r.start()) { Log.e(TAG, "renderer timed out"); return }
            activeRenderer = r
            Log.d(TAG, "renderer started on $surface")
        } catch (e: Exception) { Log.e(TAG, "attachSurface: ${e.message}") }
    }

    /** Re-activate after stop() if a bitmap is already loaded. */
    fun enable() {
        if (currentBitmap == null) return
        isActive = true
        VideoControls.isImageEnabled.value = true
        MainHook.original_preview_Surface?.let { s ->
            if (s.isValid) attachSurface(s)
        }
    }

    fun stop() {
        isActive = false
        mainHandler.post { VideoControls.isImageEnabled.value = false }
        stopRenderer()
        currentBitmap = null
        VideoToFrames.data_buffer = byteArrayOf()
        MainHook.data_buffer      = byteArrayOf()
    }

    /** Pause rendering without clearing the bitmap — toggle-friendly. */
    fun disable() {
        isActive = false
        mainHandler.post { VideoControls.isImageEnabled.value = false }
        stopRenderer()
    }

    private fun stopRenderer() {
        activeRenderer?.stop()
        activeRenderer = null
    }

    fun rotate() {
        VideoControls.rotation.value = (VideoControls.rotation.value + 90) % 360
        activeRenderer?.also { it.rotationDeg = VideoControls.rotation.value; it.needsRedraw = true }
    }

    fun flip() {
        VideoControls.isFlipped.value = !VideoControls.isFlipped.value
        activeRenderer?.also { it.flipH = VideoControls.isFlipped.value; it.needsRedraw = true }
    }

    fun adjustOffset(dx: Int, dy: Int) {
        activeRenderer?.also {
            it.offsetX     = (it.offsetX + dx * (2f / NV21_W)).coerceIn(-1.5f, 1.5f)
            it.offsetY     = (it.offsetY - dy * (2f / NV21_H)).coerceIn(-1.5f, 1.5f)
            it.needsRedraw = true
        }
    }

    fun zoomIn() {
        val s = (VideoControls.scale.value + 0.1f).coerceAtMost(3.0f)
        VideoControls.scale.value = s
        activeRenderer?.also { it.scaleValue = s; it.needsRedraw = true }
    }

    fun zoomOut() {
        val s = (VideoControls.scale.value - 0.1f).coerceAtLeast(0.3f)
        VideoControls.scale.value = s
        activeRenderer?.also { it.scaleValue = s; it.needsRedraw = true }
    }

    fun bitmapToNV21(src: Bitmap, outW: Int, outH: Int): ByteArray {
        val bmp = if (src.width != outW || src.height != outH)
            Bitmap.createScaledBitmap(src, outW, outH, true) else src
        val argb  = IntArray(outW * outH)
        bmp.getPixels(argb, 0, outW, 0, 0, outW, outH)
        val nv21  = ByteArray(outW * outH * 3 / 2)
        val frame = outW * outH
        var yIdx  = 0
        var uvIdx = frame
        for (row in 0 until outH) {
            for (col in 0 until outW) {
                val p = argb[yIdx]
                val r = (p shr 16) and 0xFF
                val g = (p shr  8) and 0xFF
                val b =  p         and 0xFF
                val y = (( 66 * r + 129 * g +  25 * b + 128) shr 8) + 16
                val u = ((-38 * r -  74 * g + 112 * b + 128) shr 8) + 128
                val v = ((112 * r -  94 * g -  18 * b + 128) shr 8) + 128
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
