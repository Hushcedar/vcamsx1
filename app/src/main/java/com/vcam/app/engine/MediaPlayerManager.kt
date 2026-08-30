package com.vcam.app.engine

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.Surface

object MediaPlayerManager {
    private const val TAG = "VCam-MPManager"
    private var mediaPlayer: MediaPlayer? = null

    fun play(ctx: Context, surface: Surface, loop: Boolean = true) {
        try {
            release()
            mediaPlayer = MediaPlayer().apply {
                setSurface(surface)
                isLooping = loop
                setDataSource(ctx, Uri.parse("content://com.vcam.app.videoprovider"))
                prepareAsync()
                setOnPreparedListener { it.start() }
                setOnErrorListener { _, w, e -> Log.e(TAG, "error $w/$e"); false }
            }
        } catch (e: Exception) { Log.e(TAG, "play: ${e.message}") }
    }

    fun release() {
        try { mediaPlayer?.stop() } catch (_: Exception) {}
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun pause()  { try { mediaPlayer?.pause()  } catch (_: Exception) {} }
    fun resume() { try { mediaPlayer?.start()  } catch (_: Exception) {} }
    fun seekTo(ms: Int) { try { mediaPlayer?.seekTo(ms) } catch (_: Exception) {} }
}
