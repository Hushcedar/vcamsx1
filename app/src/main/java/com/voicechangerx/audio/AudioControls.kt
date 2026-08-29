package com.voicechangerx.audio

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class GenderMode { OFF, MALE_TO_FEMALE, FEMALE_TO_MALE }

enum class Preset(val label: String, val pitch: Float, val gender: GenderMode, val noise: Boolean, val robot: Boolean) {
    NONE      ("None",          0f,   GenderMode.OFF,              false, false),
    DEEP      ("Deep Voice",   -6f,   GenderMode.OFF,              true,  false),
    HIGH      ("High Voice",    6f,   GenderMode.OFF,              true,  false),
    FEMALE    ("Female",        5f,   GenderMode.MALE_TO_FEMALE,   true,  false),
    MALE      ("Male",         -5f,   GenderMode.FEMALE_TO_MALE,   true,  false),
    ROBOT     ("Robot",         0f,   GenderMode.OFF,              false, true),
    ANONYMOUS ("Anonymous",    -3f,   GenderMode.MALE_TO_FEMALE,   true,  false),
}

/**
 * Singleton shared between:
 *   - MainHook (Xposed process — reads values to process audio)
 *   - UI / FloatingControls (app process — writes values)
 *
 * Because the hook runs in the target app's process, it reads these
 * values from SharedPreferences (written by the UI app process) on each
 * audio chunk. AudioControlReceiver updates SharedPreferences when UI
 * broadcasts a control action.
 */
object AudioControls {
    // These flows drive the UI display only
    val isEnabled      = MutableStateFlow(true)
    val pitchSemitones = MutableStateFlow(0f)        // -12 to +12
    val genderMode     = MutableStateFlow(GenderMode.OFF)
    val noiseSuppPress = MutableStateFlow(false)
    val robotEffect    = MutableStateFlow(false)
    val activePreset   = MutableStateFlow(Preset.NONE)

    // Prefs keys — written by UI, read by hook via SharedPreferences
    const val PREFS_NAME  = "vcx_prefs"
    const val KEY_ENABLED = "enabled"
    const val KEY_PITCH   = "pitch"
    const val KEY_GENDER  = "gender"
    const val KEY_NOISE   = "noise"
    const val KEY_ROBOT   = "robot"
}
