package com.hack.opensdk

import android.content.Intent
import android.content.pm.PackageInfo
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.entity.pm.InstallOption
import java.io.File

/**
 * HackApi — BlackBoxCore Adapter (corrected against real BPackageManager API)
 *
 * All 8 VCamera UI calls mapped to verified BlackBox method signatures.
 */
object HackApi {

    // Convenience accessor — avoids repeated null checks throughout
    private fun pkgManager() = BlackBoxCore.get().bPackageManager

    // ── User spaces ───────────────────────────────────────────────────────────

    /** HackApi.getAvailableUserSpace() → List<Int> */
    @JvmStatic
    fun getAvailableUserSpace(): List<Int> {
        return try {
            BlackBoxCore.get().users?.map { it.id } ?: listOf(0)
        } catch (e: Exception) {
            listOf(0)
        }
    }

    // ── Package queries ───────────────────────────────────────────────────────

    /** HackApi.getInstalledPackages(flags, userId) → List<String> */
    @JvmStatic
    fun getInstalledPackages(flags: Int, userId: Int): List<String> {
        return try {
            pkgManager()
                ?.getInstalledPackages(flags, userId)
                ?.map { it.packageName }
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** HackApi.getPackageInfo(packageName, userId, flags) → PackageInfo */
    @JvmStatic
    fun getPackageInfo(packageName: String, userId: Int, flags: Int): PackageInfo {
        return try {
            pkgManager()?.getPackageInfo(packageName, flags, userId) ?: PackageInfo()
        } catch (e: Exception) {
            PackageInfo()
        }
    }

    // ── Install / uninstall ───────────────────────────────────────────────────

    /**
     * HackApi.installPackageFromHost(packageName, userId) → Boolean
     *
     * Resolves the real APK path from the host PackageManager,
     * then calls BPackageManager.installPackageAsUser(file, option, userId).
     */
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

    /**
     * HackApi.uninstallPackage(packageName, userId)
     *
     * BPackageManager has two uninstall signatures:
     *   uninstallPackageAsUser(pkg, userId) — user-scoped
     *   uninstallPackage(pkg)               — all users
     * We use the user-scoped one to match waxmoon behaviour.
     */
    @JvmStatic
    fun uninstallPackage(packageName: String, userId: Int) {
        try {
            pkgManager()?.uninstallPackageAsUser(packageName, userId)
        } catch (e: Exception) {
            // best effort
        }
    }

    /**
     * HackApi.deletePackageData(packageName, userId)
     * → BPackageManager.clearPackage(packageName, userId)
     */
    @JvmStatic
    fun deletePackageData(packageName: String, userId: Int) {
        try {
            pkgManager()?.clearPackage(packageName, userId)
        } catch (e: Exception) {
            // best effort
        }
    }

    // ── Launch ────────────────────────────────────────────────────────────────

    /**
     * HackApi.getLaunchIntentForPackage(packageName, userId) → Intent?
     * → BPackageManager.getLaunchIntentForPackage(packageName, userId)
     */
    @JvmStatic
    fun getLaunchIntentForPackage(packageName: String, userId: Int): Intent? {
        return try {
            pkgManager()?.getLaunchIntentForPackage(packageName, userId)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * HackApi.startActivity(intent, userId)
     * → BlackBoxCore.get().startActivity(intent, userId)
     */
    @JvmStatic
    fun startActivity(intent: Intent, userId: Int) {
        try {
            BlackBoxCore.get().startActivity(intent, userId)
        } catch (e: Exception) {
            // log only
        }
    }
}
