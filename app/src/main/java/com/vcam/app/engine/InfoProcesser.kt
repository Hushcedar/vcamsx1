package com.vcam.app.engine

object InfoProcesser {
    var videoStatus: VideoStatues? = null
    private var infoManage: InfoManage? = null

    fun initStatus() {
        val ctx = MainHook.context ?: return
        if (infoManage == null) infoManage = InfoManage(ctx)
        videoStatus = infoManage!!.getVideoStatus()
    }
}
