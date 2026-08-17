package com.blackbox.vcam;

import android.app.Application;
import android.content.Context;

import com.blackbox.vcam.camera.VirtualCameraEngine;
import com.blackbox.vcam.hook.VCamBlackBoxPlugin;
import com.blackbox.vcam.util.VCamLogger;

/**
 * VCamApplication
 *
 * Host Application class. Initialises the VirtualCameraEngine singleton
 * and registers the VCamBlackBoxPlugin with BlackBox's plugin system.
 *
 * In AndroidManifest.xml:
 *   android:name=".VCamApplication"
 *
 * BlackBox plugin registration:
 *   BlackBox 4.x exposes BlackBoxCore.get().registerPlugin(plugin).
 *   We call this here so the plugin receives lifecycle events for every
 *   cloned app without any per-launch wiring.
 */
public class VCamApplication extends Application {

    private static final String TAG = "VCam_App";

    private static VCamApplication sInstance;
    private VCamBlackBoxPlugin mPlugin;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        VCamLogger.d(TAG, "VCamApplication starting");

        // Boot the virtual camera engine (creates handler threads, etc.)
        VirtualCameraEngine.getInstance(this);

        // Create the plugin (loads persisted configs internally)
        mPlugin = new VCamBlackBoxPlugin(this);

        // Register with BlackBox so we get onAppStarted / onAppStopped callbacks
        registerWithBlackBox();

        VCamLogger.d(TAG, "Init complete");
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  BlackBox plugin registration
    // ──────────────────────────────────────────────────────────────────────────

    private void registerWithBlackBox() {
        try {
            // BlackBox 4.x public API: BlackBoxCore.get().registerPlugin(IAppPlugin)
            // The actual interface may vary by BlackBox version; we use reflection
            // so this compiles even without BlackBox as a local dependency.
            Class<?> bbCoreClass = Class.forName("com.lody.virtual.client.VirtualCore");
            Object bbCore = bbCoreClass.getMethod("get").invoke(null);

            // Try BlackBox's registerPlugin method
            java.lang.reflect.Method registerPlugin =
                    bbCoreClass.getDeclaredMethod("registerPlugin", Object.class);
            registerPlugin.setAccessible(true);
            registerPlugin.invoke(bbCore, mPlugin);

            VCamLogger.d(TAG, "Registered with BlackBox plugin system");
        } catch (ClassNotFoundException e) {
            // BlackBox classes not available in this build context (e.g., unit test)
            VCamLogger.w(TAG, "BlackBox VirtualCore not found — plugin registration skipped");
        } catch (Exception e) {
            VCamLogger.w(TAG, "BlackBox plugin registration failed: " + e.getMessage()
                    + " — falling back to manual invocation from MainActivity");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Accessors
    // ──────────────────────────────────────────────────────────────────────────

    public static VCamApplication get() {
        return sInstance;
    }

    public VCamBlackBoxPlugin getPlugin() {
        return mPlugin;
    }

    public static VCamBlackBoxPlugin plugin() {
        return sInstance != null ? sInstance.mPlugin : null;
    }
}
