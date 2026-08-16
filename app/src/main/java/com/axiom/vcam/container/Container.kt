package com.axiom.vcam.container

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.util.Log

object Container {
    private const val TAG = "VCamContainer"
    const val USER_ID = 0

    suspend fun install(packageName: String): Boolean {
        Log.d(TAG, "install: $packageName"); return true
    }
    suspend fun uninstall(packageName: String): Boolean {
        Log.d(TAG, "uninstall: $packageName"); return true
    }
    suspend fun clearData(packageName: String): Boolean {
        Log.d(TAG, "clearData: $packageName"); return true
    }
    fun launch(packageName: String): Boolean {
        Log.d(TAG, "launch: $packageName"); return true
    }
    fun installedPackages(): List<String> = emptyList()
    fun packageInfo(pkg: String): PackageInfo? = null
    fun isInstalled(pkg: String): Boolean = false
    fun registerHookModule(context: Context) {
        Log.d(TAG, "hook module registered")
    }
}
