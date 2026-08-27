package com.vcam.app.engine

import android.content.Context
import android.net.Uri

class InfoManage(private val ctx: Context) {
    fun getVideoStatus(): VideoStatues {
        val method   = readInt(VCamPrefs.KEY_METHOD_TYPE, VCamPrefs.TYPE_DISABLED)
        val enabled  = method != VCamPrefs.TYPE_DISABLED
        val isNet    = method == VCamPrefs.TYPE_NETWORK
        val audio    = readBoolean(VCamPrefs.KEY_AUDIO_ENABLE, true)
        val netAudio = readBoolean(VCamPrefs.KEY_NETWORK_AUDIO, true)
        val url      = readString(VCamPrefs.KEY_NETWORK_URL, "")
        android.util.Log.d("VCam-InfoManage", "status: enabled=$enabled method=$method")
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

    private fun readString(key: String, def: String = ""): String {
        return try {
            val uri = Uri.parse("content://com.vcam.app.preferences/get/$key")
            ctx.contentResolver.query(uri, null, null, null, null)?.use { c ->
                if (c.moveToFirst()) c.getString(0) else def
            } ?: def
        } catch (e: Exception) { def }
    }

    private fun readBoolean(key: String, def: Boolean = false) =
        readString(key, def.toString()).toBooleanStrictOrNull() ?: def

    private fun readInt(key: String, def: Int = 0) =
        readString(key, def.toString()).toIntOrNull() ?: def
}
