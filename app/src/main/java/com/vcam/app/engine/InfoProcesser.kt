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
        val ctx = MainHook.context ?: run {
            android.util.Log.e("VCam-InfoProcesser", "context null — hook won't fire")
            return
        }
        if (infoManage == null) infoManage = InfoManage(ctx)
        videoStatus = infoManage!!.getVideoStatus()
        android.util.Log.d("VCam-InfoProcesser", "videoStatus enabled=${videoStatus?.isVideoEnable}")
    }
}
