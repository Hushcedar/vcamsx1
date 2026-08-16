package virtual.camera.app.data

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.hack.opensdk.HackApi
import virtual.camera.app.R
import virtual.camera.app.app.App
import virtual.camera.app.app.AppManager
import virtual.camera.app.bean.AppInfo
import virtual.camera.app.bean.InstalledAppBean
import virtual.camera.app.util.getString

class AppsRepository {
    val TAG: String = "AppsRepository"

    fun previewInstallList() {
        // intentionally empty — list loads fresh every time in getInstalledAppList
    }

    fun getInstalledAppList(
        userID: Int,
        loadingLiveData: MutableLiveData<Boolean>,
        appsLiveData: MutableLiveData<List<InstalledAppBean>>
    ) {
        loadingLiveData.postValue(true)
        try {
            val pm = App.getContext().packageManager
            val installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val installedList = mutableListOf<InstalledAppBean>()

            for (app in installedApplications) {
                // Skip system apps
                if ((app.flags and ApplicationInfo.FLAG_SYSTEM) != 0) continue
                // Skip ourselves
                if (app.packageName == App.getContext().packageName) continue

                val isCloned = try {
                    HackApi.getInstalledPackages(0, userID).contains(app.packageName)
                } catch (e: Exception) { false }

                installedList.add(
                    InstalledAppBean(
                        app.loadLabel(pm).toString(),
                        app.loadIcon(pm),
                        app.packageName,
                        app.sourceDir ?: "",
                        isCloned
                    )
                )
            }

            installedList.sortBy { it.name.lowercase() }
            appsLiveData.postValue(installedList)
        } catch (e: Exception) {
            Log.e(TAG, "getInstalledAppList error: ${e.message}")
            appsLiveData.postValue(emptyList())
        }
        loadingLiveData.postValue(false)
    }

    fun getInstalledModuleList(
        loadingLiveData: MutableLiveData<Boolean>,
        appsLiveData: MutableLiveData<List<InstalledAppBean>>
    ) {
        loadingLiveData.postValue(true)
        appsLiveData.postValue(emptyList())
        loadingLiveData.postValue(false)
    }

    fun getVmInstallList(userId: Int, appsLiveData: MutableLiveData<List<AppInfo>>) {
        try {
            val pm = App.getContext().packageManager
            val installedPkgs = HackApi.getInstalledPackages(0, userId)
                .filterNot { it == "com.waxmoon.ma.gp" }

            val appInfoList = mutableListOf<AppInfo>()
            for (pkg in installedPkgs) {
                try {
                    val appInfo = pm.getApplicationInfo(pkg, 0)
                    appInfoList.add(
                        AppInfo(
                            appInfo.loadLabel(pm).toString(),
                            appInfo.loadIcon(pm),
                            pkg,
                            appInfo.sourceDir ?: "",
                            false
                        )
                    )
                } catch (e: Exception) {
                    continue
                }
            }
            appsLiveData.postValue(appInfoList)
        } catch (e: Exception) {
            Log.e(TAG, "getVmInstallList error: ${e.message}")
            appsLiveData.postValue(emptyList())
        }
    }

    fun installApk(source: String, userId: Int, resultLiveData: MutableLiveData<String>) {
        val success = HackApi.installPackageFromHost(source, userId)
        Log.e("installApk", "source:$source, result:$success")
        if (success) {
            updateAppSortList(userId, source, true)
            resultLiveData.postValue(getString(R.string.install_success))
        } else {
            resultLiveData.postValue(getString(R.string.install_fail, "failed"))
        }
        scanUser()
    }

    fun unInstall(packageName: String, userID: Int, resultLiveData: MutableLiveData<String>) {
        HackApi.uninstallPackage(packageName, userID)
        updateAppSortList(userID, packageName, false)
        scanUser()
        resultLiveData.postValue(getString(R.string.uninstall_success))
    }

    fun launchApk(packageName: String, userId: Int, launchLiveData: MutableLiveData<Boolean>) {
        val intent: Intent = HackApi.getLaunchIntentForPackage(packageName, userId)
            ?: run {
                launchLiveData.postValue(false)
                return
            }
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        HackApi.startActivity(intent, userId)
        launchLiveData.postValue(true)
    }

    fun clearApkData(packageName: String, userID: Int, resultLiveData: MutableLiveData<String>) {
        HackApi.deletePackageData(packageName, userID)
        resultLiveData.postValue(getString(R.string.clear_success))
    }

    private fun scanUser() {
        val userList = HackApi.getAvailableUserSpace()
        if (userList.isEmpty()) return
        val id = userList.last()
        if (HackApi.getInstalledPackages(0, id).isEmpty()) {
            AppManager.mRemarkSharedPreferences.edit {
                remove("Remark$id")
                remove("AppList$id")
            }
        }
    }

    private fun updateAppSortList(userID: Int, pkg: String, isAdd: Boolean) {
        val savedSortList = AppManager.mRemarkSharedPreferences.getString("AppList$userID", "") ?: ""
        val sortList = linkedSetOf<String>()
        if (savedSortList.isNotEmpty()) sortList.addAll(savedSortList.split(","))
        if (isAdd) sortList.add(pkg) else sortList.remove(pkg)
        AppManager.mRemarkSharedPreferences.edit {
            putString("AppList$userID", sortList.joinToString(","))
        }
    }

    fun updateApkOrder(userID: Int, dataList: List<AppInfo>) {
        AppManager.mRemarkSharedPreferences.edit {
            putString("AppList$userID", dataList.joinToString(",") { it.packageName })
        }
    }
}
