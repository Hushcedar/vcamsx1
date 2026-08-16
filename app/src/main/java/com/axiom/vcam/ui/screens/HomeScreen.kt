package com.axiom.vcam.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axiom.vcam.media.ImagePlayer
import com.axiom.vcam.ui.components.SectionCard
import com.axiom.vcam.ui.components.VCamToggleRow
import com.axiom.vcam.ui.components.VCamButton

private val BgColor   = Color(0xFF1A1C2E)
private val CardColor = Color(0xFF252840)

@Composable
fun HomeScreen(onNavigateToApps: () -> Unit) {
    val context = LocalContext.current

    val imageActive  by ImagePlayer.isActive
    val imageHasImg  by ImagePlayer.hasImage
    val imageLoading by ImagePlayer.isLoading

    var videoEnabled by remember { mutableStateOf(false) }

    // Video picker
    val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        Toast.makeText(context, "Video selected — copy to app dir", Toast.LENGTH_SHORT).show()
        // TODO: copy uri to VideoProvider.getVideoFile(context) in a coroutine
    }

    // Image picker
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        ImagePlayer.loadImage(context, uri) { ok ->
            Toast.makeText(context, if (ok) "Image loaded ✓" else "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    Box(Modifier.fillMaxSize().background(BgColor)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                "VCam Revived",
                color      = Color.White,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // ── Media source ─────────────────────────────────────────────────
            SectionCard(title = "Media Source") {
                VCamButton(text = "Select Video") { videoLauncher.launch("video/*") }
                VCamButton(
                    text    = when {
                        imageLoading -> "Loading Image…"
                        imageHasImg  -> "Replace Image"
                        else         -> "Select Image"
                    },
                    enabled = !imageLoading
                ) { imageLauncher.launch("image/*") }
            }

            // ── Injection toggles ─────────────────────────────────────────────
            SectionCard(title = "Injection") {
                VCamToggleRow(
                    label   = "Inject Video",
                    checked = videoEnabled
                ) { on ->
                    videoEnabled = on
                    if (on) ImagePlayer.stop()
                }

                Divider(color = Color(0xFF3A3D5C), thickness = 0.5.dp)

                VCamToggleRow(
                    label   = if (imageLoading) "Inject Image (loading…)" else "Inject Image",
                    checked = imageActive,
                    enabled = !imageLoading
                ) { on ->
                    if (on) {
                        if (imageHasImg) {
                            videoEnabled = false
                            ImagePlayer.activateInjection()
                        } else {
                            Toast.makeText(context, "Select an image first", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        ImagePlayer.stop()
                    }
                }
            }

            // ── App manager ───────────────────────────────────────────────────
            SectionCard(title = "Cloned Apps") {
                VCamButton(
                    text  = "Manage Cloned Apps",
                    icon  = Icons.Default.Apps
                ) { onNavigateToApps() }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
