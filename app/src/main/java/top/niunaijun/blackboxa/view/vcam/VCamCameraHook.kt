package top.niunaijun.blackboxa.view.vcam

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageWriter
import android.util.Log
import android.view.Surface
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * VCamCameraHook
 *
 * Called by BlackBox's camera intercept point (BActivityManager / BPackageManager
 * camera open hook inside libblackbox.so) when a cloned app calls:
 *   CameraManager.openCamera() or Camera.open()
 *
 * Hook placement: override onCameraOpen() inside BlackBox's
 * core/src/main/java/com/lbe/ioc/client/ipc/sender/BActivityManagerSender.kt
 * OR wherever BB dispatches Camera2 openCamera — search for "openCamera" in BB source.
 *
 * Usage in BB's camera hook point:
 *   val hook = VCamCameraHook(context, packageName)
 *   if (hook.isActive()) {
 *       hook.start(virtualSurface)   // feed virtual surface instead of real camera
 *   }
 */
class VCamCameraHook(
    private val ctx: Context,
    private val targetPkg: String
) {
    private val TAG = "VCamCameraHook"
    private val cfg by lazy { VCamPrefs.read(ctx, targetPkg) }
    private val executor = Executors.newScheduledThreadPool(1)
    private var pumpFuture: ScheduledFuture<*>? = null
    private var writer: ImageWriter? = null

    fun isActive(): Boolean = cfg.isActive

    fun start(surface: Surface) {
        Log.d(TAG, "VCam hook start: pkg=$targetPkg method=${cfg.method}")
        when (cfg.method) {
            2    -> startLocalVideo(surface)
            3    -> startNetworkStream(surface)
            4    -> startLocalPicture(surface)
            else -> Log.d(TAG, "method=1 (disable) — passthrough")
        }
    }

    fun stop() {
        pumpFuture?.cancel(true)
        runCatching { writer?.close() }
        writer = null
    }

    // ── Local video via MediaPlayer surface ────────────────────────────────
    private fun startLocalVideo(surface: Surface) {
        if (cfg.videoUri.isBlank()) { Log.w(TAG, "no video uri"); return }
        try {
            val mp = android.media.MediaPlayer().apply {
                setDataSource(ctx, android.net.Uri.parse(cfg.videoUri))
                setSurface(surface)
                isLooping = true
                if (!cfg.audio) setVolume(0f, 0f)
                prepare()
                start()
            }
            Log.d(TAG, "MediaPlayer started: ${cfg.videoUri}")
        } catch (e: Exception) {
            Log.e(TAG, "startLocalVideo failed: ${e.message}")
        }
    }

    // ── Network stream via MediaPlayer (RTSP/HLS/HTTP) ─────────────────────
    private fun startNetworkStream(surface: Surface) {
        if (cfg.netUrl.isBlank()) { Log.w(TAG, "no net url"); return }
        try {
            val mp = android.media.MediaPlayer().apply {
                setDataSource(cfg.netUrl)
                setSurface(surface)
                isLooping = true
                if (!cfg.audio) setVolume(0f, 0f)
                prepareAsync()
                setOnPreparedListener { start() }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error what=$what extra=$extra"); false
                }
            }
            Log.d(TAG, "Network stream started: ${cfg.netUrl}")
        } catch (e: Exception) {
            Log.e(TAG, "startNetworkStream failed: ${e.message}")
        }
    }

    // ── Static picture pumped as YUV frames ────────────────────────────────
    private fun startLocalPicture(surface: Surface) {
        if (cfg.picUri.isBlank()) { Log.w(TAG, "no pic uri"); return }
        val bmp = loadBitmap() ?: return
        val yuv = bitmapToYuv420(bmp, cfg.width, cfg.height)
        bmp.recycle()

        writer = ImageWriter.newInstance(surface, 3)
        val intervalMs = 1000L / cfg.fps
        pumpFuture = executor.scheduleAtFixedRate({
            pushFrame(yuv)
        }, 0L, intervalMs, TimeUnit.MILLISECONDS)
        Log.d(TAG, "Picture pump started @ ${cfg.fps}fps")
    }

    private fun loadBitmap(): Bitmap? = runCatching {
        ctx.contentResolver.openInputStream(android.net.Uri.parse(cfg.picUri))?.use { stream ->
            BitmapFactory.decodeStream(stream)?.let { raw ->
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

    // BT.601 ARGB → planar YUV420
    private fun bitmapToYuv420(bmp: Bitmap, w: Int, h: Int): ByteArray {
        val argb = IntArray(w * h).also { bmp.getPixels(it, 0, w, 0, 0, w, h) }
        val yuv  = ByteArray(w * h * 3 / 2)
        var yi = 0; var uvi = w * h
        for (j in 0 until h) for (i in 0 until w) {
            val px = argb[j * w + i]
            val r = (px shr 16) and 0xFF; val g = (px shr 8) and 0xFF; val b = px and 0xFF
            yuv[yi++] = (((66*r + 129*g + 25*b + 128) shr 8) + 16).coerceIn(16, 235).toByte()
            if (j % 2 == 0 && i % 2 == 0) {
                yuv[uvi++] = (((-38*r - 74*g + 112*b + 128) shr 8) + 128).coerceIn(16, 240).toByte()
                yuv[uvi++] = (((112*r - 94*g - 18*b + 128) shr 8) + 128).coerceIn(16, 240).toByte()
            }
        }
        return yuv
    }
}
