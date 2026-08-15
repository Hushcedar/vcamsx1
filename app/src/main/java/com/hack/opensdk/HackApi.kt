package com.hack.opensdk

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.entity.pm.InstallOption

/**
 * HackApi — BlackBoxCore Adapter
 *
 * Drop-in replacement for the waxmoon HackApi.
 * Maps every call the VCamera UI shell makes to its BlackBoxCore equivalent.
 * Zero changes needed in the UI layer.
 *
 * HackApi call             → BlackBoxCore equivalent
 * ─────────────────────────────────────────────────
 * getAvailableUserSpace    → getUsers() → map to Int list
 * getInstalledPackages     → getInstalledPackages(flags, userId)
 * getPackageInfo           → getPackageInfo(pkg, flags, userId)
 * installPackageFromHost   → installPackageAsUser(path, userId, option)
 * uninstallPackage         → uninstallPackage(pkg, userId)
 * deletePackageData        → clearPackage(pkg, userId)
 * getLaunchIntentForPackage→ getLaunchIntent(pkg, userId)
 * startActivity            → startActivity(intent, userId)
 */
object HackApi {

    // ── User space ────────────────────────────────────────────────────────────

    /**
     * Returns list of available virtual user IDs (spaces).
     * BlackBox supports multiple users — we expose their IDs as Int list.
     * Matches the original: HackApi.getAvailableUserSpace() → List<Int>
     */
    @JvmStatic
    fun getAvailableUserSpace(): List<Int> {
        return try {
            BlackBoxCore.get().users?.map { it.id } ?: listOf(0)
        } catch (e: Exception) {
            listOf(0) // fallback to single user space
        }
    }

    // ── Package queries ───────────────────────────────────────────────────────

    /**
     * Returns list of package names installed in the virtual space.
     * Original: HackApi.getInstalledPackages(flags, userId) → List<String>
     */
    @JvmStatic
    fun getInstalledPackages(flags: Int, userId: Int): List<String> {
        return try {
            BlackBoxCore.get()
                .getInstalledPackages(flags, userId)
                ?.map { it.packageName }
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Returns PackageInfo for a package in the virtual space.
     * Original: HackApi.getPackageInfo(pkg, userId, flags) → PackageInfo
     */
    @JvmStatic
    fun getPackageInfo(packageName: String, userId: Int, flags: Int): PackageInfo {
        return try {
            BlackBoxCore.get().getPackageInfo(packageName, flags, userId)
                ?: PackageInfo() // return empty rather than crash
        } catch (e: Exception) {
            PackageInfo()
        }
    }

    // ── Install / uninstall ───────────────────────────────────────────────────

    /**
     * Installs a host APK into the virtual space.
     * Original: HackApi.installPackageFromHost(packageName, userId) → Boolean
     *
     * In the waxmoon SDK this took a package name and cloned it from the host.
     * BlackBox equivalent: installPackageAsUser with the real APK path.
     */
    @JvmStatic
    fun installPackageFromHost(packageName: String, userId: Int): Boolean {
        return try {
            // Resolve real APK path from host PackageManager
            val sourceDir = BlackBoxCore.getContext()
                .packageManager
                .getApplicationInfo(packageName, 0)
                .sourceDir

            val option = InstallOption.installFromHost(packageName)
            BlackBoxCore.get().installPackageAsUser(sourceDir, userId, option)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Uninstalls a package from the virtual space.
     * Original: HackApi.uninstallPackage(packageName, userId) → Boolean
     */
    @JvmStatic
    fun uninstallPackage(packageName: String, userId: Int): Boolean {
        return try {
            BlackBoxCore.get().uninstallPackage(packageName, userId)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Clears all data for a package in the virtual space.
     * Original: HackApi.deletePackageData(packageName, userId)
     */
    @JvmStatic
    fun deletePackageData(packageName: String, userId: Int) {
        try {
            BlackBoxCore.get().clearPackage(packageName, userId)
        } catch (e: Exception) {
            // swallow — best effort clear
        }
    }

    // ── Launch ────────────────────────────────────────────────────────────────

    /**
     * Returns a launch intent for a virtual package.
     * Original: HackApi.getLaunchIntentForPackage(packageName, userId) → Intent?
     */
    @JvmStatic
    fun getLaunchIntentForPackage(packageName: String, userId: Int): Intent? {
        return try {
            BlackBoxCore.get().getLaunchIntent(packageName, userId)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Starts an activity inside the virtual space.
     * Original: HackApi.startActivity(intent, userId)
     */
    @JvmStatic
    fun startActivity(intent: Intent, userId: Int) {
        try {
            BlackBoxCore.get().startActivity(intent, userId)
        } catch (e: Exception) {
            // log only — don't crash caller
        }
    }
}
