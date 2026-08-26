package com.axiom.voicepitch.engine

import kotlin.math.*

/**
 * Pure-Kotlin PSOLA (Pitch-Synchronous Overlap-Add) pitch shifter.
 * No native dependencies — runs entirely on the JVM.
 *
 * Thread-safe: each call is stateless beyond internal frame buffers.
 */
object PsolaEngine {

    // ── Public API ───────────────────────────────────────────────────────

    /**
     * Shift pitch of raw PCM-16 LE bytes by [semitones].
     * Positive = higher, Negative = lower. Range: -12..+12
     */
    fun shiftBytes(buf: ByteArray, length: Int, semitones: Float, sampleRate: Int): ByteArray {
        val shorts = pcm16BytesToShorts(buf, length)
        val shifted = shiftShorts(shorts, shorts.size, semitones)
        return shortsTopcm16Bytes(shifted)
    }

    /**
     * Shift pitch of a ShortArray PCM-16 buffer by [semitones].
     */
    fun shiftShorts(input: ShortArray, length: Int, semitones: Float): ShortArray {
        val ratio = semitonesToRatio(semitones)
        if (abs(ratio - 1f) < 0.001f) return input.copyOf(length)
        return psolaProcess(input, length, ratio)
    }

    // ── Conversion helpers ───────────────────────────────────────────────

    fun pcm16BytesToShorts(buf: ByteArray, length: Int): ShortArray {
        val count = length / 2
        return ShortArray(count) { i ->
            ((buf[i * 2 + 1].toInt() shl 8) or (buf[i * 2].toInt() and 0xFF)).toShort()
        }
    }

    fun shortsTopcm16Bytes(shorts: ShortArray): ByteArray {
        return ByteArray(shorts.size * 2) { i ->
            val s = shorts[i / 2]
            if (i % 2 == 0) (s.toInt() and 0xFF).toByte()
            else ((s.toInt() ushr 8) and 0xFF).toByte()
        }
    }

    // ── Core PSOLA phase-vocoder ─────────────────────────────────────────

    private fun psolaProcess(input: ShortArray, length: Int, ratio: Float): ShortArray {
        val frameSize    = 2048
        val hopAnalysis  = frameSize / 4
        val hopSynthesis = (hopAnalysis * ratio).roundToInt().coerceAtLeast(1)

        val floatIn   = FloatArray(length) { input[it] / 32768f }
        val accumSize = (length * ratio * 1.2f).toInt() + frameSize * 2
        val outputAccum = FloatArray(accumSize)
        val window      = hannWindow(frameSize)

        val lastPhase      = FloatArray(frameSize / 2 + 1)
        val sumPhase       = FloatArray(frameSize / 2 + 1)
        val synthMagnitude = FloatArray(frameSize / 2 + 1)
        val synthFrequency = FloatArray(frameSize / 2 + 1)

        val twoPi = (2.0 * PI).toFloat()
        var inPos  = 0
        var outPos = 0

        while (inPos + frameSize < floatIn.size) {
            // ── Windowed analysis frame ──────────────────────────────────
            val fftReal = FloatArray(frameSize) { i ->
                if (inPos + i < floatIn.size) floatIn[inPos + i] * window[i] else 0f
            }
            val fftImag = FloatArray(frameSize)

            fft(fftReal, fftImag, frameSize)

            // ── Phase vocoder analysis ───────────────────────────────────
            for (k in 0..frameSize / 2) {
                val re  = fftReal[k]
                val im  = fftImag[k]
                val mag = sqrt(re * re + im * im)
                val ph  = atan2(im, re)

                val delta        = ph - lastPhase[k]
                lastPhase[k]     = ph
                val expected     = twoPi * k.toFloat() / frameSize * hopAnalysis
                val trueFreq     = twoPi * k.toFloat() / frameSize +
                                   wrapPhase(delta - expected) / hopAnalysis

                synthMagnitude[k] = mag
                synthFrequency[k] = trueFreq
            }

            // ── Phase vocoder synthesis ──────────────────────────────────
            val outReal = FloatArray(frameSize)
            val outImag = FloatArray(frameSize)

            for (k in 0..frameSize / 2) {
                sumPhase[k] += synthFrequency[k] * hopSynthesis
                outReal[k]   = synthMagnitude[k] * cos(sumPhase[k])
                outImag[k]   = synthMagnitude[k] * sin(sumPhase[k])
            }

            // Mirror for real-valued IFFT
            for (k in 1 until frameSize / 2) {
                outReal[frameSize - k] =  outReal[k]
                outImag[frameSize - k] = -outImag[k]
            }

            ifft(outReal, outImag, frameSize)

            // ── Overlap-add into accumulator ─────────────────────────────
            for (i in 0 until frameSize) {
                val idx = outPos + i
                if (idx < outputAccum.size)
                    outputAccum[idx] += outReal[i] * window[i]
            }

            inPos  += hopAnalysis
            outPos += hopSynthesis
        }

        // ── Normalize & convert ──────────────────────────────────────────
        val peak = outputAccum.take(outPos).maxOrNull()?.takeIf { it > 0f } ?: 1f
        val resultLen = minOf(outPos, accumSize)
        return ShortArray(resultLen) { i ->
            (outputAccum[i] / peak * 32767f)
                .roundToInt()
                .coerceIn(-32768, 32767)
                .toShort()
        }
    }

