package com.wangyiheng.vcamsx.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
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

            // FIX: Encode a properly looping MP4 with enough real frames
            val outDir  = context.getExternalFilesDir(null) ?: context.filesDir
            val outFile = File(outDir, "copied_video.mp4")
            Log.d(TAG, "encoding image→mp4...")
            val encodeOk = bitmapToMp4Loop(bmp, outFile)
            Log.d(TAG, "image→mp4 done ok=$encodeOk size=${outFile.length()}b")

            mainHandler.post {
                hasImage.value  = true
                isLoading.value = false
                isActive.value  = true
                onResult(encodeOk)
            }

            // FIX: After MP4 is fully written, explicitly trigger the reader surface
            // so VideoPlayer.c2_reader_play feeds the virtual surface pipeline — mirrors video mode
            if (encodeOk) {
                triggerC2ReaderPlay()
            }

            // Also start GL renderer on virtual surface as live path
            HookBridge.getVirtualSurface()?.takeIf { it.isValid }?.let {
                Log.d(TAG, "virtual surface ready — starting GL renderer")
                startRenderer(bmp, it)
            }

        } catch (e: Exception) {
            Log.e(TAG, "decodeAndStart: ${e.message}", e)
            mainHandler.post { isLoading.value = false; onResult(false) }
        }
    }

    /**
     * FIX (Bug 1): The original encoder drew only 1 frame via lockCanvas then signaled EOS
     * immediately, producing a near-empty or corrupt MP4.
     *
     * This version:
     * - Uses a dedicated encoder input Surface rendered via Canvas
     * - Draws EVERY frame for the full DURATION_SEC at FPS rate
     * - Properly drains the encoder output buffer after each presentation
     * - Waits for BUFFER_FLAG_END_OF_STREAM before releasing
     */
    private fun bitmapToMp4Loop(src: Bitmap, outFile: File): Boolean {
        val W            = 720
        val H            = 1280
        val FPS          = 10
        val DURATION_SEC = 10    // 10s loop at 10fps = 100 frames — tight but valid
        val TOTAL_FRAMES = FPS * DURATION_SEC
        val FRAME_US     = 1_000_000L / FPS

        val scaled = if (src.width == W && src.height == H) src
                     else Bitmap.createScaledBitmap(src, W, H, true)

        val mf = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, W, H).apply {
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            setInteger(MediaFormat.KEY_BIT_RATE,         2_000_000)
            setInteger(MediaFormat.KEY_FRAME_RATE,       FPS)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
        }

        val codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        codec.configure(mf, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        val inputSurface = codec.createInputSurface()
        codec.start()

        val muxer  = MediaMuxer(outFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        val paint  = Paint(Paint.ANTI_ALIAS_FLAG)
        val dstRect = RectF(0f, 0f, W.toFloat(), H.toFloat())

        var trackIdx = -1
        var muxerStarted = false
        val info = MediaCodec.BufferInfo()

        // FIX: Draw every frame explicitly; drain encoder output after each push
        for (frameIdx in 0 until TOTAL_FRAMES) {
            // Push frame into encoder input surface
            val canvas: Canvas = inputSurface.lockCanvas(null)
            canvas.drawBitmap(scaled, null, dstRect, paint)
            inputSurface.unlockCanvasAndPost(canvas)

            // Drain whatever the encoder has produced so far
            val deadlineMs = System.currentTimeMillis() + 200L
            while (System.currentTimeMillis() < deadlineMs) {
                val outIdx = codec.dequeueOutputBuffer(info, 5_000L)
                when {
                    outIdx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                        trackIdx = muxer.addTrack(codec.outputFormat)
                        muxer.start()
                        muxerStarted = true
                    }
                    outIdx >= 0 -> {
                        val buf = codec.getOutputBuffer(outIdx)
                        if (buf != null && muxerStarted && trackIdx >= 0 &&
                            (info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0 &&
                            info.size > 0) {
                            info.presentationTimeUs = frameIdx * FRAME_US
                            muxer.writeSampleData(trackIdx, buf, info)
                        }
                        codec.releaseOutputBuffer(outIdx, false)
                        // Keep draining if encoder still has buffered output
                        if ((info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) break
                        if (outIdx >= 0 && info.size > 0) break // one good sample per frame push is enough
                    }
                    else -> break
                }
            }
        }

        // Signal end and drain remaining
        codec.signalEndOfInputStream()
        val eosDeadline = System.currentTimeMillis() + 3_000L
        while (System.currentTimeMillis() < eosDeadline) {
            val outIdx = codec.dequeueOutputBuffer(info, 10_000L)
            when {
                outIdx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                    if (!muxerStarted) {
                        trackIdx = muxer.addTrack(codec.outputFormat)
                        muxer.start(); muxerStarted = true
                    }
                }
                outIdx >= 0 -> {
                    val buf = codec.getOutputBuffer(outIdx)
                    if (buf != null && muxerStarted && trackIdx >= 0 &&
                        (info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0 &&
                        info.size > 0) {
                        muxer.writeSampleData(trackIdx, buf, info)
                    }
                    codec.releaseOutputBuffer(outIdx, false)
                    if ((info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) break
                }
            }
        }

        try { codec.stop() }   catch (_: Exception) {}
        codec.release()
        if (muxerStarted) try { muxer.stop() } catch (_: Exception) {}
        muxer.release()
        inputSurface.release()
        if (scaled !== src) scaled.recycle()

        return outFile.exists() && outFile.length() > 4096L
    }

    /**
     * FIX (Bug 2): After the MP4 is fully written, trigger c2_reader_play so the
     * ImageWriter/VideoToFrames pipeline (which feeds Camera2 ImageReader surfaces)
     * actually starts reading from our image MP4 — same as video mode does.
     */
    private fun triggerC2ReaderPlay() {
        try {
            val readerSurface = com.wangyiheng.vcamsx.MainHook.c2_reader_Surfcae
            if (readerSurface != null && readerSurface.isValid) {
                Log.d(TAG, "triggerC2ReaderPlay: kicking c2_reader_play")
                VideoPlayer.c2_reader_play(readerSurface)
            } else {
                Log.d(TAG, "triggerC2ReaderPlay: no c2_reader_surface yet — will fire on camera2Play")
            }
        } catch (e: Exception) {
            Log.e(TAG, "triggerC2ReaderPlay: ${e.message}")
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
        } catch (_: Exception) { null }
            ?: surface.takeIf { it.isValid }
            ?: return
        Log.d(TAG, "attachC1Surface: $target")
        startRenderer(bmp, target)
    }

    fun activateInjection() {
        if (!hasImage.value) return
        mainHandler.post { isActive.value = true }
        val bmp  = currentBitmap ?: return
        val virt = HookBridge.getVirtualSurface()?.takeIf { it.isValid } ?: return
        startRenderer(bmp, virt)
        // Also kick the reader pipeline so Camera2 ImageReader surfaces get data
        triggerC2ReaderPlay()
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
