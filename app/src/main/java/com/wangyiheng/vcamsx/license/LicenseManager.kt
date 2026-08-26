package com.wangyiheng.vcamsx.license

import android.content.Context
import android.provider.Settings
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.security.MessageDigest

object LicenseManager {

    private const val PREFS        = "vcamsx_lic"
    private const val KEY_LICENSE  = "k"
    private const val KEY_CHEATER  = "c"
    private const val KEY_LAST_DAY = "d"
    private const val TRIAL_DAYS   = 7
    private const val SECRET_SALT  = "VCamSX_S3cr3t_2026_!@#"

    private fun today(): Int = (System.currentTimeMillis() / 86_400_000L).toInt()

    fun init(ctx: Context) {
        val prefs    = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return

        val todayDay = today()
        val lastDay  = prefs.getInt(KEY_LAST_DAY, todayDay)

        if (todayDay < lastDay - 1) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply()
            return
        }

        val ntpDay = getNtpDay()
        if (ntpDay != null && ntpDay > lastDay && todayDay < lastDay) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply()
            return
        }

        val authoritative = maxOf(todayDay, ntpDay ?: todayDay)
        prefs.edit().putInt(KEY_LAST_DAY, authoritative).apply()
    }

    fun getStatus(ctx: Context): LicenseStatus {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        if (prefs.getBoolean(KEY_CHEATER, false)) return LicenseStatus.CHEATER

        val savedKey = prefs.getString(KEY_LICENSE, null)
        if (savedKey != null) {
            when (val r = verifyKey(ctx, savedKey)) {
                is VerifyResult.Permanent -> return LicenseStatus.LICENSED_PERMANENT
                is VerifyResult.Trial     -> {
                    val lastDay  = prefs.getInt(KEY_LAST_DAY, today())
                    val effective = maxOf(today(), lastDay)
                    val elapsed  = effective - r.issuedDay
                    val daysLeft = TRIAL_DAYS - elapsed
                    return if (daysLeft > 0) LicenseStatus.TRIAL(daysLeft)
                           else LicenseStatus.EXPIRED
                }
                is VerifyResult.Invalid -> {}
            }
        }

        return LicenseStatus.FRESH
    }

    fun isActive(ctx: Context): Boolean = when (getStatus(ctx)) {
        is LicenseStatus.TRIAL,
        is LicenseStatus.LICENSED_PERMANENT,
        is LicenseStatus.LICENSED_EXTENDED -> true
        else -> false
    }

    fun activateKey(ctx: Context, key: String): KeyResult {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return KeyResult.INVALID
        val clean = key.trim().uppercase().replace("-", "")
        return when (verifyKey(ctx, clean)) {
            is VerifyResult.Permanent -> {
                prefs.edit().putString(KEY_LICENSE, clean).apply()
                KeyResult.VALID_PERMANENT
            }
            is VerifyResult.Trial -> {
                prefs.edit().putString(KEY_LICENSE, clean).apply()
                KeyResult.VALID_TRIAL
            }
            is VerifyResult.Invalid -> KeyResult.INVALID
        }
    }

    private sealed class VerifyResult {
        object Permanent                   : VerifyResult()
        data class Trial(val issuedDay: Int): VerifyResult()
        object Invalid                     : VerifyResult()
    }

    private fun verifyKey(ctx: Context, key: String): VerifyResult {
        val clean    = key.replace("-", "").uppercase()
        val deviceId = getDeviceId(ctx)

        if (clean == sha256("${SECRET_SALT}${deviceId}PERM").take(16).uppercase())
            return VerifyResult.Permanent

        if (clean == sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase())
            return VerifyResult.Permanent

        for (daysAgo in 0..30) {
            val issueDay = today() - daysAgo
            val expected = sha256("${SECRET_SALT}${deviceId}TRIAL${issueDay}").take(16).uppercase()
            if (clean == expected) return VerifyResult.Trial(issueDay)
        }

        return VerifyResult.Invalid
    }

    private fun getNtpDay(): Int? = try {
        val socket  = DatagramSocket().also { it.soTimeout = 3000 }
        val buf     = ByteArray(48).also { it[0] = 0x1B.toByte() }
        val addr    = InetAddress.getByName("time.google.com")
        socket.send(DatagramPacket(buf, buf.size, addr, 123))
        val resp    = DatagramPacket(ByteArray(48), 48)
        socket.receive(resp); socket.close()
        val secs    = ByteBuffer.wrap(resp.data, 40, 4).int.toLong() and 0xFFFFFFFFL
        val unixMs  = (secs - 2208988800L) * 1000L
        (unixMs / 86_400_000L).toInt()
    } catch (e: Exception) { null }

    fun getDeviceId(ctx: Context): String =
        Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
            ?.uppercase() ?: "UNKNOWN"

    private fun sha256(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }

    fun generateTrialKey(deviceId: String, issueDayOverride: Int? = null): String {
        val day  = issueDayOverride ?: today()
        val h    = sha256("${SECRET_SALT}${deviceId.uppercase()}TRIAL${day}").take(16).uppercase()
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

    private fun today() = (System.currentTimeMillis() / 86_400_000L).toInt()
}

sealed class LicenseStatus {
    object FRESH                         : LicenseStatus()
    data class TRIAL(val daysLeft: Int)  : LicenseStatus()
    object EXPIRED                       : LicenseStatus()
    object LICENSED_PERMANENT            : LicenseStatus()
    object LICENSED_EXTENDED             : LicenseStatus()
    object CHEATER                       : LicenseStatus()
}

enum class KeyResult { VALID_PERMANENT, VALID_TRIAL, INVALID }
