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
    private var infoManage: InfoManage? = null

    fun initStatus() {
        val ctx = MainHook.context
        if (ctx != null) {
            // Cross-process path — hooked app reads via ContentProvider
            if (infoManage == null) infoManage = InfoManage(ctx)
            videoStatus = infoManage!!.getVideoStatus()
        } else {
            // In-process fallback — VCam's own process reads prefs directly
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
        android.util.Log.d("VCam-InfoProcesser",
            "videoStatus enabled=${videoStatus?.isVideoEnable}")
    }
}
