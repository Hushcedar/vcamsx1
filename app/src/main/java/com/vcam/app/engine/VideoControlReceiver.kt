package com.vcam.app.engine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build

class VideoControlReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_PAUSE           -> VideoPlayer.togglePause()
            ACTION_RELOAD          -> VideoPlayer.reload()
            ACTION_ROTATE          -> VideoPlayer.rotate()
            ACTION_FLIP            -> VideoPlayer.flip()
            ACTION_ZOOM_IN         -> VideoPlayer.zoomIn()
            ACTION_ZOOM_OUT        -> VideoPlayer.zoomOut()
            ACTION_RESET_TRANSFORM -> VideoPlayer.resetTransform()
            ACTION_ADJUST          -> VideoPlayer.adjustOffset(
                intent.getIntExtra("dx", 0),
                intent.getIntExtra("dy", 0)
            )
        }
    }

    companion object {
        const val ACTION_PAUSE           = "com.vcam.app.PAUSE"
        const val ACTION_RELOAD          = "com.vcam.app.RELOAD"
        const val ACTION_ROTATE          = "com.vcam.app.ROTATE"
        const val ACTION_FLIP            = "com.vcam.app.FLIP"
        const val ACTION_ZOOM_IN         = "com.vcam.app.ZOOM_IN"
        const val ACTION_ZOOM_OUT        = "com.vcam.app.ZOOM_OUT"
        const val ACTION_ADJUST          = "com.vcam.app.ADJUST"
        const val ACTION_RESET_TRANSFORM = "com.vcam.app.RESET_TRANSFORM"

        fun register(context: Context) {
            val filter = IntentFilter().apply {
                addAction(ACTION_PAUSE); addAction(ACTION_RELOAD)
                addAction(ACTION_ROTATE); addAction(ACTION_FLIP)
                addAction(ACTION_ZOOM_IN); addAction(ACTION_ZOOM_OUT)
                addAction(ACTION_ADJUST); addAction(ACTION_RESET_TRANSFORM)
            }
            try {
                if (Build.VERSION.SDK_INT >= 33)
                    context.registerReceiver(VideoControlReceiver(), filter, Context.RECEIVER_EXPORTED)
                else
                    context.registerReceiver(VideoControlReceiver(), filter)
            } catch (e: Exception) {
                try { context.registerReceiver(VideoControlReceiver(), filter) } catch (_: Exception) {}
            }
        }
    }
}
