package com.blackbox.vcam.hook;

import android.content.Context;
import android.util.Log;

import com.blackbox.vcam.camera.VirtualCameraEngine;
import com.blackbox.vcam.model.VCamConfig;
import com.blackbox.vcam.util.VCamLogger;
import com.blackbox.vcam.util.VCamPreferences;
import java.util.Map;

/**
 * VCamBlackBoxPlugin
 *
 * Implements BlackBox 4.x's IAppPlugin interface (stub shown below).
 * BlackBox calls these lifecycle methods for every guest app it manages.
 *
 * Registration happens in VCamApplication.onCreate() via:
 *   BlackBoxCore.get().registerPlugin(new VCamBlackBoxPlugin(context));
 *
 * BlackBox's plugin interface (from its public API):
 * ┌─────────────────────────────────────────────────┐
 * │ interface IAppPlugin {                           │
 * │   void onAppInstalled(String pkg, int userId);  │
 * │   void onAppUninstalled(String pkg, int userId);│
 * │   void onAppStarted(String pkg, int userId);    │
 * │   void onAppStopped(String pkg, int userId);    │
 * │ }                                               │
 * └─────────────────────────────────────────────────┘
 *
 * We implement this interface (or its available variant) so BlackBox notifies
 * us exactly when a cloned app starts/stops — no polling needed.
 */
public class VCamBlackBoxPlugin {

    private static final String TAG = "VCam_BBPlugin";

    private final Context             mContext;
    private final VirtualCameraEngine mEngine;
    private final BlackBoxHookBridge  mBridge;
    private final VCamPreferences     mPrefs;

    public VCamBlackBoxPlugin(Context context) {
        mContext = context.getApplicationContext();
        mEngine  = VirtualCameraEngine.getInstance(mContext);
        mBridge  = new BlackBoxHookBridge(mContext, mEngine);
        mPrefs   = new VCamPreferences(mContext);

        // Restore persisted configs for all previously configured guest apps
        restorePersistedConfigs();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  IAppPlugin callbacks
    // ──────────────────────────────────────────────────────────────────────────

    /** BlackBox guest app installed into the virtual env. */
    public void onAppInstalled(String packageName, int userId) {
        VCamLogger.d(TAG, "onAppInstalled: " + packageName + " userId=" + userId);
        // No action needed at install time; config applied on start
    }

    /** BlackBox guest app removed from virtual env. */
    public void onAppUninstalled(String packageName, int userId) {
        VCamLogger.d(TAG, "onAppUninstalled: " + packageName + " userId=" + userId);
        mPrefs.removeConfig(packageName);
    }

    /** BlackBox guest app process is starting. THIS is where we arm the camera. */
    public void onAppStarted(String packageName, int userId) {
        VCamLogger.d(TAG, "onAppStarted: " + packageName + " userId=" + userId);

        // Load persisted config for this package
        VCamConfig config = mPrefs.loadConfig(packageName);
        if (config != null) {
            mBridge.setConfigForGuest(packageName, config);
        }

        mBridge.onGuestAppStart(packageName, userId);

        // Attempt reflection hooks as supplemental intercept path
        mBridge.tryReflectionHook();
    }

    /** BlackBox guest app process has stopped. */
    public void onAppStopped(String packageName, int userId) {
        VCamLogger.d(TAG, "onAppStopped: " + packageName + " userId=" + userId);
        mBridge.onGuestAppStop(packageName);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Config API (called from BottomSheetVCamMenu via host Activity)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Called from the three-dot menu when the user configures virtual camera
     * settings for a specific cloned app.
     */
    public void applyVCamConfig(String packageName, VCamConfig config) {
        // Persist so it survives restarts
        mPrefs.saveConfig(packageName, config);
        mBridge.setConfigForGuest(packageName, config);
        VCamLogger.d(TAG, "VCam config applied for " + packageName
                + " enabled=" + config.isEnabled()
                + " source=" + config.getSourceType());
    }

    public VCamConfig getVCamConfig(String packageName) {
        return mBridge.getConfigForGuest(packageName);
    }

    public BlackBoxHookBridge getBridge() { return mBridge; }

    // ──────────────────────────────────────────────────────────────────────────
    //  Internal
    // ──────────────────────────────────────────────────────────────────────────

    private void restorePersistedConfigs() {
        Map<String, VCamConfig> all = mPrefs.loadAllConfigs();
        for (Map.Entry<String, VCamConfig> entry : all.entrySet()) {
            mBridge.setConfigForGuest(entry.getKey(), entry.getValue());
        }
        VCamLogger.d(TAG, "Restored " + all.size() + " persisted VCam configs");
    }
}
