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

    private var mSeekBar: SeekBar? = null
    private var mLabel: TextView? = null
    private var mSwitch: Switch? = null
    private lateinit var mPrefs: SharedPreferences

    private val SEEK_MAX = 480
    private val ST_RANGE = 24f
    private val ST_MIN = -12f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPrefs = PitchPrefs.get(this)
        setContentView(buildUI())
        syncFromPrefs()
    }

    private fun buildUI(): ScrollView {
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

        mSwitch = Switch(this).apply {
            text = "  Enable Pitch Shift"
            textSize = 16f
            setTextColor(Color.WHITE)
            setPadding(0, 0, 0, 32)
            setOnCheckedChangeListener { _, checked ->
                PitchPrefs.setEnabled(mPrefs, checked)
            }
        }
        root.addView(mSwitch)

        mLabel = TextView(this).apply {
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#6C63FF"))
            setPadding(0, 0, 0, 8)
        }
        root.addView(mLabel)

        mSeekBar = SeekBar(this).apply {
            max = SEEK_MAX
            setPadding(0, 0, 0, 8)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar, p: Int, fromUser: Boolean) {
                    val st = progressToSt(p)
                    PitchPrefs.setSemitones(mPrefs, st)
                    updateLabel(st)
                }
                override fun onStartTrackingTouch(sb: SeekBar) {}
                override fun onStopTrackingTouch(sb: SeekBar) {}
            })
        }
        root.addView(mSeekBar)

        root.addView(LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 0, 0, 32)
            addView(TextView(this@SettingsActivity).apply {
                text = "-12 st"
                textSize = 11f
                setTextColor(Color.parseColor("#888888"))
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
            addView(TextView(this@SettingsActivity).apply {
                text = "+12 st"
                textSize = 11f
                setTextColor(Color.parseColor("#888888"))
                gravity = Gravity.END
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
        })

        root.addView(TextView(this).apply {
            text = "Presets"
            textSize = 14f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE)
            setPadding(0, 0, 0, 12)
        })

        root.addView(LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 0, 0, 32)
            listOf(
                "Robot" to -12f,
                "Deep"  to  -6f,
                "Normal" to  0f,
                "High"  to   6f,
                "Chip"  to  12f
            ).forEach { (lbl, st) ->
                addView(Button(this@SettingsActivity).apply {
                    text = "$lbl\n${if (st >= 0) "+${st.toInt()}" else st.toInt()}"
                    textSize = 10f
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.parseColor("#2A2A4E"))
                    layoutParams = LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                    ).also { it.setMargins(4, 0, 4, 0) }
                    setOnClickListener { applyPreset(st) }
                })
            }
        })

        root.addView(Button(this).apply {
            text = "Reset to 0"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#6C63FF"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.setMargins(0, 0, 0, 32) }
            setOnClickListener { applyPreset(0f) }
        })

        root.addView(TextView(this).apply {
            text = "⚠  Force-stop target app after changing settings.\n" +
                   "Scope this module to target apps in LSPosed Manager."
            textSize = 12f
            setTextColor(Color.parseColor("#888888"))
            lineSpacingMultiplier = 1.5f
        })

        return ScrollView(this).apply { addView(root) }
    }

    private fun syncFromPrefs() {
        mSwitch?.isChecked = PitchPrefs.isEnabled(mPrefs)
        val st = PitchPrefs.getSemitones(mPrefs)
        mSeekBar?.progress = stToProgress(st)
        updateLabel(st)
    }

    private fun applyPreset(st: Float) {
        mSeekBar?.progress = stToProgress(st)
        PitchPrefs.setSemitones(mPrefs, st)
        updateLabel(st)
    }

    private fun progressToSt(p: Int): Float = ST_MIN + (p.toFloat() / SEEK_MAX) * ST_RANGE
    private fun stToProgress(st: Float): Int =
        ((st - ST_MIN) / ST_RANGE * SEEK_MAX).toInt().coerceIn(0, SEEK_MAX)

    private fun updateLabel(st: Float) {
        val sign = if (st >= 0) "+" else ""
        mLabel?.text = "Pitch: $sign${"%.1f".format(st)} semitones"
    }
}
