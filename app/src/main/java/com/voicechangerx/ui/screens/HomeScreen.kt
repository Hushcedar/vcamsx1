package com.voicechangerx.ui.screens

import android.content.Context
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.voicechangerx.audio.*
import com.voicechangerx.service.FloatingControlsService

private val BG     = Color(0xFF0A0A0F)
private val Card1  = Color(0xFF13131A)
private val Card2  = Color(0xFF1E1E28)
private val Green  = Color(0xFF6EE7B7)
private val Purple = Color(0xFF7C6EFA)
private val TextSec = Color(0xFF8888A8)
private val TextPri = Color(0xFFEEEEF5)

@Composable
fun HomeScreen() {
    val ctx       = LocalContext.current
    val enabled   by AudioControls.isEnabled.collectAsState()
    val pitch     by AudioControls.pitchSemitones.collectAsState()
    val gender    by AudioControls.genderMode.collectAsState()
    val noise     by AudioControls.noiseSuppPress.collectAsState()
    val robot     by AudioControls.robotEffect.collectAsState()
    val preset    by AudioControls.activePreset.collectAsState()

    Column(
        Modifier.fillMaxSize().background(BG)
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(40.dp))

        // Header
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("VoiceChangerX", color = Green, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("LSPosed Audio Hook", color = TextSec, fontSize = 12.sp)
            }
            Switch(
                checked = enabled,
                onCheckedChange = { AudioControlReceiver.send(ctx, "com.voicechangerx.TOGGLE") },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = Green)
            )
        }

        // Presets
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Card1)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PRESETS", color = Green, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Preset.values().filter { it != Preset.NONE }.forEach { p ->
                        FilterChip(
                            selected = preset == p,
                            onClick = {
                                AudioControlReceiver.send(ctx, "com.voicechangerx.PRESET_APPLY", mapOf("preset" to p.name))
                                AudioControls.activePreset.value = p
                                AudioControls.pitchSemitones.value = p.pitch
                                AudioControls.genderMode.value = p.gender
                                AudioControls.noiseSuppPress.value = p.noise
                                AudioControls.robotEffect.value = p.robot
                            },
                            label = { Text(p.label, fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Green, selectedLabelColor = Color.Black,
                                containerColor = Card2, labelColor = TextPri
                            )
                        )
                    }
                }
            }
        }

        // Pitch slider
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Card1)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("PITCH", color = Green, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    Text("${if (pitch > 0) "+" else ""}${pitch.toInt()} st", color = Green, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = pitch,
                    onValueChange = {
                        AudioControls.pitchSemitones.value = it
                        ctx.getSharedPreferences("vcx_prefs", Context.MODE_PRIVATE).edit().putFloat("pitch", it).apply()
                    },
                    valueRange = -12f..12f, steps = 23,
                    colors = SliderDefaults.colors(thumbColor = Green, activeTrackColor = Green, inactiveTrackColor = Card2)
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf(-6f, -3f, 0f, 3f, 6f).forEach { v ->
                        TextButton(onClick = {
                            AudioControls.pitchSemitones.value = v
                            ctx.getSharedPreferences("vcx_prefs", Context.MODE_PRIVATE).edit().putFloat("pitch", v).apply()
                        }) {
                            Text("${if (v > 0) "+" else ""}${v.toInt()}", color = if (pitch == v) Green else TextSec, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Gender filter
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Card1)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("GENDER FILTER", color = Green, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        GenderMode.OFF to "Off",
                        GenderMode.MALE_TO_FEMALE to "→ Female",
                        GenderMode.FEMALE_TO_MALE to "→ Male"
                    ).forEach { (mode, label) ->
                        val sel = gender == mode
                        Button(
                            onClick = {
                                AudioControls.genderMode.value = mode
                                AudioControlReceiver.send(ctx, when (mode) {
                                    GenderMode.OFF -> "com.voicechangerx.GENDER_OFF"
                                    GenderMode.MALE_TO_FEMALE -> "com.voicechangerx.GENDER_FEMALE"
                                    GenderMode.FEMALE_TO_MALE -> "com.voicechangerx.GENDER_MALE"
                                })
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (sel) Green else Card2),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Text(label, color = if (sel) Color.Black else TextPri, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        // Toggles
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Card1)) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                listOf(
                    Triple("Noise Suppression", noise, "com.voicechangerx.NOISE_TOGGLE"),
                    Triple("Robot Effect", robot, "com.voicechangerx.ROBOT_TOGGLE")
                ).forEachIndexed { idx, (label, checked, action) ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(label, color = TextPri, fontSize = 15.sp)
                        Switch(
                            checked = checked,
                            onCheckedChange = {
                                AudioControlReceiver.send(ctx, action)
                                when (action) {
                                    "com.voicechangerx.NOISE_TOGGLE" -> AudioControls.noiseSuppPress.value = !checked
                                    "com.voicechangerx.ROBOT_TOGGLE" -> AudioControls.robotEffect.value = !checked
                                }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = Green, uncheckedTrackColor = Card2)
                        )
                    }
                    if (idx == 0) Divider(color = Card2, thickness = 0.5.dp)
                }
            }
        }

        // Floating overlay button
        Button(
            onClick = {
                if (Settings.canDrawOverlays(ctx)) FloatingControlsService.start(ctx)
                else Toast.makeText(ctx, "Grant 'Display over other apps' permission first", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Launch In-Call Controls", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))
    }
}
