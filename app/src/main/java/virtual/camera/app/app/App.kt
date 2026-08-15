package virtual.camera.app.app

import android.app.Application
import android.content.Context
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.entity.ClientConfiguration

/**
 * App — Application class
 *
 * Replaces the waxmoon HackApplication with BlackBoxCore initialization.
 * BlackBox mirrors the same attach → onCreate lifecycle.
 */
class App : Application() {

    companion object {
        private lateinit var instance: App
        fun getContext(): Context = instance.applicationContext
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // Initialize BlackBox engine — equivalent to HackApplication.attachBaseContext
        BlackBoxCore.get().doAttachBaseContext(base, object : ClientConfiguration() {
            override fun getHostPackageName(): String = base.packageName
        })
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Create default user space (User 0) if none exist
        BlackBoxCore.get().onCreate()
        ensureDefaultUser()
    }

    private fun ensureDefaultUser() {
        try {
            val users = BlackBoxCore.get().users
            if (users.isNullOrEmpty()) {
                BlackBoxCore.get().createUser("User 0")
            }
        } catch (e: Exception) {
            // Engine not ready yet — will be handled on first use
        }
    }
}
