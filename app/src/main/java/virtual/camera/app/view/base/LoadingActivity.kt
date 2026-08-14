package virtual.camera.app.view.base

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity

open class LoadingActivity : AppCompatActivity() {

    private var loadingDialog: ProgressDialog? = null

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this).apply {
                isIndeterminate = true
                setCancelable(false)
            }
        }
        if (!isFinishing) loadingDialog?.show()
    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }
}
