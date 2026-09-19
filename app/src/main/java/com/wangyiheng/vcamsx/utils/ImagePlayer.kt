package com.wangyiheng.vcamsx.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import androidx.compose.runtime.mutableStateOf
import java.io.File

object ImagePlayer {

    private const val TAG = "VCamSX-ImagePlayer"

    val isActive  = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val hasImage  = mutableStateOf(false)

    @Volatile private var currentBitmap: Bitmap? = null
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

        Thread({ decodeAndStart(context, bytes, onResult) }, "VCamSX-ImgLoad")
            .apply { isDaemon = true; start() }
    }

    private fun decodeAndStart(context: Context, bytes: ByteArray, onResult: (Boolean) -> Unit) {
        try {
            val probe = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, probe)
            if (probe.outWidth <= 0 || probe.outHeight <= 0) {
                mainHandler.post { isLoading.value = false; onResult(false) }
                return
            }

            var sample = 1
            while (probe.outWidth / sample > 1080 || probe.outHeight / sample > 1920) sample *= 2

            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size,
                BitmapFactory.Options().apply {
                    inSampleSize      = sample
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
            ) ?: run {
                mainHandler.post { isLoading.value = false; onResult(false) }
                return
            }

            Log.d(TAG, "decoded ${bmp.width}x${bmp.height}")
            currentBitmap?.recycle()
            currentBitmap = bmp

            val outDir  = context.getExternalFilesDir(null) ?: context.filesDir
            val outFile = File(outDir, "copied_video.mp4")
            Log.d(TAG, "encoding image→mp4...")
            bitmapToMp4Loop(bmp, outFile)
            Log.d(TAG, "image→mp4 done: ${outFile.length()}b")

            mainHandler.post {
                hasImage.value  = true
                isLoading.value = false
                isActive.value  = true
                onResult(true)
            }

            HookBridge.getVirtualSurface()?.takeIf { it.isValid }?.let {
                Log.d(TAG, "virtual surface ready — also starting GL renderer")
                startRenderer(bmp, it)
            }

        } catch (e: Exception) {
            Log.e(TAG, "decodeAndStart: ${e.message}", e)
            mainHandler.post { isLoading.value = false; onResult(false) }
        }
    }

    private fun bitmapToMp4Loop(src: Bitmap, outFile: File) {
        val W   = 720; val H = 1280
        val FPS = 1;   val DURATION_SEC = 30

        val scale   = minOf(W.toFloat() / src.width, H.toFloat() / src.height, 1.0f)
        val drawW   = (src.width  * scale).toInt().coerceAtLeast(1)
        val drawH   = (src.height * scale).toInt().coerceAtLeast(1)
        val left    = (W - drawW) / 2
        val top     = (H - drawH) / 2

        val canvas720 = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888)
        val c         = android.graphics.Canvas(canvas720)
        c.drawColor(android.graphics.Color.BLACK)
        val scaled    = if (drawW == src.width && drawH == src.height) src
                        else Bitmap.createScaledBitmap(src, drawW, drawH, true)
        c.drawBitmap(scaled, left.toFloat(), top.toFloat(), null)
        if (scaled !== src) scaled.recycle()

        val muxer = MediaMuxer(
            outFile.absolutePath,
            MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4
        )

        val mf = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, W, H).apply {
            setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            setInteger(MediaFormat.KEY_BIT_RATE,          2_000_000)
            setInteger(MediaFormat.KEY_FRAME_RATE,        FPS)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,  1)
            setInteger(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1_000_000 / FPS)
        }

        val codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        codec.configure(mf, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        val inputSurface = codec.createInputSurface()
        codec.start()

        val encCanvas = inputSurface.lockCanvas(null)
        encCanvas.drawBitmap(canvas720, null, RectF(0f, 0f, W.toFloat(), H.toFloat()), null)
        inputSurface.unlockCanvasAndPost(encCanvas)
        canvas720.recycle()

        Thread.sleep((DURATION_SEC * 1000L / FPS).coerceAtMost(2000L))
        codec.signalEndOfInputStream()

        val info     = MediaCodec.BufferInfo()
        var trackIdx = -1
        var started  = false
        val deadline = System.currentTimeMillis() + 5000L

        while (System.currentTimeMillis() < deadline) {
            val idx = codec.dequeueOutputBuffer(info, 10_000L)
            when {
                idx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                    trackIdx = muxer.addTrack(codec.outputFormat)
                    muxer.start(); started = true
                }
                idx >= 0 -> {
                    val buf = codec.getOutputBuffer(idx)
                    if (buf != null && started && trackIdx >= 0 &&
                        (info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0) {
                        muxer.writeSampleData(trackIdx, buf, info)
                    }
                    codec.releaseOutputBuffer(idx, false)
                    if ((info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) break
                }
            }
        }

        try { codec.stop() } catch (_: Exception) {}
        codec.release()
        if (started) try { muxer.stop() } catch (_: Exception) {}
        muxer.release()
        inputSurface.release()
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
        } catch (_: Exception) { null }
            ?: surface.takeIf { it.isValid }
            ?: return
        Log.d(TAG, "attachC1Surface: $target")
        startRenderer(bmp, target)
    }

    fun activateInjection() {
        if (!hasImage.value) return
        mainHandler.post { isActive.value = true }
        val bmp = currentBitmap ?: return
        val virt = HookBridge.getVirtualSurface()?.takeIf { it.isValid }
        if (virt != null) {
            startRenderer(bmp, virt)
            return
        }
        val fakeST = HookBridge.getFakeSurfaceTexture()
        if (fakeST != null) {
            try {
                val s = Surface(fakeST)
                if (s.isValid) { startRenderer(bmp, s); return }
            } catch (_: Exception) {}
        }
        Log.d(TAG, "activateInjection: no surface yet, renderer will attach on camera open")
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
            if (!r.start()) { Log.e(TAG, "renderer failed to start"); return }
            activeRenderer = r
            Log.d(TAG, "renderer running on $targetSurface")
        } catch (e: Exception) { Log.e(TAG, "startRenderer: ${e.message}", e) }
    }

    private fun stopRenderer() {
        activeRenderer?.stop()
        activeRenderer = null
    }
}
