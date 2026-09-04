package com.wangyiheng.vcamsx.modules.home.view

import android.Manifest
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wangyiheng.vcamsx.FloatingControlsService
import com.wangyiheng.vcamsx.components.LivePlayerDialog
import com.wangyiheng.vcamsx.components.VideoPlayerDialog
import com.wangyiheng.vcamsx.modules.home.controllers.HomeController
import com.wangyiheng.vcamsx.ui.theme.*
import com.wangyiheng.vcamsx.utils.ImagePlayer

// ── Local colors ──────────────────────────────────────────────────────────────
private val BG      = Color(0xFF000005)
private val CARD    = Color(0xFF07071A)
private val BORDER  = Color(0xFF1A1A40)
private val TEXT    = CyberText
private val SUB     = CyberSubtext
private val ACCENT  = NeonCyan
private val PURPLE  = NeonPurple
private val GREEN   = NeonGreen
private val RED     = NeonRed

@Composable
private fun Modifier.neonBorder(color: Color, radius: Int = 14): Modifier =
    this.border(
        width  = 1.dp,
        brush  = Brush.linearGradient(listOf(color.copy(0.8f), PURPLE.copy(0.5f), color.copy(0.8f))),
        shape  = RoundedCornerShape(radius.dp)
    ).drawBehind {
        drawRoundRect(
            color       = color.copy(0.08f),
            topLeft     = Offset(-3f, -3f),
            size        = size.copy(size.width + 6, size.height + 6),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius * 4f),
            style       = Stroke(6f)
        )
    }

@Composable
private fun CyberGrid() {
    val c = ACCENT.copy(0.03f)
    Box(Modifier.fillMaxSize().drawBehind {
        var x = 0f; while (x < size.width)  { drawLine(c, Offset(x,0f), Offset(x,size.height), 0.5f); x+=40f }
        var y = 0f; while (y < size.height) { drawLine(c, Offset(0f,y), Offset(size.width,y),  0.5f); y+=40f }
    })
}

