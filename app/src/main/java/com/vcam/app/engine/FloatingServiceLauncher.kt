package com.vcam.app.engine

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

object FloatingServiceLauncher {
    private const val TAG        = "VCam-FloatLauncher"
    private const val VCAM_PKG   = "com.vcam.app"
    private const val VCAM_SVC   = "com.vcam.app.ui.FloatingService"

    fun start(ctx: Context) {
        try {
            val intent = Intent().apply {
                component = ComponentName(VCAM_PKG, VCAM_SVC)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (Build.VERSION.SDK_INT >= 26)
                ctx.startForegroundService(intent)
            else
                ctx.startService(intent)
            Log.d(TAG, "FloatingService started from hooked process")
        } catch (e: Exception) {
            Log.e(TAG, "start failed: ${e.message}")
        }
    }

    fun stop(ctx: Context) {
        try {
            ctx.stopService(Intent().apply {
                component = ComponentName(VCAM_PKG, VCAM_SVC)
            })
        } catch (e: Exception) {
            Log.e(TAG, "stop failed: ${e.message}")
        }
    }
}
