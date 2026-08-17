package top.niunaijun.blackboxa.view.vcam

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageWriter
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.Surface
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class VCamCameraHook(
    private val ctx: Context,
    private val targetPkg: String
) {
    private val TAG = "VCamCameraHook"
    private val cfg by lazy { VCamPrefs.read(ctx, targetPkg) }
    private val executor = Executors.newScheduledThreadPool(1)
    private var pumpFuture: ScheduledFuture<*>? = null
    private var writer: ImageWriter? = null
    private var mediaPlayer: MediaPlayer? = null

    fun isActive(): Boolean = cfg.isActive

    fun start(surface: Surface) {
        Log.d(TAG, "start: pkg=$targetPkg method=${cfg.method}")
        when (cfg.method) {
            2    -> startLocalVideo(surface)
            3    -> startNetworkStream(surface)
            4    -> startLocalPicture(surface)
            else -> Log.d(TAG, "method=1 passthrough")
        }
    }

    fun stop() {
        pumpFuture?.cancel(true)
        runCatching { mediaPlayer?.stop(); mediaPlayer?.release() }
        runCatching { writer?.close() }
        mediaPlayer = null
        writer = null
    }

    private fun startLocalVideo(surface: Surface) {
        if (cfg.videoUri.isBlank()) return
        mediaPlayer = MediaPlayer().apply {
            setDataSource(ctx, Uri.parse(cfg.videoUri))
            setSurface(surface)
            isLooping = true
            if (!cfg.audio) setVolume(0f, 0f)
            prepare()
            start()
        }
    }

    private fun startNetworkStream(surface: Surface) {
        if (cfg.netUrl.isBlank()) return
        mediaPlayer = MediaPlayer().apply {
            setDataSource(cfg.netUrl)
            setSurface(surface)
            isLooping = true
            if (!cfg.audio) setVolume(0f, 0f)
            prepareAsync()
            setOnPreparedListener { start() }
            setOnErrorListener { _, w, e -> Log.e(TAG, "stream error w=$w e=$e"); false }
        }
    }

    private fun startLocalPicture(surface: Surface) {
        if (cfg.picUri.isBlank()) return
        val bmp = loadBitmap() ?: return
        val yuv = bitmapToYuv420(bmp, cfg.width, cfg.height)
        bmp.recycle()
        writer = ImageWriter.newInstance(surface, 3)
        pumpFuture = executor.scheduleAtFixedRate(
            { pushFrame(yuv) }, 0L, 1000L / cfg.fps, TimeUnit.MILLISECONDS
        )
    }

    private fun loadBitmap(): Bitmap? = runCatching {
        ctx.contentResolver.openInputStream(Uri.parse(cfg.picUri))?.use { s ->
            BitmapFactory.decodeStream(s)?.let { raw ->
                Bitmap.createScaledBitmap(raw, cfg.width, cfg.height, true)
                    .also { if (it !== raw) raw.recycle() }
            }
        }
    }.getOrNull()

    private fun pushFrame(yuv: ByteArray) {
        val w = writer ?: return
        runCatching {
            val img: Image = w.dequeueInputImage()
            val ySize = cfg.width * cfg.height
            val uvSize = ySize / 4
            img.planes[0].buffer.put(yuv, 0, ySize)
            img.planes[1].buffer.put(yuv, ySize, uvSize)
            img.planes[2].buffer.put(yuv, ySize + uvSize, uvSize)
            img.timestamp = System.nanoTime()
            w.queueInputImage(img)
        }
    }

    private fun bitmapToYuv420(bmp: Bitmap, w: Int, h: Int): ByteArray {
        val argb = IntArray(w * h).also { bmp.getPixels(it, 0, w, 0, 0, w, h) }
        val yuv  = ByteArray(w * h * 3 / 2)
        var yi = 0; var uvi = w * h
        for (j in 0 until h) for (i in 0 until w) {
            val px = argb[j * w + i]
            val r = (px shr 16) and 0xFF; val g = (px shr 8) and 0xFF; val b = px and 0xFF
            yuv[yi++] = (((66*r+129*g+25*b+128) shr 8)+16).coerceIn(16,235).toByte()
            if (j%2==0 && i%2==0) {
                yuv[uvi++] = (((-38*r-74*g+112*b+128) shr 8)+128).coerceIn(16,240).toByte()
                yuv[uvi++] = (((112*r-94*g-18*b+128) shr 8)+128).coerceIn(16,240).toByte()
            }
        }
        return yuv
    }
}
