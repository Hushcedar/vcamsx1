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
    val context = LocalContext.current
    var keyInput  by remember { mutableStateOf("") }
    var errorMsg  by remember { mutableStateOf("") }
    var deviceId  by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        deviceId = LicenseManager.getDeviceId(context)
    }

    val accent  = Color(0xFFB0BAF0)
    val bg      = Color(0xFF000000)
    val card    = Color(0xFF0A0A0A)
    val text    = Color(0xFFE0E4FF)
    val subtext = Color(0xFF555566)

    Box(modifier = Modifier.fillMaxSize().background(bg),
        contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("🎥", fontSize = 56.sp)
            Text("VCamSX", color = text, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            when (status) {
                is LicenseStatus.TRIAL -> {
                    Card(shape = RoundedCornerShape(16.dp),
                         colors = CardDefaults.cardColors(containerColor = card),
                         modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp),
                               horizontalAlignment = Alignment.CenterHorizontally,
                               verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Free Trial", color = Color(0xFF4CAF50),
                                 fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("${status.daysLeft} days remaining",
                                 color = text, fontSize = 15.sp)
                            Text("Full access to all features",
                                 color = subtext, fontSize = 13.sp)
                            Spacer(Modifier.height(4.dp))
                            Button(onClick = onUnlocked,
                                   modifier = Modifier.fillMaxWidth().height(48.dp),
                                   shape = RoundedCornerShape(12.dp),
                                   colors = ButtonDefaults.buttonColors(containerColor = accent)) {
                                Text("Continue", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                LicenseStatus.EXPIRED -> {
                    Card(shape = RoundedCornerShape(16.dp),
                         colors = CardDefaults.cardColors(containerColor = card),
                         modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp),
                               horizontalAlignment = Alignment.CenterHorizontally,
                               verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Trial Expired", color = Color(0xFFEF5350),
                                 fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Enter your license key to continue",
                                 color = subtext, fontSize = 13.sp, textAlign = TextAlign.Center)
                        }
                    }
                }
                else -> {}
            }

            Card(shape = RoundedCornerShape(16.dp),
                 colors = CardDefaults.cardColors(containerColor = card),
                 modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp),
                       verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("License Key", color = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)

                    OutlinedTextField(
                        value = keyInput,
                        onValueChange = { keyInput = it; errorMsg = "" },
                        placeholder = { Text("XXXX-XXXX-XXXX-XXXX", color = subtext) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = text, containerColor = Color.Transparent,
                            cursorColor = accent, focusedBorderColor = accent,
                            unfocusedBorderColor = Color(0xFF1A1A1A)
                        )
                    )

                    if (errorMsg.isNotEmpty()) {
                        Text(errorMsg, color = Color(0xFFEF5350), fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            when (LicenseManager.activateKey(context, keyInput)) {
                                KeyResult.VALID_PERMANENT, KeyResult.VALID_TRIAL_EXT -> onUnlocked()
                                KeyResult.INVALID -> errorMsg = "Invalid license key. Check and try again."
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accent),
                        enabled = keyInput.length >= 4
                    ) {
                        Text("Activate", color = Color(0xFF0A0A0A), fontWeight = FontWeight.Bold)
                    }

                    Divider(color = Color(0xFF1A1A1A))

                    Text("Device ID (share this to get your key):",
                         color = subtext, fontSize = 11.sp)
                    Text(deviceId, color = text, fontSize = 13.sp,
                         fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
                         modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
