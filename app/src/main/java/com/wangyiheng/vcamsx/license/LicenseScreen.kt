package com.wangyiheng.vcamsx.license

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val APP_VERSION = "1.0.9"

private fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val net = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(net) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
           caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(status: LicenseStatus, onUnlocked: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var keyInput by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var isActivating by remember { mutableStateOf(false) }
    var online by remember { mutableStateOf(isOnline(context)) }
    val deviceId = remember { LicenseManager.getDeviceId(context) }

    val bg      = Color(0xFF000000)
    val card    = Color(0xFF0A0A0A)
    val accent  = Color(0xFFB0BAF0)
    val text    = Color(0xFFE0E4FF)
    val subtext = Color(0xFF555566)
    val red     = Color(0xFFEF5350)
    val green   = Color(0xFF4CAF50)
    val orange  = Color(0xFFFFB74D)

    Box(
        modifier = Modifier.fillMaxSize().background(bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(if (status == LicenseStatus.CHEATER) "⛔" else "🎥", fontSize = 52.sp)
            Text("VCamSX  v$APP_VERSION", color = text, fontSize = 26.sp, fontWeight = FontWeight.Bold)

            if (!online) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A0000))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌐", fontSize = 18.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text("No Internet Connection", color = red, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Please connect to activate your license.", color = subtext, fontSize = 11.sp)
                        }
                        TextButton(onClick = { online = isOnline(context) }) {
                            Text("Retry", color = orange, fontSize = 12.sp)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = card)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (status) {
                        LicenseStatus.FRESH -> {
                            Text("Welcome to VCamSX", color = accent, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Enter a license key to get started.\nNew user? Request a free trial key.",
                                color = subtext, fontSize = 13.sp, textAlign = TextAlign.Center)
                        }
                        is LicenseStatus.TRIAL -> {
                            Text("Free Trial Active", color = green, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("${status.minsLeft} minutes remaining", color = text, fontSize = 15.sp)
                            Button(
                                onClick = onUnlocked,
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = accent)
                            ) { Text("Open App", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold) }
                        }
                        LicenseStatus.EXPIRED -> {
                            Text("Trial Expired", color = red, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Your trial has ended.\nEnter a license key to continue.",
                                color = subtext, fontSize = 13.sp, textAlign = TextAlign.Center)
                        }
                        LicenseStatus.CHEATER -> {
                            Text("Device Locked", color = red, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Clock manipulation was detected.\nThis device is permanently banned.",
                                color = subtext, fontSize = 13.sp, textAlign = TextAlign.Center)
                            Text("Contact support with your Device ID to appeal.",
                                color = subtext, fontSize = 11.sp, textAlign = TextAlign.Center)
                        }
                        else -> {}
                    }
                }
            }

            if (status != LicenseStatus.CHEATER) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = card)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("License Key", color = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)

                        OutlinedTextField(
                            value = keyInput,
                            onValueChange = { newVal ->
                                val clean = newVal.filter { it.isLetterOrDigit() }.uppercase().take(16)
                                keyInput = clean.chunked(4).joinToString("-")
                                errorMsg = ""
                            },
                            placeholder = { Text("XXXX-XXXX-XXXX-XXXX", color = subtext) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = online && !isActivating,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = text,
                                containerColor = Color.Transparent,
                                cursorColor = accent,
                                focusedBorderColor = accent,
                                unfocusedBorderColor = Color(0xFF1A1A1A),
                                disabledBorderColor = Color(0xFF111111),
                                disabledTextColor = subtext
                            )
                        )

                        if (errorMsg.isNotEmpty()) {
                            Text(errorMsg, color = red, fontSize = 12.sp)
                        }

                        if (!online) {
                            Text(
                                "🌐 Connect to internet to activate",
                                color = orange, fontSize = 12.sp, textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Button(
                            onClick = {
                                online = isOnline(context)
                                if (!online) {
                                    errorMsg = "🌐 No internet connection. Please connect and retry."
                                    return@Button
                                }
                                scope.launch {
                                    isActivating = true
                                    errorMsg = ""
                                    val result = withContext(Dispatchers.IO) {
                                        LicenseManager.activateKey(context, keyInput)
                                    }
                                    isActivating = false
                                    when (result) {
                                        KeyResult.VALID_PERMANENT -> onUnlocked()
                                        KeyResult.VALID_TRIAL     -> onUnlocked()
                                        KeyResult.ALREADY_USED    -> errorMsg = "⚠️ This key has already been used on another device.\nPlease request a new key."
                                        KeyResult.EXPIRED         -> errorMsg = "⏱️ This key has expired. Request a new one."
                                        KeyResult.INVALID         -> errorMsg = "❌ Invalid key. Check and try again."
                                        KeyResult.OLD_FORMAT      -> errorMsg = "⚠️ This key is not compatible with this version.\nPlease request a new key."
                                        KeyResult.NO_INTERNET     -> {
                                            online = false
                                            errorMsg = "🌐 No internet. Connect and retry."
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (online) accent else Color(0xFF333333)),
                            enabled = online && keyInput.length >= 4 && !isActivating
                        ) {
                            if (isActivating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = Color(0xFF0A0A0A)
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Text("Activate", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = card)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Your Device ID (share this to get a key):", color = subtext, fontSize = 11.sp)
                    Text(deviceId, color = text, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
