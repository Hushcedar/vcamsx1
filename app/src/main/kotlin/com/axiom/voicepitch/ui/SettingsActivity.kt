package com.axiom.voicepitch.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.axiom.voicepitch.R
import com.axiom.voicepitch.prefs.PitchPrefs

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var seekBar: SeekBar
    private lateinit var semitoneLabel: TextView
    private lateinit var enableSwitch: Switch
    private lateinit var statusChip: TextView

    // SeekBar: 0..480  →  semitones: -12..+12  (step = 0.05 st)
    private val SEEK_MAX    = 480
    private val ST_RANGE    = 24f   // total range in semitones
    private val ST_MIN      = -12f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = PitchPrefs.get(this)

        enableSwitch  = findViewById(R.id.switch_enable)
        seekBar       = findViewById(R.id.seekbar_pitch)
        semitoneLabel = findViewById(R.id.tv_semitones)
        statusChip    = findViewById(R.id.tv_status)

        seekBar.max = SEEK_MAX

        // ── Restore saved state ──────────────────────────────────────────
        val savedSt  = PitchPrefs.getSemitones(prefs)
        val savedEn  = PitchPrefs.isEnabled(prefs)
        enableSwitch.isChecked = savedEn
        seekBar.progress = stToProgress(savedSt)
        updateLabel(savedSt)
        setControlsEnabled(savedEn)

        // ── Enable toggle ────────────────────────────────────────────────
        enableSwitch.setOnCheckedChangeListener { _, checked ->
            PitchPrefs.setEnabled(prefs, checked)
            setControlsEnabled(checked)
        }

        // ── Pitch slider ─────────────────────────────────────────────────
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                val st = progressToSt(progress)
                PitchPrefs.setSemitones(prefs, st)
                updateLabel(st)
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })

        // ── Preset buttons ───────────────────────────────────────────────
        mapOf<Int, Float>(
            R.id.btn_preset_deep      to -6f,
            R.id.btn_preset_normal    to  0f,
            R.id.btn_preset_high      to  6f,
            R.id.btn_preset_chipmunk  to 12f,
            R.id.btn_preset_robot     to -12f
        ).forEach { (id, st) ->
            findViewById<Button>(id).setOnClickListener {
                seekBar.progress = stToProgress(st)
                PitchPrefs.setSemitones(prefs, st)
                updateLabel(st)
            }
        }

        // ── Reset ────────────────────────────────────────────────────────
        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            seekBar.progress = stToProgress(0f)
            PitchPrefs.setSemitones(prefs, 0f)
            updateLabel(0f)
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private fun progressToSt(progress: Int): Float =
        ST_MIN + (progress.toFloat() / SEEK_MAX) * ST_RANGE

    private fun stToProgress(st: Float): Int =
        ((st - ST_MIN) / ST_RANGE * SEEK_MAX).toInt().coerceIn(0, SEEK_MAX)

    private fun updateLabel(semitones: Float) {
        val sign = if (semitones >= 0) "+" else ""
        semitoneLabel.text = "Pitch: $sign${"%.2f".format(semitones)} semitones"
        statusChip.text = when {
            semitones > 0  -> "▲ Higher pitch"
            semitones < 0  -> "▼ Lower pitch"
            else           -> "● No shift"
        }
    }

    private fun setControlsEnabled(enabled: Boolean) {
        seekBar.isEnabled = enabled
        seekBar.alpha     = if (enabled) 1f else 0.4f
        listOf(
            R.id.btn_preset_deep, R.id.btn_preset_normal,
            R.id.btn_preset_high, R.id.btn_preset_chipmunk,
            R.id.btn_preset_robot, R.id.btn_reset
        ).forEach { id ->
            val v = findViewById<View>(id)
            v.isEnabled = enabled
            v.alpha     = if (enabled) 1f else 0.4f
        }
    }
}
