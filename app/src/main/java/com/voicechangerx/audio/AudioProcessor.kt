package com.voicechangerx.audio

import android.content.Context
import android.content.SharedPreferences
import kotlin.math.*

object AudioProcessor {

    private var prefs: SharedPreferences? = null

    fun init(ctx: Context) {
        prefs = ctx.getSharedPreferences(AudioControls.PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Called from MainHook on every AudioRecord.read() chunk
    fun process(pcm: ByteArray, sampleRate: Int): ByteArray {
        val p = prefs ?: return pcm
        if (!p.getBoolean(AudioControls.KEY_ENABLED, true)) return pcm

        val pitch   = p.getFloat(AudioControls.KEY_PITCH, 0f)
        val gender  = GenderMode.valueOf(p.getString(AudioControls.KEY_GENDER, "OFF") ?: "OFF")
        val noise   = p.getBoolean(AudioControls.KEY_NOISE, false)
        val robot   = p.getBoolean(AudioControls.KEY_ROBOT, false)

        var samples = pcmBytesToShorts(pcm)

        if (noise)          samples = noiseSuppressor(samples)
        if (robot)          samples = robotEffect(samples)

        val effectivePitch = pitch + when (gender) {
            GenderMode.MALE_TO_FEMALE -> 5f
            GenderMode.FEMALE_TO_MALE -> -5f
            GenderMode.OFF            -> 0f
        }
        if (effectivePitch != 0f) samples = PitchShifter.shift(samples, effectivePitch)

        return shortsToPcmBytes(samples)
    }

    // ── Noise suppression — spectral gate + high-pass ─────────────────────────
    private fun noiseSuppressor(input: ShortArray): ShortArray {
        val threshold = 300f  // silence below this RMS level
        val rms = sqrt(input.map { it.toFloat().pow(2) }.average()).toFloat()
        if (rms < threshold) return ShortArray(input.size)   // gate: silence

        // Simple high-pass FIR — remove DC and low rumble below ~80Hz
        val out = ShortArray(input.size)
        var prev = 0f
        val alpha = 0.97f   // ~80Hz cutoff at 16kHz
        for (i in input.indices) {
            val curr  = input[i].toFloat()
            val hp    = alpha * (prev + curr - (if (i > 0) input[i-1].toFloat() else 0f))
            out[i]    = hp.toInt().coerceIn(-32768, 32767).toShort()
            prev      = hp
        }
        return out
    }

    // ── Robot effect — ring modulation ────────────────────────────────────────
    private fun robotEffect(input: ShortArray): ShortArray {
        val carrierFreq = 100.0   // Hz
        val sampleRate  = 16000.0
        return ShortArray(input.size) { i ->
            val carrier = sin(2.0 * PI * carrierFreq * i / sampleRate).toFloat()
            (input[i] * carrier).toInt().coerceIn(-32768, 32767).toShort()
        }
    }

    // ── PCM byte ↔ short conversion ───────────────────────────────────────────
    private fun pcmBytesToShorts(bytes: ByteArray): ShortArray {
        val shorts = ShortArray(bytes.size / 2)
        for (i in shorts.indices) {
            shorts[i] = ((bytes[i * 2 + 1].toInt() shl 8) or (bytes[i * 2].toInt() and 0xFF)).toShort()
        }
        return shorts
    }

    private fun shortsToPcmBytes(shorts: ShortArray): ByteArray {
        val bytes = ByteArray(shorts.size * 2)
        for (i in shorts.indices) {
            bytes[i * 2]     = (shorts[i].toInt() and 0xFF).toByte()
            bytes[i * 2 + 1] = (shorts[i].toInt() shr 8).toByte()
        }
        return bytes
    }
}
