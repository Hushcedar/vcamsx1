package com.wangyiheng.vcamsx.modules.home.view

import android.Manifest
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
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

    val selectVideoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { homeController.copyVideoToAppDir(context, it) } }

    val selectImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        ImagePlayer.loadImage(context, uri) { ok ->
            Toast.makeText(context, if (ok) "Image loaded" else "Failed to load image", Toast.LENGTH_SHORT).show()
            if (ok) {
                ImagePlayer.activateInjection()
                context.sendBroadcast(android.content.Intent("com.wangyiheng.vcamsx.IMAGE_RELOAD"))
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted || Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
            selectVideoLauncher.launch("video/*")
        else
            Toast.makeText(context, "Grant storage permission in Settings", Toast.LENGTH_SHORT).show()
    }

    LivePlayerDialog(homeController)
    VideoPlayerDialog(homeController, context, videoPath)

    var showImagePreview by remember { mutableStateOf(false) }
    if (showImagePreview) {
        val previewBitmap = remember(imageHasImage) {
            if (imageHasImage) ImagePlayer.currentBitmapSnapshot() else null
        }
        AlertDialog(
            onDismissRequest = { showImagePreview = false },
            title   = { Text("Preview", color = TextHigh, fontWeight = FontWeight.SemiBold) },
            text    = {
                if (previewBitmap != null) {
                    androidx.compose.foundation.Image(
                        bitmap             = previewBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier           = Modifier
                            .fillMaxWidth()
                            .aspectRatio(previewBitmap.width.toFloat() / previewBitmap.height.coerceAtLeast(1))
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale       = androidx.compose.ui.layout.ContentScale.Fit
                    )
                } else {
                    Text("No image selected.", color = TextMid, fontSize = 14.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showImagePreview = false }) {
                    Text("Done", color = Cyan, fontWeight = FontWeight.Medium)
                }
            },
            containerColor = Surface,
            shape          = RoundedCornerShape(16.dp)
        )
    }

    Box(
        modifier         = Modifier.fillMaxSize().background(Obsidian),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "VCamSX",
                        color      = TextHigh,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                    Text(
                        "Injection Control",
                        color    = TextMid,
                        fontSize = 12.sp,
                        letterSpacing = 0.2.sp
                    )
                }
                // Status pill
                Row(
                    modifier          = Modifier
                        .background(GreenDim, RoundedCornerShape(20.dp))
                        .border(0.5.dp, Green.copy(0.3f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Green, CircleShape)
                    )
                    Text("Active", color = Green, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }

            // ── Stream URL card ───────────────────────────────────────────────
            SectionCard {
                Text(
                    "Stream URL",
                    color      = TextMid,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value         = homeController.liveURL.value,
                    onValueChange = { homeController.liveURL.value = it },
                    placeholder   = { Text("HLS · RTMP · RTSP · HTTP", color = TextLow, fontSize = 13.sp) },
                    singleLine    = true,
                    textStyle     = TextStyle(color = TextHigh, fontSize = 14.sp),
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(10.dp),
                    colors        = TextFieldDefaults.outlinedTextFieldColors(
                        textColor            = TextHigh,
                        containerColor       = InputFill,
                        cursorColor          = Cyan,
                        focusedBorderColor   = Cyan,
                        unfocusedBorderColor = StrokeDefault
                    )
                )
            }

            // ── Source selection card ─────────────────────────────────────────
            SectionCard {
                Text(
                    "Input Source",
                    color      = TextMid,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton("Live Stream") { homeController.isLiveStreamingDisplay.value = true }
                    AppButton("Select Video") {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                    AppButton(
                        text    = when {
                            imageIsLoading -> "Loading..."
                            imageHasImage  -> "Replace Image"
                            else           -> "Select Image"
                        },
                        enabled = !imageIsLoading
                    ) { selectImageLauncher.launch("image/*") }
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppButton(
                            text     = "View Image",
                            enabled  = imageHasImage,
                            modifier = Modifier.weight(1f)
                        ) { showImagePreview = true }
                        AppButton(
                            text     = "View Video",
                            modifier = Modifier.weight(1f)
                        ) { homeController.isVideoDisplay.value = true }
                    }
                }
            }

            // ── Overlay controls button ───────────────────────────────────────
            Button(
                onClick  = {
                    if (Settings.canDrawOverlays(context)) FloatingControlsService.start(context)
                    else Toast.makeText(context, "Grant 'Display over other apps' permission", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Cyan)
            ) {
                Text(
                    "Launch Overlay Controls",
                    color      = Obsidian,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    letterSpacing = 0.3.sp
                )
            }

            // ── Toggles card ──────────────────────────────────────────────────
            SectionCard(padding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)) {
                ToggleRow("Inject Video", homeController.isVideoEnabled.value) {
                    homeController.isVideoEnabled.value = it; homeController.saveState()
                }
                RowDivider()
                ToggleRow(
                    label   = if (imageIsLoading) "Inject Image  —  loading" else "Inject Image",
                    checked = imageIsActive,
                    enabled = !imageIsLoading
                ) { on ->
                    if (on) {
                        if (imageHasImage) {
                            homeController.isVideoEnabled.value = false
                            homeController.saveState()
                            ImagePlayer.activateInjection()
                            context.sendBroadcast(android.content.Intent("com.wangyiheng.vcamsx.IMAGE_RELOAD"))
                        } else Toast.makeText(context, "Select an image first", Toast.LENGTH_SHORT).show()
                    } else ImagePlayer.stop()
                }
                RowDivider()
                ToggleRow("Live Streaming", homeController.isLiveStreamingEnabled.value) {
                    homeController.isLiveStreamingEnabled.value = it; homeController.saveState()
                }
                RowDivider()
                ToggleRow("Inject Volume", homeController.isVolumeEnabled.value) {
                    homeController.isVolumeEnabled.value = it; homeController.saveState()
                }
                RowDivider()
                ToggleRow("Soft Decoding", homeController.codecType.value) {
                    homeController.codecType.value = it; homeController.saveState()
                }
                RowDivider()
                ToggleRow("Pass-through (Real Scene)", homeController.isRealSceneEnabled.value) {
                    homeController.isRealSceneEnabled.value = it; homeController.saveState()
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Shared components ─────────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, SurfaceStroke, RoundedCornerShape(16.dp)),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(padding), content = content)
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        modifier  = Modifier.padding(0.dp),
        thickness = 0.5.dp,
        color     = SurfaceStroke
    )
}

