package virtual.camera.app.app

import android.app.Application
import android.content.Context
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.app.configuration.ClientConfiguration

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
        BlackBoxCore.get().doCreate()
        ensureDefaultUser()
    }

    private fun ensureDefaultUser() {
        try {
            val users = BlackBoxCore.get().users
            if (users.isNullOrEmpty()) {
                BlackBoxCore.get().createUser(0)
            }
        } catch (e: Exception) {
            // Engine warming up — handled on first use
        }
    }
}
