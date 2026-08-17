package com.blackbox.vcam.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.blackbox.vcam.VCamApplication;
import com.blackbox.vcam.camera.VirtualCameraEngine;
import com.blackbox.vcam.model.VCamConfig;
import com.blackbox.vcam.util.VCamLogger;

/**
 * VCamForegroundService
 *
 * Keeps the VirtualCameraEngine alive as a foreground service while
 * a BlackBox guest app with virtual camera enabled is running.
 *
 * Started by VCamBlackBoxPlugin.onAppStarted() when the config is active.
 * Stopped by VCamBlackBoxPlugin.onAppStopped().
 *
 * Extra: ACTION_STOP intent allows the notification "Stop" action to kill
 * the service without needing to open the app.
 */
public class VCamForegroundService extends Service {

    private static final String TAG              = "VCam_Service";
    private static final String CHANNEL_ID       = "vcam_channel";
    private static final int    NOTIF_ID         = 0x1CAM;

    public static final String ACTION_START      = "com.blackbox.vcam.START";
    public static final String ACTION_STOP       = "com.blackbox.vcam.STOP";
    public static final String EXTRA_PACKAGE     = "guest_package";

    // ── Service lifecycle ──────────────────────────────────────────────────────

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        VCamLogger.d(TAG, "VCamForegroundService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_NOT_STICKY;

        String action = intent.getAction();
        if (ACTION_STOP.equals(action)) {
            VCamLogger.d(TAG, "Stop action received");
            stopSelf();
            return START_NOT_STICKY;
        }

        String guestPackage = intent.getStringExtra(EXTRA_PACKAGE);
        VCamLogger.d(TAG, "Starting foreground for: " + guestPackage);

        // Promote to foreground with notification
        startForeground(NOTIF_ID, buildNotification(guestPackage));

        // Ensure engine is running with the correct config
        if (guestPackage != null && VCamApplication.get() != null) {
            VCamConfig cfg = VCamApplication.get().getPlugin().getVCamConfig(guestPackage);
            VirtualCameraEngine.getInstance(this).applyConfig(cfg);
        }

        return START_STICKY; // restart if killed by OOM
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VCamLogger.d(TAG, "VCamForegroundService destroyed — stopping engine");
        VirtualCameraEngine.getInstance(this).applyConfig(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ── Notification ───────────────────────────────────────────────────────────

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Virtual Camera",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Virtual camera is active for a cloned app");
            channel.setShowBadge(false);
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification(String guestPackage) {
        Intent stopIntent = new Intent(this, VCamForegroundService.class);
        stopIntent.setAction(ACTION_STOP);
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? PendingIntent.FLAG_IMMUTABLE : 0;
        PendingIntent stopPi = PendingIntent.getService(this, 0, stopIntent, flags);

        String contentText = guestPackage != null
                ? "Virtual camera active — " + guestPackage
                : "Virtual camera is running";

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("VCam Active")
                .setContentText(contentText)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_pause, "Stop",  stopPi)
                .build();
    }

    // ── Static helpers ─────────────────────────────────────────────────────────

    public static void start(Context context, String guestPackage) {
        Intent intent = new Intent(context, VCamForegroundService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(EXTRA_PACKAGE, guestPackage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, VCamForegroundService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }
}
