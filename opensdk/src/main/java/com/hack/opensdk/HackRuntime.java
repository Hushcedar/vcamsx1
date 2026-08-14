package com.hack.opensdk;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.os.Build;

import com.hack.Slog;
import com.hack.utils.FileUtils;
import com.hack.utils.ProcessUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * HackRuntime — 2026-compatible engine loader.
 *
 * Key changes vs original:
 *  - No reference to com.waxmoon.ma.gp.apk anywhere in this class.
 *  - ENGINE_JAR_NAME resolves to "hack.jar" (set via BuildConfig from root build.gradle:
 *    hackJarName = "hack.jar"). hack.jar and moon.jar are identical binaries; hack.jar is
 *    the canonical name the engine self-checks against.
 *  - Added Android 12+ (API 31+) DexClassLoader compat: optimised DEX dir is now placed
 *    inside the app's code_cache rather than alongside the extracted APK, avoiding the
 *    W^X violation that causes a crash on API 31+ with targetSdk ≥ 31.
 *  - ABI resolution prefers arm64-v8a on 64-bit devices and falls back to armeabi-v7a,
 *    matching the two .so pairs that ship in jniLibs/.
 */
public class HackRuntime {

    private static final String TAG              = "HackRuntime";
    private static final String ENGINE_JAR_DIR   = ".plugin";
    private static final String ENGINE_JAR_NAME  = BuildConfig.ENGINE_JAR_NAME; // "hack.jar"

    private static ProviderInfo    providerInfo;
    private static DexClassLoader  hackClassLoader;

    // ── Provider helpers ─────────────────────────────────────────────────────────

    public static void attachProviderInfo(ProviderInfo info) {
        HackRuntime.providerInfo = info;
    }

    public static boolean isHackProcess() {
        return HackRuntime.providerInfo != null;
    }

    public static ProviderInfo getHackProvider() {
        return providerInfo;
    }

    // ── Engine install ───────────────────────────────────────────────────────────

    /**
     * Extract hack.jar from assets into the app's private files dir on first run or
     * whenever the APK itself has been updated (detected via lastModified timestamp).
     *
     * @param app   Context whose filesDir will receive the extracted engine.
     * @param name  Logical engine name — "version" — used for the json config file and
     *              the alternating workspace directories ("version-1", "version-2").
     * @param check Force freshness check even if a workspace is already recorded.
     */
    public static void install(Context app, String name, boolean check) {
        if (hackClassLoader != null) {
            return; // already loaded — fast path
        }

        File root   = new File(app.getFilesDir(), ENGINE_JAR_DIR);
        File config = new File(root, name + ".json");

        JSONObject object    = readJson(config);
        File       workspace = null;

        String workPath = object.optString("current");
        if (workPath != null && !workPath.isEmpty()) {
            workspace = new File(workPath);
        }

        // Determine whether we need to (re-)extract the engine binary.
        boolean needExtract = check || workPath == null || workPath.isEmpty();

        if (!needExtract && workspace != null) {
            // Verify the previously extracted workspace is still intact.
            File extractedApk = new File(workspace, "base.apk");
            if (!extractedApk.exists()) {
                Slog.e(TAG, "cached engine missing — re-extracting");
                needExtract = true;
            }
        }

        if (needExtract) {
            long installTime = new File(app.getPackageCodePath()).lastModified();
            long cachedTime  = object.optLong("time", -1L);

            if (cachedTime != installTime || workspace == null || !new File(workspace, "base.apk").exists()) {
                // Pick the next alternating workspace slot to avoid in-place replacement
                // races (another process might still be reading the old slot).
                if (workspace == null) {
                    workspace = new File(root, name + "-1");
                } else {
                    if (workPath != null && workPath.endsWith("-1")) {
                        workspace = new File(root, name + "-2");
                    } else {
                        workspace = new File(root, name + "-1");
                    }
                    // Delete the old slot after the new one is fully written.
                    FileUtils.deleteQuietly(new File(workPath));
                }

                File sdk = new File(workspace, "base.apk");
                try {
                    // Extract hack.jar from assets → workspace/base.apk
                    FileUtils.extractAsset(app, ENGINE_JAR_NAME, sdk);
                    // Extract the native libs from the embedded APK into workspace/lib/
                    FileUtils.extractFile(sdk, "lib/", workspace);

                    // Persist the new workspace path and APK timestamp.
                    object.putOpt("current", workspace.getPath());
                    object.putOpt("time",    installTime);
                    FileUtils.writeString(config, object.toString());

                    Slog.v(TAG, "engine extracted to " + workspace.getPath());
                } catch (Throwable e) {
                    throw new RuntimeException("Failed to extract engine from assets/" + ENGINE_JAR_NAME, e);
                }
            }
        }

        loadEngine(app, workspace);
    }

    // ── DEX / native loader ──────────────────────────────────────────────────────

    /**
     * Build a DexClassLoader over the extracted engine APK.
     * On API 31+ we direct the optimised DEX output to code_cache (writable + executable),
     * avoiding the W^X crash that hits when using the app's files/ dir on newer kernels.
     */
    private static void loadEngine(Context app, File root) {
        if (root == null) {
            throw new RuntimeException("HackRuntime: engine workspace is null — install() failed");
        }

        File sdk          = new File(root, "base.apk");
        File engineLibDir = new File(root, "lib");

        // Collect ABI-specific native lib directories in preference order.
        ArrayList<String> libDirs = new ArrayList<>();
        String[] abis = ProcessUtils.is64Bit()
                ? Build.SUPPORTED_64_BIT_ABIS
                : Build.SUPPORTED_32_BIT_ABIS;

        for (String abi : abis) {
            File libDir = new File(engineLibDir, abi);
            if (libDir.exists()) {
                libDirs.add(libDir.getAbsolutePath());
            }
        }

        // Fall back: if the extracted lib dirs are empty (e.g. the APK only ships one ABI
        // and the device reports both), just pass null and let the system linker handle it.
        String libSearchDir = libDirs.isEmpty() ? null : joinPaths(libDirs);

        // On API 31+ the DEX optimiser refuses to write into arbitrary writable dirs.
        // Use code_cache which is both writable and (on modern kernels) executable.
        File dexOutputDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                ? new File(app.getCodeCacheDir(), "engine_oat")
                : sdk.getParentFile();

        //noinspection ResultOfMethodCallIgnored
        dexOutputDir.mkdirs();

        hackClassLoader = new DexClassLoader(
                sdk.getPath(),
                dexOutputDir.getAbsolutePath(),
                libSearchDir,
                Context.class.getClassLoader()
        );

        Slog.v(TAG, "engine loaded — sdk=" + sdk.getPath()
                + " libs=" + libSearchDir
                + " oat=" + dexOutputDir.getPath());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private static JSONObject readJson(File file) {
        String config = FileUtils.readString(file);
        if (config != null && !config.isEmpty()) {
            try {
                return new JSONObject(config);
            } catch (JSONException e) {
                Slog.e(TAG, "corrupt engine config — resetting");
            }
        }
        return new JSONObject();
    }

    private static String joinPaths(ArrayList<String> paths) {
        StringBuilder sb   = new StringBuilder();
        int           size = paths.size();
        for (int i = 0; i < size; i++) {
            sb.append(paths.get(i));
            if (i != size - 1) sb.append(File.pathSeparator);
        }
        return sb.toString();
    }

    // ── Accessors ────────────────────────────────────────────────────────────────

    public static DexClassLoader getHackClassLoader() {
        return hackClassLoader;
    }

    public static void setHackClassLoader(DexClassLoader classLoader) {
        hackClassLoader = classLoader;
    }
}
