package com.axiom.voicepitch.prefs

import android.content.Context
import android.content.SharedPreferences

object PitchPrefs {

    const val PREFS_NAME    = "axiom_pitch_prefs"
    const val KEY_SEMITONES = "semitones"
    const val KEY_ENABLED   = "enabled"

    // UI side — plain MODE_PRIVATE, no world-readable
    fun get(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isEnabled(prefs: SharedPreferences) =
        prefs.getBoolean(KEY_ENABLED, true)

    fun getSemitones(prefs: SharedPreferences) =
        prefs.getFloat(KEY_SEMITONES, 0f)

    fun setEnabled(prefs: SharedPreferences, value: Boolean) =
        prefs.edit().putBoolean(KEY_ENABLED, value).apply()

    fun setSemitones(prefs: SharedPreferences, value: Float) =
        prefs.edit().putFloat(KEY_SEMITONES, value.coerceIn(-12f, 12f)).apply()
}
