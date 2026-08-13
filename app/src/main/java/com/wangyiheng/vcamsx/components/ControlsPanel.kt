package com.wangyiheng.vcamsx.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wangyiheng.vcamsx.utils.VideoControls
import com.wangyiheng.vcamsx.utils.VideoPlayer

private val CardColor   = Color(0xFF2E3140)
private val AccentColor = Color(0xFFB0BAF0)
private val TextColor   = Color(0xFFE0E4FF)
private val DimColor    = Color(0xFF454860)

@Composable
fun ControlsPanel(visible: Boolean, onDismiss: () -> Unit) {
    if (!visible) return
    val isPaused  by VideoControls.isPaused
    val rotation  by VideoControls.rotation
    val isFlipped by VideoControls.isFlipped

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(20.dp),
             colors = CardDefaults.cardColors(containerColor = CardColor)) {
            Column(modifier = Modifier.padding(20.dp),
                   verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Video Controls", color = TextColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Divider(color = DimColor)

                CtrlBtn(if (isPaused) "▶  Resume" else "⏸  Pause",
                        if (isPaused) Color(0xFF4CAF50) else AccentColor)
                { VideoPlayer.togglePause() }

                CtrlBtn("↺  Reload", AccentColor) { VideoPlayer.reload() }

                CtrlBtn("⟳  Rotate ($rotation°)", AccentColor) { VideoPlayer.rotate() }

                CtrlBtn(if (isFlipped) "↔  Flip  ON" else "↔  Flip  OFF",
                        if (isFlipped) Color(0xFFFF9800) else AccentColor)
                { VideoPlayer.flip() }

                Divider(color = DimColor)
                Button(onClick = onDismiss,
                       modifier = Modifier.fillMaxWidth().height(44.dp),
                       shape = RoundedCornerShape(10.dp),
                       colors = ButtonDefaults.buttonColors(containerColor = DimColor)) {
                    Text("Close", color = TextColor)
                }
            }
        }
    }
}

@Composable
fun CtrlBtn(label: String, color: Color, onClick: () -> Unit) {
    Button(onClick = onClick,
           modifier = Modifier.fillMaxWidth().height(48.dp),
           shape = RoundedCornerShape(12.dp),
           colors = ButtonDefaults.buttonColors(containerColor = color)) {
        Text(label, color = Color(0xFF1A1F35), fontWeight = FontWeight.Medium, fontSize = 15.sp)
    }
}
