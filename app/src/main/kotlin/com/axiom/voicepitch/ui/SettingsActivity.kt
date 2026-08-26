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

class SettingsActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var label: TextView
    private lateinit var toggle: Switch
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PitchPrefs.get(this)

        val scroll = ScrollView(this)
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(Color.parseColor("#1A1A2E"))
        root.setPadding(48, 64, 48, 48)

        val t1 = TextView(this)
        t1.text = "Axiom Voice Pitch"
        t1.textSize = 24f
        t1.setTypeface(null, Typeface.BOLD)
        t1.setTextColor(Color.parseColor("#6C63FF"))
        t1.setPadding(0, 0, 0, 8)
        root.addView(t1)

        val t2 = TextView(this)
        t2.text = "WhatsApp · Telegram · Discord · Signal"
        t2.textSize = 12f
        t2.setTextColor(Color.parseColor("#888888"))
        t2.setPadding(0, 0, 0, 40)
        root.addView(t2)

        toggle = Switch(this)
        toggle.text = "Enable Pitch Shift"
        toggle.textSize = 16f
        toggle.setTextColor(Color.WHITE)
        toggle.setPadding(0, 0, 0, 32)
        toggle.isChecked = PitchPrefs.isEnabled(prefs)
        root.addView(toggle)

        label = TextView(this)
        label.textSize = 18f
        label.setTypeface(null, Typeface.BOLD)
        label.setTextColor(Color.parseColor("#6C63FF"))
        label.setPadding(0, 0, 0, 8)
        root.addView(label)

        seekBar = SeekBar(this)
        seekBar.max = 480
        seekBar.setPadding(0, 0, 0, 32)
        root.addView(seekBar)

        val presetsLabel = TextView(this)
        presetsLabel.text = "Presets"
        presetsLabel.textSize = 14f
        presetsLabel.setTypeface(null, Typeface.BOLD)
        presetsLabel.setTextColor(Color.WHITE)
        presetsLabel.setPadding(0, 0, 0, 12)
        root.addView(presetsLabel)

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.setPadding(0, 0, 0, 32)

        val names = arrayOf("Robot", "Deep", "Normal", "High", "Chip")
        val values = floatArrayOf(-12f, -6f, 0f, 6f, 12f)

        for (i in names.indices) {
            val b = Button(this)
            val v = values[i]
            val vi = v.toInt()
            val vs = if (vi >= 0) "+$vi" else "$vi"
            b.text = names[i] + "\n" + vs
            b.textSize = 10f
            b.setTextColor(Color.WHITE)
            b.setBackgroundColor(Color.parseColor("#2A2A4E"))
            val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            lp.setMargins(4, 0, 4, 0)
            b.layoutParams = lp
            b.setOnClickListener {
                val prog = stToProgress(v)
                seekBar.progress = prog
                PitchPrefs.setSemitones(prefs, v)
                refreshLabel(v)
            }
            row.addView(b)
        }
        root.addView(row)

        val reset = Button(this)
        reset.text = "Reset to 0"
        reset.setTextColor(Color.WHITE)
        reset.setBackgroundColor(Color.parseColor("#6C63FF"))
        val rlp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rlp.setMargins(0, 0, 0, 32)
        reset.layoutParams = rlp
        reset.setOnClickListener {
            seekBar.progress = stToProgress(0f)
            PitchPrefs.setSemitones(prefs, 0f)
            refreshLabel(0f)
        }
        root.addView(reset)

        val foot = TextView(this)
        foot.text = "Force-stop target app after changing settings. Scope in LSPosed Manager."
        foot.textSize = 12f
        foot.setTextColor(Color.parseColor("#888888"))
        root.addView(foot)

        scroll.addView(root)
        setContentView(scroll)

        val initSt = PitchPrefs.getSemitones(prefs)
        seekBar.progress = stToProgress(initSt)
        refreshLabel(initSt)

        toggle.setOnCheckedChangeListener { _, checked ->
            PitchPrefs.setEnabled(prefs, checked)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, p: Int, fromUser: Boolean) {
                val st = progressToSt(p)
                PitchPrefs.setSemitones(prefs, st)
                refreshLabel(st)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun stToProgress(st: Float): Int = ((st + 12f) / 24f * 480).toInt().coerceIn(0, 480)
    private fun progressToSt(p: Int): Float = (p.toFloat() / 480f) * 24f - 12f
    private fun refreshLabel(st: Float) {
        val sign = if (st >= 0f) "+" else ""
        label.text = "Pitch: $sign${"%.1f".format(st)} semitones"
    }
}
