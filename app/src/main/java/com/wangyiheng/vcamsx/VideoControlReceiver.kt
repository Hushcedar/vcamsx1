package com.wangyiheng.vcamsx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.wangyiheng.vcamsx.utils.ImagePlayer
import com.wangyiheng.vcamsx.utils.VideoPlayer

class VideoControlReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            // ── Video actions ─────────────────────────────────────────────────
            ACTION_PAUSE    -> VideoPlayer.togglePause()
            ACTION_RELOAD   -> VideoPlayer.reload()
            ACTION_ROTATE   -> {
                VideoPlayer.rotate()
                if (ImagePlayer.isActive.value) ImagePlayer.rotate()
            }
            ACTION_FLIP     -> {
                VideoPlayer.flip()
                if (ImagePlayer.isActive.value) ImagePlayer.flip()
            }
            ACTION_ZOOM_IN  -> {
                VideoPlayer.zoomIn()
                if (ImagePlayer.isActive.value) ImagePlayer.zoomIn()
            }
            ACTION_ZOOM_OUT -> {
                VideoPlayer.zoomOut()
                if (ImagePlayer.isActive.value) ImagePlayer.zoomOut()
            }
            ACTION_ADJUST   -> {
                val dx = intent.getIntExtra("dx", 0)
                val dy = intent.getIntExtra("dy", 0)
                VideoPlayer.adjustOffset(dx, dy)
                if (ImagePlayer.isActive.value) ImagePlayer.adjustOffset(dx, dy)
            }
            // ── Image-only actions ────────────────────────────────────────────
            ACTION_IMAGE_STOP   -> ImagePlayer.stop()
            ACTION_IMAGE_RELOAD -> {
                // Re-attach the GL renderer to the current camera surface
                if (ImagePlayer.isActive.value) ImagePlayer.activateInjection()
            }
        }
    }

    companion object {
        const val ACTION_PAUSE        = "com.wangyiheng.vcamsx.PAUSE"
        const val ACTION_RELOAD       = "com.wangyiheng.vcamsx.RELOAD"
        const val ACTION_ROTATE       = "com.wangyiheng.vcamsx.ROTATE"
        const val ACTION_FLIP         = "com.wangyiheng.vcamsx.FLIP"
        const val ACTION_ZOOM_IN      = "com.wangyiheng.vcamsx.ZOOM_IN"
        const val ACTION_ZOOM_OUT     = "com.wangyiheng.vcamsx.ZOOM_OUT"
        const val ACTION_ADJUST       = "com.wangyiheng.vcamsx.ADJUST"
        const val ACTION_IMAGE_STOP   = "com.wangyiheng.vcamsx.IMAGE_STOP"
        const val ACTION_IMAGE_RELOAD = "com.wangyiheng.vcamsx.IMAGE_RELOAD"

        fun register(context: Context) {
            val filter = IntentFilter().apply {
                addAction(ACTION_PAUSE)
                addAction(ACTION_RELOAD)
                addAction(ACTION_ROTATE)
                addAction(ACTION_FLIP)
                addAction(ACTION_ZOOM_IN)
                addAction(ACTION_ZOOM_OUT)
                addAction(ACTION_ADJUST)
                addAction(ACTION_IMAGE_STOP)
                addAction(ACTION_IMAGE_RELOAD)
            }
            try {
                if (Build.VERSION.SDK_INT >= 33) {
                    context.registerReceiver(VideoControlReceiver(), filter, Context.RECEIVER_EXPORTED)
                } else {
                    context.registerReceiver(VideoControlReceiver(), filter)
                }
            } catch (e: Exception) {
                try { context.registerReceiver(VideoControlReceiver(), filter) } catch (_: Exception) {}
            }
        }
    }
}
