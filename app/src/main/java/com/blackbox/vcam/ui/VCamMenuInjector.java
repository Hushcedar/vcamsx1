package com.blackbox.vcam.ui;

import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.blackbox.vcam.hook.VCamBlackBoxPlugin;
import com.blackbox.vcam.util.VCamLogger;

/**
 * VCamMenuInjector
 *
 * Injects "Virtual Camera" as a menu item into BlackBox's per-app context menu
 * (the three-dot popup that appears when you long-press a cloned app card).
 *
 * Usage from BlackBox's app adapter / host Activity:
 *
 *   // In your RecyclerView adapter's onCreateContextMenu or the host fragment:
 *   VCamMenuInjector.inject(menu, packageName, activity, plugin);
 *
 *   // In onContextItemSelected / onOptionsItemSelected:
 *   VCamMenuInjector.handleSelection(item, packageName, fragmentManager, plugin);
 *
 * The injector adds one menu item with a camera icon and the label
 * "Virtual Camera". Tapping it opens BottomSheetVCamMenu for that app.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * Integration into BlackBox source:
 *
 * BlackBox's app-item long-press menu lives in:
 *   app/src/main/java/com/lody/virtual/client/stub/...  (varies by fork)
 *   or in the host Activity that manages the installed-apps RecyclerView.
 *
 * Find the method that builds the PopupMenu / ContextMenu for each app card
 * and call VCamMenuInjector.inject() there.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class VCamMenuInjector {

    private VCamMenuInjector() {}

    public static final int MENU_ID_VCAM = 0x7A1C; // arbitrary unique ID

    private static final String TAG = "VCam_Injector";

    /**
     * Inject the "Virtual Camera" item into an existing PopupMenu or ContextMenu.
     *
     * @param menu        The Menu to inject into
     * @param packageName The guest package this menu item will target
     * @param plugin      The active VCamBlackBoxPlugin instance
     */
    public static void inject(Menu menu, String packageName, VCamBlackBoxPlugin plugin) {
        if (menu == null || packageName == null || plugin == null) return;

        MenuItem item = menu.add(Menu.NONE, MENU_ID_VCAM, Menu.NONE, "Virtual Camera");
        item.setIcon(android.R.drawable.ic_menu_camera);
        VCamLogger.d(TAG, "VCam menu item injected for: " + packageName);
    }

    /**
     * Handle selection of the injected menu item.
     *
     * @return true if the item was handled, false otherwise
     */
    public static boolean handleSelection(MenuItem item, String packageName,
                                          FragmentActivity activity,
                                          VCamBlackBoxPlugin plugin) {
        if (item.getItemId() == MENU_ID_VCAM) {
            VCamLogger.d(TAG, "VCam menu selected for: " + packageName);
            BottomSheetVCamMenu.show(activity.getSupportFragmentManager(), packageName, plugin);
            return true;
        }
        return false;
    }

    /**
     * Convenience: attach to a View as a context menu creator.
     * Call this in the RecyclerView adapter's onBindViewHolder for the three-dot button.
     *
     * @param dotMenuButton  The three-dot ImageButton on each app card
     * @param packageName    Package of the guest app this card represents
     * @param activity       Host FragmentActivity
     * @param plugin         Active plugin
     */
    public static void attachToAppCard(View dotMenuButton, String packageName,
                                       FragmentActivity activity, VCamBlackBoxPlugin plugin) {
        dotMenuButton.setOnCreateContextMenuListener((menu, v, menuInfo) ->
                inject(menu, packageName, plugin));

        // Also handle direct click → show popup immediately
        dotMenuButton.setOnClickListener(v -> {
            android.widget.PopupMenu popup = new android.widget.PopupMenu(activity, v);
            inject(popup.getMenu(), packageName, plugin);

            // Inject BlackBox's existing items before VCam
            // (The host Activity should call its own injectDefaultItems here)

            popup.setOnMenuItemClickListener(item ->
                    handleSelection(item, packageName, activity, plugin));
            popup.show();
        });

        VCamLogger.d(TAG, "VCam attached to card for: " + packageName);
    }
}
