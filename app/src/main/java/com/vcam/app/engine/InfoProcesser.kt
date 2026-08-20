package com.vcam.app.engine

object InfoProcesser {
    var videoStatus: VideoStatues? = null
    private var infoManage: InfoManage? = null

    fun initStatus() {
        val ctx = MainHook.context ?: run {
            android.util.Log.e("VCam-InfoProcesser", "context is null — hooks won't fire")
            return
        }
        if (infoManage == null) infoManage = InfoManage(ctx)
        videoStatus = infoManage!!.getVideoStatus()
        android.util.Log.d("VCam-InfoProcesser", "videoStatus=${videoStatus?.isVideoEnable}")
    }
}
