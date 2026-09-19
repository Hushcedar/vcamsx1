package com.wangyiheng.vcamsx.utils

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.Surface
import com.wangyiheng.vcamsx.MainHook
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object VideoPlayer {
    private const val TAG = "VCamSX-Player"

    var ijkMediaPlayer: IjkMediaPlayer? = null
    var mediaPlayer: MediaPlayer?        = null
    var c2_hw_decode_obj: VideoToFrames? = null
    var copyReaderSurface: Surface?      = null
    var currentRunningSurface: Surface?  = null

    @Volatile private var isInitializing  = false
    private var activeTransformer: VideoSurfaceTransformer? = null

    private val imageWriters = ConcurrentHashMap<android.media.ImageWriter, Triple<Int,Int,Int>>()
    @Volatile private var writerThread: Thread? = null

    fun addImageWriterTarget(surface: Surface, format: Int, w: Int, h: Int) {
        if (Build.VERSION.SDK_INT < 23) return
        val injectable = setOf(35, 17, 16, 842094169)
        if (format !in injectable) { Log.d(TAG, "IW skip fmt=0x${format.toString(16)}"); return }
        try {
            val writer = android.media.ImageWriter.newInstance(surface, 3)
            imageWriters[writer] = Triple(format, w, h)
            startWriterLoop()
            Log.d(TAG, "IW registered fmt=$format ${w}x${h}")
        } catch (e: Exception) { Log.e(TAG, "addIW: ${e.message}") }
    }

    fun startWriterLoop() {
        if (writerThread?.isAlive == true) return
        writerThread = Thread({
            while (!Thread.currentThread().isInterrupted) {
                val frame = if (ImagePlayer.isActive.value) {
                    ImagePlayer.currentBitmapSnapshot()?.let { bmp ->
                        bitmapToNv21(bmp, 720, 1280)
                    } ?: VideoToFrames.data_buffer
                } else {
                    VideoToFrames.data_buffer
                }
                if (frame.size > 1) {
                    imageWriters.forEach { (writer, info) ->
                        val (_, w, h) = info
                        try {
                            val img = writer.dequeueInputImage() ?: return@forEach
                            writeNV21(img, frame, w, h)
                            writer.queueInputImage(img)
                        } catch (_: Exception) {}
                    }
                }
                try { Thread.sleep(33) } catch (_: InterruptedException) { return@Thread }
            }
        }, "VCamSX-IW").apply { isDaemon = true; start() }
    }

    private fun writeNV21(img: android.media.Image, nv21: ByteArray, w: Int, h: Int) {
        val planes = img.planes; val ySize = w * h
        val yBuf = planes[0].buffer; val yStr = planes[0].rowStride
        if (yStr == w) {
            yBuf.put(nv21, 0, minOf(ySize, yBuf.remaining(), nv21.size))
        } else {
            for (r in 0 until h) {
                yBuf.position(r * yStr)
                val off = r * w; val len = minOf(w, yBuf.remaining(), nv21.size - off)
                if (len <= 0) break; yBuf.put(nv21, off, len)
            }
        }
        if (planes.size < 3) return
        val uBuf = planes[1].buffer; val vBuf = planes[2].buffer
        val uStr = planes[1].rowStride; val uPix = planes[1].pixelStride
        for (r in 0 until h / 2) {
            for (c in 0 until w / 2) {
                val src = ySize + r * w + c * 2; if (src + 1 >= nv21.size) break
                val dst = r * uStr + c * uPix
                if (dst < uBuf.capacity()) { uBuf.position(dst); uBuf.put(nv21[src + 1]) }
                if (dst < vBuf.capacity()) { vBuf.position(dst); vBuf.put(nv21[src]) }
            }
        }
    }

    init {
        Executors.newSingleThreadScheduledExecutor()
            .scheduleWithFixedDelay({ healthCheck() }, 3L, 3L, TimeUnit.SECONDS)
    }

    fun initializeTheStateAsWellAsThePlayer() {
        InfoProcesser.initStatus()
        val status = InfoProcesser.videoStatus ?: return
        if (status.isLiveStreamingEnabled && !status.RealsceneEnabled) initRTMPStreamPlayer()
    }

    fun camera2Play() {
        MainHook.original_preview_Surface?.let { s ->
            if (s.isValid) {
                if (ImagePlayer.isActive.value) {
                    val virt = MainHook.c2_virtual_surface
                    if (virt != null && virt.isValid) ImagePlayer.attachSurface(virt)
                    else ImagePlayer.attachSurface(s)
                } else if (InfoProcesser.videoStatus?.isVideoEnable == true) {
                    handleMediaPlayer(s)
                }
            }
        }
        MainHook.c2_reader_Surfcae?.let { s -> c2_reader_play(s) }
    }

    fun c1_camera_play() {
        if (ImagePlayer.isActive.value) {
            MainHook.original_c1_preview_SurfaceTexture?.let {
                try { val s = Surface(it); if (s.isValid) ImagePlayer.attachC1Surface(s) } catch (_: Exception) {}
            }
            MainHook.oriHolder?.surface?.let {
                try { if (it.isValid) ImagePlayer.attachC1Surface(it) } catch (_: Exception) {}
            }
            return
        }
        val status = InfoProcesser.videoStatus ?: return
        if (status.isVideoEnable != true) return
        val st = MainHook.original_c1_preview_SurfaceTexture
        if (st != null) {
            try { val s = Surface(st); if (s.isValid) handleMediaPlayer(s) } catch (_: Exception) {}
        }
        val holder = MainHook.oriHolder
        if (holder != null) {
            try { holder.surface?.let { if (it.isValid) handleMediaPlayerDirect(it) } } catch (_: Exception) {}
        }
        MainHook.c2_reader_Surfcae?.let { c2_reader_play(it) }
    }

    fun onCameraSwitch() {
        activeTransformer?.stop()
        activeTransformer     = null
        currentRunningSurface = null
    }

    fun c2_reader_play(surface: Surface) {
        if (surface == copyReaderSurface) return
        copyReaderSurface = surface
        if (ImagePlayer.isActive.value) {
            startWriterLoop()
            Log.d(TAG, "c2_reader_play: image mode, writer loop running")
            return
        }
        c2_hw_decode_obj?.stopDecode()
        c2_hw_decode_obj = VideoToFrames()
        if (InfoProcesser.videoStatus?.RealsceneEnabled == true) { releaseMediaPlayer(); return }
        try {
            c2_hw_decode_obj?.setSaveFrames(OutputImageFormat.NV21)
            c2_hw_decode_obj?.set_surface(surface)
            c2_hw_decode_obj?.decode(Uri.parse("content://com.wangyiheng.vcamsx.videoprovider"))
        } catch (e: Exception) { Log.d(TAG, "c2_reader_play: ${e.message}") }
    }

    private fun handleMediaPlayer(surface: Surface) {
        try {
            InfoProcesser.initStatus()
            val status = InfoProcesser.videoStatus ?: return
            val volume = if (status.volume) 1f else 0f
            if (status.isLiveStreamingEnabled) {
                ijkMediaPlayer?.setVolume(volume, volume); ijkMediaPlayer?.setSurface(surface); return
            }
            val mp = mediaPlayer
            if (mp != null && mp.isPlaying) {
                mp.setVolume(volume, volume)
                if (surface != currentRunningSurface) {
                    val tx = buildTransformer(surface)
                    activeTransformer?.stop()
                    activeTransformer     = tx
                    currentRunningSurface = surface
                    val newTarget = tx?.inputSurface ?: surface
                    try { mp.setSurface(newTarget) } catch (e: Exception) { Log.e(TAG, "reattach: ${e.message}") }
                }
                return
            }
            if (isInitializing) return
            val tx = buildTransformer(surface)
            activeTransformer?.stop()
            activeTransformer = tx
            initMediaPlayer(tx?.inputSurface ?: surface)
        } catch (e: Exception) { Log.e(TAG, "handleMP: ${e.message}") }
    }

    private fun handleMediaPlayerDirect(surface: Surface) {
        val mp = mediaPlayer
        if (mp != null && mp.isPlaying && surface == currentRunningSurface) return
        initMediaPlayerSurface(surface)
    }

    private fun initMediaPlayer(surface: Surface) {
        if (isInitializing) return; isInitializing = true
        val ctx    = MainHook.context ?: run { isInitializing = false; return }
        val status = InfoProcesser.videoStatus
        val volume = if (status?.volume == true) 1f else 0f
        try {
            mediaPlayer = MediaPlayer().apply {
                isLooping = true; setSurface(surface); setVolume(volume, volume)
                setOnPreparedListener { player ->
                    isInitializing = false; currentRunningSurface = surface
                    applySpeed(player)
                    if (!VideoControls.isPaused.value) player.start()
                    try { MainHook.origin_preview_camera?.stopPreview() } catch (_: Exception) {}
                }
                setOnErrorListener { _, w, e ->
                    Log.e(TAG, "MP error $w/$e"); isInitializing = false; false
                }
                setDataSource(ctx, Uri.parse("content://com.wangyiheng.vcamsx.videoprovider"))
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e(TAG, "initMP: ${e.message}"); activeTransformer?.stop(); activeTransformer = null; isInitializing = false
        }
    }

    private fun initMediaPlayerSurface(surface: Surface) {
        if (isInitializing) return; isInitializing = true
        val ctx    = MainHook.context ?: run { isInitializing = false; return }
        val status = InfoProcesser.videoStatus
        val volume = if (status?.volume == true) 1f else 0f
        try {
            mediaPlayer = MediaPlayer().apply {
                isLooping = true; setSurface(surface); setVolume(volume, volume)
                setOnPreparedListener { player ->
                    isInitializing = false; currentRunningSurface = surface
                    applySpeed(player)
                    if (!VideoControls.isPaused.value) player.start()
                    try { MainHook.origin_preview_camera?.stopPreview() } catch (_: Exception) {}
                }
                setOnErrorListener { _, w, e ->
                    Log.e(TAG, "MPS error $w/$e"); isInitializing = false; false
                }
                setDataSource(ctx, Uri.parse("content://com.wangyiheng.vcamsx.videoprovider"))
                prepareAsync()
            }
        } catch (e: Exception) { Log.e(TAG, "initMPS: ${e.message}"); isInitializing = false }
    }

    private fun buildTransformer(target: Surface): VideoSurfaceTransformer? {
        return try {
            val t = VideoSurfaceTransformer(
                targetSurface = target,
                rotationDeg   = VideoControls.rotation.value,
                flipH         = VideoControls.isFlipped.value,
                bufferWidth   = 720,
                bufferHeight  = 1280
            )
            if (t.start()) t else { Log.e(TAG, "GL timeout"); null }
        } catch (e: Exception) { Log.e(TAG, "buildTX: ${e.message}"); null }
    }

    private fun applySpeed(mp: MediaPlayer) {
        if (Build.VERSION.SDK_INT >= 23) {
            try { mp.playbackParams = mp.playbackParams.setSpeed(VideoControls.speed) }
            catch (e: Exception) { Log.e(TAG, "applySpeed: ${e.message}") }
        }
    }

    fun cycleSpeed() {
        val steps = VideoControls.speedSteps
        VideoControls.speedIndex.value = (VideoControls.speedIndex.value + 1) % steps.size
        val newSpeed = VideoControls.speed
        try {
            mediaPlayer?.let { mp ->
                if (Build.VERSION.SDK_INT >= 23)
                    mp.playbackParams = mp.playbackParams.setSpeed(newSpeed)
            }
            ijkMediaPlayer?.setSpeed(newSpeed)
        } catch (e: Exception) { Log.e(TAG, "cycleSpeed: ${e.message}") }
    }

    fun togglePause() {
        try {
            if (VideoControls.isPaused.value) {
                mediaPlayer?.start(); ijkMediaPlayer?.start()
                VideoControls.isPaused.value = false
            } else {
                mediaPlayer?.pause(); ijkMediaPlayer?.pause()
                VideoControls.isPaused.value = true
            }
        } catch (e: Exception) { Log.e(TAG, "pause: ${e.message}") }
    }

    fun reload() {
        try { VideoControls.isPaused.value = false; mediaPlayer?.seekTo(0); mediaPlayer?.start() }
        catch (e: Exception) { Log.e(TAG, "reload: ${e.message}") }
    }

    fun rotate() {
        VideoControls.rotation.value = (VideoControls.rotation.value + 90) % 360
        if (ImagePlayer.isActive.value) { ImagePlayer.rotate(); return }
        activeTransformer?.also { it.rotationDeg = VideoControls.rotation.value; it.needsRedraw = true }
        restartDecoders()
    }

    fun flip() {
        VideoControls.isFlipped.value = !VideoControls.isFlipped.value
        if (ImagePlayer.isActive.value) { ImagePlayer.flip(); return }
        activeTransformer?.also { it.flipH = VideoControls.isFlipped.value; it.needsRedraw = true }
    }

    fun adjustOffset(dx: Int, dy: Int) {
        if (ImagePlayer.isActive.value) { ImagePlayer.adjustOffset(dx, dy); return }
        try {
            val t = activeTransformer ?: return
            t.offsetX = (t.offsetX + dx * (2f / 1280f)).coerceIn(-1.5f, 1.5f)
            t.offsetY = (t.offsetY + dy * (2f / 720f)).coerceIn(-1.5f, 1.5f)
            t.needsRedraw = true
        } catch (e: Exception) { Log.e(TAG, "adj: ${e.message}") }
    }

    fun zoomIn() {
        if (ImagePlayer.isActive.value) { ImagePlayer.zoomIn(); return }
        try {
            val s = (VideoControls.scale.value + 0.1f).coerceAtMost(3.0f)
            VideoControls.scale.value = s
            activeTransformer?.also { it.scaleValue = s; it.needsRedraw = true }
        } catch (e: Exception) { Log.e(TAG, "zoomIn: ${e.message}") }
    }

    fun zoomOut() {
        if (ImagePlayer.isActive.value) { ImagePlayer.zoomOut(); return }
        try {
            val s = (VideoControls.scale.value - 0.1f).coerceAtLeast(0.3f)
            VideoControls.scale.value = s
            activeTransformer?.also { it.scaleValue = s; it.needsRedraw = true }
        } catch (e: Exception) { Log.e(TAG, "zoomOut: ${e.message}") }
    }

    private fun restartDecoders() {
        val rs = copyReaderSurface ?: return
        copyReaderSurface = null; c2_hw_decode_obj?.stopDecode(); c2_reader_play(rs)
    }

    fun initRTMPStreamPlayer() {
        synchronized(this) {
            ijkMediaPlayer?.release(); ijkMediaPlayer = null
            val status = InfoProcesser.videoStatus ?: return
            ijkMediaPlayer = IjkMediaPlayer().apply {
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport",   "tcp")
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration",  1L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize",        1024L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop",        1L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets",    1L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "video-rotate",     VideoControls.rotation.value.toLong())
                dataSource = status.liveURL; prepareAsync()
                setOnPreparedListener {
                    MainHook.original_preview_Surface?.let { s -> setSurface(s) }; start()
                }
            }
        }
    }

    private fun healthCheck() {
        val s = currentRunningSurface ?: return
        if (!s.isValid) {
            activeTransformer?.stop(); activeTransformer    = null
            mediaPlayer?.release();    mediaPlayer          = null
            currentRunningSurface = null
            isInitializing = false
        }
    }

    fun bitmapToNv21Public(src: android.graphics.Bitmap, outW: Int, outH: Int): ByteArray =
        bitmapToNv21(src, outW, outH)

    private fun bitmapToNv21(src: android.graphics.Bitmap, outW: Int, outH: Int): ByteArray {
        val scaled = if (src.width == outW && src.height == outH) src
                     else android.graphics.Bitmap.createScaledBitmap(src, outW, outH, true)
        val argb  = IntArray(outW * outH)
        scaled.getPixels(argb, 0, outW, 0, 0, outW, outH)
        if (scaled !== src) scaled.recycle()
        val nv21  = ByteArray(outW * outH * 3 / 2)
        var yIdx  = 0; var uvIdx = outW * outH
        for (row in 0 until outH) {
            for (col in 0 until outW) {
                val px = argb[row * outW + col]
                val r  = (px shr 16) and 0xFF
                val g  = (px shr  8) and 0xFF
                val b  =  px         and 0xFF
                val y  = ((66 * r + 129 * g + 25 * b + 128) shr 8) + 16
                nv21[yIdx++] = y.coerceIn(16, 235).toByte()
                if (row % 2 == 0 && col % 2 == 0) {
                    val cb = ((-38 * r - 74 * g + 112 * b + 128) shr 8) + 128
                    val cr = ((112 * r - 94 * g - 18 * b + 128) shr 8) + 128
                    nv21[uvIdx++] = cr.coerceIn(16, 240).toByte()
                    nv21[uvIdx++] = cb.coerceIn(16, 240).toByte()
                }
            }
        }
        return nv21
    }

    fun releaseMediaPlayer() {
        try { if (mediaPlayer?.isPlaying == true)    mediaPlayer?.stop()    } catch (_: Exception) {}
        try { mediaPlayer?.release()                                         } catch (_: Exception) {}
        try { if (ijkMediaPlayer?.isPlaying == true) ijkMediaPlayer?.stop() } catch (_: Exception) {}
        try { ijkMediaPlayer?.release()                                      } catch (_: Exception) {}
        try { activeTransformer?.stop()                                      } catch (_: Exception) {}
        mediaPlayer = null; ijkMediaPlayer = null; activeTransformer = null; isInitializing = false
    }
}
