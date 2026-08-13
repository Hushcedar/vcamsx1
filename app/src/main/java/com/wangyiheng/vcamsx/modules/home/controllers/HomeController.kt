package com.wangyiheng.vcamsx.modules.home.controllers

import android.content.Context
import android.net.Uri
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wangyiheng.vcamsx.data.models.VideoStatues
import com.wangyiheng.vcamsx.utils.InfoManager
import com.wangyiheng.vcamsx.utils.ImagePlayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.File

class HomeController : ViewModel(), KoinComponent {

    val context by inject<Context>()
    val isVideoEnabled         = mutableStateOf(false)
    val isVolumeEnabled        = mutableStateOf(false)
    val videoPlayer            = mutableStateOf(1)
    val codecType              = mutableStateOf(true)
    val isLiveStreamingEnabled = mutableStateOf(false)
    val isRealSceneEnabled     = mutableStateOf(false)

    val infoManager by inject<InfoManager>()
    var mediaPlayer: IjkMediaPlayer? = null
    private var retryCount    = 0
    private val maxRetryCount = 5
    val isLiveStreamingDisplay = mutableStateOf(false)
    val isVideoDisplay         = mutableStateOf(false)
    var liveURL = mutableStateOf("rtmp://ns8.indexforce.com/home/mystream")

    fun init() { getState() }

    fun copyVideoToAppDir(context: Context, videoUri: Uri) {
        val inputStream = context.contentResolver.openInputStream(videoUri)
        val outputFile  = File(context.getExternalFilesDir(null)!!.absolutePath, "copied_video.mp4")
        inputStream?.use { input -> outputFile.outputStream().use { input.copyTo(it) } }
    }

    fun saveState() {
        infoManager.removeVideoStatus()
        infoManager.saveVideoStatus(
            VideoStatues(
                isVideoEnable          = isVideoEnabled.value,
                volume                 = isVolumeEnabled.value,
                videoPlayer            = videoPlayer.value,
                codecType              = codecType.value,
                isLiveStreamingEnabled = isLiveStreamingEnabled.value,
                liveURL                = liveURL.value,
                RealsceneEnabled       = isRealSceneEnabled.value
            )
        )
    }

    fun getState() {
        infoManager.getVideoStatus()?.let {
            isVideoEnabled.value         = it.isVideoEnable
            isVolumeEnabled.value        = it.volume
            videoPlayer.value            = it.videoPlayer
            codecType.value              = it.codecType
            isLiveStreamingEnabled.value = it.isLiveStreamingEnabled
            liveURL.value                = it.liveURL
            isRealSceneEnabled.value     = it.RealsceneEnabled
        }
    }

    fun playVideo(holder: SurfaceHolder, videoPath: String) {
        mediaPlayer = IjkMediaPlayer().apply {
            dataSource = videoPath
            setDisplay(holder)
            prepareAsync()
            setOnPreparedListener { start() }
        }
    }

    fun playRTMPStream(holder: SurfaceHolder, rtmpUrl: String) {
        mediaPlayer = IjkMediaPlayer().apply {
            try {
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "analyzemaxduration", 100L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "probesize", 1024L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "flush_packets", 1L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 1L)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L)
                setOnErrorListener { _, _, _ -> true }
                dataSource = rtmpUrl
                setDisplay(holder)
                prepareAsync()
                setOnPreparedListener { start() }
            } catch (e: Exception) {
                if (retryCount < maxRetryCount) retryCount++
            }
        }
    }

    fun release() { mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null }
}
