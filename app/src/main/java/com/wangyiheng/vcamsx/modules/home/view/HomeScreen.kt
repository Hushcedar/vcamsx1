package com.wangyiheng.vcamsx.modules.home.view

import android.Manifest
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wangyiheng.vcamsx.FloatingControlsService
import com.wangyiheng.vcamsx.MainHook
import com.wangyiheng.vcamsx.components.LivePlayerDialog
import com.wangyiheng.vcamsx.components.VideoPlayerDialog
import com.wangyiheng.vcamsx.modules.home.controllers.HomeController
import com.wangyiheng.vcamsx.utils.ImagePlayer
import com.wangyiheng.vcamsx.utils.VideoControls

private val BgColor      = Color(0xFF3D4050)
private val CardColor    = Color(0xFF2E3140)
private val ButtonColor  = Color(0xFFB0BAF0)
private val TextColor    = Color(0xFFE0E4FF)
private val SubTextColor = Color(0xFF8890AA)
private val DividerColor = Color(0xFF454860)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context        = LocalContext.current
    val homeController = viewModel<HomeController>()
    val videoPath      = context.getExternalFilesDir(null)!!.absolutePath + "/copied_video.mp4"

    LaunchedEffect(Unit) { homeController.init() }

    // ── Image picker ──────────────────────────────────────────────────────────
    val selectImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { u ->
            ImagePlayer.loadImage(context, u) { ok ->
                if (ok) Toast.makeText(context, "Image loaded ✓", Toast.LENGTH_SHORT).show()
                else    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ── Video picker ──────────────────────────────────────────────────────────
    val selectVideoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { homeController.copyVideoToAppDir(context, it) } }

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

    // ── Image preview dialog ──────────────────────────────────────────────────
    var showImagePreview by remember { mutableStateOf(false) }
    if (showImagePreview) {
        val bmp = ImagePlayer.currentBitmap
        AlertDialog(
            onDismissRequest = { showImagePreview = false },
            title   = { Text("Image Preview", color = TextColor) },
            text    = {
                if (bmp != null) {
                    Image(
                        bitmap             = bmp.asImageBitmap(),
                        contentDescription = "Selected image",
                        modifier           = Modifier.fillMaxWidth()
                            .aspectRatio(bmp.width.toFloat() / bmp.height),
                        contentScale       = ContentScale.Fit
                    )
                } else {
                    Text("No image selected yet.", color = SubTextColor)
                }
            },
            confirmButton = {
                TextButton(onClick = { showImagePreview = false }) {
                    Text("Close", color = ButtonColor)
                }
            },
            containerColor = CardColor
        )
    }

    // ── Root layout ───────────────────────────────────────────────────────────
    Box(
        modifier           = Modifier.fillMaxSize().background(BgColor),
        contentAlignment   = Alignment.TopCenter
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Stream URL card ───────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = CardColor)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("HLS/HTTP/RTMP/RTSP", color = SubTextColor, fontSize = 11.sp)
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value         = homeController.liveURL.value,
                        onValueChange = { homeController.liveURL.value = it },
                        placeholder   = { Text("Stream URL", color = SubTextColor) },
                        singleLine    = true,
                        textStyle     = TextStyle(color = TextColor, fontSize = 15.sp),
                        modifier      = Modifier.fillMaxWidth(),
                        colors        = TextFieldDefaults.outlinedTextFieldColors(
                            textColor            = TextColor,
                            containerColor       = Color.Transparent,
                            cursorColor          = ButtonColor,
                            focusedBorderColor   = ButtonColor,
                            unfocusedBorderColor = SubTextColor
                        )
                    )
                }
            }

            // ── Action buttons card ───────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = CardColor)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppButton("RTMP-RTSP-HTTP")    { homeController.isLiveStreamingDisplay.value = true }
                    AppButton("Select Video")       { requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
                    AppButton("Select Image")       { selectImageLauncher.launch("image/*") }
                    AppButton("View Image")         { showImagePreview = true }
                    AppButton("View Video")         { homeController.isVideoDisplay.value = true }
                    AppButton("View Live Stream")   { homeController.isLiveStreamingDisplay.value = true }
                }
            }

            // ── Floating controls button ──────────────────────────────────────
            Button(
                onClick  = {
                    if (Settings.canDrawOverlays(context)) {
                        FloatingControlsService.start(context)
                    } else {
                        Toast.makeText(
                            context,
                            "Grant 'Display over other apps' permission first",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
            ) {
                Text("Controls", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            // ── Toggle switches card ──────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = CardColor)
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

                    // Inject Video
                    ToggleRow("Inject Video", homeController.isVideoEnabled.value) { on ->
                        homeController.isVideoEnabled.value = on
                        // If switching video ON while image is active, disable image
                        if (on && VideoControls.isImageEnabled.value) {
                            ImagePlayer.disable()
                        }
                        homeController.saveState()
                    }

                    Divider(color = DividerColor, thickness = 0.5.dp)

                    // Inject Image — driven by the Compose-observable VideoControls.isImageEnabled
                    ToggleRow("Inject Image", VideoControls.isImageEnabled.value) { on ->
                        if (on) {
                            if (ImagePlayer.currentBitmap != null) {
                                // Re-enable if previously stopped, or reattach on new surface
                                ImagePlayer.enable()
                            } else {
                                // Nothing loaded yet — bounce the toggle back to off
                                Toast.makeText(
                                    context,
                                    "Select an image first",
                                    Toast.LENGTH_SHORT
                                ).show()
                                VideoControls.isImageEnabled.value = false
                            }
                        } else {
                            // Disable rendering but keep bitmap in memory
                            ImagePlayer.disable()
                        }
                    }

                    Divider(color = DividerColor, thickness = 0.5.dp)

                    ToggleRow("Live Streaming Switch", homeController.isLiveStreamingEnabled.value) { on ->
                        homeController.isLiveStreamingEnabled.value = on; homeController.saveState()
                    }

                    Divider(color = DividerColor, thickness = 0.5.dp)

                    ToggleRow("Inject Volume", homeController.isVolumeEnabled.value) { on ->
                        homeController.isVolumeEnabled.value = on; homeController.saveState()
                    }

                    Divider(color = DividerColor, thickness = 0.5.dp)

                    ToggleRow("Soft Decoding", homeController.codecType.value) { on ->
                        homeController.codecType.value = on; homeController.saveState()
                    }

                    Divider(color = DividerColor, thickness = 0.5.dp)

                    ToggleRow("Open The Real Scene", homeController.isRealSceneEnabled.value) { on ->
                        homeController.isRealSceneEnabled.value = on; homeController.saveState()
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AppButton(text: String, onClick: () -> Unit) {
    Button(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = ButtonColor)
    ) {
        Text(text, color = Color(0xFF2E3140), fontWeight = FontWeight.Medium, fontSize = 15.sp)
    }
}

@Composable
fun ToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier            = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextColor, fontSize = 15.sp)
        Switch(
            checked         = checked,
            onCheckedChange = onChange,
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = Color.White,
                checkedTrackColor   = ButtonColor,
                uncheckedThumbColor = Color(0xFF888AAA),
                uncheckedTrackColor = Color(0xFF454860),
                uncheckedBorderColor= Color(0xFF454860)
            )
        )
    }
}
