package com.wangyiheng.vcamsx

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.wangyiheng.vcamsx.modules.home.view.HomeScreen
import com.wangyiheng.vcamsx.ui.theme.VCAMSXTheme

class MainActivity : ComponentActivity() {
    private val overlayLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* user granted permission — bubble starts when Controls pressed */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Settings.canDrawOverlays(this)) {
            overlayLauncher.launch(
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
            )
        }
        setContent { VCAMSXTheme { HomeScreen() } }
    }
}
