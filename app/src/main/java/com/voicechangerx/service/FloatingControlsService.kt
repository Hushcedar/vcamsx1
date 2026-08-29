package com.voicechangerx.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.*
import android.view.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.*
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.*
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.voicechangerx.audio.AudioControlReceiver
import com.voicechangerx.audio.AudioControls

private val BG      = Color(0xFF13131A)
private val Green   = Color(0xFF6EE7B7)
private val GreenDim = Color(0x336EE7B7)
private val TextSec = Color(0xFF8888A8)
private val Card2   = Color(0xFF1E1E28)

class FloatingControlsService : Service(), LifecycleOwner, SavedStateRegistryOwner {

    private lateinit var wm: WindowManager
    private var fv: android.view.View? = null
    private val lr  = LifecycleRegistry(this)
    private val src = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lr
    override val savedStateRegistry: SavedStateRegistry get() = src.savedStateRegistry

    companion object {
        fun start(ctx: Context) = ctx.startForegroundService(Intent(ctx, FloatingControlsService::class.java))
        fun stop(ctx: Context)  = ctx.stopService(Intent(ctx, FloatingControlsService::class.java))
    }

    override fun onCreate() {
        super.onCreate()
        src.performRestore(null)
        lr.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        createChannel()
        startForeground(2, buildNote())
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        show()
    }

    private fun show() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = 20; y = 300 }

        val cv = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@FloatingControlsService)
            setViewTreeSavedStateRegistryOwner(this@FloatingControlsService)
            setContent {
                FloatingUI(
                    ctx     = this@FloatingControlsService,
                    onMove  = { dx, dy -> params.x += dx.toInt(); params.y += dy.toInt(); wm.updateViewLayout(fv, params) },
                    onClose = { stop(this@FloatingControlsService) }
                )
            }
        }
        fv = cv
        lr.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lr.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        wm.addView(cv, params)
    }

    override fun onDestroy() {
        lr.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fv?.let { wm.removeView(it) }
        super.onDestroy()
    }

    override fun onBind(i: Intent?) = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel("vcx", "VoiceChangerX", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(ch)
        }
    }

    private fun buildNote() = Notification.Builder(this, "vcx")
        .setContentTitle("VoiceChangerX — voice effects active")
        .setSmallIcon(android.R.drawable.ic_btn_speak_now).build()
}

@Composable
fun FloatingUI(ctx: Context, onMove: (Float, Float) -> Unit, onClose: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val pitch   by AudioControls.pitchSemitones.collectAsState()
    val enabled by AudioControls.isEnabled.collectAsState()

    if (!expanded) {
        Box(
            Modifier.size(52.dp)
                .pointerInput(Unit) { detectDragGestures { _, d -> onMove(d.x, d.y) } }
                .clip(CircleShape).background(BG)
                .drawBehind { drawCircle(color = if (enabled) Green else TextSec, radius = size.minDimension / 2f, style = Stroke(2f)) },
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick  = { expanded = true },
                modifier = Modifier.fillMaxSize(),
                shape    = CircleShape,
                colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) { Text("🎙", fontSize = 22.sp) }
        }
    } else {
        Box(
            Modifier.width(220.dp)
                .clip(RoundedCornerShape(18.dp)).background(BG)
                .drawBehind { drawRoundRect(color = GreenDim, size = size, cornerRadius = CornerRadius(18.dp.toPx()), style = Stroke(1.5f)) }
                .pointerInput(Unit) { detectDragGestures { _, d -> onMove(d.x, d.y) } }
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("🎙 VCX", color = Green, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    Row {
                        TextButton(onClick = { AudioControlReceiver.send(ctx, "com.voicechangerx.TOGGLE") }, contentPadding = PaddingValues(4.dp)) {
                            Text(if (enabled) "ON" else "OFF", color = if (enabled) Green else TextSec, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        TextButton(onClick = { expanded = false }, contentPadding = PaddingValues(4.dp)) {
                            Text("–", color = TextSec, fontSize = 18.sp)
                        }
                    }
                }
                Divider(color = Card2, thickness = 0.5.dp)
                Text("Pitch: ${if (pitch > 0) "+" else ""}${pitch.toInt()} st", color = TextSec, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("−" to "com.voicechangerx.PITCH_DOWN", "0" to "com.voicechangerx.PITCH_RESET", "+" to "com.voicechangerx.PITCH_UP").forEach { (label, action) ->
                        Button(onClick = { AudioControlReceiver.send(ctx, action) },
                            modifier = Modifier.weight(1f).height(38.dp), shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Card2), contentPadding = PaddingValues(0.dp)) {
                            Text(label, color = Green, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("♀" to "com.voicechangerx.GENDER_FEMALE", "○" to "com.voicechangerx.GENDER_OFF", "♂" to "com.voicechangerx.GENDER_MALE").forEach { (label, action) ->
                        Button(onClick = { AudioControlReceiver.send(ctx, action) },
                            modifier = Modifier.weight(1f).height(38.dp), shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Card2), contentPadding = PaddingValues(0.dp)) {
                            Text(label, color = Green, fontSize = 15.sp)
                        }
                    }
                }
                Divider(color = Card2, thickness = 0.5.dp)
                Button(onClick = onClose, modifier = Modifier.fillMaxWidth().height(36.dp), shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D1515)), contentPadding = PaddingValues(0.dp)) {
                    Text("✕ Close", color = Color(0xFFEF4444), fontSize = 13.sp)
                }
            }
        }
    }
}
