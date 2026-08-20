package com.vcam.app

import android.app.Application
import com.vcam.app.engine.VCamPrefs

class VCamApp : Application() {
    companion object {
        lateinit var instance: VCamApp private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        VCamPrefs.init(this)
    }
}
