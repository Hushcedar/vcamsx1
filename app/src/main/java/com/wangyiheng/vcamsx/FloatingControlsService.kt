package com.wangyiheng.vcamsx

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.*
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.wangyiheng.vcamsx.utils.VideoControls

class FloatingControlsService : Service(), LifecycleOwner, SavedStateRegistryOwner {

    private lateinit var windowManager: WindowManager
    private var floatView: android.view.View? = null
    private val lifecycleRegistry = LifecycleRegistry(this)
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
                    onMove  = { dx, dy ->
                        params.x += dx.toInt(); params.y += dy.toInt()
                        windowManager.updateViewLayout(floatView, params)
                    },
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
        .setContentTitle("VCamSX Controls")
        .setSmallIcon(android.R.drawable.ic_media_play)
        .build()
}

private val PanelBg  = Color(0xFF080B12)
private val CardBg   = Color(0xFF111622)
private val Accent   = Color(0xFF00E5FF)
private val TextHigh = Color(0xFFE0E4FF)
private val TextDim  = Color(0xFF5A6478)
private val Divider  = Color(0xFF1C2333)
private val GreenCol = Color(0xFF00E676)
private val RedCol   = Color(0xFFFF4757)
private val OrbBg    = Color(0xFF111622)

@Composable
fun FloatingUI(context: Context, onMove: (Float, Float) -> Unit, onClose: () -> Unit) {
    var expanded   by remember { mutableStateOf(false) }
    var showAdjust by remember { mutableStateOf(false) }
    var isPaused   by remember { mutableStateOf(false) }
    var rotation   by remember { mutableStateOf(0) }
    var isFlipped  by remember { mutableStateOf(false) }

    val speedIdx by VideoControls.speedIndex
    val speedLabel = VideoControls.speedSteps[speedIdx].let {
        if (it == it.toLong().toFloat()) "${it.toLong()}×" else "${it}×"
    }
    val speedAtDefault = speedIdx == 1

    if (!expanded) {
        Box(
            modifier         = Modifier
                .size(48.dp)
                .pointerInput(Unit) { detectDragGestures { _, d -> onMove(d.x, d.y) } },
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick        = { expanded = true },
                modifier       = Modifier.size(48.dp),
                shape          = CircleShape,
                colors         = ButtonDefaults.buttonColors(containerColor = OrbBg),
                border         = androidx.compose.foundation.BorderStroke(1.dp, Accent.copy(0.5f)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(Modifier.size(10.dp).background(Accent, CircleShape))
            }
        }
    } else {
        Card(
            shape    = RoundedCornerShape(16.dp),
            colors   = CardDefaults.cardColors(containerColor = PanelBg),
            modifier = Modifier
                .width(220.dp)
                .border(0.5.dp, Divider, RoundedCornerShape(16.dp))
                .pointerInput(Unit) { detectDragGestures { _, d -> onMove(d.x, d.y) } }
        ) {
            Column(
                Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("Controls", color = TextHigh, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    TextButton(
                        onClick        = { expanded = false; showAdjust = false },
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("—", color = TextDim, fontSize = 16.sp) }
                }

                Divider(color = Divider, thickness = 0.5.dp)

                if (!showAdjust) {
                    FBtn(if (isPaused) "Resume" else "Pause", if (isPaused) GreenCol else Accent) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_PAUSE)
                        isPaused = !isPaused
                    }
                    FBtn("Reload", Accent) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_RELOAD)
                        isPaused = false
                    }
                    FBtn("Rotate  ($rotation°)", Accent) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ROTATE)
                        rotation = (rotation + 90) % 360
                    }
                    FBtn(
                        label = if (isFlipped) "Flip  ON" else "Flip  OFF",
                        color = if (isFlipped) Color(0xFFFFB300) else Accent
                    ) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_FLIP)
                        isFlipped = !isFlipped
                    }

                    FBtn(
                        label = "Speed  $speedLabel",
                        color = if (speedAtDefault) Accent else GreenCol
                    ) {
                        FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_SPEED)
                    }

                    FBtn("Adjust", TextDim) { showAdjust = true }

                } else {
                    Text("Adjust Position", color = TextDim, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        FBtn("Up", Accent, 80) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, 0, 30)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        FBtn("Left", Accent, 90) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, -30, 0)
                        }
                        FBtn("Right", Accent, 90) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, 30, 0)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        FBtn("Down", Accent, 80) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ADJUST, 0, -30)
                        }
                    }

                    Divider(color = Divider, thickness = 0.5.dp)

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        FBtn("Zoom In",  GreenCol, 100) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ZOOM_IN)
                        }
                        FBtn("Zoom Out", RedCol, 100) {
                            FloatingControlsService.sendControl(context, VideoControlReceiver.ACTION_ZOOM_OUT)
                        }
                    }
                    FBtn("Back", TextDim) { showAdjust = false }
                }

                Divider(color = Divider, thickness = 0.5.dp)
                FBtn("Close", RedCol) { onClose() }
            }
        }
    }
}

@Composable
fun FBtn(label: String, color: Color, width: Int = 0, onClick: () -> Unit) {
    val mod = if (width > 0) Modifier.width(width.dp).height(38.dp)
              else Modifier.fillMaxWidth().height(38.dp)
    Button(
        onClick        = onClick,
        modifier       = mod,
        shape          = RoundedCornerShape(8.dp),
        colors         = ButtonDefaults.buttonColors(containerColor = CardBg),
        border         = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(0.5f)),
        contentPadding = PaddingValues(horizontal = 6.dp)
    ) {
        Text(label, color = color, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
