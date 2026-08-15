package com.hack.opensdk

import android.content.Intent
import android.content.pm.PackageInfo
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.entity.pm.InstallOption
import java.io.File

object HackApi {

    private fun pkgManager() = BlackBoxCore.get().getBPackageManager()

    @JvmStatic
    fun getAvailableUserSpace(): List<Int> {
        return try {
            BlackBoxCore.get().users?.map { user -> user.id } ?: listOf(0)
        } catch (e: Exception) {
            listOf(0)
        }
    }

    @JvmStatic
    fun getInstalledPackages(flags: Int, userId: Int): List<String> {
        return try {
            pkgManager()
                ?.getInstalledPackages(flags, userId)
                ?.mapNotNull { pkg -> pkg?.packageName }
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    @JvmStatic
    fun getPackageInfo(packageName: String, userId: Int, flags: Int): PackageInfo {
        return try {
            pkgManager()?.getPackageInfo(packageName, flags, userId) ?: PackageInfo()
        } catch (e: Exception) {
            PackageInfo()
        }
    }

    @JvmStatic
    fun installPackageFromHost(packageName: String, userId: Int): Boolean {
        return try {
            val context = BlackBoxCore.getContext()
            val sourceDir = context.packageManager
                .getApplicationInfo(packageName, 0)
                .sourceDir
            val apkFile = File(sourceDir)
            val option = InstallOption()
            val result = pkgManager()?.installPackageAsUser(apkFile, option, userId)
            result?.isSuccess ?: false
        } catch (e: Exception) {
            false
        }
    }

    @JvmStatic
    fun uninstallPackage(packageName: String, userId: Int) {
        try {
            pkgManager()?.uninstallPackageAsUser(packageName, userId)
        } catch (e: Exception) {}
    }

    @JvmStatic
    fun deletePackageData(packageName: String, userId: Int) {
        try {
            pkgManager()?.clearPackage(packageName, userId)
        } catch (e: Exception) {}
    }

    @JvmStatic
    fun getLaunchIntentForPackage(packageName: String, userId: Int): Intent? {
        return try {
            pkgManager()?.getLaunchIntentForPackage(packageName, userId)
        } catch (e: Exception) {
            null
        }
    }

    @JvmStatic
    fun startActivity(intent: Intent, userId: Int) {
        try {
            BlackBoxCore.get().startActivity(intent, userId)
        } catch (e: Exception) {}
    }
}
