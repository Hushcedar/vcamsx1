package com.wangyiheng.vcamsx.utils

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.wangyiheng.vcamsx.MainHook
import java.nio.ByteBuffer
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

object AudioInjector {
    private const val TAG = "VCamSX-Audio"

    private val running  = AtomicBoolean(false)
    private val pcmQueue = LinkedBlockingQueue<ByteArray>(64)
    private var decoderThread: Thread? = null

    @Volatile var enabled = false

    fun start() {
        if (running.getAndSet(true)) return
        decoderThread = Thread({ decodeLoop() }, "VCamSX-AudioDec").apply {
            isDaemon = true; start()
        }
        Log.d(TAG, "started")
    }

    fun stop() {
        running.set(false)
        decoderThread?.interrupt()
        decoderThread = null
        pcmQueue.clear()
        Log.d(TAG, "stopped")
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
            var codec: MediaCodec? = null
            var extractor: MediaExtractor? = null
            try {
                val ctx = MainHook.context
                if (ctx == null) { Thread.sleep(500); continue }

                val uri = android.net.Uri.parse("content://com.wangyiheng.vcamsx.videoprovider")
                extractor = MediaExtractor()
                extractor.setDataSource(ctx, uri, null)

                // Find audio track
                var audioTrackIndex = -1
                var format: MediaFormat? = null
                for (i in 0 until extractor.trackCount) {
                    val fmt  = extractor.getTrackFormat(i)
                    val mime = fmt.getString(MediaFormat.KEY_MIME) ?: continue
                    if (mime.startsWith("audio/")) {
                        audioTrackIndex = i
                        format = fmt
                        break
                    }
                }

                if (audioTrackIndex < 0 || format == null) {
                    Log.d(TAG, "no audio track in video")
                    extractor.release()
                    extractor = null
                    Thread.sleep(2000)
                    continue
                }

                extractor.selectTrack(audioTrackIndex)
                val mime = format.getString(MediaFormat.KEY_MIME)!!
                codec = MediaCodec.createDecoderByType(mime)
                codec.configure(format, null, null, 0)
                codec.start()

                val info = MediaCodec.BufferInfo()
                var eos  = false

                while (running.get()) {
                    // Feed
                    if (!eos) {
                        val inIdx = codec.dequeueInputBuffer(5000)
                        if (inIdx >= 0) {
                            val inBuf = codec.getInputBuffer(inIdx)!!
                            val size  = extractor.readSampleData(inBuf, 0)
                            if (size < 0) {
                                codec.queueInputBuffer(
                                    inIdx, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                )
                                eos = true
                            } else {
                                codec.queueInputBuffer(inIdx, 0, size, extractor.sampleTime, 0)
                                extractor.advance()
                            }
                        }
                    }

                    // Drain
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

                    // Loop video audio
                    if (eos) {
                        extractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                        codec.flush()
                        eos = false
                    }
                }

            } catch (e: InterruptedException) {
                Log.d(TAG, "interrupted")
                return
            } catch (e: Exception) {
                Log.e(TAG, "decodeLoop error: ${e.message}")
                try { Thread.sleep(1000) } catch (ie: InterruptedException) { return }
            } finally {
                try { codec?.stop() } catch (_: Exception) {}
                try { codec?.release() } catch (_: Exception) {}
                try { extractor?.release() } catch (_: Exception) {}
            }
        }
    }
}
