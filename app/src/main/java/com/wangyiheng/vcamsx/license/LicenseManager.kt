package com.wangyiheng.vcamsx.license

import android.content.Context
import android.provider.Settings
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

object LicenseManager {

    private const val PREFS          = "vcamsx_license"
    private const val KEY_INSTALL    = "install_ts"
    private const val KEY_LICENSE    = "license_key"
    private const val TRIAL_DAYS     = 7L
    private const val SECRET_SALT    = "VCamSX_S3cr3t_2026_!@#"

    fun init(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (!prefs.contains(KEY_INSTALL)) {
            prefs.edit().putLong(KEY_INSTALL, System.currentTimeMillis()).apply()
        }
    }

    fun getStatus(ctx: Context): LicenseStatus {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        val savedKey = prefs.getString(KEY_LICENSE, null)
        if (savedKey != null && verifyKey(ctx, savedKey) != KeyResult.INVALID) {
            return when (verifyKey(ctx, savedKey)) {
                KeyResult.VALID_PERMANENT -> LicenseStatus.LICENSED_PERMANENT
                KeyResult.VALID_TRIAL_EXT -> LicenseStatus.LICENSED_EXTENDED
                else -> LicenseStatus.LICENSED_PERMANENT
            }
        }

        val installTs  = prefs.getLong(KEY_INSTALL, System.currentTimeMillis())
        val elapsed    = System.currentTimeMillis() - installTs
        val daysLeft   = TRIAL_DAYS - TimeUnit.MILLISECONDS.toDays(elapsed)

        return if (daysLeft > 0) LicenseStatus.TRIAL(daysLeft.toInt())
        else LicenseStatus.EXPIRED
    }

    fun isActive(ctx: Context): Boolean = getStatus(ctx) != LicenseStatus.EXPIRED

    fun activateKey(ctx: Context, key: String): KeyResult {
        val result = verifyKey(ctx, key)
        if (result != KeyResult.INVALID) {
            ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putString(KEY_LICENSE, key.trim().uppercase()).apply()
        }
        return result
    }

    private fun verifyKey(ctx: Context, key: String): KeyResult {
        val clean = key.trim().uppercase().replace("-", "")
        if (clean.length < 16) return KeyResult.INVALID

        val deviceId = getDeviceId(ctx)

        val permHash = sha256("$SECRET_SALT${deviceId}PERM").take(16).uppercase()
        if (clean == permHash) return KeyResult.VALID_PERMANENT

        val univHash = sha256("${SECRET_SALT}UNIVERSAL").take(16).uppercase()
        if (clean == univHash) return KeyResult.VALID_PERMANENT

        val ext30Hash = sha256("$SECRET_SALT${deviceId}EXT30").take(16).uppercase()
        if (clean == ext30Hash) return KeyResult.VALID_TRIAL_EXT

        return KeyResult.INVALID
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
    object EXPIRED                      : LicenseStatus()
    object LICENSED_PERMANENT           : LicenseStatus()
    object LICENSED_EXTENDED            : LicenseStatus()
}

enum class KeyResult { VALID_PERMANENT, VALID_TRIAL_EXT, INVALID }
