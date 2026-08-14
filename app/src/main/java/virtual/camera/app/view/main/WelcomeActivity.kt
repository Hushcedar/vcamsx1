package virtual.camera.app.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import virtual.camera.app.R
import virtual.camera.app.util.HandlerUtil
import virtual.camera.app.util.InjectionUtil
import virtual.camera.app.view.list.ListViewModel

/**
 * WelcomeActivity — splash screen.
 *
 * Shows for 3.5 s while the engine finishes initialising, then jumps to MainActivity.
 * previewInstalledAppList() warms up the app-list ViewModel in the background so the
 * first frame of MainActivity is populated without a visible loading state.
 */
class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        previewInstalledAppList()
        HandlerUtil.runOnMain({ jump() }, SPLASH_DELAY_MS)
    }

    private fun jump() {
        if (!isFinishing && !isDestroyed) {
            MainActivity.start(this)
            finish()
        }
    }

    private fun previewInstalledAppList() {
        val vm = ViewModelProvider(
            this,
            InjectionUtil.getListFactory()
        ).get(ListViewModel::class.java)
        vm.previewInstalledList()
    }

    companion object {
        private const val SPLASH_DELAY_MS = 3_500L
    }
}
