package com.axiom.vcam

import android.app.Application
import android.util.Log

class VCamApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("VCamRevived", "App started")
    }
}
