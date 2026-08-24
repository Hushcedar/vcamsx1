package com.wangyiheng.vcamsx.license

import android.content.Context
import android.os.Environment
import android.provider.Settings
import android.util.Log
import java.io.File
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

object LicenseManager {

    private const val PREFS         = "vcamsx_license"
    private const val KEY_INSTALL   = "install_ts"
    private const val KEY_LICENSE   = "license_key"
    private const val KEY_LAST_SEEN = "last_seen_ts"
    private const val KEY_CHEATER   = "cheater"
    private const val KEY_TRIAL_USED = "trial_used"
    private const val KEY_LICENSE_TYPE = "license_type"  // "TRIAL" or "PERM"
    private const val TRIAL_DAYS    = 7L
    private const val SECRET_SALT   = "VCamSX_S3cr3t_2026_!@#"
    private const val NTP_HOST      = "time.google.com"
    private const val ROLLBACK_GRACE = 60_000L

    private fun getExternalStateFile(): File {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        dir.mkdirs()
        return File(dir, ".vcsx")
    }

    private fun readExternalState(): Map<String, String> {
        return try {
            val file = getExternalStateFile()
            if (!file.exists()) return emptyMap()
            file.readLines()
                .mapNotNull { line ->
                    val parts = line.split("=", limit = 2)
                    if (parts.size == 2) parts[0] to parts[1] else null
                }.toMap()
        } catch (e: Exception) { emptyMap() }
    }

    private fun writeExternalState(key: String, value: String) {
        try {
            val file = getExternalStateFile()
            val state = readExternalState().toMutableMap()
            state[key] = value
            file.writeText(state.entries.joinToString("\n") { "${it.key}=${it.value}" })
        } catch (e: Exception) {
            Log.e("VCamSX", "writeExternalState: ${e.message}")
        }
    }

    fun init(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val ext   = readExternalState()

        if (ext[KEY_CHEATER] == "true" && !prefs.getBoolean(KEY_CHEATER, false)) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply()
        }
        if (ext[KEY_TRIAL_USED] == "true" && !prefs.getBoolean(KEY_TRIAL_USED, false)) {
            prefs.edit().putBoolean(KEY_TRIAL_USED, true).apply()
        }
        if (ext[KEY_INSTALL] != null && !prefs.contains(KEY_INSTALL)) {
            prefs.edit().putLong(KEY_INSTALL, ext[KEY_INSTALL]!!.toLongOrNull() ?: 0L).apply()
        }
        if (ext[KEY_LICENSE] != null && prefs.getString(KEY_LICENSE, null) == null) {
            prefs.edit().putString(KEY_LICENSE, ext[KEY_LICENSE]).apply()
        }

        if (prefs.getBoolean(KEY_CHEATER, false)) return

        val now      = System.currentTimeMillis()
        val lastSeen = prefs.getLong(KEY_LAST_SEEN, now)

        if (now < lastSeen - ROLLBACK_GRACE) {
            prefs.edit().putBoolean(KEY_CHEATER, true).apply()
            writeExternalState(KEY_CHEATER, "true")
            Log.w("VCamSX", "Clock rollback → permanent lock")
            return
        }

