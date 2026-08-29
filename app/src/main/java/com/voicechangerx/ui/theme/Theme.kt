package com.voicechangerx.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val scheme = darkColorScheme(
    primary=Color(0xFF6EE7B7), onPrimary=Color.Black,
    background=Color(0xFF0A0A0F), onBackground=Color(0xFFEEEEF5),
    surface=Color(0xFF13131A), onSurface=Color(0xFFEEEEF5),
    surfaceVariant=Color(0xFF1E1E28), onSurfaceVariant=Color(0xFF8888A8),
    secondary=Color(0xFF7C6EFA), error=Color(0xFFEF4444)
)
@Composable fun VoiceChangerXTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme=scheme, content=content)
}
