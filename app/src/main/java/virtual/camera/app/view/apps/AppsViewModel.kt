package virtual.camera.app.view.apps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import virtual.camera.app.bean.AppInfo
import virtual.camera.app.data.AppsRepository

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppsRepository()

    val loadingLiveData      = MutableLiveData<Boolean>()
    val appsLiveData         = MutableLiveData<List<AppInfo>>()
    val resultLiveData       = MutableLiveData<String>()
    val launchLiveData       = MutableLiveData<Boolean>()
    val updateSortLiveData   = MutableLiveData<Boolean>()

    // Called as getInstalledApps(userID) from AppsFragment
    fun getInstalledApps(userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getVmInstallList(userID, appsLiveData)
        }
    }

    // Called as install(source, userID) from AppsFragment
    fun install(source: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                repository.installApk(source, userId, resultLiveData)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    fun unInstall(packageName: String, userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                repository.unInstall(packageName, userID, resultLiveData)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    fun launchApk(packageName: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.launchApk(packageName, userId, launchLiveData)
        }
    }

    fun clearApkData(packageName: String, userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            try {
                repository.clearApkData(packageName, userID, resultLiveData)
            } finally {
                loadingLiveData.postValue(false)
            }
        }
    }

    fun updateApkOrder(userID: Int, dataList: List<AppInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateApkOrder(userID, dataList)
        }
    }
}
