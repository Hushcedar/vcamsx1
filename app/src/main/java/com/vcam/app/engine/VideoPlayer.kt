package com.vcam.app.engine

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.Surface
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object VideoPlayer {
    private const val TAG = "VCam-Player"

    var currentRunningSurface: Surface? = null
    var c2_hw_decode_obj: VideoToFrames? = null
    var copyReaderSurface: Surface? = null

    private var mediaPlayer:   MediaPlayer?      = null
    private var ijkPlayer:     IjkMediaPlayer?   = null
    private var transformer:   VideoSurfaceTransformer? = null
    private var isInitializing = false

    var outWidth  = 1280
    var outHeight = 720

    init {
        Executors.newSingleThreadScheduledExecutor()
            .scheduleWithFixedDelay({ checkSurface() }, 3L, 3L, TimeUnit.SECONDS)
    }

    fun initializeTheStateAsWellAsThePlayer() {
        InfoProcesser.initStatus()
        val status = InfoProcesser.videoStatus ?: return
        if (status.isLiveStreamingEnabled && !status.RealsceneEnabled) initRtmp()
    }

    fun camera2Play() {
        MainHook.original_preview_Surface?.let { if (it.isValid) play(it) }
        MainHook.c2_reader_Surfcae?.let { c2_reader_play(it) }
    }

    fun c1_camera_play() {
        val st     = MainHook.original_c1_preview_SurfaceTexture
        val status = InfoProcesser.videoStatus
        if (st != null && status?.isVideoEnable == true) {
            val s = Surface(st); if (s.isValid) play(s)
        }
        MainHook.oriHolder?.surface?.let { if (it.isValid) play(it) }
        MainHook.c2_reader_Surfcae?.let { c2_reader_play(it) }
    }

    fun c2_reader_play(surface: Surface) {
        if (surface == copyReaderSurface) return
        copyReaderSurface = surface
        c2_hw_decode_obj?.stopDecode()
        c2_hw_decode_obj = VideoToFrames()
        if (InfoProcesser.videoStatus?.RealsceneEnabled == true) { releaseAll(); return }
        try {
            c2_hw_decode_obj?.setSaveFrames(OutputImageFormat.NV21)
            c2_hw_decode_obj?.set_surface(surface)
            c2_hw_decode_obj?.decode(Uri.parse("content://com.vcam.app.videoprovider"))
        } catch (e: Exception) { Log.d(TAG, "c2_reader_play: ${e.message}") }
    }

    private fun play(surface: Surface) {
        try {
            InfoProcesser.initStatus()
            val status = InfoProcesser.videoStatus ?: return
            if (status.isLiveStreamingEnabled) { ijkPlayer?.setSurface(surface); return }
            if (surface == currentRunningSurface && mediaPlayer?.isPlaying == true) return
            if (!isInitializing) startPlayer(surface)
        } catch (e: Exception) { Log.e(TAG, "play: ${e.message}") }
    }

    private fun startPlayer(surface: Surface) {
        if (isInitializing) return
        isInitializing = true
        val ctx = MainHook.context ?: run { isInitializing = false; return }
        val vol = if (InfoProcesser.videoStatus?.volume == true) 1f else 0f
        try {
            releasePipelineAndPlayer()
            val t = VideoSurfaceTransformer(surface, outWidth, outHeight).also { it.start() }
            transformer = t
            mediaPlayer = MediaPlayer().apply {
                isLooping = true
                setSurface(t.inputSurface)
                setVolume(vol, vol)
                setOnPreparedListener {
                    isInitializing = false
                    currentRunningSurface = surface
                    if (!VideoControls.isPaused.value) it.start()
                    Log.d(TAG, "MediaPlayer ready")
                }
                setOnErrorListener { _, w, e ->
                    Log.e(TAG, "MP error $w/$e"); isInitializing = false; false
                }
                setDataSource(ctx, Uri.parse("content://com.vcam.app.videoprovider"))
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e(TAG, "startPlayer: ${e.message}", e); isInitializing = false
        }
    }

    fun togglePause() {
        try {
            if (VideoControls.isPaused.value) {
                mediaPlayer?.start(); ijkPlayer?.start()
                VideoControls.isPaused.value = false
            } else {
                mediaPlayer?.pause(); ijkPlayer?.pause()
                VideoControls.isPaused.value = true
            }
        } catch (e: Exception) { Log.e(TAG, "togglePause: ${e.message}") }
    }

    fun reload() {
        try {
            VideoControls.isPaused.value = false
            mediaPlayer?.seekTo(0); mediaPlayer?.start()
            ijkPlayer?.seekTo(0);   ijkPlayer?.start()
        } catch (e: Exception) { Log.e(TAG, "reload: ${e.message}") }
    }

    fun rotate() {
        val r = (VideoControls.rotation.value + 90) % 360
        VideoControls.rotation.value = r
        transformer?.setRotation(r)
    }

    fun flip() {
        val f = !VideoControls.isFlipped.value
        VideoControls.isFlipped.value = f
        transformer?.setFlip(f)
    }

    fun zoomIn() {
        val s = (VideoControls.scale.value + 0.05f).coerceAtMost(4f)
        VideoControls.scale.value = s
        transformer?.setScale(s)
    }

    fun zoomOut() {
        val s = (VideoControls.scale.value - 0.05f).coerceAtLeast(0.1f)
        VideoControls.scale.value = s
        transformer?.setScale(s)
    }

    fun adjustOffset(dx: Int, dy: Int) {
        val step = 0.04f
        val x = (VideoControls.offsetX.value + if (dx > 0) step else if (dx < 0) -step else 0f).coerceIn(-2f, 2f)
        val y = (VideoControls.offsetY.value + if (dy > 0) step else if (dy < 0) -step else 0f).coerceIn(-2f, 2f)
        VideoControls.offsetX.value = x; VideoControls.offsetY.value = y
        transformer?.setOffset(x, y)
    }

    fun resetTransform() {
        VideoControls.scale.value   = 1f
        VideoControls.offsetX.value = 0f
        VideoControls.offsetY.value = 0f
        transformer?.setScale(1f); transformer?.setOffset(0f, 0f)
    }

    private fun initRtmp() {
        val status = InfoProcesser.videoStatus ?: return
        ijkPlayer?.release()
        ijkPlayer = IjkMediaPlayer().apply {
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport",   "tcp")
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration",  1L)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize",        1024L)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop",        1L)
            dataSource = status.liveURL
            prepareAsync()
            setOnPreparedListener {
                MainHook.original_preview_Surface?.let { s -> setSurface(s) }
                start()
            }
        }
    }

    private fun checkSurface() {
        val s = currentRunningSurface ?: return
        if (!s.isValid) { Log.w(TAG, "surface died"); releaseAll() }
    }

    private fun releasePipelineAndPlayer() {
        try { mediaPlayer?.stop() } catch (_: Exception) {}
        mediaPlayer?.release(); mediaPlayer = null
        transformer?.release();  transformer  = null
        isInitializing = false
    }

    fun releaseAll() {
        releasePipelineAndPlayer()
        try { ijkPlayer?.stop() } catch (_: Exception) {}
        ijkPlayer?.release(); ijkPlayer = null
        currentRunningSurface = null
    }

    fun releaseMediaPlayer() = releaseAll()
    val ijkMediaPlayer: IjkMediaPlayer? get() = ijkPlayer
}
