package com.axiom.vcam.container

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.util.Log
import com.waxmoon.sdk.api.HackApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Container {

    private const val TAG  = "VCamContainer"
    const val USER_ID      = 0

    suspend fun install(packageName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            HackApi.installPackageFromHost(packageName, USER_ID, false)
            Log.d(TAG, "installed $packageName")
            true
        } catch (e: Throwable) { Log.e(TAG, "install: ${e.message}"); false }
    }

    suspend fun uninstall(packageName: String): Boolean = withContext(Dispatchers.IO) {
        try { HackApi.uninstallPackage(packageName, USER_ID); true }
        catch (e: Throwable) { Log.e(TAG, "uninstall: ${e.message}"); false }
    }

    suspend fun clearData(packageName: String): Boolean = withContext(Dispatchers.IO) {
        try { HackApi.deletePackageData(packageName, USER_ID); true }
        catch (e: Throwable) { Log.e(TAG, "clearData: ${e.message}"); false }
    }

    fun launch(packageName: String): Boolean {
        return try {
            val intent = HackApi.getLaunchIntentForPackage(packageName, USER_ID)
                ?: return false
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            HackApi.startActivity(intent, USER_ID)
            true
        } catch (e: Throwable) { Log.e(TAG, "launch: ${e.message}"); false }
    }

    fun installedPackages(): List<String> {
        return try {
            HackApi.getInstalledPackages(0, USER_ID)
                ?.map { it.packageName } ?: emptyList()
        } catch (e: Throwable) { emptyList() }
    }

    fun packageInfo(pkg: String): PackageInfo? {
        return try { HackApi.getPackageInfo(pkg, USER_ID, 0) }
        catch (_: Throwable) { null }
    }

    fun isInstalled(pkg: String): Boolean = packageInfo(pkg) != null

    fun registerHookModule(context: Context) {
        // MultiApp injects hook modules via opensdk's Xposed bridge
        // Hook entry point declared in hook/src/main/assets/xposed_init
        Log.d(TAG, "hook module registered via xposed_init")
    }
}
