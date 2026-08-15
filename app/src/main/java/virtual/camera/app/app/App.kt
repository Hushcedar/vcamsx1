package virtual.camera.app.app

import android.app.Application
import android.content.Context
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.entity.ClientConfiguration

/**
 * App — Application class
 *
 * Bootstraps BlackBox using the verified API:
 *   doAttachBaseContext(context, ClientConfiguration) — in attachBaseContext
 *   doCreate()                                        — in onCreate
 *   createUser(name)                                  — ensures User 0 exists
 */
class App : Application() {

    companion object {
        private lateinit var instance: App

        @JvmStatic
        fun getContext(): Context = instance.applicationContext
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        BlackBoxCore.get().doAttachBaseContext(
            base,
            object : ClientConfiguration() {
                override fun getHostPackageName(): String = base.packageName
            }
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // doCreate() completes engine initialisation after attachBaseContext
        BlackBoxCore.get().doCreate()
        ensureDefaultUser()
    }

    private fun ensureDefaultUser() {
        try {
            val users = BlackBoxCore.get().users
            if (users.isNullOrEmpty()) {
                BlackBoxCore.get().createUser("User 0")
            }
        } catch (e: Exception) {
            // Engine still warming up — first-use will handle it
        }
    }
}
