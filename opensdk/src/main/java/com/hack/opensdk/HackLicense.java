package com.hack.opensdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * HackLicense — writes the GITHUB-tier WaxMoon engine licence into SharedPreferences
 * BEFORE the engine's CMD_APPLICATION_ATTACHBASE call reads it.
 *
 * Why this is needed:
 *   The MultiApp engine (hack.jar) validates the calling application against its
 *   licence database during initialisation. The Google Play build (com.waxmoon.ma.gp.apk,
 *   now removed) checks if the host package ID is in a GP-approved list; if not it fires
 *   a market:// intent — that's the "Please use Official app" redirect.
 *
 *   The GITHUB build (hack.jar / moon.jar) uses a different validation path:
 *   it reads two SharedPreferences keys from the "engine_license" prefs file:
 *     - "engine.license.appId"  →  "GITHUB"
 *     - "engine.license.key"    →  "WMMYOPLTIU2q3k"
 *   When both match the values embedded in assets/license.txt the engine skips
 *   the package-ID check entirely and proceeds with full functionality.
 *
 * Usage:
 *   Call HackLicense.inject(context) inside HackApplication.attachBaseContext()
 *   BEFORE HackRuntime.install() is called.
 */
public class HackLicense {

    private static final String TAG      = "HackLicense";
    private static final String PREFS    = "engine_license";
    private static final String APP_ID   = "GITHUB";
    private static final String KEY      = "WMMYOPLTIU2q3k";

    private static volatile boolean sInjected = false;

    /**
     * Idempotent — safe to call multiple times; the write only happens once per process.
     */
    public static void inject(Context context) {
        if (sInjected) return;
        synchronized (HackLicense.class) {
            if (sInjected) return;
            try {
                SharedPreferences prefs = context
                        .getApplicationContext()
                        .getSharedPreferences(PREFS, Context.MODE_PRIVATE);

                SharedPreferences.Editor ed = prefs.edit();
                ed.putString("engine.license.appId", APP_ID);
                ed.putString("engine.license.key",   KEY);
                // commit() (sync) on purpose — engine reads these immediately after this call
                ed.commit();

                sInjected = true;
                Log.d(TAG, "licence injected — AppId=" + APP_ID);
            } catch (Throwable t) {
                Log.e(TAG, "licence injection failed: " + t.getMessage());
                // Non-fatal: engine may still work if it falls through to the embedded key
            }
        }
    }
}
