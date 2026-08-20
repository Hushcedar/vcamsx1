package com.vcam.app.engine

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class VCamPrefsProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.vcam.app.preferences"
        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "get/*", 1)
        }

        fun readString(ctx: android.content.Context, key: String, def: String = ""): String {
            return try {
                val uri = Uri.parse("content://$AUTHORITY/get/$key")
                ctx.contentResolver.query(uri, null, null, null, null)?.use { c ->
                    if (c.moveToFirst()) c.getString(0) else def
                } ?: def
            } catch (e: Exception) { def }
        }

        fun readBoolean(ctx: android.content.Context, key: String, def: Boolean = false): Boolean =
            readString(ctx, key, def.toString()).toBoolean()

        fun readInt(ctx: android.content.Context, key: String, def: Int = 0): Int =
            readString(ctx, key, def.toString()).toIntOrNull() ?: def
    }

    override fun onCreate() = true

    override fun query(uri: Uri, p: Array<String>?, s: String?, sa: Array<String>?, so: String?): Cursor? {
        if (URI_MATCHER.match(uri) != 1) return null
        val key   = uri.lastPathSegment ?: return null
        val ctx   = context ?: return null
        val prefs = ctx.getSharedPreferences("vcam_prefs", android.content.Context.MODE_PRIVATE)
        val value = when {
            prefs.contains(key) -> when (val v = prefs.all[key]) {
                is Boolean -> v.toString()
                is Int     -> v.toString()
                is String  -> v
                else       -> ""
            }
            else -> ""
        }
        return MatrixCursor(arrayOf("value")).apply { addRow(arrayOf(value)) }
    }

    override fun getType(uri: Uri)                                             = "text/plain"
    override fun insert(uri: Uri, v: ContentValues?)                          = null
    override fun delete(uri: Uri, s: String?, sa: Array<String>?)             = 0
    override fun update(uri: Uri, v: ContentValues?, s: String?, sa: Array<String>?) = 0
}
