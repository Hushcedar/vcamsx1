package com.wangyiheng.vcamsx.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import com.wangyiheng.vcamsx.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Serves the virtual camera video file to IjkMediaPlayer running
 * inside the hooked target process.
 *
 * Video priority:
 *   1. /sdcard/Android/data/com.wangyiheng.vcamsx/files/vcamsx_video.mp4  (user-placed)
 *   2. copied_video.mp4 copied there by HomeController when user picks a video
 *   3. R.raw.vcamsx bundled in the APK (last resort fallback)
 */
class VideoProvider : ContentProvider() {

    companion object {
        private const val TAG             = "VCamSX-Provider"
        private const val USER_VIDEO      = "vcamsx_video.mp4"
        private const val COPIED_VIDEO    = "copied_video.mp4"
    }

    override fun onCreate(): Boolean = true

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val ctx        = context ?: return null
        val externalDir = ctx.getExternalFilesDir(null) ?: ctx.filesDir

        // 1. User-placed video (highest priority)
        val userVideo = File(externalDir, USER_VIDEO)
        if (userVideo.exists() && userVideo.length() > 0) {
            Log.d(TAG, "Serving user video: ${userVideo.absolutePath}")
            return ParcelFileDescriptor.open(userVideo, ParcelFileDescriptor.MODE_READ_ONLY)
        }

        // 2. Video copied by HomeController when user picks via picker
        val copiedVideo = File(externalDir, COPIED_VIDEO)
        if (copiedVideo.exists() && copiedVideo.length() > 0) {
            Log.d(TAG, "Serving copied video: ${copiedVideo.absolutePath}")
            return ParcelFileDescriptor.open(copiedVideo, ParcelFileDescriptor.MODE_READ_ONLY)
        }

        // 3. Bundled raw resource fallback
        val bundled = File(externalDir, "bundled_default.mp4")
        if (!bundled.exists() || bundled.length() == 0L) {
            if (!copyRawResource(bundled)) {
                Log.e(TAG, "No video source available")
                return null
            }
        }
        Log.d(TAG, "Serving bundled default: ${bundled.absolutePath}")
        return ParcelFileDescriptor.open(bundled, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    private fun copyRawResource(dest: File): Boolean {
        return try {
            context!!.resources.openRawResource(R.raw.vcamsx).use { input ->
                FileOutputStream(dest).use { output -> input.copyTo(output) }
            }
            true
        } catch (e: IOException) {
            Log.e(TAG, "copyRawResource IOException: ${e.message}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "copyRawResource error (raw resource missing?): ${e.message}")
            false
        }
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        val cursor = MatrixCursor(arrayOf("_id", "display_name", "size", "date_modified", "file"))
        val dir    = context?.getExternalFilesDir(null) ?: return cursor
        val file   = sequenceOf(USER_VIDEO, COPIED_VIDEO)
            .map { File(dir, it) }
            .firstOrNull { it.exists() } ?: return cursor
        cursor.addRow(arrayOf(0, file.name, file.length(), file.lastModified(), file.absolutePath))
        return cursor
    }

    override fun getType(uri: Uri): String = "video/mp4"
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
