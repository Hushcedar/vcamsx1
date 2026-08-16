package com.axiom.vcam

import android.app.Application
import android.util.Log
import com.blackbox.api.BlackBoxCore

class VCamApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initBlackBox()
    }

    private fun initBlackBox() {
        try {
            // BlackBox boots its container runtime here.
            // All cloned apps run inside BB's virtual process space.
            // Our hook module (com.axiom.vcam.hook) is registered as a
            // BXposed module and gets injected into every cloned app process.
            BlackBoxCore.get().doAttachBaseContext(this, true)
            Log.d(TAG, "BlackBox initialised")
        } catch (e: Throwable) {
            Log.e(TAG, "BlackBox init failed: ${e.message}", e)
        }
    }

    companion object {
        const val TAG = "VCamRevived"
    }
}