        val ntpTime = getNtpTime()
        if (ntpTime != null) {
            val drift = kotlin.math.abs(ntpTime - now)
            if (drift > TimeUnit.MINUTES.toMillis(5) && ntpTime < lastSeen) {
                prefs.edit().putBoolean(KEY_CHEATER, true).apply()
                writeExternalState(KEY_CHEATER, "true")
                Log.w("VCamSX", "NTP mismatch → permanent lock")
                return
            }
            prefs.edit().putLong(KEY_LAST_SEEN, maxOf(now, ntpTime)).apply()
        } else {
            prefs.edit().putLong(KEY_LAST_SEEN, now).apply()
        }
    }

    fun getStatus(ctx: Context): LicenseStatus {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        if (prefs.getBoolean(KEY_CHEATER, false)) return LicenseStatus.CHEATER

        val savedKey = prefs.getString(KEY_LICENSE, null)
        if (savedKey != null && verifyKey(ctx, savedKey) != KeyResult.INVALID) {
            return LicenseStatus.LICENSED_PERMANENT
        }

        if (!prefs.contains(KEY_INSTALL) && !prefs.getBoolean(KEY_TRIAL_USED, false)) {
            return LicenseStatus.FRESH
        }

        if (prefs.getBoolean(KEY_TRIAL_USED, false) && !prefs.contains(KEY_INSTALL)) {
            return LicenseStatus.EXPIRED
        }

        val installTs    = prefs.getLong(KEY_INSTALL, System.currentTimeMillis())
        val lastSeen     = prefs.getLong(KEY_LAST_SEEN, System.currentTimeMillis())
        val effectiveNow = maxOf(System.currentTimeMillis(), lastSeen)
        val elapsed      = effectiveNow - installTs
        val daysLeft     = TRIAL_DAYS - TimeUnit.MILLISECONDS.toDays(elapsed)

        return if (daysLeft > 0) LicenseStatus.TRIAL(daysLeft.toInt())
        else LicenseStatus.EXPIRED
    }

    fun isActive(ctx: Context): Boolean =
        getStatus(ctx).let {
            it is LicenseStatus.TRIAL ||
            it is LicenseStatus.LICENSED_PERMANENT ||
            it is LicenseStatus.LICENSED_EXTENDED
        }

    fun startTrial(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val now   = System.currentTimeMillis()
        prefs.edit()
            .putLong(KEY_INSTALL, now)
            .putLong(KEY_LAST_SEEN, now)
            .putBoolean(KEY_TRIAL_USED, true)
            .apply()
        writeExternalState(KEY_INSTALL,    now.toString())
        writeExternalState(KEY_LAST_SEEN,  now.toString())
        writeExternalState(KEY_TRIAL_USED, "true")
    }

    fun activateKey(ctx: Context, key: String): KeyResult {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_CHEATER, false)) return KeyResult.INVALID

        val result = verifyKey(ctx, key)
        if (result != KeyResult.INVALID) {
            val clean    = key.trim().uppercase()
            val keyType  = if (result == KeyResult.VALID_TRIAL) "TRIAL" else "PERM"
            prefs.edit()
                .putString(KEY_LICENSE, clean)
                .putString(KEY_LICENSE_TYPE, keyType)
                .apply()
            writeExternalState(KEY_LICENSE, clean)
            writeExternalState(KEY_LICENSE_TYPE, keyType)
        }
        return result
    }

    private fun verifyKey(ctx: Context, key: String): KeyResult {
        val clean    = key.trim().uppercase().replace("-", "")
        if (clean.length < 16) return KeyResult.INVALID
        val deviceId = getDeviceId(ctx)

        if (clean == sha256("$SECRET_SALT${deviceId}TRIAL").take(16).uppercase())
            return KeyResult.VALID_TRIAL

        if (clean == sha256("$SECRET_SALT${deviceId}PERM").take(16).uppercase())
            return KeyResult.VALID_PERMANENT

        if (clean == sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase())
            return KeyResult.VALID_PERMANENT

        return KeyResult.INVALID
    }

    private fun getNtpTime(): Long? = try {
        val socket  = DatagramSocket().also { it.soTimeout = 3000 }
        val buf     = ByteArray(48).also { it[0] = 0x1B.toByte() }
        val address = InetAddress.getByName(NTP_HOST)
        socket.send(DatagramPacket(buf, buf.size, address, 123))
        val resp = DatagramPacket(ByteArray(48), 48)
        socket.receive(resp); socket.close()
        val secs = ByteBuffer.wrap(resp.data, 40, 4).int.toLong() and 0xFFFFFFFFL
        (secs - 2208988800L) * 1000L
    } catch (e: Exception) { null }

    fun getDeviceId(ctx: Context): String =
        Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
            ?.uppercase() ?: "UNKNOWN"

    private fun sha256(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }

    fun generateKey(deviceId: String, type: String = "PERM"): String {
        val h = sha256("$SECRET_SALT${deviceId.uppercase()}$type").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }
    fun generateUniversalKey(): String {
        val h = sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase()
        return "${h.substring(0,4)}-${h.substring(4,8)}-${h.substring(8,12)}-${h.substring(12,16)}"
    }
}

sealed class LicenseStatus {
    object FRESH                     : LicenseStatus()
    data class TRIAL(val daysLeft: Int) : LicenseStatus()
    object EXPIRED                   : LicenseStatus()
    object LICENSED_PERMANENT        : LicenseStatus()
    object LICENSED_EXTENDED         : LicenseStatus()
    object CHEATER                   : LicenseStatus()
}

enum class KeyResult { VALID_PERMANENT, VALID_TRIAL, VALID_TRIAL_EXT, INVALID }
