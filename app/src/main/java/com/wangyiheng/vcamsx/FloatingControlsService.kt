package com.wangyiheng.vcamsx

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.*
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.wangyiheng.vcamsx.ui.theme.*

class FloatingControlsService : Service(), LifecycleOwner, SavedStateRegistryOwner {

    private lateinit var windowManager: WindowManager
    private var floatView: android.view.View? = null
    private val lifecycleRegistry          = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    companion object {
        fun start(context: Context) {
            context.startForegroundService(Intent(context, FloatingControlsService::class.java))
        }
        fun stop(context: Context) {
            context.stopService(Intent(context, FloatingControlsService::class.java))
        }
        fun sendControl(context: Context, action: String, dx: Int = 0, dy: Int = 0) {
            val intent = Intent(action).apply {
                addFlags(0x01000000)
                if (action == VideoControlReceiver.ACTION_ADJUST) {
                    putExtra("dx", dx); putExtra("dy", dy)
                }
            }
            context.sendBroadcast(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        createNotificationChannel()
        startForeground(1, buildNotification())
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        showFloating()
    }

    private fun showFloating() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = 20; y = 200 }

        val cv = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@FloatingControlsService)
            setViewTreeSavedStateRegistryOwner(this@FloatingControlsService)
            setContent {
                FloatingUI(
                    context = this@FloatingControlsService,
                    onMove  = { dx, dy -> params.x += dx.toInt(); params.y += dy.toInt(); windowManager.updateViewLayout(floatView, params) },
                    onClose = { stop(this@FloatingControlsService) }
                )
            }
        }
        floatView = cv
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        windowManager.addView(cv, params)
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        floatView?.let { windowManager.removeView(it) }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel("vcamsx_ctrl", "VCamSX Controls", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(ch)
        }
    }

    private fun buildNotification() = Notification.Builder(this, "vcamsx_ctrl")
        .setContentTitle("VCamSX Active")
        .setSmallIcon(android.R.drawable.ic_media_play)
        .build()
}

// ── Colors ────────────────────────────────────────────────────────────────────
private val BG      = Color(0xF0000010)
private val CARD    = Color(0xF007071A)
private val ACCENT  = NeonCyan
private val PURPLE  = NeonPurple
private val GREEN   = NeonGreen
private val RED     = NeonRed
private val ORANGE  = NeonOrange
private val TEXT    = CyberText
private val DIM     = CyberSubtext
private val BORDER  = Color(0xFF1A1A40)

// ── Glow modifier ─────────────────────────────────────────────────────────────
@Composable
private fun Modifier.neonGlow(color: Color, radius: Dp = 12.dp): Modifier =
    this.border(1.dp, Brush.linearGradient(listOf(color.copy(0.9f), PURPLE.copy(0.4f), color.copy(0.9f))), RoundedCornerShape(radius))
        .drawBehind {
            drawRoundRect(color = color.copy(0.1f), topLeft = Offset(-4f,-4f),
                size = size.copy(size.width+8, size.height+8),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius.toPx()),
                style = Stroke(8f))
        }

// ── Neon orb ─────────────────────────────────────────────────────────────────
@Composable
private fun NeonOrb(onClick: () -> Unit, onMove: (Float, Float) -> Unit) {
    val pulse by rememberInfiniteTransition(label = "orb").animateFloat(
        0.4f, 1f, infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "p"
    )
    val ring by rememberInfiniteTransition(label = "ring").animateFloat(
        0f, 360f, infiniteRepeatable(tween(4000, easing = LinearEasing)), label = "r"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .pointerInput(Unit) { detectDragGestures { _, d -> onMove(d.x, d.y) } },
        contentAlignment = Alignment.Center
    ) {
        // Outer glow ring
        Box(
            modifier = Modifier
                .size(60.dp)
                .drawBehind {
                    drawCircle(
                        brush  = Brush.sweepGradient(listOf(ACCENT.copy(pulse * 0.6f), PURPLE.copy(0.2f), ACCENT.copy(pulse * 0.6f))),
                        radius = size.minDimension / 2f,
                        style  = Stroke(2f)
                    )
                    drawCircle(color = ACCENT.copy(pulse * 0.15f), radius = size.minDimension / 2f)
                }
        )
        // Inner button
        Button(
            onClick        = onClick,
            modifier       = Modifier.size(46.dp),
            shape          = CircleShape,
            colors         = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A0A25)),
            contentPadding = PaddingValues(0.dp),
            border         = androidx.compose.foundation.BorderStroke(1.dp, ACCENT.copy(pulse))
        ) {
            Text("⬡", fontSize = 20.sp, color = ACCENT.copy(pulse))
        }
    }
}

