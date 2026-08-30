package com.vcam.app.engine

import android.media.*
import android.net.Uri
import android.util.Log
import android.view.Surface


class VideoToFrames : Runnable {
    companion object {
        private const val TAG     = "VCamSX-Decoder"
        private const val TIMEOUT = 10_000L
        // EMPTY (size=0) not byteArrayOf(0) (size=1).
        // Size==0 means "no frame yet". Camera1 spin-wait checks size <= 1
        // so byteArrayOf(0) would never trigger the wait — that was the Skout bug.
        @JvmField @Volatile var data_buffer = byteArrayOf()
    }

    @Volatile private var stopDecode = false
    private var outputImageFormat: OutputImageFormat? = null
    private var videoFilePath: Any? = null
    private var childThread: Thread? = null

    var play_surf: Surface? = null

    fun stopDecode()                          { stopDecode = true }
    fun setSaveFrames(fmt: OutputImageFormat) { outputImageFormat = fmt }
    fun set_surface(s: Surface)               { play_surf = s }

    fun decode(path: Any) {
        videoFilePath = path
        if (childThread == null)
            childThread = Thread(this, "VCamSX-decode").apply { start() }
    }

    override fun run() {
        try { videoFilePath?.let { loop(it) } }
        catch (e: Exception) { Log.e(TAG, "run: ${e.message}") }
    }

    private fun loop(path: Any) {
        var ext: MediaExtractor? = null
        var dec: MediaCodec?     = null
        try {
            ext = MediaExtractor()
            val ctx = MainHook.context
            when {
                path is Uri && ctx != null -> ext.setDataSource(ctx, path, null)
                path is String             -> ext.setDataSource(path)
                else -> return
            }
            val track = (0 until ext.trackCount).firstOrNull {
                ext.getTrackFormat(it).getString(MediaFormat.KEY_MIME)?.startsWith("video/") == true
            } ?: return
            ext.selectTrack(track)
            val fmt  = ext.getTrackFormat(track)
            val mime = fmt.getString(MediaFormat.KEY_MIME) ?: return

            val rot = VideoControls.rotation.value
            if (rot != 0) fmt.setInteger(MediaFormat.KEY_ROTATION, rot)

            dec = MediaCodec.createDecoderByType(mime)
            if (play_surf == null) {
                val caps = dec.codecInfo.getCapabilitiesForType(mime)
                val flex = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible
                if (caps.colorFormats.contains(flex)) fmt.setInteger(MediaFormat.KEY_COLOR_FORMAT, flex)
            }

            decodeFrames(dec, ext, fmt); dec.stop()
            while (!stopDecode) {
                ext.seekTo(0L, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
                decodeFrames(dec, ext, fmt); dec.stop()
            }
        } catch (e: Exception) {
            Log.e(TAG, "loop: ${e.message}", e)
        } finally {
            try { dec?.stop()    } catch (_: Exception) {}
            try { dec?.release() } catch (_: Exception) {}
            try { ext?.release() } catch (_: Exception) {}
        }
    }

    private fun decodeFrames(dec: MediaCodec, ext: MediaExtractor, fmt: MediaFormat) {
        dec.configure(fmt, play_surf, null, 0); dec.start()
        val info  = MediaCodec.BufferInfo()
        var inEOS = false; var outEOS = false
        var startMs = 0L; var first = true

        while (!outEOS && !stopDecode) {
            if (!inEOS) {
                val idx = dec.dequeueInputBuffer(TIMEOUT)
                if (idx >= 0) {
                    val buf  = dec.getInputBuffer(idx)!!
                    val read = ext.readSampleData(buf, 0)
                    if (read < 0) {
                        dec.queueInputBuffer(idx, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        inEOS = true
                    } else {
                        dec.queueInputBuffer(idx, 0, read, ext.sampleTime, 0)
                        ext.advance()
                    }
                }
            }
            val out = dec.dequeueOutputBuffer(info, TIMEOUT)
            if (out >= 0) {
                if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) outEOS = true
                if (info.size > 0) {
                    if (first) { startMs = System.currentTimeMillis(); first = false }
                    if (play_surf == null) {
                        dec.getOutputImage(out)?.use { img ->
                            if (outputImageFormat != null) {
                                val nv21 = toNV21(img)
                                data_buffer = nv21; MainHook.data_buffer = nv21
                            }
                        }
                    }
                    val sleep = info.presentationTimeUs / 1000 - (System.currentTimeMillis() - startMs)
                    if (sleep > 0) Thread.sleep(sleep)
                    dec.releaseOutputBuffer(out, play_surf != null)
                } else {
                    dec.releaseOutputBuffer(out, false)
                }
            }
        }
    }

    private fun toNV21(img: android.media.Image): ByteArray {
        val crop = img.cropRect
        val w = crop.width(); val h = crop.height()
        val planes = img.planes
        val out = ByteArray(w * h * 3 / 2)
        val yBuf = planes[0].buffer; val yStr = planes[0].rowStride
        val tmpY = ByteArray(yStr)
        for (r in 0 until h) {
            yBuf.position(yStr * (crop.top + r) + crop.left)
            val len = minOf(w, yBuf.remaining()); yBuf.get(tmpY, 0, len)
            System.arraycopy(tmpY, 0, out, r * w, len)
        }
        val uBuf = planes[1].buffer; val vBuf = planes[2].buffer
        val uStr = planes[1].rowStride; val vStr = planes[2].rowStride
        val uPix = planes[1].pixelStride; val vPix = planes[2].pixelStride
        val tU = ByteArray(uStr); val tV = ByteArray(vStr)
        var dst = w * h
        for (r in 0 until h / 2) {
            uBuf.position((crop.top / 2 + r) * uStr + crop.left / 2)
            vBuf.position((crop.top / 2 + r) * vStr + crop.left / 2)
            uBuf.get(tU, 0, minOf(uStr, uBuf.remaining()))
            vBuf.get(tV, 0, minOf(vStr, vBuf.remaining()))
            for (c in 0 until w / 2) {
                if (dst + 1 >= out.size) break
                out[dst++] = tV[c * vPix]; out[dst++] = tU[c * uPix]
            }
        }
        return out
    }
}

enum class OutputImageFormat(val friendlyName: String) {
    I420("I420"), NV21("NV21"), JPEG("JPEG");
    override fun toString() = friendlyName
}
