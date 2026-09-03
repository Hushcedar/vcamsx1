package com.wangyiheng.vcamsx.license

import android.util.Log
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

object SupabaseClient {

    // ── REPLACE THESE WITH YOUR REAL VALUES ─────────────────────────────────
    private const val BASE_URL = "https://dtecnszvepmdazskivqd.supabase.co/rest/v1"
    private const val ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImR0ZWNuc3p2ZXBtZGF6c2tpdnFkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODgyNzgxMzgsImV4cCI6MjEwMzg1NDEzOH0.DMyNhZINNiaKp4AczZv2xx-qioShot5Ds7IXlgRNeFA"
    // ────────────────────────────────────────────────────────────────────────

    private const val TABLE    = "vcamsx_users"
    private val executor       = Executors.newSingleThreadExecutor()
    private val iso            = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        .also { it.timeZone = TimeZone.getTimeZone("UTC") }

    // Called on key activation
    fun recordActivation(deviceId: String, keyHash: String, keyType: String, expiresAt: Date?) {
        executor.execute {
            try {
                val body = JSONObject().apply {
                    put("device_id",    deviceId)
                    put("key_hash",     keyHash)
                    put("key_type",     keyType)
                    put("activated_at", iso.format(Date()))
                    put("last_seen_at", iso.format(Date()))
                    put("expires_at",   if (expiresAt != null) iso.format(expiresAt) else JSONObject.NULL)
                    put("is_revoked",   false)
                    put("install_count", 1)
                }
                val resp = request(
                    "POST",
                    "$BASE_URL/$TABLE?on_conflict=device_id",
                    body.toString(),
                    mapOf("Prefer" to "resolution=merge-duplicates,return=representation")
                )
                Log.d("VCamSX-Supabase", "recordActivation response: $resp")
            } catch (e: Exception) {
                Log.e("VCamSX-Supabase", "recordActivation FAILED: ${e.message}")
            }
        }
    }

    // Called on every launch — updates last_seen, checks revoke status
    fun ping(deviceId: String, onRevoked: () -> Unit) {
        executor.execute {
            try {
                // Update last_seen
                val body = JSONObject().apply {
                    put("last_seen_at", iso.format(Date()))
                }
                request("PATCH", "$BASE_URL/$TABLE?device_id=eq.$deviceId", body.toString())

                // Check revoke status
                val resp = request("GET",
                    "$BASE_URL/$TABLE?device_id=eq.$deviceId&select=is_revoked", null)
                Log.d("VCamSX-Supabase", "ping response: $resp")

                if (resp?.contains("\"is_revoked\":true") == true) {
                    onRevoked()
                }
            } catch (e: Exception) {
                Log.e("VCamSX-Supabase", "ping FAILED: ${e.message}")
            }
        }
    }

    // Called when reinstall detected
    fun recordReinstall(deviceId: String) {
        executor.execute {
            try {
                val resp = request("GET",
                    "$BASE_URL/$TABLE?device_id=eq.$deviceId&select=install_count", null)
                val current = resp?.let {
                    Regex("\"install_count\":(\\d+)").find(it)?.groupValues?.get(1)?.toIntOrNull()
                } ?: 1

                val body = JSONObject().apply {
                    put("install_count", current + 1)
                    put("last_seen_at",  iso.format(Date()))
                }
                request("PATCH", "$BASE_URL/$TABLE?device_id=eq.$deviceId", body.toString())
                Log.d("VCamSX-Supabase", "reinstall recorded, count=${current + 1}")
            } catch (e: Exception) {
                Log.e("VCamSX-Supabase", "recordReinstall FAILED: ${e.message}")
            }
        }
    }

    private fun request(
        method:       String,
        url:          String,
        body:         String?,
        extraHeaders: Map<String, String> = emptyMap()
    ): String? {
        val conn = URL(url).openConnection() as HttpURLConnection
        return try {
            conn.requestMethod = method
            conn.setRequestProperty("apikey",        ANON_KEY)
            conn.setRequestProperty("Authorization", "Bearer $ANON_KEY")
            conn.setRequestProperty("Content-Type",  "application/json")
            conn.setRequestProperty("Prefer",        "return=minimal")
            extraHeaders.forEach { (k, v) -> conn.setRequestProperty(k, v) }
            conn.connectTimeout = 8000
            conn.readTimeout    = 8000

            if (body != null) {
                conn.doOutput = true
                OutputStreamWriter(conn.outputStream).use { it.write(body) }
            }

            val code   = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val result = stream?.bufferedReader()?.readText()
            Log.d("VCamSX-Supabase", "$method $url → $code")
            result
        } catch (e: Exception) {
            Log.e("VCamSX-Supabase", "request error: ${e.message}")
            null
        } finally {
            conn.disconnect()
        }
    }
}