@Composable
private fun PulsingDot(color: Color, size: Int = 8) {
    val alpha by rememberInfiniteTransition(label="dot").animateFloat(
        0.3f, 1f, infiniteRepeatable(tween(900, easing=FastOutSlowInEasing), RepeatMode.Reverse), label="a"
    )
    Box(Modifier.size(size.dp).background(color.copy(alpha), CircleShape))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context        = LocalContext.current
    val homeController = viewModel<HomeController>()
    val videoPath      = context.getExternalFilesDir(null)!!.absolutePath + "/copied_video.mp4"

    LaunchedEffect(Unit) { homeController.init() }

    val imageIsActive  by ImagePlayer.isActive
    val imageHasImage  by ImagePlayer.hasImage
    val imageIsLoading by ImagePlayer.isLoading

    val selectVideoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { homeController.copyVideoToAppDir(context, it) }
    }
    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        ImagePlayer.loadImage(context, uri) { ok ->
            Toast.makeText(context, if (ok) "Image loaded" else "Failed to load image", Toast.LENGTH_SHORT).show()
            if (ok) { ImagePlayer.activateInjection(); context.sendBroadcast(android.content.Intent("com.wangyiheng.vcamsx.IMAGE_RELOAD")) }
        }
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted || Build.VERSION.SDK_INT > Build.VERSION_CODES.P) selectVideoLauncher.launch("video/*")
        else Toast.makeText(context, "Grant storage permission in Settings", Toast.LENGTH_SHORT).show()
    }

    LivePlayerDialog(homeController)
    VideoPlayerDialog(homeController, context, videoPath)

    var showImagePreview by remember { mutableStateOf(false) }
    if (showImagePreview) {
        val bmp = remember(imageHasImage) { if (imageHasImage) ImagePlayer.currentBitmapSnapshot() else null }
        AlertDialog(
            onDismissRequest = { showImagePreview = false },
            title   = { Text("IMAGE PREVIEW", color = ACCENT, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp, fontSize = 14.sp) },
            text    = {
                if (bmp != null) {
                    androidx.compose.foundation.Image(bmp.asImageBitmap(), null,
                        modifier = Modifier.fillMaxWidth().aspectRatio(bmp.width.toFloat() / bmp.height.coerceAtLeast(1)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit)
                } else Text("No image selected.", color = SUB, fontFamily = FontFamily.Monospace)
            },
            confirmButton = { TextButton(onClick = { showImagePreview = false }) { Text("[ CLOSE ]", color = ACCENT, fontFamily = FontFamily.Monospace) } },
            containerColor = CARD,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Box(Modifier.fillMaxSize().background(BG)) {
        CyberGrid()

        // Ambient glows
        Box(Modifier.size(250.dp).offset((-60).dp, (-60).dp).background(Brush.radialGradient(listOf(ACCENT.copy(0.04f), Color.Transparent))))
        Box(Modifier.size(200.dp).align(Alignment.BottomEnd).offset(50.dp, 50.dp).background(Brush.radialGradient(listOf(PURPLE.copy(0.05f), Color.Transparent))))

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Header ────────────────────────────────────────────────────
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("VCAMSX", color = ACCENT, fontSize = 20.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp)
                    Text("// CONTROL PANEL", color = SUB, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PulsingDot(GREEN)
                    Text("ONLINE", color = GREEN, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }

            // ── Stream URL ────────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().neonBorder(PURPLE)
                    .background(Brush.linearGradient(listOf(CARD, Color(0xFF08082A))), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("// STREAM_URL", color = SUB, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Text("HLS / HTTP / RTMP / RTSP", color = SUB.copy(0.5f), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    OutlinedTextField(
                        value         = homeController.liveURL.value,
                        onValueChange = { homeController.liveURL.value = it },
                        placeholder   = { Text("rtmp://...", color = SUB, fontFamily = FontFamily.Monospace, fontSize = 13.sp) },
                        singleLine    = true,
                        textStyle     = TextStyle(color = ACCENT, fontFamily = FontFamily.Monospace, fontSize = 14.sp),
                        modifier      = Modifier.fillMaxWidth(),
                        colors        = TextFieldDefaults.outlinedTextFieldColors(
                            textColor            = ACCENT,
                            containerColor       = Color.Transparent,
                            cursorColor          = ACCENT,
                            focusedBorderColor   = ACCENT,
                            unfocusedBorderColor = BORDER
                        )
                    )
                }
            }

            // ── Source Buttons ────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().neonBorder(ACCENT)
                    .background(Brush.linearGradient(listOf(CARD, Color(0xFF050510))), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("// INPUT_SOURCE", color = SUB, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    CyberActionButton("RTMP · RTSP · HTTP", PURPLE) { homeController.isLiveStreamingDisplay.value = true }
                    CyberActionButton("SELECT VIDEO FILE", ACCENT) { requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
                    CyberActionButton(
                        label = when { imageIsLoading -> "LOADING IMAGE..." ; imageHasImage -> "REPLACE IMAGE" ; else -> "SELECT IMAGE" },
                        color = if (imageIsLoading) SUB else ACCENT,
                        enabled = !imageIsLoading
                    ) { selectImageLauncher.launch("image/*") }
                    CyberActionButton("VIEW IMAGE", if (imageHasImage) GREEN else SUB, enabled = imageHasImage) { showImagePreview = true }
                    CyberActionButton("VIEW VIDEO",       ACCENT) { homeController.isVideoDisplay.value = true }
                    CyberActionButton("VIEW LIVE STREAM", ACCENT) { homeController.isLiveStreamingDisplay.value = true }
                }
            }

            // ── Floating Controls Launch ───────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().neonBorder(NeonOrange)
                    .background(NeonOrange.copy(0.06f), RoundedCornerShape(14.dp))
                    .padding(2.dp)
            ) {
                Button(
                    onClick = {
                        if (Settings.canDrawOverlays(context)) FloatingControlsService.start(context)
                        else Toast.makeText(context, "Grant 'Display over other apps' permission first", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("⬡  LAUNCH OVERLAY CONTROLS", color = NeonOrange,
                        fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                        fontSize = 13.sp, letterSpacing = 2.sp)
                }
            }

            // ── Toggle Panel ──────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().neonBorder(ACCENT.copy(0.5f))
                    .background(Brush.linearGradient(listOf(CARD, Color(0xFF060618))), RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    Text("// INJECTION_FLAGS", color = SUB, fontSize = 10.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(vertical = 8.dp))

                    CyberToggleRow("INJECT VIDEO", homeController.isVideoEnabled.value) {
                        homeController.isVideoEnabled.value = it; homeController.saveState()
                    }
                    CyberDivider()
                    CyberToggleRow(
                        label   = if (imageIsLoading) "INJECT IMAGE  [loading…]" else "INJECT IMAGE",
                        checked = imageIsActive,
                        enabled = !imageIsLoading
                    ) { on ->
                        if (on) {
                            if (imageHasImage) {
                                homeController.isVideoEnabled.value = false; homeController.saveState()
                                ImagePlayer.activateInjection()
                                context.sendBroadcast(android.content.Intent("com.wangyiheng.vcamsx.IMAGE_RELOAD"))
                            } else Toast.makeText(context, "Select an image first", Toast.LENGTH_SHORT).show()
                        } else ImagePlayer.stop()
                    }
                    CyberDivider()
                    CyberToggleRow("LIVE STREAMING", homeController.isLiveStreamingEnabled.value) {
                        homeController.isLiveStreamingEnabled.value = it; homeController.saveState()
                    }
                    CyberDivider()
                    CyberToggleRow("INJECT VOLUME", homeController.isVolumeEnabled.value) {
                        homeController.isVolumeEnabled.value = it; homeController.saveState()
                    }
                    CyberDivider()
                    CyberToggleRow("SOFT DECODE", homeController.codecType.value) {
                        homeController.codecType.value = it; homeController.saveState()
                    }
                    CyberDivider()
                    CyberToggleRow("PASSTHROUGH (REAL SCENE)", homeController.isRealSceneEnabled.value) {
                        homeController.isRealSceneEnabled.value = it; homeController.saveState()
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CyberDivider() {
    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(
        Brush.horizontalGradient(listOf(Color.Transparent, BORDER, ACCENT.copy(0.3f), BORDER, Color.Transparent))
    ))
}

@Composable
fun CyberActionButton(label: String, color: Color, enabled: Boolean = true, onClick: () -> Unit) {
    val c = if (enabled) color else SUB.copy(0.3f)
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = Modifier.fillMaxWidth().height(46.dp),
        shape    = RoundedCornerShape(10.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = c.copy(0.1f), disabledContainerColor = SUB.copy(0.05f)),
        border   = androidx.compose.foundation.BorderStroke(0.5.dp, c.copy(0.6f))
    ) {
        Text(label, color = c, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium, fontSize = 12.sp, letterSpacing = 1.sp)
    }
}

@Composable
fun CyberToggleRow(label: String, checked: Boolean, enabled: Boolean = true, onChange: (Boolean) -> Unit) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PulsingDot(if (checked && enabled) GREEN else SUB, 6)
            Text(label, color = if (enabled) TEXT else SUB, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onChange,
            enabled         = enabled,
            colors          = SwitchDefaults.colors(
                checkedThumbColor    = CyberBlack,
                checkedTrackColor    = GREEN,
                uncheckedThumbColor  = SUB,
                uncheckedTrackColor  = BORDER,
                uncheckedBorderColor = BORDER
            )
        )
    }
}

// Keep old names for compatibility if referenced elsewhere
@Composable
fun AppButton(text: String, enabled: Boolean = true, onClick: () -> Unit) =
    CyberActionButton(text, NeonCyan, enabled, onClick)

@Composable
fun ToggleRow(label: String, checked: Boolean, enabled: Boolean = true, onChange: (Boolean) -> Unit) =
    CyberToggleRow(label, checked, enabled, onChange)
