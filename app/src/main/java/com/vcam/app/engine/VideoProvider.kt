package com.vcam.app.engine

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.File

class VideoProvider : ContentProvider() {
    companion object {
        private const val TAG = "VCam-Provider"
    }

    override fun onCreate() = true

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        return try {
            val ctx = context ?: return null
            val prefs = ctx.getSharedPreferences("vcam_prefs", android.content.Context.MODE_PRIVATE)
            val path  = prefs.getString("video_path_final", "") ?: ""

            if (path.isBlank() || !File(path).exists()) {
                Log.w(TAG, "No video file at: $path"); return null
            }
            ParcelFileDescriptor.open(File(path), ParcelFileDescriptor.MODE_READ_ONLY)
        } catch (e: Exception) {
            Log.e(TAG, "openFile: ${e.message}"); null
        }
    }

    override fun query(uri: Uri, p: Array<String>?, s: String?, sa: Array<String>?, so: String?): Cursor? = null
    override fun getType(uri: Uri): String = "video/mp4"
    override fun insert(uri: Uri, v: ContentValues?): Uri? = null
    override fun delete(uri: Uri, s: String?, sa: Array<String>?) = 0
    override fun update(uri: Uri, v: ContentValues?, s: String?, sa: Array<String>?) = 0
}
