package com.vcam.app.ui

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

// Stub — floating overlay replaced by in-app Controls dialog
class FloatingService : Service() {
    companion object {
        fun start(ctx: Context) {}
        fun stop(ctx: Context)  {}
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