    // ── DSP utilities ────────────────────────────────────────────────────

    private fun semitonesToRatio(semitones: Float): Float =
        2f.pow(semitones / 12f)

    private fun wrapPhase(phase: Float): Float {
        val twoPi = (2.0 * PI).toFloat()
        var p = phase
        while (p >  PI.toFloat()) p -= twoPi
        while (p < -PI.toFloat()) p += twoPi
        return p
    }

    private fun hannWindow(size: Int) = FloatArray(size) { i ->
        (0.5 * (1.0 - cos(2.0 * PI * i / (size - 1)))).toFloat()
    }

    // ── Cooley-Tukey Radix-2 FFT / IFFT (in-place) ──────────────────────

    fun fft(real: FloatArray, imag: FloatArray, n: Int) {
        bitReversal(real, imag, n)
        var len = 2
        while (len <= n) {
            val half = len / 2
            val ang  = (2.0 * PI / len).toFloat()
            val wRe  = cos(ang); val wIm = -sin(ang)
            var i = 0
            while (i < n) {
                var cRe = 1f; var cIm = 0f
                for (j in 0 until half) {
                    val uRe = real[i + j];         val uIm = imag[i + j]
                    val vRe = real[i + j + half] * cRe - imag[i + j + half] * cIm
                    val vIm = real[i + j + half] * cIm + imag[i + j + half] * cRe
                    real[i + j] = uRe + vRe;       imag[i + j] = uIm + vIm
                    real[i + j + half] = uRe - vRe; imag[i + j + half] = uIm - vIm
                    val nRe = cRe * wRe - cIm * wIm
                    cIm = cRe * wIm + cIm * wRe;  cRe = nRe
                }
                i += len
            }
            len *= 2
        }
    }

    fun ifft(real: FloatArray, imag: FloatArray, n: Int) {
        for (i in imag.indices) imag[i] = -imag[i]
        fft(real, imag, n)
        val nf = n.toFloat()
        for (i in real.indices) { real[i] /= nf; imag[i] = -imag[i] / nf }
    }

    private fun bitReversal(real: FloatArray, imag: FloatArray, n: Int) {
        var j = 0
        for (i in 1 until n) {
            var bit = n shr 1
            while (j and bit != 0) { j = j xor bit; bit = bit shr 1 }
            j = j xor bit
            if (i < j) {
                real[i] = real[j].also { real[j] = real[i] }
                imag[i] = imag[j].also { imag[j] = imag[i] }
            }
        }
    }
}
