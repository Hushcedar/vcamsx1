package com.vcam.app.engine

data class VideoStatues(
    val isVideoEnable:          Boolean = false,
    val volume:                 Boolean = true,
    val isLiveStreamingEnabled: Boolean = false,
    val liveURL:                String  = "",
    val RealsceneEnabled:       Boolean = false,
    val videoPlayer:            Int     = 0,
    val codecType:              Boolean = false
)

object InfoProcesser {
    var videoStatus: VideoStatues? = null

    fun initStatus() {
        val method = VCamPrefs.methodType
        videoStatus = VideoStatues(
            isVideoEnable          = method != VCamPrefs.TYPE_DISABLED,
            volume                 = VCamPrefs.audioEnable,
            isLiveStreamingEnabled = method == VCamPrefs.TYPE_NETWORK,
            liveURL                = VCamPrefs.networkUrl,
            RealsceneEnabled       = false,
            videoPlayer            = 0,
            codecType              = false
        )
    }
}
