package com.vcam.app.engine

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object VCamPrefs {
    private lateinit var prefs: SharedPreferences

    const val KEY_METHOD_TYPE    = "method_type"
    const val KEY_VIDEO_PATH     = "video_path_final"
    const val KEY_VIDEO_URI      = "video_path_uri"
    const val KEY_IMAGE_PATH     = "image_path_final"
    const val KEY_IMAGE_URI      = "image_path_uri"
    const val KEY_NETWORK_URL    = "network_url"
    const val KEY_AUDIO_ENABLE   = "audio_enable"
    const val KEY_NETWORK_AUDIO  = "network_audio_enable"
    const val KEY_IS_ENABLED     = "is_enabled"

    // Per-channel keys — ch = 1,2,3,4
    fun keySize(ch: Int)     = "ch${ch}_size"
    fun keyZoom(ch: Int)     = "ch${ch}_zoom"
    fun keyRotate(ch: Int)   = "ch${ch}_rotate"
    fun keyFlip(ch: Int)     = "ch${ch}_flip"
    fun keyOffsetX(ch: Int)  = "ch${ch}_offset_x"
    fun keyOffsetY(ch: Int)  = "ch${ch}_offset_y"

    const val TYPE_DISABLED    = 0
    const val TYPE_LOCAL_VIDEO = 1
    const val TYPE_NETWORK     = 2
    const val TYPE_LOCAL_IMAGE = 3

    fun init(ctx: Context) {
        prefs = ctx.getSharedPreferences("vcam_prefs", Context.MODE_PRIVATE)
    }

    fun getInt(key: String, def: Int = 0)            = prefs.getInt(key, def)
    fun getString(key: String, def: String = "")      = prefs.getString(key, def) ?: def
    fun getBoolean(key: String, def: Boolean = false) = prefs.getBoolean(key, def)
    fun getFloat(key: String, def: Float = 0f)        = prefs.getFloat(key, def)
    fun setInt(key: String, v: Int)                   = prefs.edit { putInt(key, v) }
    fun setString(key: String, v: String)              = prefs.edit { putString(key, v) }
    fun setBoolean(key: String, v: Boolean)            = prefs.edit { putBoolean(key, v) }
    fun setFloat(key: String, v: Float)                = prefs.edit { putFloat(key, v) }

    val isEnabled   get() = getBoolean(KEY_IS_ENABLED)
    val methodType  get() = getInt(KEY_METHOD_TYPE, TYPE_DISABLED)
    val videoPath   get() = getString(KEY_VIDEO_PATH)
    val imagePath   get() = getString(KEY_IMAGE_PATH)
    val networkUrl  get() = getString(KEY_NETWORK_URL)
    val audioEnable get() = getBoolean(KEY_AUDIO_ENABLE, true)
}
