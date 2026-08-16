package com.axiom.vcam.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.axiom.vcam.container.Container
import com.axiom.vcam.ui.theme.VCamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register camera hook module with BlackBox on first launch
        Container.registerHookModule(this)
        setContent {
            VCamTheme {
                VCamNavHost()
            }
        }
    }
}
