package com.wangyiheng.vcamsx.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CyberColorScheme = darkColorScheme(
    primary          = NeonCyan,
    secondary        = NeonPurple,
    tertiary         = NeonGreen,
    background       = CyberBlack,
    surface          = CyberCard,
    onPrimary        = CyberBlack,
    onSecondary      = CyberBlack,
    onBackground     = CyberText,
    onSurface        = CyberText,
    error            = NeonRed,
    outline          = CyberBorder
)

@Composable
fun VCAMSXTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CyberBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography  = Typography,
        content     = content
    )
}
