package com.voicechangerx.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AudioControlReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        val prefs = ctx.getSharedPreferences(AudioControls.PREFS_NAME, Context.MODE_PRIVATE).edit()
        when (intent.action) {
            "com.voicechangerx.TOGGLE"       -> { val cur = ctx.getSharedPreferences(AudioControls.PREFS_NAME, Context.MODE_PRIVATE).getBoolean(AudioControls.KEY_ENABLED, true); prefs.putBoolean(AudioControls.KEY_ENABLED, !cur) }
            "com.voicechangerx.PITCH_UP"     -> { val cur = ctx.getSharedPreferences(AudioControls.PREFS_NAME, Context.MODE_PRIVATE).getFloat(AudioControls.KEY_PITCH, 0f); prefs.putFloat(AudioControls.KEY_PITCH, (cur + 1f).coerceAtMost(12f)) }
            "com.voicechangerx.PITCH_DOWN"   -> { val cur = ctx.getSharedPreferences(AudioControls.PREFS_NAME, Context.MODE_PRIVATE).getFloat(AudioControls.KEY_PITCH, 0f); prefs.putFloat(AudioControls.KEY_PITCH, (cur - 1f).coerceAtLeast(-12f)) }
            "com.voicechangerx.PITCH_RESET"  -> prefs.putFloat(AudioControls.KEY_PITCH, 0f)
            "com.voicechangerx.GENDER_MALE"  -> prefs.putString(AudioControls.KEY_GENDER, GenderMode.FEMALE_TO_MALE.name)
            "com.voicechangerx.GENDER_FEMALE"-> prefs.putString(AudioControls.KEY_GENDER, GenderMode.MALE_TO_FEMALE.name)
            "com.voicechangerx.GENDER_OFF"   -> prefs.putString(AudioControls.KEY_GENDER, GenderMode.OFF.name)
            "com.voicechangerx.NOISE_TOGGLE" -> { val cur = ctx.getSharedPreferences(AudioControls.PREFS_NAME, Context.MODE_PRIVATE).getBoolean(AudioControls.KEY_NOISE, false); prefs.putBoolean(AudioControls.KEY_NOISE, !cur) }
            "com.voicechangerx.ROBOT_TOGGLE" -> { val cur = ctx.getSharedPreferences(AudioControls.PREFS_NAME, Context.MODE_PRIVATE).getBoolean(AudioControls.KEY_ROBOT, false); prefs.putBoolean(AudioControls.KEY_ROBOT, !cur) }
            "com.voicechangerx.PRESET_APPLY" -> {
                val name = intent.getStringExtra("preset") ?: return
                val preset = Preset.valueOf(name)
                prefs.putFloat(AudioControls.KEY_PITCH, preset.pitch)
                prefs.putString(AudioControls.KEY_GENDER, preset.gender.name)
                prefs.putBoolean(AudioControls.KEY_NOISE, preset.noise)
                prefs.putBoolean(AudioControls.KEY_ROBOT, preset.robot)
            }
        }
        prefs.apply()
    }

    companion object {
        fun send(ctx: Context, action: String, extras: Map<String, String> = emptyMap()) {
            val intent = Intent(action).apply {
                addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                extras.forEach { (k, v) -> putExtra(k, v) }
            }
            ctx.sendBroadcast(intent)
        }
    }
}
