package virtual.camera.app.view.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import virtual.camera.app.data.AppsRepository

class AppsFactory(private val repository: AppsRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppsViewModel(repository) as T
    }
}
