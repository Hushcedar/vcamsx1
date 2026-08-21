package virtual.camera.app.view.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import virtual.camera.app.R;
import virtual.camera.app.app.AppManager;

public class DialogUtil {

    private static final String KEY_NEVER_SHOW = "tips_never_show";

    public static void showTipsDialog(Activity activity) {
        if (AppManager.INSTANCE.getMSharedPreferences()
                .getBoolean(KEY_NEVER_SHOW, false)) return;

        new AlertDialog.Builder(activity)
                .setTitle(R.string.tips)
                .setMessage(R.string.tips_content)
                .setNeutralButton(R.string.never_show, (dialog, which) -> {
                    // Fix: save never show pref safely
                    try {
                        AppManager.INSTANCE.getMSharedPreferences()
                                .edit().putBoolean(KEY_NEVER_SHOW, true).apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    // Fix: just dismiss — no crash
                    dialog.dismiss();
                })
                .setPositiveButton(R.string.go_to, (dialog, which) -> {
                    // Fix: wrap in try/catch + FLAG_ACTIVITY_NEW_TASK
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/andvipgroup/VCamera"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
}
