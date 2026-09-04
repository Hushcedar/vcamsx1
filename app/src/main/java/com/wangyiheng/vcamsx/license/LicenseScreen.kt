package com.wangyiheng.vcamsx.license

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wangyiheng.vcamsx.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(status: LicenseStatus, onUnlocked: () -> Unit) {
    val context  = LocalContext.current
    var keyInput by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    val deviceId = remember { LicenseManager.getDeviceId(context) }

    Box(
        modifier         = Modifier.fillMaxSize().background(Obsidian),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Brand header ──────────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "VCamSX",
                    color         = TextHigh,
                    fontSize      = 28.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    "Secure Access Gateway",
                    color         = TextMid,
                    fontSize      = 12.sp,
                    letterSpacing = 0.5.sp
                )
            }

            // ── Status card ───────────────────────────────────────────────────
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, SurfaceStroke, RoundedCornerShape(16.dp)),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier            = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (status) {
                        LicenseStatus.FRESH -> {
                            Text(
                                "No Active License",
                                color      = TextHigh,
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Enter your license key below to get started.\nNeed a key? Request a free trial.",
                                color     = TextMid,
                                fontSize  = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )
                        }
                        is LicenseStatus.TRIAL -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Green, androidx.compose.foundation.shape.CircleShape)
                                )
                                Text(
                                    "Trial License Active",
                                    color      = Green,
                                    fontSize   = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                "${status.minsLeft} minutes remaining",
                                color    = TextHigh,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Button(
                                onClick  = onUnlocked,
                                modifier = Modifier.fillMaxWidth().height(44.dp),
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = Cyan)
                            ) {
                                Text(
                                    "Continue to App",
                                    color      = Obsidian,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 14.sp
                                )
                            }
                        }
                        LicenseStatus.EXPIRED -> {
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Red, androidx.compose.foundation.shape.CircleShape)
                                )
                                Text(
                                    "License Expired",
                                    color      = Red,
                                    fontSize   = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                "Your trial period has ended.\nEnter a new key to continue.",
                                color     = TextMid,
                                fontSize  = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )
                        }
                        LicenseStatus.CHEATER -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(RedDim, RoundedCornerShape(10.dp))
                                    .border(0.5.dp, Red.copy(0.3f), RoundedCornerShape(10.dp))
                                    .padding(16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        "Device Blocked",
                                        color      = Red,
                                        fontSize   = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign  = TextAlign.Center
                                    )
                                    Text(
                                        "Clock tampering was detected on this device.\nAccess has been permanently revoked.",
                                        color     = Red.copy(0.7f),
                                        fontSize  = 12.sp,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 18.sp
                                    )
                                    Text(
                                        "Contact support with your Device ID to appeal.",
                                        color     = TextMid,
                                        fontSize  = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            // ── Key entry card ────────────────────────────────────────────────
            if (status != LicenseStatus.CHEATER) {
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, SurfaceStroke, RoundedCornerShape(16.dp)),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = Surface),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier            = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "License Key",
                            color      = TextMid,
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                        OutlinedTextField(
                            value         = keyInput,
                            onValueChange = { newVal ->
                                val clean = newVal.filter { it.isLetterOrDigit() }.uppercase().take(16)
                                keyInput  = clean.chunked(4).joinToString("-")
                                errorMsg  = ""
                            },
                            placeholder = {
                                Text(
                                    "XXXX-XXXX-XXXX-XXXX",
                                    color    = TextLow,
                                    fontSize = 14.sp
                                )
                            },
                            singleLine = true,
                            modifier   = Modifier.fillMaxWidth(),
                            shape      = RoundedCornerShape(10.dp),
                            colors     = TextFieldDefaults.outlinedTextFieldColors(
                                textColor            = TextHigh,
                                containerColor       = InputFill,
                                cursorColor          = Cyan,
                                focusedBorderColor   = Cyan,
                                unfocusedBorderColor = StrokeDefault
                            )
                        )

                        if (errorMsg.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(RedDim, RoundedCornerShape(8.dp))
                                    .border(0.5.dp, Red.copy(0.25f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(errorMsg, color = Red, fontSize = 12.sp, lineHeight = 17.sp)
                            }
                        }

                        Button(
                            onClick = {
                                when (LicenseManager.activateKey(context, keyInput)) {
                                    KeyResult.VALID_PERMANENT -> onUnlocked()
                                    KeyResult.VALID_TRIAL     -> onUnlocked()
                                    KeyResult.ALREADY_USED    -> errorMsg = "This key is already linked to another device."
                                    KeyResult.EXPIRED         -> errorMsg = "This key has expired. Request a new one."
                                    KeyResult.INVALID         -> errorMsg = "Invalid key. Please check and try again."
                                    KeyResult.OLD_FORMAT      -> errorMsg = "This key isn't compatible with this version."
                                    KeyResult.NO_INTERNET     -> errorMsg = "No internet connection. Please connect and retry."
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(46.dp),
                            shape    = RoundedCornerShape(10.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = Cyan),
                            enabled  = keyInput.length >= 4
                        ) {
                            Text(
                                "Verify & Activate",
                                color      = Obsidian,
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 14.sp,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }

            // ── Device ID card ────────────────────────────────────────────────
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, SurfaceStroke, RoundedCornerShape(12.dp)),
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier            = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Device ID",
                        color         = TextMid,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        deviceId,
                        color      = TextHigh,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign  = TextAlign.Center,
                        modifier   = Modifier.fillMaxWidth(),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "Share this ID to receive a license key",
                        color     = TextMid,
                        fontSize  = 10.sp,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
