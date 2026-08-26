package com.axiom.voicepitch.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.axiom.voicepitch.prefs.PitchPrefs

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private var seekBar: SeekBar? = null
    private var semitoneLabel: TextView? = null
    private var enableSwitch: Switch? = null

    private val SEEK_MAX = 480
    private val ST_RANGE = 24f
    private val ST_MIN   = -12f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PitchPrefs.get(this)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#1A1A2E"))
            setPadding(48, 64, 48, 48)
        }

        root.addView(TextView(this).apply {
            text = "🎙 Axiom Voice Pitch"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#6C63FF"))
            setPadding(0, 0, 0, 8)
        })

        root.addView(TextView(this).apply {
            text = "WhatsApp · Telegram · Discord · Signal"
            textSize = 12f
            setTextColor(Color.parseColor("#888888"))
            setPadding(0, 0, 0, 40)
        })

        val sw = Switch(this).apply {
            text = "  Enable Pitch Shift"
            textSize = 16f
            setTextColor(Color.WHITE)
            isChecked = PitchPrefs.isEnabled(prefs)
            setPadding(0, 0, 0, 32)
        }
        enableSwitch = sw
        root.addView(sw)

        val label = TextView(this).apply {
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#6C63FF"))
            setPadding(0, 0, 0, 8)
        }
        semitoneLabel = label
        root.addView(label)

        val sb = SeekBar(this).apply {
            max = SEEK_MAX
            progress = stToProgress(PitchPrefs.getSemitones(prefs))
            setPadding(0, 0, 0, 8)
        }
        seekBar = sb
        root.addView(sb)

        root.addView(LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 0, 0, 32)
            addView(TextView(this@SettingsActivity).apply {
                text = "-12 st"
                textSize = 11f
                setTextColor(Color.parseColor("#888888"))
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
            addView(TextView(this@SettingsActivity).apply {
                text = "+12 st"
                textSize = 11f
                setTextColor(Color.parseColor("#888888"))
                gravity = Gravity.END
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
        })

        root.addView(TextView(this).apply {
            text = "Presets"
            textSize = 14f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE)
            setPadding(0, 0, 0, 12)
        })

        val presetRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 0, 0, 32)
        }
        listOf("Robot" to -12f, "Deep" to -6f, "Normal" to 0f, "High" to 6f, "Chip" to 12f).forEach { (lbl, st) ->
            presetRow.addView(Button(this).apply {
                text = "$lbl\n${if (st >= 0) "+${st.toInt()}" else st.toInt()}"
                textSize = 10f
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.parseColor("#2A2A4E"))
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setMargins(4, 0, 4, 0)
                }
                setOnClickListener { applyPreset(st) }
            })
        }
        root.addView(presetRow)

        root.addView(Button(this).apply {
            text = "Reset to 0"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#6C63FF"))
            setOnClickListener { applyPreset(0f) }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 32) }
        })

        root.addView(TextView(this).apply {
            text = "⚠  Force-stop target app after changing settings.\nScope this module to target apps in LSPosed Manager."
            textSize = 12f
            setTextColor(Color.parseColor("#888888"))
            lineSpacingMultiplier = 1.5f
        })

        updateLabel(progressToSt(sb.progress))

        sw.setOnCheckedChangeListener { _, checked ->
            PitchPrefs.setEnabled(prefs, checked)
        }

        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar, progress: Int, fromUser: Boolean) {
                val st = progressToSt(progress)
                PitchPrefs.setSemitones(prefs, st)
                updateLabel(st)
            }
            override fun onStartTrackingTouch(s: SeekBar) {}
            override fun onStopTrackingTouch(s: SeekBar) {}
        })

        setContentView(ScrollView(this).apply { addView(root) })
    }

    private fun applyPreset(st: Float) {
        seekBar?.progress = stToProgress(st)
        PitchPrefs.setSemitones(prefs, st)
        updateLabel(st)
    }

    private fun progressToSt(p: Int) = ST_MIN + (p.toFloat() / SEEK_MAX) * ST_RANGE
    private fun stToProgress(st: Float) = ((st - ST_MIN) / ST_RANGE * SEEK_MAX).toInt().coerceIn(0, SEEK_MAX)

    private fun updateLabel(st: Float) {
        val sign = if (st >= 0) "+" else ""
        semitoneLabel?.text = "Pitch: $sign${"%.1f".format(st)} semitones"
    }
}
