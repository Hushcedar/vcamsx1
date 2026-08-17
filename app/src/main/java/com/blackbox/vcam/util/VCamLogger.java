package com.blackbox.vcam.util;

import android.util.Log;

public final class VCamLogger {
    private static final boolean DEBUG = true; // set false for release
    private VCamLogger() {}

    public static void d(String tag, String msg) { if (DEBUG) Log.d(tag, msg); }
    public static void w(String tag, String msg) { Log.w(tag, msg); }
    public static void e(String tag, String msg) { Log.e(tag, msg); }
    public static void i(String tag, String msg) { Log.i(tag, msg); }
}
