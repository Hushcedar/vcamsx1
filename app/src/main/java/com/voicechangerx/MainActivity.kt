package com.voicechangerx
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.voicechangerx.ui.screens.HomeScreen
import com.voicechangerx.ui.theme.VoiceChangerXTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VoiceChangerXTheme { HomeScreen() } }
    }
}
