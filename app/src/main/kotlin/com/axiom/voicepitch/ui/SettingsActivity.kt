package com.axiom.voicepitch.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.axiom.voicepitch.prefs.PitchPrefs

class SettingsActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private var mSeekBar: SeekBar = SeekBar(this)
    private var mLabel: TextView = TextView(this)
    private var mSwitch: Switch = Switch(this)
    private var mPrefs: SharedPreferences? = null

    private val SEEK_MAX = 480
    private val ST_RANGE = 24f
    private val ST_MIN = -12f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPrefs = PitchPrefs.get(this)
        mSeekBar = SeekBar(this)
        mLabel = TextView(this)
        mSwitch = Switch(this)

        val scroll = ScrollView(this)
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(Color.parseColor("#1A1A2E"))
        root.setPadding(48, 64, 48, 48)

        val title = TextView(this)
        title.text = "🎙 Axiom Voice Pitch"
        title.textSize = 24f
        title.setTypeface(null, Typeface.BOLD)
        title.setTextColor(Color.parseColor("#6C63FF"))
        title.setPadding(0, 0, 0, 8)
        root.addView(title)

        val sub = TextView(this)
        sub.text = "WhatsApp · Telegram · Discord · Signal"
        sub.textSize = 12f
        sub.setTextColor(Color.parseColor("#888888"))
        sub.setPadding(0, 0, 0, 40)
        root.addView(sub)

        mSwitch.text = "  Enable Pitch Shift"
        mSwitch.textSize = 16f
        mSwitch.setTextColor(Color.WHITE)
        mSwitch.setPadding(0, 0, 0, 32)
        mSwitch.isChecked = PitchPrefs.isEnabled(mPrefs!!)
        mSwitch.setOnCheckedChangeListener { _, checked ->
            PitchPrefs.setEnabled(mPrefs!!, checked)
        }
        root.addView(mSwitch)

        mLabel.textSize = 18f
        mLabel.setTypeface(null, Typeface.BOLD)
        mLabel.setTextColor(Color.parseColor("#6C63FF"))
        mLabel.setPadding(0, 0, 0, 8)
        root.addView(mLabel)

        mSeekBar.max = SEEK_MAX
        mSeekBar.setPadding(0, 0, 0, 8)
        mSeekBar.setOnSeekBarChangeListener(this)
        root.addView(mSeekBar)

        val rangeRow = LinearLayout(this)
        rangeRow.orientation = LinearLayout.HORIZONTAL
        rangeRow.setPadding(0, 0, 0, 32)

        val rLeft = TextView(this)
        rLeft.text = "-12 st"
        rLeft.textSize = 11f
        rLeft.setTextColor(Color.parseColor("#888888"))
        rLeft.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        rangeRow.addView(rLeft)

        val rRight = TextView(this)
        rRight.text = "+12 st"
        rRight.textSize = 11f
        rRight.setTextColor(Color.parseColor("#888888"))
        rRight.gravity = Gravity.END
        rRight.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        rangeRow.addView(rRight)
        root.addView(rangeRow)

        val presetsTitle = TextView(this)
        presetsTitle.text = "Presets"
        presetsTitle.textSize = 14f
        presetsTitle.setTypeface(null, Typeface.BOLD)
        presetsTitle.setTextColor(Color.WHITE)
        presetsTitle.setPadding(0, 0, 0, 12)
        root.addView(presetsTitle)

        val presetRow = LinearLayout(this)
        presetRow.orientation = LinearLayout.HORIZONTAL
        presetRow.setPadding(0, 0, 0, 32)

        val presets = arrayOf(
            Pair("Robot", -12f),
            Pair("Deep",   -6f),
            Pair("Normal",  0f),
            Pair("High",    6f),
            Pair("Chip",   12f)
        )
        for (preset in presets) {
            val lbl = preset.first
            val st  = preset.second
            val stInt = st.toInt()
            val stStr = if (stInt >= 0) "+$stInt" else "$stInt"
            val btn = Button(this)
            btn.text = "$lbl\n$stStr"
            btn.textSize = 10f
            btn.setTextColor(Color.WHITE)
            btn.setBackgroundColor(Color.parseColor("#2A2A4E"))
            val btnParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            btnParams.setMargins(4, 0, 4, 0)
            btn.layoutParams = btnParams
            val stVal = st
            btn.setOnClickListener { applyPreset(stVal) }
            presetRow.addView(btn)
        }
        root.addView(presetRow)

        val resetBtn = Button(this)
        resetBtn.text = "Reset to 0"
        resetBtn.setTextColor(Color.WHITE)
        resetBtn.setBackgroundColor(Color.parseColor("#6C63FF"))
        val resetParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        resetParams.setMargins(0, 0, 0, 32)
        resetBtn.layoutParams = resetParams
        resetBtn.setOnClickListener { applyPreset(0f) }
        root.addView(resetBtn)

        val footer = TextView(this)
        footer.text = "Force-stop target app after changing settings.\nScope this module in LSPosed Manager."
        footer.textSize = 12f
        footer.setTextColor(Color.parseColor("#888888"))
        footer.lineSpacingMultiplier = 1.5f
        root.addView(footer)

        scroll.addView(root)
        setContentView(scroll)

        val initSt = PitchPrefs.getSemitones(mPrefs!!)
        mSeekBar.progress = stToProgress(initSt)
        updateLabel(initSt)
    }

    override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
        val st = progressToSt(progress)
        PitchPrefs.setSemitones(mPrefs!!, st)
        updateLabel(st)
    }
    override fun onStartTrackingTouch(sb: SeekBar) {}
    override fun onStopTrackingTouch(sb: SeekBar) {}

    private fun applyPreset(st: Float) {
        mSeekBar.progress = stToProgress(st)
        PitchPrefs.setSemitones(mPrefs!!, st)
        updateLabel(st)
    }

    private fun progressToSt(p: Int): Float = ST_MIN + (p.toFloat() / SEEK_MAX) * ST_RANGE
    private fun stToProgress(st: Float): Int = ((st - ST_MIN) / ST_RANGE * SEEK_MAX).toInt().coerceIn(0, SEEK_MAX)
    private fun updateLabel(st: Float) {
        val sign = if (st >= 0) "+" else ""
        mLabel.text = "Pitch: $sign${"%.1f".format(st)} semitones"
    }
}
