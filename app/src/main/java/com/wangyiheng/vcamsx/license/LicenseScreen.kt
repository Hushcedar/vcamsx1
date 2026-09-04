package com.wangyiheng.vcamsx.license

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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

    // ── Colors ────────────────────────────────────────────────────────────────
    val bg      = Color(0xFF080B12)
    val card    = Color(0xFF111622)
    val accent  = Color(0xFF00E5FF)
    val text    = Color(0xFFE0E4FF)
    val subtext = Color(0xFF5A6478)
    val red     = Color(0xFFFF4757)
    val green   = Color(0xFF00E676)
    val divider = Color(0xFF1C2333)

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Dot-grid background ───────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxSize().background(bg))
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 28f
            val dotR = 0.8f
            val dotColor = accent.copy(alpha = 0.07f)
            var y = 0f
            while (y < size.height) {
                var x = 0f
                while (x < size.width) {
                    drawCircle(dotColor, dotR, Offset(x, y))
                    x += step
                }
                y += step
            }
        }

        // ── Content ───────────────────────────────────────────────────────────
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // 1. Camera icon
            Text(
                text     = if (status == LicenseStatus.CHEATER) "⛔" else "🎥",
                fontSize = 52.sp
            )

            // 2. App title
            Text(
                "VCamSX",
                color      = text,
                fontSize   = 26.sp,
                fontWeight = FontWeight.Bold
            )

            // 3. Status card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = card)
            ) {
                Column(
                    modifier            = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (status) {
                        LicenseStatus.FRESH -> {
                            Text(
                                "Welcome to VCamSX",
                                color      = accent,
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Enter a license key to get started.\nNew user? Request a free trial key.",
                                color     = subtext,
                                fontSize  = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                        is LicenseStatus.TRIAL -> {
                            Text(
                                "Free Trial Active",
                                color      = green,
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${status.minsLeft} minutes remaining",
                                color    = text,
                                fontSize = 15.sp
                            )
                            Button(
                                onClick  = onUnlocked,
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                shape    = RoundedCornerShape(12.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = accent)
                            ) {
                                Text(
                                    "Open App",
                                    color      = Color(0xFF080B12),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        LicenseStatus.EXPIRED -> {
                            Text(
                                "Trial Expired",
                                color      = red,
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Your trial has ended.\nEnter a license key to continue.",
                                color     = subtext,
                                fontSize  = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                        LicenseStatus.CHEATER -> {
                            Text(
                                "Device Locked",
                                color      = red,
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Clock manipulation was detected.\nThis device is permanently banned.",
                                color     = subtext,
                                fontSize  = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                            Text(
                                "Contact support with your Device ID to appeal.",
                                color    = subtext,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        else -> {}
                    }
                }
            }

            // 4. Key entry card
            if (status != LicenseStatus.CHEATER) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(containerColor = card)
                ) {
                    Column(
                        modifier            = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "License Key",
                            color      = text,
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        OutlinedTextField(
                            value         = keyInput,
                            onValueChange = { newVal ->
                                val clean = newVal.filter { it.isLetterOrDigit() }.uppercase().take(16)
                                keyInput  = clean.chunked(4).joinToString("-")
                                errorMsg  = ""
                            },
                            placeholder = {
                                Text("XXXX-XXXX-XXXX-XXXX", color = subtext)
                            },
                            singleLine = true,
                            modifier   = Modifier.fillMaxWidth(),
                            colors     = TextFieldDefaults.outlinedTextFieldColors(
                                textColor            = text,
                                containerColor       = Color.Transparent,
                                cursorColor          = accent,
                                focusedBorderColor   = accent,
                                unfocusedBorderColor = divider
                            )
                        )
                        if (errorMsg.isNotEmpty()) {
                            Text(errorMsg, color = red, fontSize = 12.sp)
                        }
                        Button(
                            onClick = {
                                when (LicenseManager.activateKey(context, keyInput)) {
                                    KeyResult.VALID_PERMANENT -> onUnlocked()
                                    KeyResult.VALID_TRIAL     -> onUnlocked()
                                    KeyResult.ALREADY_USED    -> errorMsg = "This key has already been used on this device."
                                    KeyResult.EXPIRED         -> errorMsg = "This key has expired. Request a new one."
                                    KeyResult.INVALID         -> errorMsg = "Invalid key. Check and try again."
                                    KeyResult.OLD_FORMAT      -> errorMsg = "This key isn't compatible with this version."
                                    KeyResult.NO_INTERNET     -> errorMsg = "No internet connection. Please connect and retry."
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(46.dp),
                            shape    = RoundedCornerShape(12.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = accent),
                            enabled  = keyInput.length >= 4
                        ) {
                            Text(
                                "Activate",
                                color      = Color(0xFF080B12),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 5. Device ID card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(12.dp),
                colors   = CardDefaults.cardColors(containerColor = card)
            ) {
                Column(
                    modifier            = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Your Device ID (share this to get a key):",
                        color    = subtext,
                        fontSize = 11.sp
                    )
                    Text(
                        deviceId,
                        color      = text,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign  = TextAlign.Center,
                        modifier   = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
