package com.wangyiheng.vcamsx.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ObsidianGlassScheme = darkColorScheme(
    // ── Core surfaces ──────────────────────────────────────────────────────
    background       = ObsidianBg,       // 0x0A0E17 — all screen backgrounds
    surface          = SurfaceDark,      // 0x121824 — cards, sheets, dialogs
    surfaceVariant   = AppGlassBg,       // 0xD9121824 — glass card variant

    // ── Brand accents ──────────────────────────────────────────────────────
    primary          = TechCyan,         // 0x00E5FF — buttons, active borders
    onPrimary        = Color(0xFF000000),// black text on cyan buttons
    primaryContainer = Color(0x1A00E5FF),// 10% cyan — subtle filled containers
    onPrimaryContainer = TechCyan,

    secondary        = TechGreen,        // 0x00FF66 — online/active indicators
    onSecondary      = Color(0xFF000000),
    secondaryContainer = Color(0x1A00FF66),
    onSecondaryContainer = TechGreen,

    tertiary         = NeonPurple,       // 0x7B61FF — secondary accent (V glyph etc)
    onTertiary       = Color(0xFF000000),

    // ── Text ───────────────────────────────────────────────────────────────
    onBackground     = TextPrimary,      // 0xFFFFFFFF
    onSurface        = TextPrimary,
    onSurfaceVariant = TextSecondary,    // 0xFF707E94

    // ── Borders / outline ──────────────────────────────────────────────────
    outline          = GlassBorder,      // 0x3300E5FF — 20% cyan card borders
    outlineVariant   = InputBorder,      // 0x1FFFFFFF — 12% white input borders

    // ── Errors ─────────────────────────────────────────────────────────────
    error            = NeonRed,
    onError          = Color(0xFF000000),
    errorContainer   = Color(0x1AFF2D55),
    onErrorContainer = NeonRed,

    // ── Scrim / overlays ───────────────────────────────────────────────────
    scrim            = Color(0xCC000000),
    inverseSurface   = TextPrimary,
    inverseOnSurface = ObsidianBg,
    inversePrimary   = TechCyan,
)

@Composable
fun VCAMSXTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Status bar matches ObsidianBg exactly
            window.statusBarColor = ObsidianBg.toArgb()
            window.navigationBarColor = ObsidianBg.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars     = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = ObsidianGlassScheme,
        typography  = Typography,
        content     = content
    )
}
