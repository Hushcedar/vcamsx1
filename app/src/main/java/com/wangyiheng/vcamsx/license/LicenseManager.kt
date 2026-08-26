package com.wangyiheng.vcamsx.license

import android.content.Context
import android.provider.Settings
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.security.MessageDigest

object LicenseManager {

    private const val PREFS          = "vcamsx_lic"
    private const val KEY_LICENSE    = "k"
    private const val KEY_CHEATER    = "c"
    private const val KEY_LAST_MIN   = "m"
    private const val KEY_USED_KEYS  = "u"
    private const val SECRET_SALT    = "VCamSX_S3cr3t_2026_!@#"

    // Change TRIAL_MINUTES to 10080 (7 days) for production
    private const val TRIAL_MINUTES  = 5
    private const val SCAN_WINDOW    = 120

    private fun nowMin(): Int = (System.currentTimeMillis() / 60_000L).toInt()

    fun init(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return

        val now     = nowMin()
        val lastMin = prefs.getInt(KEY_LAST_MIN, now)

        if (now < lastMin - 2) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply()
            return
        }

        val ntpMin = getNtpMin()
        if (ntpMin != null && ntpMin > lastMin && now < lastMin) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply()
            return
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
                    val minsLeft  = TRIAL_MINUTES - elapsed
                    return if (minsLeft > 0) LicenseStatus.TRIAL(minsLeft)
                           else LicenseStatus.EXPIRED
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

        val clean = key.trim().uppercase().replace("-", "")

        val usedKeys = prefs.getString(KEY_USED_KEYS, "") ?: ""
        val keyHash  = sha256(clean).take(12)
        if (usedKeys.split("|").contains(keyHash)) return KeyResult.ALREADY_USED

        return when (verifyKey(ctx, clean)) {
            is VerifyResult.Permanent -> {
                markUsed(prefs, usedKeys, keyHash)
                prefs.edit().putString(KEY_LICENSE, clean).apply()
                KeyResult.VALID_PERMANENT
            }
            is VerifyResult.Trial -> {
                val r = verifyKey(ctx, clean) as VerifyResult.Trial
                val elapsed = nowMin() - r.issuedMin
                if (elapsed >= TRIAL_MINUTES) return KeyResult.EXPIRED
                markUsed(prefs, usedKeys, keyHash)
                prefs.edit().putString(KEY_LICENSE, clean).apply()
                KeyResult.VALID_TRIAL
            }
            is VerifyResult.Invalid -> KeyResult.INVALID
        }
    }

    private fun markUsed(prefs: android.content.SharedPreferences, existing: String, keyHash: String) {
        val updated = if (existing.isEmpty()) keyHash else "$existing|$keyHash"
        prefs.edit().putString(KEY_USED_KEYS, updated).apply()
    }

    private sealed class VerifyResult {
        object Permanent                      : VerifyResult()
        data class Trial(val issuedMin: Int)  : VerifyResult()
        object Invalid                        : VerifyResult()
    }

    private fun verifyKey(ctx: Context, key: String): VerifyResult {
        val clean    = key.replace("-", "").uppercase()
        val deviceId = getDeviceId(ctx)

        if (clean == sha256("${SECRET_SALT}${deviceId}PERM").take(16).uppercase())
            return VerifyResult.Permanent

        if (clean == sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase())
            return VerifyResult.Permanent

        val now = nowMin()
        for (ago in 0..SCAN_WINDOW) {
            val issueMin = now - ago
            val expected = sha256("${SECRET_SALT}${deviceId}TRIAL${issueMin}").take(16).uppercase()
            if (clean == expected) return VerifyResult.Trial(issueMin)
        }

        return VerifyResult.Invalid
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

    fun generateTrialKey(deviceId: String, minsAgo: Int = 0): String {
        val issuedMin = nowMin() - minsAgo
        val h = sha256("${SECRET_SALT}${deviceId.uppercase()}TRIAL${issuedMin}").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }

    fun generatePermKey(deviceId: String): String {
        val h = sha256("${SECRET_SALT}${deviceId.uppercase()}PERM").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }

    fun generateUniversalKey(): String {
        val h = sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }

    private fun nowMin() = (System.currentTimeMillis() / 60_000L).toInt()
}

sealed class LicenseStatus {
    object FRESH                         : LicenseStatus()
    data class TRIAL(val minsLeft: Int)  : LicenseStatus()
    object EXPIRED                       : LicenseStatus()
    object LICENSED_PERMANENT            : LicenseStatus()
    object LICENSED_EXTENDED             : LicenseStatus()
    object CHEATER                       : LicenseStatus()
}

enum class KeyResult {
    VALID_PERMANENT,
    VALID_TRIAL,
    ALREADY_USED,
    EXPIRED,
    INVALID
}
