package com.axiom.vcam.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary        = Color(0xFF6C7AE0),
    secondary      = Color(0xFF8890CC),
    background     = Color(0xFF1A1C2E),
    surface        = Color(0xFF252840),
    onPrimary      = Color.White,
    onBackground   = Color.White,
    onSurface      = Color.White,
)

@Composable
fun VCamTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkScheme, content = content)
}
