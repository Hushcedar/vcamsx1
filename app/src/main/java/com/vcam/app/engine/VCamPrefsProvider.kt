package com.vcam.app.engine

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

class VCamPrefsProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.vcam.app.preferences"
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "get/*", 1)
        }
    }

    override fun onCreate() = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        if (MATCHER.match(uri) != 1) return null
        val key   = uri.lastPathSegment ?: return null
        val ctx   = context ?: return null
        val prefs = ctx.getSharedPreferences("vcam_prefs", android.content.Context.MODE_PRIVATE)
        val value = when (val v = prefs.all[key]) {
            is Boolean -> v.toString()
            is Int     -> v.toString()
            is String  -> v
            else       -> ""
        }
        Log.d("VCam-PrefsProvider", "query key=$key value=$value")
        return MatrixCursor(arrayOf("value")).apply { addRow(arrayOf(value)) }
    }

    override fun getType(uri: Uri)                                                    = "text/plain"
    override fun insert(uri: Uri, values: ContentValues?)                            = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0
    override fun update(uri: Uri, values: ContentValues?, s: String?, sa: Array<String>?) = 0
}
