package virtual.camera.app.data

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.hack.opensdk.HackApi
import virtual.camera.app.R
import virtual.camera.app.app.App
import virtual.camera.app.app.AppManager
import virtual.camera.app.bean.AppInfo
import virtual.camera.app.bean.InstalledAppBean
import virtual.camera.app.util.AbiUtils
import virtual.camera.app.util.getString
import java.io.File

class AppsRepository {
    val TAG: String = "AppsRepository"
    private var mInstalledList = mutableListOf<AppInfo>()

    fun previewInstallList() {
        synchronized(mInstalledList) {
            val installedApplications: List<ApplicationInfo> =
                App.getContext().getPackageManager().getInstalledApplications(0)
            val installedList = mutableListOf<AppInfo>()

            for (installedApplication in installedApplications) {
                val file = File(installedApplication.sourceDir)
                if ((installedApplication.flags and ApplicationInfo.FLAG_SYSTEM) != 0) continue
                val isXpModule = false
                val info = AppInfo(
                    installedApplication.loadLabel(App.getContext().getPackageManager()).toString(),
                    installedApplication.loadIcon(App.getContext().getPackageManager()),
                    installedApplication.packageName,
                    installedApplication.sourceDir,
                    isXpModule
                )
                installedList.add(info)
            }
            this.mInstalledList.clear()
            this.mInstalledList.addAll(installedList)
        }
    }

    fun getInstalledAppList(
        userID: Int,
        loadingLiveData: MutableLiveData<Boolean>,
        appsLiveData: MutableLiveData<List<InstalledAppBean>>
    ) {
        loadingLiveData.postValue(true)
        synchronized(mInstalledList) {
            Log.d(TAG, mInstalledList.joinToString(","))
            val newInstalledList = mInstalledList.map {
                val isInstalled = HackApi.getPackageInfo(it.packageName, userID, 0) != null
                InstalledAppBean(
                    it.name,
                    it.icon,
                    it.packageName,
                    it.sourceDir,
                    isInstalled
                )
            }
            appsLiveData.postValue(newInstalledList)
            loadingLiveData.postValue(false)
        }
    }

    fun getInstalledModuleList(
        loadingLiveData: MutableLiveData<Boolean>,
        appsLiveData: MutableLiveData<List<InstalledAppBean>>
    ) {
        loadingLiveData.postValue(true)
        synchronized(mInstalledList) {
            val moduleList = mInstalledList.filter {
                it.isXpModule
            }.map {
                InstalledAppBean(
                    it.name,
                    it.icon,
                    it.packageName,
                    it.sourceDir,
                    false
                )
            }
            appsLiveData.postValue(moduleList)
            loadingLiveData.postValue(false)
        }
    }

    fun getVmInstallList(userId: Int, appsLiveData: MutableLiveData<List<AppInfo>>) {
        val sortListData = AppManager.mRemarkSharedPreferences.getString("AppList$userId", "")
        val sortList = sortListData?.split(",")

        val installedPkgs = HackApi.getInstalledPackages(0, userId)
            .filterNot { it == "com.waxmoon.ma.gp" }

        val applicationList = mutableListOf<ApplicationInfo>()
        installedPkgs.forEach { pkg ->
            val packageInfo = HackApi.getPackageInfo(pkg, userId, 0)
            packageInfo.applicationInfo?.let { applicationList.add(it) }
        }

        val appInfoList = mutableListOf<AppInfo>()
        applicationList.also {
            if (sortList.isNullOrEmpty()) return@also
            it.sortWith(AppsSortComparator(sortList))
        }.forEach {
            val info = AppInfo(
                it.loadLabel(App.getContext().packageManager).toString(),
                it.loadIcon(App.getContext().packageManager),
                it.packageName,
                it.sourceDir,
                isInstalledXpModule(it.packageName)
            )
            appInfoList.add(info)
        }
        appsLiveData.postValue(appInfoList)
    }

    private fun isInstalledXpModule(packageName: String): Boolean {
        return false
    }

    fun installApk(source: String, userId: Int, resultLiveData: MutableLiveData<String>) {
        val success = HackApi.installPackageFromHost(source, userId)
        Log.e("11111", "source:$source, installResult:$success")
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
        if (1 == 1) { return }
        val userList = HackApi.getAvailableUserSpace()
        if (userList.isEmpty()) { return }
        val id = userList.last()
        if (HackApi.getInstalledPackages(0, id).isEmpty()) {
            AppManager.mRemarkSharedPreferences.edit {
                remove("Remark$id")
                remove("AppList$id")
            }
            scanUser()
        }
    }

    private fun updateAppSortList(userID: Int, pkg: String, isAdd: Boolean) {
        val savedSortList =
            AppManager.mRemarkSharedPreferences.getString("AppList$userID", "")
        val sortList = linkedSetOf<String>()
        if (savedSortList != null) {
            sortList.addAll(savedSortList.split(","))
        }
        if (isAdd) {
            sortList.add(pkg)
        } else {
            sortList.remove(pkg)
        }
        AppManager.mRemarkSharedPreferences.edit {
            putString("AppList$userID", sortList.joinToString(","))
        }
    }

    fun updateApkOrder(userID: Int, dataList: List<AppInfo>) {
        AppManager.mRemarkSharedPreferences.edit {
            putString("AppList$userID",
                dataList.joinToString(",") { it.packageName })
        }
    }
}
