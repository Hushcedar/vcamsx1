package com.wangyiheng.vcamsx.utils

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.wangyiheng.vcamsx.MainHook
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

object AudioInjector {
    private const val TAG = "VCamSX-Audio"

    private val running     = AtomicBoolean(false)
    private val pcmQueue    = java.util.concurrent.LinkedBlockingQueue<ByteArray>(64)
    private var decoderThread: Thread? = null
    private var sampleRate  = 44100
    private var channels    = 1

    @Volatile var enabled   = false

    fun start() {
        if (running.getAndSet(true)) return
        decoderThread = Thread({ decodeLoop() }, "VCamSX-AudioDec").apply {
            isDaemon = true; start()
        }
        Log.d(TAG, "AudioInjector started")
    }

    fun stop() {
        running.set(false)
        decoderThread?.interrupt()
        decoderThread = null
        pcmQueue.clear()
        Log.d(TAG, "AudioInjector stopped")
    }

    fun read(buffer: ByteArray, offsetInBytes: Int, sizeInBytes: Int): Int {
        if (!enabled) return 0
        var written = 0
        while (written < sizeInBytes) {
            val chunk = pcmQueue.poll() ?: break
            val toCopy = minOf(chunk.size, sizeInBytes - written)
            System.arraycopy(chunk, 0, buffer, offsetInBytes + written, toCopy)
            written += toCopy
        }
        return written
    }

    fun read(buffer: ByteBuffer, sizeInBytes: Int): Int {
        if (!enabled) return 0
        var written = 0
        while (written < sizeInBytes) {
            val chunk = pcmQueue.poll() ?: break
            val toCopy = minOf(chunk.size, sizeInBytes - written)
            buffer.put(chunk, 0, toCopy)
            written += toCopy
        }
        return written
    }

    private fun decodeLoop() {
        while (running.get()) {
            try {
                val ctx = MainHook.context ?: run { Thread.sleep(500); continue }
                val uri = android.net.Uri.parse("content://com.wangyiheng.vcamsx.videoprovider")

                val extractor = MediaExtractor()
                extractor.setDataSource(ctx, uri, null)

                var audioTrack = -1
                var format: MediaFormat? = null
                for (i in 0 until extractor.trackCount) {
                    val fmt = extractor.getTrackFormat(i)
                    val mime = fmt.getString(MediaFormat.KEY_MIME) ?: continue
                    if (mime.startsWith("audio/")) {
                        audioTrack = i
                        format = fmt
                        break
                    }
                }

                if (audioTrack < 0 || format == null) {
                    Log.d(TAG, "No audio track found in video")
                    extractor.release()
                    Thread.sleep(2000)
                    continue
                }

                extractor.selectTrack(audioTrack)
                sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                channels   = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)

                val mime   = format.getString(MediaFormat.KEY_MIME)!!
                val codec  = MediaCodec.createDecoderByType(mime)
                codec.configure(format, null, null, 0)
                codec.start()
                Log.d(TAG, "Audio decoder started: $mime ${sampleRate}Hz ch=$channels")

                val info = MediaCodec.BufferInfo()
                var eos  = false

                while (running.get() && !eos) {
                    val inIdx = codec.dequeueInputBuffer(5000)
                    if (inIdx >= 0) {
                        val buf  = codec.getInputBuffer(inIdx)!!
                        val size = extractor.readSampleData(buf, 0)
                        if (size < 0) {
                            codec.queueInputBuffer(inIdx, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            eos = true
                        } else {
                            codec.queueInputBuffer(inIdx, 0, size, extractor.sampleTime, 0)
                            extractor.advance()
                        }
                    }

                    val outIdx = codec.dequeueOutputBuffer(info, 5000)
                    if (outIdx >= 0) {
                        val outBuf = codec.getOutputBuffer(outIdx)!!
                        val pcm    = ByteArray(info.size)
                        outBuf.get(pcm)
                        codec.releaseOutputBuffer(outIdx, false)

                        if (pcm.isNotEmpty()) {
                            if (!pcmQueue.offer(pcm)) {
                                pcmQueue.poll()
                                pcmQueue.offer(pcm)
                            }
                        }
                    }

                    if (eos) {
                        extractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                        codec.flush()
                        eos = false
                    }
                }

                codec.stop()
                codec.release()
                extractor.release()

            } catch (e: InterruptedException) {
                break
            } catch (e: Exception) {
                Log.e(TAG, "decodeLoop error: ${e.message}")
                try { Thread.sleep(1000) } catch (_: InterruptedException) { break }
            }
        }
        Log.d(TAG, "AudioInjector decode loop ended")
    }

    fun getSampleRate() = sampleRate
    fun getChannels()   = channels
}