// ── Main floating UI ──────────────────────────────────────────────────────────
@Composable
fun FloatingUI(context: Context, onMove: (Float, Float) -> Unit, onClose: () -> Unit) {
    var expanded   by remember { mutableStateOf(false) }
    var showAdjust by remember { mutableStateOf(false) }
    var isPaused   by remember { mutableStateOf(false) }
    var rotation   by remember { mutableStateOf(0) }
    var isFlipped  by remember { mutableStateOf(false) }

    if (!expanded) {
        NeonOrb(onClick = { expanded = true }, onMove = onMove)
    } else {
        Box(
            modifier = Modifier
                .width(230.dp)
                .neonGlow(ACCENT)
                .background(
                    Brush.linearGradient(listOf(CARD, Color(0xFF05051A))),
                    RoundedCornerShape(16.dp)
                )
                .pointerInput(Unit) { detectDragGestures { _, d -> onMove(d.x, d.y) } }
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {

                // ── Header ───────────────────────────────────────────────
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Pulsing active dot
                        val dotAlpha by rememberInfiniteTransition(label="hdr").animateFloat(
                            0.3f, 1f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label="d"
                        )
                        Box(Modifier.size(6.dp).background(GREEN.copy(dotAlpha), CircleShape))
                        Text("VCAMSX", color = ACCENT, fontSize = 13.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
                    }
                    TextButton(
                        onClick        = { expanded = false; showAdjust = false },
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("—", color = DIM, fontSize = 16.sp) }
                }

                // Neon divider
                Box(Modifier.fillMaxWidth().height(0.5.dp).background(
                    Brush.horizontalGradient(listOf(Color.Transparent, ACCENT.copy(0.5f), PURPLE.copy(0.5f), Color.Transparent))
                ))

                if (!showAdjust) {
                    // ── Main controls ─────────────────────────────────────
                    NeonFBtn(
                        label = if (isPaused) "▶  RESUME" else "⏸  PAUSE",
                        color = if (isPaused) GREEN else ACCENT
                    ) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_PAUSE)
                        isPaused = !isPaused
                    }
                    NeonFBtn("↺  RELOAD", ACCENT) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_RELOAD)
                        isPaused = false
                    }
                    NeonFBtn("⟳  ROTATE  ($rotation°)", PURPLE) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ROTATE)
                        rotation = (rotation + 90) % 360
                    }
                    NeonFBtn(
                        label = if (isFlipped) "↔  FLIP  [ON]" else "↔  FLIP  [OFF]",
                        color = if (isFlipped) ORANGE else ACCENT
                    ) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_FLIP)
                        isFlipped = !isFlipped
                    }
                    NeonFBtn("⤢  ADJUST POSITION", PURPLE) { showAdjust = true }

                } else {
                    // ── Adjust panel ──────────────────────────────────────
                    Text("// ADJUST_OFFSET", color = DIM, fontSize = 9.sp, fontFamily = FontFamily.Monospace)

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        NeonFBtn("▲", ACCENT, 70) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, 0, -30)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        NeonFBtn("◄", ACCENT, 70) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, -30, 0)
                        }
                        NeonFBtn("►", ACCENT, 70) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, 30, 0)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        NeonFBtn("▼", ACCENT, 70) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, 0, 30)
                        }
                    }

                    Box(Modifier.fillMaxWidth().height(0.5.dp).background(
                        Brush.horizontalGradient(listOf(Color.Transparent, BORDER, Color.Transparent))
                    ))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        NeonFBtn("➕ ZOOM IN",  GREEN, 102) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ZOOM_IN)
                        }
                        NeonFBtn("➖ ZOOM OUT", RED, 102) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ZOOM_OUT)
                        }
                    }
                    NeonFBtn("↩  BACK", DIM.copy(0.5f)) { showAdjust = false }
                }

                // Bottom neon divider
                Box(Modifier.fillMaxWidth().height(0.5.dp).background(
                    Brush.horizontalGradient(listOf(Color.Transparent, RED.copy(0.4f), Color.Transparent))
                ))

                NeonFBtn("✕  CLOSE OVERLAY", RED) { onClose() }
            }
        }
    }
}

// ── Neon button ───────────────────────────────────────────────────────────────
@Composable
fun NeonFBtn(label: String, color: Color, width: Int = 0, onClick: () -> Unit) {
    val mod = if (width > 0) Modifier.width(width.dp).height(36.dp)
              else Modifier.fillMaxWidth().height(36.dp)
    Button(
        onClick        = onClick,
        modifier       = mod,
        shape          = RoundedCornerShape(8.dp),
        colors         = ButtonDefaults.buttonColors(containerColor = color.copy(0.1f)),
        border         = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(0.7f)),
        contentPadding = PaddingValues(horizontal = 6.dp)
    ) {
        Text(label, color = color, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

// Compat alias
@Composable
fun FBtn(label: String, color: Color, width: Int = 0, onClick: () -> Unit) =
    NeonFBtn(label, color, width, onClick)
