package com.blackbox.vcam.camera;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.blackbox.vcam.VCamApplication;
import com.blackbox.vcam.model.VCamConfig;
import com.blackbox.vcam.util.VCamLogger;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * VCamContentProvider
 *
 * Exposes the currently selected virtual camera video file to BlackBox guest
 * processes through a content:// URI. Mirrors VCamera's VideoProvider pattern.
 *
 * Guest apps (running inside BlackBox's virtual environment) cannot directly
 * access the host app's private files. By routing through a ContentProvider
 * we give them a stable, permission-gated URI to the video.
 *
 * URI scheme:
 *   content://com.blackbox.vcam.provider/video/{packageName}
 *
 * The VirtualCameraEngine reads back the video URI via this provider when
 * it needs to re-initialize the MediaPlayer after a process restart.
 */
public class VCamContentProvider extends ContentProvider {

    private static final String TAG      = "VCam_Provider";
    public  static final String AUTHORITY = "com.blackbox.vcam.provider";

    // ── ContentProvider lifecycle ──────────────────────────────────────────────

    @Override
    public boolean onCreate() {
        VCamLogger.d(TAG, "VCamContentProvider created");
        return true;
    }

    // ── openFile: the important one ────────────────────────────────────────────

    /**
     * Opens the video file for a given guest package.
     * URI path: /video/{packageName}
     */
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        String packageName = extractPackage(uri);
        if (packageName == null) {
            throw new FileNotFoundException("No package in URI: " + uri);
        }

        VCamApplication app = VCamApplication.get();
        if (app == null || app.getPlugin() == null) {
            throw new FileNotFoundException("VCamApplication not ready");
        }

        VCamConfig cfg = app.getPlugin().getVCamConfig(packageName);
        if (cfg == null || !cfg.isEnabled() || cfg.getVideoUri() == null) {
            throw new FileNotFoundException("No active VCam config for: " + packageName);
        }

        String videoUriStr = cfg.getVideoUri();
        Uri videoUri = Uri.parse(videoUriStr);

        try {
            Context ctx = getContext();
            if (ctx == null) throw new FileNotFoundException("No context");
            ParcelFileDescriptor pfd = ctx.getContentResolver().openFileDescriptor(videoUri, mode);
            VCamLogger.d(TAG, "openFile OK for " + packageName + " -> " + videoUriStr);
            return pfd;
        } catch (Exception e) {
            VCamLogger.e(TAG, "openFile failed: " + e.getMessage());
            throw new FileNotFoundException("Cannot open video: " + e.getMessage());
        }
    }

    // ── Query: used by engine to retrieve video metadata ─────────────────────

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String packageName = extractPackage(uri);
        String[] cols = {"package", "video_uri", "enabled"};
        MatrixCursor cursor = new MatrixCursor(cols);

        if (packageName != null && VCamApplication.get() != null) {
            VCamConfig cfg = VCamApplication.get().getPlugin().getVCamConfig(packageName);
            if (cfg != null) {
                cursor.addRow(new Object[]{
                        packageName,
                        cfg.getVideoUri() != null ? cfg.getVideoUri() : "",
                        cfg.isEnabled() ? 1 : 0
                });
            }
        }
        return cursor;
    }

    // ── Stub implementations ──────────────────────────────────────────────────

    @Override public String  getType(Uri uri)                                                { return "video/*"; }
    @Override public Uri     insert(Uri uri, ContentValues values)                            { return null; }
    @Override public int     delete(Uri uri, String sel, String[] args)                      { return 0; }
    @Override public int     update(Uri uri, ContentValues v, String sel, String[] args)     { return 0; }

    // ── Helper ────────────────────────────────────────────────────────────────

    private String extractPackage(Uri uri) {
        if (uri == null) return null;
        java.util.List<String> segs = uri.getPathSegments();
        // Path: /video/{packageName}  →  segments: ["video", "com.example.app"]
        if (segs != null && segs.size() >= 2 && "video".equals(segs.get(0))) {
            return segs.get(1);
        }
        return null;
    }

    /** Build the content URI for a given guest package */
    public static Uri buildUri(String packageName) {
        return Uri.parse("content://" + AUTHORITY + "/video/" + packageName);
    }
}
