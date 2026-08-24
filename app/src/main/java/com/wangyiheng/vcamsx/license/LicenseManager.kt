package com.wangyiheng.vcamsx.license

import android.content.Context
import android.provider.Settings
import android.util.Log
import java.net.InetAddress
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

object LicenseManager {

    private const val PREFS          = "vcamsx_license"
    private const val KEY_INSTALL    = "install_ts"
    private const val KEY_LICENSE    = "license_key"
    private const val KEY_LAST_SEEN  = "last_seen_ts"
    private const val KEY_CHEATER    = "cheater"
    private const val TRIAL_DAYS     = 7L
    private const val SECRET_SALT    = "VCamSX_S3cr3t_2026_!@#"
    private const val NTP_HOST       = "time.google.com"
    private const val ROLLBACK_GRACE = 60000L

    fun init(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return

        val now = System.currentTimeMillis()

        if (!prefs.contains(KEY_INSTALL)) {
            prefs.edit()
                .putLong(KEY_INSTALL, now)
                .putLong(KEY_LAST_SEEN, now)
                .apply()
            return
        }

        val lastSeen = prefs.getLong(KEY_LAST_SEEN, now)

        if (now < lastSeen - ROLLBACK_GRACE) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply()
            Log.w("VCamSX", "Clock rollback detected. Permanently locked.")
            return
        }

        val ntpTime = getNtpTime()
        if (ntpTime != null) {
            val drift = kotlin.math.abs(ntpTime - now)
            if (drift > TimeUnit.MINUTES.toMillis(5) && ntpTime < lastSeen) {
                prefs.edit().putBoolean(KEY_CHEATER, true).apply()
                Log.w("VCamSX", "NTP mismatch detected. Permanently locked.")
                return
            }
            prefs.edit().putLong(KEY_LAST_SEEN, maxOf(now, ntpTime)).apply()
        } else {
            prefs.edit().putLong(KEY_LAST_SEEN, now).apply()
        }
    }

    fun getStatus(ctx: Context): LicenseStatus {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        if (prefs.getBoolean(KEY_CHEATER, false)) {
            return LicenseStatus.CHEATER
        }

        val savedKey = prefs.getString(KEY_LICENSE, null)
        if (savedKey != null && verifyKey(ctx, savedKey) != KeyResult.INVALID) {
            return LicenseStatus.LICENSED_PERMANENT
        }

        val installTs = prefs.getLong(KEY_INSTALL, System.currentTimeMillis())
        val lastSeen  = prefs.getLong(KEY_LAST_SEEN, System.currentTimeMillis())
        val effectiveNow = maxOf(System.currentTimeMillis(), lastSeen)
        val elapsed  = effectiveNow - installTs
        val daysLeft = TRIAL_DAYS - TimeUnit.MILLISECONDS.toDays(elapsed)

        return if (daysLeft > 0) LicenseStatus.TRIAL(daysLeft.toInt())
        else LicenseStatus.EXPIRED
    }

    fun isActive(ctx: Context): Boolean =
        getStatus(ctx).let { it != LicenseStatus.EXPIRED && it != LicenseStatus.CHEATER }

    fun activateKey(ctx: Context, key: String): KeyResult {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return KeyResult.INVALID

        val result = verifyKey(ctx, key)
        if (result != KeyResult.INVALID) {
            prefs.edit().putString(KEY_LICENSE, key.trim().uppercase()).apply()
        }
        return result
    }

    private fun verifyKey(ctx: Context, key: String): KeyResult {
        val clean = key.trim().uppercase().replace("-", "")
        if (clean.length < 16) return KeyResult.INVALID
        val deviceId = getDeviceId(ctx)

        if (clean == sha256("$SECRET_SALT${deviceId}PERM").take(16).uppercase())
            return KeyResult.VALID_PERMANENT

        if (clean == sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase())
            return KeyResult.VALID_PERMANENT

        if (clean == sha256("$SECRET_SALT${deviceId}EXT30").take(16).uppercase())
            return KeyResult.VALID_TRIAL_EXT

        return KeyResult.INVALID
    }

    private fun getNtpTime(): Long? {
        return try {
            val socket = DatagramSocket()
            socket.soTimeout = 3000
            val buf = ByteArray(48)
            buf[0] = 0x1B.toByte()
            val address = InetAddress.getByName(NTP_HOST)
            val request = DatagramPacket(buf, buf.size, address, 123)
            socket.send(request)
            val response = DatagramPacket(ByteArray(48), 48)
            socket.receive(response)
            socket.close()
            val seconds = ByteBuffer.wrap(response.data, 40, 4).int.toLong() and 0xFFFFFFFFL
            (seconds - 2208988800L) * 1000L
        } catch (e: Exception) {
            null
        }
    }

    fun getDeviceId(ctx: Context): String =
        Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
            ?.uppercase() ?: "UNKNOWN"

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun generateKeyForDevice(deviceId: String, type: String = "PERM"): String {
        val hash = sha256("$SECRET_SALT${deviceId}$type").take(16).uppercase()
        return "${hash.substring(0,4)}-${hash.substring(4,8)}-${hash.substring(8,12)}-${hash.substring(12,16)}"
    }

    fun generateUniversalKey(): String {
        val hash = sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase()
        return "${hash.substring(0,4)}-${hash.substring(4,8)}-${hash.substring(8,12)}-${hash.substring(12,16)}"
    }
}

sealed class LicenseStatus {
    data class TRIAL(val daysLeft: Int) : LicenseStatus()
    object EXPIRED           : LicenseStatus()
    object LICENSED_PERMANENT: LicenseStatus()
    object LICENSED_EXTENDED : LicenseStatus()
    object CHEATER           : LicenseStatus()
}

enum class KeyResult { VALID_PERMANENT, VALID_TRIAL_EXT, INVALID }
