package com.wangyiheng.vcamsx.license

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.Date

object LicenseManager {

    private const val PREFS         = "vcamsx_lic"
    private const val KEY_LICENSE   = "k"
    private const val KEY_CHEATER   = "c"
    private const val KEY_LAST_MIN  = "m"
    private const val KEY_USED_KEYS = "u"
    private const val SECRET_SALT   = "VCamSX_S3cr3t_2026_!@#"
    private const val V2_SUFFIX     = "_v2"

    private val SUPPORTED_DURATIONS = listOf(5, 1440, 2880, 4320, 10080, 20160, 43200, 129600, 525600)
    private const val SCAN_WINDOW   = 4320

    private fun nowMin(): Int = (System.currentTimeMillis() / 60_000L).toInt()

    fun init(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return
        val now     = nowMin()
        val lastMin = prefs.getInt(KEY_LAST_MIN, now)
        if (now < lastMin - 2) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply(); return
        }
        val ntpMin = getNtpMin()
        if (ntpMin != null && ntpMin > lastMin && now < lastMin) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply(); return
        }
        prefs.edit().putInt(KEY_LAST_MIN, maxOf(now, ntpMin ?: now)).apply()
    }

    fun getStatus(ctx: Context): LicenseStatus {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return LicenseStatus.CHEATER
        val savedKey = prefs.getString(KEY_LICENSE, null)
        if (savedKey != null) {
            when (val r = verifyKey(ctx, savedKey)) {
                is VerifyResult.Permanent -> return LicenseStatus.LICENSED_PERMANENT
                is VerifyResult.Trial -> {
                    val lastMin   = prefs.getInt(KEY_LAST_MIN, nowMin())
                    val effective = maxOf(nowMin(), lastMin)
                    val elapsed   = effective - r.issuedMin
                    val minsLeft  = r.durationMin - elapsed
                    return if (minsLeft > 0) LicenseStatus.TRIAL(minsLeft) else LicenseStatus.EXPIRED
                }
                is VerifyResult.Invalid -> {}
            }
        }
        return LicenseStatus.FRESH
    }

    fun isActive(ctx: Context): Boolean = when (getStatus(ctx)) {
        is LicenseStatus.TRIAL,
        LicenseStatus.LICENSED_PERMANENT,
        LicenseStatus.LICENSED_EXTENDED -> true
        else -> false
    }

    fun activateKey(ctx: Context, key: String): KeyResult {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return KeyResult.INVALID

        if (!isOnline(ctx)) return KeyResult.NO_INTERNET

        val clean    = key.trim().uppercase().replace("-", "")
        val usedKeys = prefs.getString(KEY_USED_KEYS, "") ?: ""
        val keyHash  = sha256(clean).take(12)

        if (isOldFormatKey(ctx, clean)) return KeyResult.OLD_FORMAT

        if (usedKeys.split("|").contains(keyHash)) return KeyResult.ALREADY_USED

        val supabaseCheck = SupabaseClient.checkKeyHash(keyHash)
        if (supabaseCheck == SupabaseClient.KeyHashResult.USED_OTHER_DEVICE) return KeyResult.ALREADY_USED

        return when (val r = verifyKey(ctx, clean)) {
            is VerifyResult.Permanent -> {
                markUsed(prefs, usedKeys, keyHash)
                prefs.edit().putString(KEY_LICENSE, clean).apply()
                SupabaseClient.recordActivation(getDeviceId(ctx), keyHash, "permanent", null)
                KeyResult.VALID_PERMANENT
            }
            is VerifyResult.Trial -> {
                val elapsed = nowMin() - r.issuedMin
                if (elapsed >= r.durationMin) return KeyResult.EXPIRED
                val minsLeft  = r.durationMin - elapsed
                val expiresAt = Date(System.currentTimeMillis() + minsLeft * 60_000L)
                markUsed(prefs, usedKeys, keyHash)
                prefs.edit().putString(KEY_LICENSE, clean).apply()
                SupabaseClient.recordActivation(getDeviceId(ctx), keyHash, "trial", expiresAt)
                KeyResult.VALID_TRIAL
            }
            is VerifyResult.Invalid -> KeyResult.INVALID
        }
    }

    private fun markUsed(prefs: SharedPreferences, existing: String, keyHash: String) {
        val updated = if (existing.isEmpty()) keyHash else "$existing|$keyHash"
        prefs.edit().putString(KEY_USED_KEYS, updated).apply()
    }

    private sealed class VerifyResult {
        object Permanent : VerifyResult()
        data class Trial(val issuedMin: Int, val durationMin: Int) : VerifyResult()
        object Invalid : VerifyResult()
    }

    private fun verifyKey(ctx: Context, key: String): VerifyResult {
        val clean    = key.replace("-", "").uppercase()
        val deviceId = getDeviceId(ctx)

        if (clean == sha256("$SECRET_SALT${deviceId}PERM$V2_SUFFIX").take(16).uppercase())
            return VerifyResult.Permanent

        if (clean == sha256("${SECRET_SALT}UNIVERSAL$V2_SUFFIX").take(16).uppercase())
            return VerifyResult.Permanent

        val now = nowMin()
        for (ago in 0..SCAN_WINDOW) {
            val issueMin = now - ago
            for (dur in SUPPORTED_DURATIONS) {
                val expected = sha256("$SECRET_SALT${deviceId}TRIAL${issueMin}_${dur}$V2_SUFFIX")
                    .take(16).uppercase()
                if (clean == expected) return VerifyResult.Trial(issueMin, dur)
            }
        }

        return VerifyResult.Invalid
    }

    private fun isOldFormatKey(ctx: Context, clean: String): Boolean {
        val deviceId = getDeviceId(ctx)
        val now = nowMin()

        if (clean == sha256("$SECRET_SALT${deviceId}PERM").take(16).uppercase()) return true
        if (clean == sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase()) return true
        for (ago in 0..10) {
            val issueMin = now - ago
            for (dur in SUPPORTED_DURATIONS) {
                val v1 = sha256("$SECRET_SALT${deviceId}TRIAL${issueMin}_$dur").take(16).uppercase()
                if (clean == v1) return true
            }
        }
        return false
    }

    private fun isOnline(ctx: Context): Boolean {
        // Step 1: must have an active network with internet capability
        val cm   = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val net  = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(net) ?: return false
        if (!caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) return false
        // Note: NET_CAPABILITY_VALIDATED is intentionally NOT required —
        // many carriers and ROMs don't reliably set it.

        // Step 2: HTTP probe — any response (200, 204, 301…) counts as online.
        // Only timeout / DNS failure / no-route = truly offline.
        val probeUrls = listOf(
            "https://www.google.com/generate_204",
            "https://www.cloudflare.com/cdn-cgi/trace"
        )
        for (urlStr in probeUrls) {
            try {
                val conn = java.net.URL(urlStr).openConnection() as java.net.HttpURLConnection
                conn.connectTimeout = 8000
                conn.readTimeout    = 8000
                conn.requestMethod  = "GET"
                conn.instanceFollowRedirects = false
                conn.connect()
                val code = conn.responseCode
                conn.disconnect()
                if (code > 0) return true
            } catch (_: Exception) { }
        }

        // Step 3: DNS fallback
        return try {
            java.net.InetAddress.getByName("8.8.8.8") != null
        } catch (_: Exception) { false }
    }

    private fun getNtpMin(): Int? = try {
        val socket = DatagramSocket().also { it.soTimeout = 3000 }
        val buf    = ByteArray(48).also { it[0] = 0x1B.toByte() }
        val addr   = InetAddress.getByName("time.google.com")
        socket.send(DatagramPacket(buf, buf.size, addr, 123))
        val resp   = DatagramPacket(ByteArray(48), 48)
        socket.receive(resp); socket.close()
        val secs   = ByteBuffer.wrap(resp.data, 40, 4).int.toLong() and 0xFFFFFFFFL
        val unixMs = (secs - 2208988800L) * 1000L
        (unixMs / 60_000L).toInt()
    } catch (e: Exception) { null }

    fun getDeviceId(ctx: Context): String =
        Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
            ?.uppercase() ?: "UNKNOWN"

    private fun sha256(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }

    fun generateTrialKey(deviceId: String, durationMin: Int, minsAgo: Int = 0): String {
        val issuedMin = nowMin() - minsAgo
        val h = sha256("$SECRET_SALT${deviceId.uppercase()}TRIAL${issuedMin}_${durationMin}$V2_SUFFIX").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }

    fun generatePermKey(deviceId: String): String {
        val h = sha256("$SECRET_SALT${deviceId.uppercase()}PERM$V2_SUFFIX").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }

    fun generateUniversalKey(): String {
        val h = sha256("${SECRET_SALT}UNIVERSAL$V2_SUFFIX").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }
}

sealed class LicenseStatus {
    object FRESH                        : LicenseStatus()
    data class TRIAL(val minsLeft: Int) : LicenseStatus()
    object EXPIRED                      : LicenseStatus()
    object LICENSED_PERMANENT           : LicenseStatus()
    object LICENSED_EXTENDED            : LicenseStatus()
    object CHEATER                      : LicenseStatus()
}

enum class KeyResult {
    VALID_PERMANENT,
    VALID_TRIAL,
    ALREADY_USED,
    EXPIRED,
    INVALID,
    OLD_FORMAT,
    NO_INTERNET
}
