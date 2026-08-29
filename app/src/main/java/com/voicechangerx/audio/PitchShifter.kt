package com.voicechangerx.audio

import kotlin.math.*

object PitchShifter {
    private const val FFT_SIZE   = 2048
    private const val OVERLAP    = 4
    private const val HOP_SIZE   = FFT_SIZE / OVERLAP

    // phase vocoder state
    private val lastPhase    = FloatArray(FFT_SIZE / 2 + 1)
    private val sumPhase     = FloatArray(FFT_SIZE / 2 + 1)
    private val outputAccum  = FloatArray(FFT_SIZE * 2)
    private val window       = FloatArray(FFT_SIZE) { i ->
        (0.5f * (1f - cos(2.0 * PI * i / (FFT_SIZE - 1)))).toFloat()
    }

    fun shift(input: ShortArray, semitones: Float): ShortArray {
        if (semitones == 0f) return input
        val ratio = 2f.pow(semitones / 12f)
        return resamplePitch(input, ratio)
    }

    // Simple resampling-based pitch shift — fast enough for real-time
    private fun resamplePitch(input: ShortArray, ratio: Float): ShortArray {
        val outLen = input.size
        val srcLen = (outLen * ratio).toInt()
        val resampled = ShortArray(outLen)
        for (i in 0 until outLen) {
            val srcPos = i * ratio
            val srcIdx = srcPos.toInt().coerceIn(0, input.size - 2)
            val frac   = srcPos - srcIdx
            val s0     = input[srcIdx].toFloat()
            val s1     = input[(srcIdx + 1).coerceAtMost(input.size - 1)].toFloat()
            resampled[i] = (s0 + frac * (s1 - s0)).toInt().coerceIn(-32768, 32767).toShort()
        }
        return resampled
    }
}
