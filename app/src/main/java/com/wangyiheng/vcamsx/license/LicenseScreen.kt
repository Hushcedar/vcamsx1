package com.wangyiheng.vcamsx.license

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wangyiheng.vcamsx.MainActivity
import com.wangyiheng.vcamsx.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ── Cyber colors local ────────────────────────────────────────────────────────
private val BG       = Color(0xFF000005)
private val CARD     = Color(0xFF07071A)
private val ACCENT   = NeonCyan
private val PURPLE   = NeonPurple
private val GREEN    = NeonGreen
private val RED      = NeonRed
private val TEXT     = CyberText
private val SUBTEXT  = CyberSubtext
private val BORDER   = Color(0xFF1A1A40)

private fun isOnline(context: Context): Boolean {
    val cm   = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val net  = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(net) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
           caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

// ── Glow border modifier ──────────────────────────────────────────────────────
@Composable
fun Modifier.cyberGlowBorder(color: Color, radius: Dp = 16.dp, glowAlpha: Float = 0.6f): Modifier {
    val glow = color.copy(alpha = glowAlpha)
    return this
        .border(width = 1.dp, brush = Brush.linearGradient(listOf(color, PURPLE, color)), shape = RoundedCornerShape(radius))
        .drawBehind {
            drawRoundRect(
                color  = glow.copy(alpha = 0.15f),
                size   = size.copy(width = size.width + 8, height = size.height + 8),
                topLeft = Offset(-4f, -4f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius.toPx()),
                style  = Stroke(width = 8f)
            )
        }
}

// ── Grid background ───────────────────────────────────────────────────────────
@Composable
fun CyberGrid(modifier: Modifier = Modifier) {
    val gridColor = ACCENT.copy(alpha = 0.04f)
    Box(modifier = modifier.drawBehind {
        val step = 40f
        var x = 0f
        while (x < size.width) {
            drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.5f)
            x += step
        }
        var y = 0f
        while (y < size.height) {
            drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.5f)
            y += step
        }
    })
}

