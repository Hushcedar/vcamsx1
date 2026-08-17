package com.blackbox.vcam.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.blackbox.vcam.model.VCamConfig;
import com.blackbox.vcam.model.VideoSourceType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * VCamPreferences
 *
 * Thin persistence layer for VCamConfig objects, keyed by guest package name.
 * Uses Gson serialization into a single SharedPreferences file.
 * Mirrors VCamera's InfoManager / RemotePreferences approach but simplified
 * since we operate in-process within BlackBox's host app.
 */
public class VCamPreferences {

    private static final String PREFS_NAME   = "vcam_blackbox_prefs";
    private static final String KEY_CONFIGS  = "guest_configs_map";

    private final SharedPreferences mPrefs;
    private final Gson              mGson;

    public VCamPreferences(Context context) {
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mGson  = new Gson();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Save / Load single config
    // ──────────────────────────────────────────────────────────────────────────

    public void saveConfig(String packageName, VCamConfig config) {
        Map<String, VCamConfig> all = loadAllConfigs();
        all.put(packageName, config);
        mPrefs.edit()
              .putString(KEY_CONFIGS, mGson.toJson(all))
              .apply();
    }

    public VCamConfig loadConfig(String packageName) {
        Map<String, VCamConfig> all = loadAllConfigs();
        return all.get(packageName);
    }

    public void removeConfig(String packageName) {
        Map<String, VCamConfig> all = loadAllConfigs();
        all.remove(packageName);
        mPrefs.edit()
              .putString(KEY_CONFIGS, mGson.toJson(all))
              .apply();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Load all configs
    // ──────────────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public Map<String, VCamConfig> loadAllConfigs() {
        String json = mPrefs.getString(KEY_CONFIGS, null);
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }
        try {
            Type type = new TypeToken<Map<String, VCamConfig>>(){}.getType();
            Map<String, VCamConfig> result = mGson.fromJson(json, type);
            return result != null ? result : new HashMap<>();
        } catch (Exception e) {
            VCamLogger.e("VCamPreferences", "Failed to parse configs: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Convenience helpers
    // ──────────────────────────────────────────────────────────────────────────

    /** Returns true if virtual camera is currently enabled for this guest package. */
    public boolean isEnabledForPackage(String packageName) {
        VCamConfig cfg = loadConfig(packageName);
        return cfg != null && cfg.isEnabled();
    }

    /** Quick toggle without loading/re-saving full config. */
    public void setEnabled(String packageName, boolean enabled) {
        VCamConfig cfg = loadConfig(packageName);
        if (cfg == null) {
            cfg = new VCamConfig();
            cfg.setGuestPackage(packageName);
        }
        cfg.setEnabled(enabled);
        saveConfig(packageName, cfg);
    }
}
