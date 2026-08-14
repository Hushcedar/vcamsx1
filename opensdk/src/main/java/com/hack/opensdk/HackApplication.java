package com.hack.opensdk;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.hack.Features;

/**
 * HackApplication — 2026-compatible engine bootstrap.
 *
 * Fix summary:
 *  ✅  com.waxmoon.ma.gp.apk removed — it was the source of the Play Store redirect.
 *  ✅  HackLicense.inject() called before HackRuntime.install() so the engine
 *      validation branch reads the GITHUB key and skips the package-ID blocklist.
 *  ✅  Cross-context creation uses CONTEXT_INCLUDE_CODE for API 28+ compatibility.
 *  ✅  No hard crash if master package isn't installed in client process — graceful fallback.
 */
public class HackApplication extends Application {

    private static final boolean DEBUG = Features.DEBUG;
    private static final String  TAG   = HackApplication.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (DEBUG) Log.d(TAG, "attachBaseContext start pkg=" + base.getPackageName());

        Context engineContext = base;

        if (TextUtils.equals(base.getPackageName(), BuildConfig.MASTER_PACKAGE)) {
            // ── Master (host) process ────────────────────────────────────────────
            engineContext.getSharedPreferences("hack", Context.MODE_PRIVATE)
                    .edit()
                    .putString("sp.assist.pkg", BuildConfig.ASSIST_PACKAGE)
                    .commit();
        } else {
            // ── Cloned-app process ───────────────────────────────────────────────
            try {
                PackageManager pm = base.getPackageManager();
                int masterUid = pm.getPackageInfo(BuildConfig.MASTER_PACKAGE, 0)
                        .applicationInfo.uid;
                if (masterUid == Process.myUid()) {
                    engineContext = base.createPackageContext(
                            BuildConfig.MASTER_PACKAGE,
                            Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY
                    );
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "master package not installed — using local context");
            } catch (SecurityException se) {
                Log.w(TAG, "cross-context denied: " + se.getMessage());
            }
        }

        // CRITICAL: inject the GITHUB licence BEFORE install() so the engine's
        // validation path finds the key and doesn't redirect to the Play Store.
        HackLicense.inject(engineContext);

        // Extract and DexClassLoader-load hack.jar from assets.
        HackRuntime.install(engineContext, "version", true);

        // Signal engine to hook this Application.
        Cmd.INSTANCE().exec(CmdConstants.CMD_APPLICATION_ATTACHBASE, this, base);

        if (DEBUG) Log.d(TAG, "attachBaseContext end");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) Log.d(TAG, "onCreate");
        Cmd.INSTANCE().exec(CmdConstants.CMD_APPLICATION_ONCREATE);
    }
}
