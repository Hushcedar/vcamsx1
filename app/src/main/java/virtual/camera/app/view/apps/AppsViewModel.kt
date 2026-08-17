package virtual.camera.app.view.apps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import virtual.camera.app.bean.AppInfo
import virtual.camera.app.bean.InstalledAppBean
import virtual.camera.app.data.AppsRepository

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppsRepository()

    val loadingLiveData   = MutableLiveData<Boolean>()
    val appsLiveData      = MutableLiveData<List<InstalledAppBean>>()
    val vmAppsLiveData    = MutableLiveData<List<AppInfo>>()
    val resultLiveData    = MutableLiveData<String>()

    fun getInstalledAppList(userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getInstalledAppList(userID, loadingLiveData, appsLiveData)
        }
    }

    fun getVmInstallList(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getVmInstallList(userId, vmAppsLiveData)
        }
    }

    fun installApk(source: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            repository.installApk(source, userId, resultLiveData)
            loadingLiveData.postValue(false)  // FIX: always stop loading
        }
    }

    fun unInstall(packageName: String, userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            repository.unInstall(packageName, userID, resultLiveData)
            loadingLiveData.postValue(false)  // FIX: always stop loading
        }
    }

    fun launchApk(packageName: String, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.launchApk(packageName, userId, MutableLiveData())
        }
    }

    fun clearApkData(packageName: String, userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingLiveData.postValue(true)
            repository.clearApkData(packageName, userID, resultLiveData)
            loadingLiveData.postValue(false)
        }
    }

    fun updateApkOrder(userID: Int, dataList: List<AppInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateApkOrder(userID, dataList)
        }
    }
}
