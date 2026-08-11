package com.wangyiheng.vcamsx.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import androidx.compose.runtime.mutableStateOf

object ImagePlayer {

    private const val TAG = "VCamSX-ImagePlayer"

    val isActive  = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val hasImage  = mutableStateOf(false)

    @Volatile private var currentBitmap:  Bitmap? = null
    @Volatile private var activeRenderer: ImageSurfaceRenderer? = null

    private val mainHandler = Handler(Looper.getMainLooper())

    fun loadImage(context: Context, uri: Uri, onResult: (Boolean) -> Unit) {
        mainHandler.post { isLoading.value = true }

        val bytes: ByteArray? = try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) { Log.e(TAG, "readBytes: ${e.message}"); null }

        if (bytes == null || bytes.isEmpty()) {
            mainHandler.post { isLoading.value = false; onResult(false) }
            return
        }

        Thread({ decodeAndStart(bytes, onResult) }, "VCamSX-ImgLoad")
            .apply { isDaemon = true; start() }
    }

    private fun decodeAndStart(bytes: ByteArray, onResult: (Boolean) -> Unit) {
        try {
            val probe = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, probe)
            if (probe.outWidth <= 0 || probe.outHeight <= 0) {
                mainHandler.post { isLoading.value = false; onResult(false) }; return
            }

            var sample = 1
            while (probe.outWidth / sample > 1080 || probe.outHeight / sample > 1920) sample *= 2

            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size,
                BitmapFactory.Options().apply {
                    inSampleSize      = sample
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
            ) ?: run { mainHandler.post { isLoading.value = false; onResult(false) }; return }

            Log.d(TAG, "decoded ${bmp.width}x${bmp.height}")
            currentBitmap?.recycle()
            currentBitmap = bmp

            mainHandler.post {
                hasImage.value  = true
                isLoading.value = false
                isActive.value  = true
                onResult(true)
            }

            HookBridge.getVirtualSurface()?.takeIf { it.isValid }?.let {
                Log.d(TAG, "camera open — attaching to virtual surface immediately")
                startRenderer(bmp, it)
            }

        } catch (e: Exception) {
            Log.e(TAG, "decodeAndStart: ${e.message}", e)
            mainHandler.post { isLoading.value = false; onResult(false) }
        }
    }

    fun attachSurface(surface: Surface) {
        val bmp = currentBitmap ?: return
        Log.d(TAG, "attachSurface: $surface")
        startRenderer(bmp, surface)
    }

    fun attachC1Surface(surface: Surface) {
        val bmp = currentBitmap ?: return
        val target = try {
            HookBridge.getFakeSurfaceTexture()
                ?.let { Surface(it) }
                ?.takeIf { it.isValid }
        } catch (_: Exception) { null } ?: surface.takeIf { it.isValid } ?: return
        Log.d(TAG, "attachC1Surface: $target")
        startRenderer(bmp, target)
    }

    private fun startRenderer(bmp: Bitmap, targetSurface: Surface) {
        stopRenderer()
        try {
            val r = ImageSurfaceRenderer(
                targetSurface = targetSurface,
                bitmap        = bmp,
                rotationDeg   = VideoControls.rotation.value,
                flipH         = VideoControls.isFlipped.value,
                scaleValue    = VideoControls.scale.value
            )
            if (!r.start()) {
                Log.e(TAG, "ImageSurfaceRenderer failed to start")
                return
            }
            activeRenderer = r
            Log.d(TAG, "renderer running on $targetSurface")
        } catch (e: Exception) {
            Log.e(TAG, "startRenderer: ${e.message}", e)
        }
    }

    fun activateInjection() {
        if (!hasImage.value) return
        mainHandler.post { isActive.value = true }
        val bmp  = currentBitmap ?: return
        val virt = HookBridge.getVirtualSurface()?.takeIf { it.isValid } ?: return
        startRenderer(bmp, virt)
    }

    fun stop() {
        mainHandler.post { isActive.value = false }
        stopRenderer()
    }

    fun reset() {
        stop()
        currentBitmap?.recycle()
        currentBitmap = null
        mainHandler.post { hasImage.value = false }
    }

    fun currentBitmapSnapshot(): Bitmap? = currentBitmap

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
            it.offsetX     = (it.offsetX + dx * (2f / 720f)).coerceIn(-1.5f, 1.5f)
            it.offsetY     = (it.offsetY - dy * (2f / 1280f)).coerceIn(-1.5f, 1.5f)
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

    private fun stopRenderer() {
        activeRenderer?.stop()
        activeRenderer = null
    }
}
