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

private val AppColorScheme = darkColorScheme(
    background           = Obsidian,
    surface              = Surface,
    surfaceVariant       = SurfaceRaised,
    primary              = Cyan,
    onPrimary            = Obsidian,
    primaryContainer     = CyanDim,
    onPrimaryContainer   = Cyan,
    secondary            = Green,
    onSecondary          = Obsidian,
    secondaryContainer   = GreenDim,
    onSecondaryContainer = Green,
    tertiary             = Color(0xFF7C6FF7),
    onTertiary           = Color.White,
    onBackground         = TextHigh,
    onSurface            = TextHigh,
    onSurfaceVariant     = TextMid,
    outline              = SurfaceStroke,
    outlineVariant       = StrokeDefault,
    error                = Red,
    onError              = Color.White,
    errorContainer       = RedDim,
    onErrorContainer     = Red,
    scrim                = Color(0xCC000000),
)

@Composable
fun VCAMSXTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor     = Obsidian.toArgb()
            window.navigationBarColor = Obsidian.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars     = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography  = Typography,
        content     = content
    )
}
