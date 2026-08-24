package com.wangyiheng.vcamsx

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import com.wangyiheng.vcamsx.license.LicenseManager
import com.wangyiheng.vcamsx.license.LicenseScreen
import com.wangyiheng.vcamsx.license.LicenseStatus
import com.wangyiheng.vcamsx.modules.home.view.HomeScreen
import com.wangyiheng.vcamsx.ui.theme.VCAMSXTheme

class MainActivity : ComponentActivity() {
    private val overlayLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LicenseManager.init(this)

        if (!Settings.canDrawOverlays(this)) {
            overlayLauncher.launch(
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
            )
        }

        setContent {
            VCAMSXTheme {
                val context = this
                var status  by remember { mutableStateOf(LicenseManager.getStatus(context)) }
                val active  = status is LicenseStatus.TRIAL ||
                              status is LicenseStatus.LICENSED_PERMANENT ||
                              status is LicenseStatus.LICENSED_EXTENDED
                var showApp by remember { mutableStateOf(active) }

                if (!showApp) {
                    LicenseScreen(status = status) {
                        status  = LicenseManager.getStatus(context)
                        showApp = true
                    }
                } else {
                    HomeScreen()
                }
            }
        }
    }
}
