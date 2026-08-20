package com.vcam.app.engine

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object VCamPrefs {
    private lateinit var prefs: SharedPreferences

    const val KEY_METHOD_TYPE   = "method_type"
    const val KEY_VIDEO_PATH    = "video_path_final"
    const val KEY_VIDEO_URI     = "video_path_uri"
    const val KEY_NETWORK_URL   = "network_url"
    const val KEY_AUDIO_ENABLE  = "audio_enable"
    const val KEY_NETWORK_AUDIO = "network_audio_enable"
    const val KEY_IS_ENABLED    = "is_enabled"

    const val TYPE_DISABLED    = 0
    const val TYPE_LOCAL_VIDEO = 1
    const val TYPE_NETWORK     = 2

    fun init(ctx: Context) {
        prefs = ctx.getSharedPreferences("vcam_prefs", Context.MODE_WORLD_READABLE)
    }

    fun getInt(key: String, def: Int = 0)            = prefs.getInt(key, def)
    fun getString(key: String, def: String = "")      = prefs.getString(key, def) ?: def
    fun getBoolean(key: String, def: Boolean = false) = prefs.getBoolean(key, def)
    fun setInt(key: String, v: Int)                   = prefs.edit { putInt(key, v) }
    fun setString(key: String, v: String)              = prefs.edit { putString(key, v) }
    fun setBoolean(key: String, v: Boolean)            = prefs.edit { putBoolean(key, v) }

    val isEnabled   get() = getBoolean(KEY_IS_ENABLED)
    val methodType  get() = getInt(KEY_METHOD_TYPE, TYPE_DISABLED)
    val videoPath   get() = getString(KEY_VIDEO_PATH)
    val networkUrl  get() = getString(KEY_NETWORK_URL)
    val audioEnable get() = getBoolean(KEY_AUDIO_ENABLE, true)
}
