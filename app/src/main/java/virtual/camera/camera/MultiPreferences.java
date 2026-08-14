package virtual.camera.camera;

import android.content.Context;
import android.content.SharedPreferences;
import virtual.camera.app.app.App;

/**
 * Stub replacement for the original virtual.camera.camera.MultiPreferences
 * from the now-dead camera AAR library.
 */
public class MultiPreferences {

    private static final String PREFS_NAME = "vcamera_multi_prefs";
    private static MultiPreferences instance;
    private final SharedPreferences prefs;

    private MultiPreferences() {
        prefs = App.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized MultiPreferences getInstance() {
        if (instance == null) instance = new MultiPreferences();
        return instance;
    }

    public void setInt(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public void setString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }
}
