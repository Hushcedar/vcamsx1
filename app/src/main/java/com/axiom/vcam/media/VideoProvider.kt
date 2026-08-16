package com.axiom.vcam.media

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.File
import java.io.FileNotFoundException

/**
 * Serves copied_video.mp4 (and image-encoded MP4) to the hook module
 * running inside a cloned app process via BlackBox.
 *
 * URI: content://com.axiom.vcam.videoprovider
 */
class VideoProvider : ContentProvider() {

    companion object {
        private const val TAG = "VCamProvider"

        fun getVideoFile(context: android.content.Context): File =
            File(context.getExternalFilesDir(null) ?: context.filesDir, "copied_video.mp4")
    }

    override fun onCreate(): Boolean = true

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val ctx = context ?: throw FileNotFoundException("no context")
        val file = getVideoFile(ctx)
        if (!file.exists() || file.length() == 0L) {
            Log.e(TAG, "video file missing or empty: ${file.absolutePath}")
            throw FileNotFoundException("video not ready")
        }
        Log.d(TAG, "serving ${file.name} (${file.length()}b) to ${uri}")
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    override fun query(uri: Uri, p: Array<String>?, s: String?, a: Array<String>?, o: String?): Cursor? = null
    override fun getType(uri: Uri): String = "video/mp4"
    override fun insert(uri: Uri, v: ContentValues?): Uri? = null
    override fun delete(uri: Uri, s: String?, a: Array<String>?): Int = 0
    override fun update(uri: Uri, v: ContentValues?, s: String?, a: Array<String>?): Int = 0
}
