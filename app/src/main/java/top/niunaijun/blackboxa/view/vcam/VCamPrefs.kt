package top.niunaijun.blackboxa.view.vcam

import android.content.Context
import android.content.SharedPreferences

object VCamPrefs {

    private const val PREFS_NAME = "vcam_per_app"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun read(ctx: Context, pkg: String): VCamConfig {
        val p = prefs(ctx); val x = "${pkg}_"
        return VCamConfig(
            method   = p.getInt("${x}method", 1),
            videoUri = p.getString("${x}video", "") ?: "",
            netUrl   = p.getString("${x}url",   "") ?: "",
            picUri   = p.getString("${x}pic",   "") ?: "",
            audio    = p.getBoolean("${x}audio", false),
            width    = p.getInt("${x}width",  1280),
            height   = p.getInt("${x}height", 720),
            fps      = p.getInt("${x}fps",    30)
        )
    }

    fun write(ctx: Context, pkg: String, cfg: VCamConfig) {
        val x = "${pkg}_"
        prefs(ctx).edit().apply {
            putInt("${x}method",     cfg.method)
            putString("${x}video",   cfg.videoUri)
            putString("${x}url",     cfg.netUrl)
            putString("${x}pic",     cfg.picUri)
            putBoolean("${x}audio",  cfg.audio)
            putInt("${x}width",      cfg.width)
            putInt("${x}height",     cfg.height)
            putInt("${x}fps",        cfg.fps)
        }.apply()
    }

    fun isActive(ctx: Context, pkg: String) = read(ctx, pkg).isActive

    fun activePackages(ctx: Context): Set<String> =
        prefs(ctx).all.keys
            .filter { it.endsWith("_method") }
            .map { it.removeSuffix("_method") }
            .filter { prefs(ctx).getInt("${it}_method", 1) != 1 }
            .toSet()
}
