package com.wangyiheng.vcamsx.utils

import com.wangyiheng.vcamsx.MainHook
import com.wangyiheng.vcamsx.data.models.VideoStatues

object InfoProcesser {
    var infoManager: InfoManager? = null
    var videoStatus: VideoStatues? = null

    fun initStatus() {
        val context = MainHook.context ?: return
        infoManager = InfoManager(context)
        videoStatus = infoManager!!.getVideoStatus()
    }
}