@Composable
fun AppButton(
    text:     String,
    enabled:  Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick:  () -> Unit
) {
    Button(
        onClick   = onClick,
        enabled   = enabled,
        modifier  = modifier.height(46.dp),
        shape     = RoundedCornerShape(10.dp),
        colors    = ButtonDefaults.buttonColors(
            containerColor         = CyanDim,
            disabledContainerColor = Color(0xFF0D111A)
        ),
        border    = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = if (enabled) Cyan.copy(alpha = 0.5f) else SurfaceStroke
        )
    ) {
        Text(
            text       = text,
            color      = if (enabled) Cyan else TextMid,
            fontWeight = FontWeight.Medium,
            fontSize   = 14.sp,
            letterSpacing = 0.2.sp
        )
    }
}

@Composable
fun ToggleRow(
    label:   String,
    checked: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(vertical = 13.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color    = if (enabled) TextHigh else TextMid,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        Switch(
            checked         = checked,
            onCheckedChange = onChange,
            enabled         = enabled,
            colors          = SwitchDefaults.colors(
                checkedThumbColor    = Obsidian,
                checkedTrackColor    = Cyan,
                uncheckedThumbColor  = TextMid,
                uncheckedTrackColor  = SurfaceStroke,
                uncheckedBorderColor = SurfaceStroke
            )
        )
    }
}
