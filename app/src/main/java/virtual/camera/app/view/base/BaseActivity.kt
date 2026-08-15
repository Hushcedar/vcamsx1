package virtual.camera.app.view.base

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class BaseActivity : AppCompatActivity() {

    private var loadingDialog: ProgressDialog? = null

    fun initToolbar(toolbar: Toolbar, titleRes: Int, showBack: Boolean = false) {
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(titleRes)
        if (showBack) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this).apply {
                isIndeterminate = true
                setCancelable(false)
            }
        }
        if (!isFinishing) loadingDialog?.show()
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
    }
}
