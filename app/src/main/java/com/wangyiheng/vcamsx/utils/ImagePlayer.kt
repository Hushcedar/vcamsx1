package com.wangyiheng.vcamsx.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.Surface
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ImagePlayer {
    private const val TAG    = "VCamSX-ImagePlayer"
    private const val NV21_W = 720
    private const val NV21_H = 1280

    val isActive  = mutableStateOf(false)
    val hasImage  = mutableStateOf(false)

    @Volatile var currentBitmap: Bitmap? = null
        private set

    private var activeRenderer: StaticImageRenderer? = null

    private val _loadResult = MutableStateFlow<Boolean?>(null)
    val loadResult = _loadResult.asStateFlow()
    fun clearLoadResult() { _loadResult.value = null }

    fun loadImage(context: Context, uri: Uri) {
        val bytes: ByteArray? = try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) {
            Log.e(TAG, "readBytes: ${e.message}"); null
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
                while (probe.outWidth  / sample > 1080 ||
                       probe.outHeight / sample > 1920) sample *= 2

                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size,
                    BitmapFactory.Options().apply {
                        inSampleSize      = sample
                        inPreferredConfig = Bitmap.Config.ARGB_8888
                    }
                ) ?: run { _loadResult.value = false; return@Thread }

                currentBitmap?.recycle()
                currentBitmap = bmp
                Log.d(TAG, "decoded ${bmp.width}x${bmp.height} sample=$sample")

                val nv21 = bitmapToNV21(bmp, NV21_W, NV21_H)
                setVideoToFramesBuffer(nv21)
                setMainHookField("data_buffer", nv21)

                hasImage.value  = true
                isActive.value  = false
                _loadResult.value = true

            } catch (e: Exception) {
                Log.e(TAG, "decode thread: ${e.message}", e)
                _loadResult.value = false
            }
        }, "VCamSX-ImgLoad").apply { isDaemon = true; start() }
    }

    fun attachSurface(surface: Surface) {
        if (!isActive.value) return
        val bmp = currentBitmap ?: return
        if (!surface.isValid) return
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
            Log.d(TAG, "renderer running on $surface")
        } catch (e: Exception) { Log.e(TAG, "attachSurface: ${e.message}", e) }
    }

    fun activateInjection() {
        if (!hasImage.value) return
        isActive.value = true
        currentBitmap?.let { bmp ->
            val nv21 = bitmapToNV21(bmp, NV21_W, NV21_H)
            setVideoToFramesBuffer(nv21)
            setMainHookField("data_buffer", nv21)
        }
        val surface = getMainHookSurface()
        if (surface != null && surface.isValid) {
            attachSurface(surface)
        }
    }

    fun stop() {
        isActive.value = false
        stopRenderer()
        setVideoToFramesBuffer(byteArrayOf())
        setMainHookField("data_buffer", byteArrayOf())
    }

    fun reset() {
        stop()
        currentBitmap?.recycle()
        currentBitmap = null
        hasImage.value = false
    }

    fun currentBitmapSnapshot(): Bitmap? = currentBitmap

    private fun stopRenderer() { activeRenderer?.stop(); activeRenderer = null }

    private fun setVideoToFramesBuffer(nv21: ByteArray) {
        try {
            Class.forName("com.wangyiheng.vcamsx.utils.VideoToFrames")
                .getField("data_buffer").set(null, nv21)
        } catch (_: Exception) {}
    }

    private fun setMainHookField(field: String, value: Any?) {
        try {
            Class.forName("com.wangyiheng.vcamsx.MainHook")
                .getField(field).set(null, value)
        } catch (_: Exception) {}
    }

    private fun getMainHookSurface(): Surface? = try {
        Class.forName("com.wangyiheng.vcamsx.MainHook")
            .getField("original_preview_Surface").get(null) as? Surface
    } catch (_: Exception) { null }

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
