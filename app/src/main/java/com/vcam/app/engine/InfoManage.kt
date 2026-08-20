package com.vcam.app.engine

import android.content.Context

class InfoManage(private val ctx: Context) {
    fun getVideoStatus(): VideoStatues {
        val method  = VCamPrefsProvider.readInt(ctx, VCamPrefs.KEY_METHOD_TYPE, VCamPrefs.TYPE_DISABLED)
        val enabled = method != VCamPrefs.TYPE_DISABLED
        val isNet   = method == VCamPrefs.TYPE_NETWORK
        val audio   = VCamPrefsProvider.readBoolean(ctx, VCamPrefs.KEY_AUDIO_ENABLE, true)
        val netAudio = VCamPrefsProvider.readBoolean(ctx, VCamPrefs.KEY_NETWORK_AUDIO, true)
        val url     = VCamPrefsProvider.readString(ctx, VCamPrefs.KEY_NETWORK_URL)

        return VideoStatues(
            isVideoEnable          = enabled,
            volume                 = if (isNet) netAudio else audio,
            isLiveStreamingEnabled = isNet,
            liveURL                = url,
            RealsceneEnabled       = false,
            videoPlayer            = 0,
            codecType              = false
        )
    }
}
