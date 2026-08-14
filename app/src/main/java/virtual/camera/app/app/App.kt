package virtual.camera.app.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.hack.opensdk.HackApplication

/**
 * App — 2026-compatible root Application.
 *
 * Extends HackApplication which now:
 *  - Injects the GITHUB license key before engine init
 *  - Does NOT reference com.waxmoon.ma.gp.apk
 *  - Is Android 12+ / API 35 W^X safe via code_cache DEX output
 *
 * mContext is stored here for global access (SharedPreferences, resources, etc.)
 * Volatile + @SuppressLint("StaticFieldLeak") is intentional: this context is
 * the Application context, not an Activity — no leak risk.
 */
class App : HackApplication() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private lateinit var mContext: Context

        @JvmStatic
        fun getContext(): Context = mContext
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)   // HackApplication.attachBaseContext handles engine init
        mContext = base!!
    }
}