// ── Pulsing glow dot ─────────────────────────────────────────────────────────
@Composable
fun GlowDot(color: Color, size: Dp = 8.dp) {
    val pulse by rememberInfiniteTransition(label = "dot").animateFloat(
        initialValue = 0.4f, targetValue = 1f, label = "pulse",
        animationSpec = infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse)
    )
    Box(
        modifier = Modifier
            .size(size)
            .background(color.copy(alpha = pulse), shape = androidx.compose.foundation.shape.CircleShape)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen(status: LicenseStatus, onUnlocked: () -> Unit) {
    val context  = LocalContext.current
    val scope    = rememberCoroutineScope()
    var keyInput     by remember { mutableStateOf("") }
    var errorMsg     by remember { mutableStateOf("") }
    var isActivating by remember { mutableStateOf(false) }
    var online       by remember { mutableStateOf(isOnline(context)) }
    val deviceId     = remember { LicenseManager.getDeviceId(context) }

    // Title glow pulse
    val titleGlow by rememberInfiniteTransition(label = "title").animateFloat(
        initialValue = 0.5f, targetValue = 1f, label = "glow",
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse)
    )

    Box(modifier = Modifier.fillMaxSize().background(BG)) {
        // Grid background
        CyberGrid(modifier = Modifier.fillMaxSize())

        // Ambient glow blobs
        Box(modifier = Modifier.size(300.dp).offset((-80).dp, (-80).dp)
            .background(Brush.radialGradient(listOf(ACCENT.copy(0.06f), Color.Transparent))))
        Box(modifier = Modifier.size(300.dp).align(Alignment.BottomEnd).offset(80.dp, 80.dp)
            .background(Brush.radialGradient(listOf(PURPLE.copy(0.06f), Color.Transparent))))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Header ──────────────────────────────────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("🎥", fontSize = 48.sp)
                Text(
                    "VCAMSX",
                    fontSize    = 32.sp,
                    fontWeight  = FontWeight.Black,
                    fontFamily  = FontFamily.Monospace,
                    color       = ACCENT.copy(alpha = titleGlow),
                    letterSpacing = 8.sp
                )
                Text(
                    "v${MainActivity.APP_VERSION}",
                    fontSize   = 11.sp,
                    color      = SUBTEXT,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp
                )
                Text(
                    "CAMERA INJECTION MODULE",
                    fontSize      = 9.sp,
                    color         = PURPLE.copy(alpha = 0.7f),
                    fontFamily    = FontFamily.Monospace,
                    letterSpacing = 3.sp
                )
            }

            // ── No internet banner ───────────────────────────────────────────
            if (!online) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .cyberGlowBorder(RED, 12.dp)
                        .background(RED.copy(0.05f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("⚠", color = RED, fontSize = 16.sp)
                        Column(Modifier.weight(1f)) {
                            Text("NO SIGNAL", color = RED, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Text("Internet required to activate", color = RED.copy(0.6f), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                        TextButton(onClick = { online = isOnline(context) }, contentPadding = PaddingValues(4.dp)) {
                            Text("RETRY", color = ACCENT, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            // ── Status card ──────────────────────────────────────────────────
            val statusBorder = when (status) {
                is LicenseStatus.TRIAL            -> GREEN
                LicenseStatus.LICENSED_PERMANENT  -> ACCENT
                LicenseStatus.EXPIRED             -> RED
                LicenseStatus.CHEATER             -> RED
                else                              -> PURPLE
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .cyberGlowBorder(statusBorder, 16.dp)
                    .background(
                        Brush.linearGradient(listOf(CARD, Color(0xFF0A0A25))),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (status) {
                        LicenseStatus.FRESH -> {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlowDot(PURPLE)
                                Text("AWAITING AUTHORIZATION", color = PURPLE, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Text("Enter license key to initialize module\nNew user? Request a trial key.",
                                color = SUBTEXT, fontSize = 12.sp, textAlign = TextAlign.Center, fontFamily = FontFamily.Monospace)
                        }
                        is LicenseStatus.TRIAL -> {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlowDot(GREEN)
                                Text("TRIAL LICENSE ACTIVE", color = GREEN, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Text("${status.minsLeft} MINUTES REMAINING", color = TEXT, fontSize = 14.sp, fontFamily = FontFamily.Monospace)
                            CyberButton("[ LAUNCH MODULE ]", GREEN) { onUnlocked() }
                        }
                        LicenseStatus.EXPIRED -> {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlowDot(RED)
                                Text("LICENSE EXPIRED", color = RED, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Text("Trial ended. Enter a new key to continue.", color = SUBTEXT, fontSize = 12.sp, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center)
                        }
                        LicenseStatus.CHEATER -> {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlowDot(RED)
                                Text("⛔  DEVICE BANNED", color = RED, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                            Text("Clock manipulation detected.\nThis device is permanently locked.", color = RED.copy(0.7f), fontSize = 11.sp, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center)
                            Text("Contact support with Device ID to appeal.", color = SUBTEXT, fontSize = 10.sp, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center)
                        }
                        else -> {}
                    }
                }
            }

            // ── Key entry ────────────────────────────────────────────────────
            if (status != LicenseStatus.CHEATER) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .cyberGlowBorder(ACCENT.copy(if (online) 0.5f else 0.15f), 16.dp)
                        .background(Brush.linearGradient(listOf(CARD, Color(0xFF050515))), RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("// LICENSE KEY", color = SUBTEXT, fontSize = 10.sp, fontFamily = FontFamily.Monospace)

                        OutlinedTextField(
                            value         = keyInput,
                            onValueChange = { newVal ->
                                val clean = newVal.filter { it.isLetterOrDigit() }.uppercase().take(16)
                                keyInput  = clean.chunked(4).joinToString("-")
                                errorMsg  = ""
                            },
                            placeholder = { Text("XXXX-XXXX-XXXX-XXXX", color = SUBTEXT, fontFamily = FontFamily.Monospace, fontSize = 14.sp) },
                            singleLine  = true,
                            modifier    = Modifier.fillMaxWidth(),
                            enabled     = online && !isActivating,
                            textStyle   = androidx.compose.ui.text.TextStyle(
                                color      = ACCENT,
                                fontFamily = FontFamily.Monospace,
                                fontSize   = 16.sp,
                                letterSpacing = 2.sp
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor            = ACCENT,
                                containerColor       = Color.Transparent,
                                cursorColor          = ACCENT,
                                focusedBorderColor   = ACCENT,
                                unfocusedBorderColor = BORDER,
                                disabledBorderColor  = BORDER.copy(0.4f),
                                disabledTextColor    = SUBTEXT
                            )
                        )

                        if (errorMsg.isNotEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .background(RED.copy(0.08f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(errorMsg, color = RED, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            }
                        }

                        // Activate button
                        val btnColor = when {
                            !online      -> SUBTEXT.copy(0.3f)
                            isActivating -> ACCENT.copy(0.5f)
                            else         -> ACCENT
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .cyberGlowBorder(btnColor, 10.dp)
                                .background(btnColor.copy(0.1f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = {
                                    online = isOnline(context)
                                    if (!online) { errorMsg = "NO SIGNAL — Connect to internet first"; return@Button }
                                    scope.launch {
                                        isActivating = true; errorMsg = ""
                                        val result = withContext(Dispatchers.IO) {
                                            LicenseManager.activateKey(context, keyInput)
                                        }
                                        isActivating = false
                                        when (result) {
                                            KeyResult.VALID_PERMANENT -> onUnlocked()
                                            KeyResult.VALID_TRIAL     -> onUnlocked()
                                            KeyResult.ALREADY_USED    -> errorMsg = "ERR: KEY_USED — This key is bound to another device"
                                            KeyResult.EXPIRED         -> errorMsg = "ERR: KEY_EXPIRED — Request a new key"
                                            KeyResult.INVALID         -> errorMsg = "ERR: KEY_INVALID — Check and try again"
                                            KeyResult.OLD_FORMAT      -> errorMsg = "ERR: KEY_VERSION — Key not compatible with v${MainActivity.APP_VERSION}"
                                            KeyResult.NO_INTERNET     -> { online = false; errorMsg = "ERR: NO_SIGNAL — Connect and retry" }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                                shape    = RoundedCornerShape(10.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                enabled  = online && keyInput.length >= 4 && !isActivating,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                if (isActivating) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = ACCENT)
                                    Spacer(Modifier.width(8.dp))
                                    Text("AUTHENTICATING...", color = ACCENT, fontFamily = FontFamily.Monospace, fontSize = 12.sp, letterSpacing = 2.sp)
                                } else {
                                    Text(
                                        if (online) "[ ACTIVATE ]" else "[ NO SIGNAL ]",
                                        color         = if (online) ACCENT else SUBTEXT,
                                        fontFamily    = FontFamily.Monospace,
                                        fontWeight    = FontWeight.Bold,
                                        fontSize      = 14.sp,
                                        letterSpacing = 4.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Device ID ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CARD.copy(0.6f), RoundedCornerShape(12.dp))
                    .border(0.5.dp, BORDER, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("// DEVICE_ID", color = SUBTEXT, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text(deviceId, color = PURPLE.copy(0.8f), fontSize = 12.sp, fontFamily = FontFamily.Monospace,
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun CyberButton(label: String, color: Color = NeonCyan, onClick: () -> Unit) {
    Button(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().height(46.dp),
        shape    = RoundedCornerShape(10.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = color.copy(0.15f)),
        border   = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(label, color = color, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 2.sp)
    }
}
