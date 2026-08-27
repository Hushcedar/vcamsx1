package com.axiom.voicepitch.engine

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import kotlin.math.*

object PsolaEngine {

    fun shiftBytes(buf: ByteArray, length: Int, semitones: Float, sampleRate: Int): ByteArray {
        val shorts = pcm16BytesToShorts(buf, length)
        val shifted = shiftShorts(shorts, shorts.size, semitones)
        return shortsTopcm16Bytes(shifted)
    }

    fun shiftShorts(input: ShortArray, length: Int, semitones: Float): ShortArray {
        val ratio = semitonesToRatio(semitones)
        if (abs(ratio - 1f) < 0.001f) return input.copyOf(length)
        return psolaProcess(input, length, ratio)
    }

    fun shiftAudioFile(path: String, semitones: Float) {
        val file = java.io.File(path)
        if (!file.exists()) return

        val extractor = MediaExtractor()
        extractor.setDataSource(path)

        var audioTrack   = -1
        var sampleRate   = 48000
        var channelCount = 1
        var mime         = ""

        for (i in 0 until extractor.trackCount) {
            val fmt = extractor.getTrackFormat(i)
            val m   = fmt.getString(MediaFormat.KEY_MIME) ?: continue
            if (m.startsWith("audio/")) {
                audioTrack   = i
                sampleRate   = fmt.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                channelCount = fmt.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
                mime         = m
                break
            }
        }
        if (audioTrack < 0) { extractor.release(); return }

        extractor.selectTrack(audioTrack)
        val trackFmt = extractor.getTrackFormat(audioTrack)

        val decoder  = MediaCodec.createDecoderByType(mime)
        decoder.configure(trackFmt, null, null, 0)
        decoder.start()

        val pcmChunks = mutableListOf<ShortArray>()
        val bufInfo   = MediaCodec.BufferInfo()
        var inputDone = false
        var outputDone = false

        while (!outputDone) {
            if (!inputDone) {
                val inIdx = decoder.dequeueInputBuffer(10000)
                if (inIdx >= 0) {
                    val inBuf = decoder.getInputBuffer(inIdx)!!
                    val n = extractor.readSampleData(inBuf, 0)
                    if (n < 0) {
                        decoder.queueInputBuffer(inIdx, 0, 0, 0,
                            MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        inputDone = true
                    } else {
                        decoder.queueInputBuffer(inIdx, 0, n, extractor.sampleTime, 0)
                        extractor.advance()
                    }
                }
            }
            val outIdx = decoder.dequeueOutputBuffer(bufInfo, 10000)
            if (outIdx >= 0) {
                val outBuf = decoder.getOutputBuffer(outIdx)!!
                val shorts = ShortArray(bufInfo.size / 2)
                outBuf.order(java.nio.ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts)
                pcmChunks.add(shorts)
                decoder.releaseOutputBuffer(outIdx, false)
                if (bufInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0)
                    outputDone = true
            }
        }
        decoder.stop(); decoder.release(); extractor.release()
        if (pcmChunks.isEmpty()) return

        val totalSamples = pcmChunks.sumOf { it.size }
        val allPcm = ShortArray(totalSamples)
        var offset = 0
        for (chunk in pcmChunks) { chunk.copyInto(allPcm, offset); offset += chunk.size }

        val shifted = shiftShorts(allPcm, totalSamples, semitones)

        val encFmt = MediaFormat.createAudioFormat(mime, sampleRate, channelCount)
        encFmt.setInteger(MediaFormat.KEY_BIT_RATE, 64000)
        encFmt.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 65536)

        val tmpPath = "$path.tmp"
        val encoder = MediaCodec.createEncoderByType(mime)
        encoder.configure(encFmt, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        encoder.start()

        val muxer    = MediaMuxer(tmpPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        var muxTrack = -1
        var muxStarted = false

        var encInputDone  = false
        var encOutputDone = false
        var sampleIdx     = 0
        val encBufInfo    = MediaCodec.BufferInfo()

        while (!encOutputDone) {
            if (!encInputDone) {
                val inIdx = encoder.dequeueInputBuffer(10000)
                if (inIdx >= 0) {
                    val inBuf    = encoder.getInputBuffer(inIdx)!!
                    val capacity = inBuf.capacity() / 2
                    val chunk    = minOf(capacity, shifted.size - sampleIdx)
                    if (chunk <= 0) {
                        encoder.queueInputBuffer(inIdx, 0, 0, 0,
                            MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        encInputDone = true
                    } else {
                        inBuf.clear()
                        inBuf.order(java.nio.ByteOrder.LITTLE_ENDIAN)
                            .asShortBuffer()
                            .put(shifted, sampleIdx, chunk)
                        sampleIdx += chunk
                        encoder.queueInputBuffer(inIdx, 0, chunk * 2, 0, 0)
                    }
                }
            }
            val outIdx = encoder.dequeueOutputBuffer(encBufInfo, 10000)
            when {
                outIdx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                    muxTrack = muxer.addTrack(encoder.outputFormat)
                    muxer.start()
                    muxStarted = true
                }
                outIdx >= 0 -> {
                    val outBuf = encoder.getOutputBuffer(outIdx)!!
                    if (muxStarted &&
                        encBufInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG == 0) {
                        muxer.writeSampleData(muxTrack, outBuf, encBufInfo)
                    }
                    encoder.releaseOutputBuffer(outIdx, false)
                    if (encBufInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0)
                        encOutputDone = true
                }
            }
        }

        encoder.stop(); encoder.release()
        if (muxStarted) { muxer.stop(); muxer.release() }

        java.io.File(tmpPath).renameTo(file)
    }

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
            val fftReal = FloatArray(frameSize) { i ->
                if (inPos + i < floatIn.size) floatIn[inPos + i] * window[i] else 0f
            }
            val fftImag = FloatArray(frameSize)
            fft(fftReal, fftImag, frameSize)

            for (k in 0..frameSize / 2) {
                val re  = fftReal[k]; val im = fftImag[k]
                val mag = sqrt(re * re + im * im)
                val ph  = atan2(im, re)
                val delta    = ph - lastPhase[k]
                lastPhase[k] = ph
                val expected = twoPi * k.toFloat() / frameSize * hopAnalysis
                val trueFreq = twoPi * k.toFloat() / frameSize +
                               wrapPhase(delta - expected) / hopAnalysis
                synthMagnitude[k] = mag
                synthFrequency[k] = trueFreq
            }

            val outReal = FloatArray(frameSize)
            val outImag = FloatArray(frameSize)
            for (k in 0..frameSize / 2) {
                sumPhase[k] += synthFrequency[k] * hopSynthesis
                outReal[k]   = synthMagnitude[k] * cos(sumPhase[k])
                outImag[k]   = synthMagnitude[k] * sin(sumPhase[k])
            }
            for (k in 1 until frameSize / 2) {
                outReal[frameSize - k] =  outReal[k]
                outImag[frameSize - k] = -outImag[k]
            }
            ifft(outReal, outImag, frameSize)

            for (i in 0 until frameSize) {
                val idx = outPos + i
                if (idx < outputAccum.size)
                    outputAccum[idx] += outReal[i] * window[i]
            }
            inPos  += hopAnalysis
            outPos += hopSynthesis
        }

        val peak = outputAccum.take(outPos).maxOrNull()?.takeIf { it > 0f } ?: 1f
        return ShortArray(minOf(outPos, accumSize)) { i ->
            (outputAccum[i] / peak * 32767f).roundToInt().coerceIn(-32768, 32767).toShort()
        }
    }

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
                    val uRe = real[i+j];          val uIm = imag[i+j]
                    val vRe = real[i+j+half]*cRe - imag[i+j+half]*cIm
                    val vIm = real[i+j+half]*cIm + imag[i+j+half]*cRe
                    real[i+j] = uRe+vRe;          imag[i+j] = uIm+vIm
                    real[i+j+half] = uRe-vRe;     imag[i+j+half] = uIm-vIm
                    val nRe = cRe*wRe - cIm*wIm
                    cIm = cRe*wIm + cIm*wRe;      cRe = nRe
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

    private fun hannWindow(size: Int) = FloatArray(size) { i ->
        (0.5 * (1.0 - cos(2.0 * PI * i / (size - 1)))).toFloat()
    }

    private fun wrapPhase(phase: Float): Float {
        val twoPi = (2.0 * PI).toFloat()
        var p = phase
        while (p >  PI.toFloat()) p -= twoPi
        while (p < -PI.toFloat()) p += twoPi
        return p
    }

    private fun semitonesToRatio(semitones: Float): Float = 2f.pow(semitones / 12f)
}
