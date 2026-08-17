package virtual.camera.app.app

import android.content.Context
import android.content.SharedPreferences

object AppManager {

    private const val PREFS_NAME      = "vcamera_prefs"
    private const val REMARK_PREFS    = "vcamera_remark"

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mRemarkSharedPreferences: SharedPreferences

    fun init(context: Context) {
        mSharedPreferences       = context.getSharedPreferences(PREFS_NAME,   Context.MODE_PRIVATE)
        mRemarkSharedPreferences = context.getSharedPreferences(REMARK_PREFS, Context.MODE_PRIVATE)
    }

    // ── Virtual camera source ─────────────────────────────────────────────────

    var activeSourceType: SourceType
        get() = SourceType.valueOf(
            mSharedPreferences.getString(KEY_SOURCE_TYPE, SourceType.NONE.name) ?: SourceType.NONE.name
        )
        set(v) = mSharedPreferences.edit().putString(KEY_SOURCE_TYPE, v.name).apply()

    var activeSourceUri: String
        get() = mSharedPreferences.getString(KEY_SOURCE_URI, "") ?: ""
        set(v) = mSharedPreferences.edit().putString(KEY_SOURCE_URI, v).apply()

    // ── Transforms ────────────────────────────────────────────────────────────

    var flipHorizontal: Boolean
        get() = mSharedPreferences.getBoolean(KEY_FLIP_H, false)
        set(v) = mSharedPreferences.edit().putBoolean(KEY_FLIP_H, v).apply()

    var flipVertical: Boolean
        get() = mSharedPreferences.getBoolean(KEY_FLIP_V, false)
        set(v) = mSharedPreferences.edit().putBoolean(KEY_FLIP_V, v).apply()

    var mirrorMode: Boolean
        get() = mSharedPreferences.getBoolean(KEY_MIRROR, false)
        set(v) = mSharedPreferences.edit().putBoolean(KEY_MIRROR, v).apply()

    var rotation: Int
        get() = mSharedPreferences.getInt(KEY_ROTATION, 0)
        set(v) = mSharedPreferences.edit().putInt(KEY_ROTATION, v).apply()

    var scalePercent: Int
        get() = mSharedPreferences.getInt(KEY_SCALE, 100)
        set(v) = mSharedPreferences.edit().putInt(KEY_SCALE, v).apply()

    // ── Service state ─────────────────────────────────────────────────────────

    var isServiceRunning: Boolean
        get() = mSharedPreferences.getBoolean(KEY_SVC_RUNNING, false)
        set(v) = mSharedPreferences.edit().putBoolean(KEY_SVC_RUNNING, v).apply()

    // ── Target apps ───────────────────────────────────────────────────────────

    var targetPackages: Set<String>
        get() = mSharedPreferences.getStringSet(KEY_TARGET_PKGS, emptySet()) ?: emptySet()
        set(v) = mSharedPreferences.edit().putStringSet(KEY_TARGET_PKGS, v).apply()

    // ── Keys ──────────────────────────────────────────────────────────────────

    private const val KEY_SOURCE_TYPE  = "source_type"
    private const val KEY_SOURCE_URI   = "source_uri"
    private const val KEY_FLIP_H       = "flip_h"
    private const val KEY_FLIP_V       = "flip_v"
    private const val KEY_MIRROR       = "mirror"
    private const val KEY_ROTATION     = "rotation"
    private const val KEY_SCALE        = "scale"
    private const val KEY_SVC_RUNNING  = "svc_running"
    private const val KEY_TARGET_PKGS  = "target_pkgs"

    // ── Source types ──────────────────────────────────────────────────────────

    enum class SourceType {
        NONE,
        LOCAL_VIDEO,
        LOCAL_IMAGE,
        NETWORK_RTMP,
        NETWORK_HLS,
        NETWORK_RTSP,
        REAL_CAMERA
    }
}
