package com.wangyiheng.vcamsx

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wangyiheng.vcamsx.license.LicenseManager
import com.wangyiheng.vcamsx.license.LicenseScreen
import com.wangyiheng.vcamsx.license.LicenseStatus
import com.wangyiheng.vcamsx.license.SupabaseClient
import com.wangyiheng.vcamsx.modules.home.view.HomeScreen
import com.wangyiheng.vcamsx.ui.theme.VCAMSXTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    companion object {
        const val APP_VERSION = "1.0.9"
        const val DOWNLOAD_URL = "https://github.com/BashCedar/vcamsx2/releases/latest"
    }

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
                AppGate()
            }
        }
    }

    @Composable
    fun AppGate() {
        val context = this
        val scope   = rememberCoroutineScope()

        var online         by remember { mutableStateOf(isOnline()) }
        var versionOk      by remember { mutableStateOf<Boolean?>(null) }
        var latestVersion  by remember { mutableStateOf(APP_VERSION) }
        var gatePassed     by remember { mutableStateOf(false) }

        var status  by remember { mutableStateOf(LicenseManager.getStatus(context)) }
        val active  = status is LicenseStatus.TRIAL ||
                      status == LicenseStatus.LICENSED_PERMANENT ||
                      status == LicenseStatus.LICENSED_EXTENDED
        var showApp by remember { mutableStateOf(false) }

        val bg     = Color(0xFF000000)
        val card   = Color(0xFF0A0A0A)
        val accent = Color(0xFFB0BAF0)
        val text   = Color(0xFFE0E4FF)
        val red    = Color(0xFFEF5350)

        LaunchedEffect(Unit) {
            online = isOnline()
            if (!online) return@LaunchedEffect

            val deviceId = LicenseManager.getDeviceId(context)
            SupabaseClient.ping(deviceId) {
                getSharedPreferences("vcamsx_lic", MODE_PRIVATE).edit()
                    .remove("k").putBoolean("c", true).apply()
                recreate()
            }

            val minVersion = withContext(Dispatchers.IO) { SupabaseClient.getMinVersion() }
            if (minVersion != null) {
                latestVersion = minVersion
                versionOk = versionAtLeast(APP_VERSION, minVersion)
            } else {
                versionOk = true
            }

            gatePassed = true
        }

        when {
            !online -> {
                Box(Modifier.fillMaxSize().background(bg), contentAlignment = Alignment.Center) {
                    Card(Modifier.padding(32.dp), shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = card)) {
                        Column(Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("🌐", fontSize = 48.sp)
                            Text("No Internet Connection", color = text, fontSize = 18.sp,
                                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("This app requires internet to function.\nPlease connect and try again.",
                                color = Color(0xFF555566), fontSize = 13.sp, textAlign = TextAlign.Center)
                            Button(
                                onClick = {
                                    online = isOnline()
                                    if (online) scope.launch {
                                        val minVersion = withContext(Dispatchers.IO) { SupabaseClient.getMinVersion() }
                                        if (minVersion != null) {
                                            latestVersion = minVersion
                                            versionOk = versionAtLeast(APP_VERSION, minVersion)
                                        } else versionOk = true
                                        gatePassed = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = accent)
                            ) { Text("Retry", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            }

            !gatePassed || versionOk == null -> {
                Box(Modifier.fillMaxSize().background(bg), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        CircularProgressIndicator(color = accent)
                        Text("Initializing VCamSX...", color = Color(0xFF555566), fontSize = 13.sp)
                    }
                }
            }

            versionOk == false -> {
                Box(Modifier.fillMaxSize().background(bg), contentAlignment = Alignment.Center) {
                    Card(Modifier.padding(32.dp), shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = card)) {
                        Column(Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("⚠️", fontSize = 48.sp)
                            Text("Update Required", color = text, fontSize = 18.sp,
                                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text("You are using an outdated version ($APP_VERSION).\nPlease download the latest version ($latestVersion) to continue.",
                                color = Color(0xFF555566), fontSize = 13.sp, textAlign = TextAlign.Center)
                            Button(
                                onClick = {
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(DOWNLOAD_URL)))
                                },
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = accent)
                            ) { Text("Download Update", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            }

            else -> {
                if (!showApp && !active) {
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

    private fun isOnline(): Boolean {
        val cm   = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val net  = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(net) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun versionAtLeast(current: String, minimum: String): Boolean {
        val c = current.split(".").map { it.toIntOrNull() ?: 0 }
        val m = minimum.split(".").map { it.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(c.size, m.size)) {
            val cv = c.getOrElse(i) { 0 }
            val mv = m.getOrElse(i) { 0 }
            if (cv > mv) return true
            if (cv < mv) return false
        }
        return true
    }
}
