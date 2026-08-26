package com.wangyiheng.vcamsx.license

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(status: LicenseStatus, onUnlocked: () -> Unit) {
    val context  = LocalContext.current
    var keyInput by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    val deviceId = remember { LicenseManager.getDeviceId(context) }

    val bg      = Color(0xFF000000)
    val card    = Color(0xFF0A0A0A)
    val accent  = Color(0xFFB0BAF0)
    val text    = Color(0xFFE0E4FF)
    val subtext = Color(0xFF555566)
    val red     = Color(0xFFEF5350)
    val green   = Color(0xFF4CAF50)

    Box(Modifier.fillMaxSize().background(bg), contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth().padding(24.dp),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.spacedBy(14.dp)) {

            Text(if (status == LicenseStatus.CHEATER) "⛔" else "🎥", fontSize = 52.sp)
            Text("VCamSX", color = text, fontSize = 26.sp, fontWeight = FontWeight.Bold)

            Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp),
                 CardDefaults.cardColors(containerColor = card)) {
                Column(Modifier.padding(20.dp),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (status) {
                        LicenseStatus.FRESH -> {
                            Text("Welcome to VCamSX", color = accent, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Enter a license key to get started.\nRequest a free trial key or purchase full access.",
                                 color = subtext, fontSize = 13.sp, textAlign = TextAlign.Center)
                        }
                        is LicenseStatus.TRIAL -> {
                            Text("Free Trial Active", color = green, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("${status.daysLeft} days remaining", color = text, fontSize = 15.sp)
                            Button(onClick = onUnlocked, Modifier.fillMaxWidth().height(46.dp),
                                   RoundedCornerShape(12.dp),
                                   ButtonDefaults.buttonColors(containerColor = accent)) {
                                Text("Open App", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
                            }
                        }
                        LicenseStatus.EXPIRED -> {
                            Text("Trial Expired", color = red, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Your trial has ended.\nEnter a permanent key to continue.",
                                 color = subtext, fontSize = 13.sp, textAlign = TextAlign.Center)
                        }
                        LicenseStatus.CHEATER -> {
                            Text("Device Locked", color = red, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Time manipulation detected.\nThis device is permanently banned.",
                                 color = subtext, fontSize = 13.sp, textAlign = TextAlign.Center)
                            Text("Contact support with your Device ID to appeal.",
                                 color = subtext, fontSize = 11.sp, textAlign = TextAlign.Center)
                        }
                        else -> {}
                    }
                }
            }

            if (status != LicenseStatus.CHEATER) {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp),
                     CardDefaults.cardColors(containerColor = card)) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("License Key", color = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        OutlinedTextField(
                            value = keyInput,
                            onValueChange = { keyInput = it; errorMsg = "" },
                            placeholder = { Text("XXXX-XXXX-XXXX-XXXX", color = subtext) },
                            singleLine = true, modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = text, containerColor = Color.Transparent,
                                cursorColor = accent, focusedBorderColor = accent,
                                unfocusedBorderColor = Color(0xFF1A1A1A)
                            )
                        )
                        if (errorMsg.isNotEmpty())
                            Text(errorMsg, color = red, fontSize = 12.sp)

                        Button(
                            onClick = {
                                when (LicenseManager.activateKey(context, keyInput)) {
                                    KeyResult.VALID_TRIAL -> onUnlocked()
                                    KeyResult.VALID_PERMANENT -> onUnlocked()
                                    KeyResult.INVALID ->
                                        errorMsg = "Invalid key. Check and try again."
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = accent),
                            enabled = keyInput.length >= 4
                        ) {
                            Text("Activate", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Card(Modifier.fillMaxWidth(), RoundedCornerShape(12.dp),
                 CardDefaults.cardColors(containerColor = card)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Your Device ID:", color = subtext, fontSize = 11.sp)
                    Text(deviceId, color = text, fontSize = 13.sp,
                         fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
                         modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
