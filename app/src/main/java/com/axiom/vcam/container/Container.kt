package com.axiom.vcam.container

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.util.Log
import com.blackbox.api.BlackBoxCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Thin wrapper around BlackBoxCore.
 * All dual-space operations go through here — keeps BB details out of the UI.
 */
object Container {

    private const val TAG   = "VCamContainer"
    const val DEFAULT_USER  = 0

    // ── Install ───────────────────────────────────────────────────────────────

    /**
     * Clone a host-installed app into BlackBox space.
     * Returns true on success.
     */
    suspend fun install(packageName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = BlackBoxCore.get().installPackageAsUser(packageName, DEFAULT_USER)
            Log.d(TAG, "install $packageName → $result")
            result == 1   // INSTALL_SUCCEEDED = 1
        } catch (e: Throwable) {
            Log.e(TAG, "install error: ${e.message}", e)
            false
        }
    }

    /**
     * Uninstall a cloned app from BlackBox space.
     */
    suspend fun uninstall(packageName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            BlackBoxCore.get().uninstallPackage(packageName, DEFAULT_USER)
            true
        } catch (e: Throwable) {
            Log.e(TAG, "uninstall error: ${e.message}", e)
            false
        }
    }

    /**
     * Clear data for a cloned app.
     */
    suspend fun clearData(packageName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            BlackBoxCore.get().clearPackageData(packageName, DEFAULT_USER)
            true
        } catch (e: Throwable) {
            Log.e(TAG, "clearData error: ${e.message}", e)
            false
        }
    }

    // ── Launch ────────────────────────────────────────────────────────────────

    /**
     * Launch a cloned app inside BlackBox.
     * Returns true if the activity started successfully.
     */
    fun launch(packageName: String): Boolean {
        return try {
            val intent = BlackBoxCore.get().getLaunchIntent(packageName, DEFAULT_USER)
                ?: run { Log.e(TAG, "no launch intent for $packageName"); return false }
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                            android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            val result = BlackBoxCore.get().startActivity(intent, DEFAULT_USER)
            Log.d(TAG, "launch $packageName → $result")
            result == 0
        } catch (e: Throwable) {
            Log.e(TAG, "launch error: ${e.message}", e)
            false
        }
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /**
     * List of packages currently cloned in BlackBox.
     */
    fun installedPackages(): List<String> {
        return try {
            BlackBoxCore.get().getInstalledPackages(DEFAULT_USER, 0) ?: emptyList()
        } catch (e: Throwable) {
            Log.e(TAG, "installedPackages: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * PackageInfo for a cloned app (null if not installed in BB).
     */
    fun packageInfo(packageName: String): PackageInfo? {
        return try {
            BlackBoxCore.get().getPackageInfo(packageName, DEFAULT_USER, 0)
        } catch (_: Throwable) { null }
    }

    /**
     * ApplicationInfo for a cloned app.
     */
    fun appInfo(packageName: String): ApplicationInfo? {
        return packageInfo(packageName)?.applicationInfo
    }

    /**
     * True if packageName is installed in BlackBox.
     */
    fun isInstalled(packageName: String): Boolean = packageInfo(packageName) != null

    // ── Hook module registration ──────────────────────────────────────────────

    /**
     * Registers our camera hook module with BXposed so it gets injected
     * into every cloned app process that BlackBox starts.
     *
     * Called once at app startup from MainActivity.
     */
    fun registerHookModule(context: Context) {
        try {
            // BXposed looks for the module by package name and loads its
            // IXposedHookLoadPackage entry point declared in assets/xposed_init.
            // Since our hook lives in the same APK we register the host package.
            BlackBoxCore.get().setAppSystemInterface(
                context.packageName,
                "com.axiom.vcam.hook.CameraHook"
            )
            Log.d(TAG, "Hook module registered")
        } catch (e: Throwable) {
            Log.e(TAG, "registerHookModule: ${e.message}", e)
        }
    }
}
