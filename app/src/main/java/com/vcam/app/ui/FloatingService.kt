package com.vcam.app.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Button
import androidx.core.app.NotificationCompat
import com.vcam.app.R
import com.vcam.app.engine.VideoControlReceiver

class FloatingService : Service() {

    private lateinit var wm: WindowManager
    private var rootView: View? = null
    private var expanded = false

    companion object {
        const val CHANNEL_ID = "vcam_float"
        fun start(ctx: Context) = ctx.startForegroundService(Intent(ctx, FloatingService::class.java))
        fun stop(ctx: Context)  = ctx.stopService(Intent(ctx, FloatingService::class.java))
        fun send(ctx: Context, action: String, dx: Int = 0, dy: Int = 0) {
            ctx.sendBroadcast(Intent(action).apply {
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                if (action == VideoControlReceiver.ACTION_ADJUST) { putExtra("dx", dx); putExtra("dy", dy) }
            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(1, buildNotif())
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        show()
    }

    private fun show() {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_float_panel, null)
        rootView = view

        val lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = 16; y = 300 }

        val handle = view.findViewById<View>(R.id.drag_handle)
        var lx = 0f; var ly = 0f
        handle.setOnTouchListener { _, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> { lx = e.rawX; ly = e.rawY }
                MotionEvent.ACTION_MOVE -> {
                    lp.x += (e.rawX - lx).toInt(); lp.y += (e.rawY - ly).toInt()
                    lx = e.rawX; ly = e.rawY; wm.updateViewLayout(view, lp)
                }
            }
            true
        }
        handle.setOnClickListener {
            expanded = !expanded
            view.findViewById<View>(R.id.panel_controls).visibility =
                if (expanded) View.VISIBLE else View.GONE
        }

        fun btn(id: Int, action: String, dx: Int = 0, dy: Int = 0) =
            view.findViewById<Button>(id).setOnClickListener { send(this, action, dx, dy) }

        btn(R.id.btn_pause,    VideoControlReceiver.ACTION_PAUSE)
        btn(R.id.btn_reload,   VideoControlReceiver.ACTION_RELOAD)
        btn(R.id.btn_rotate,   VideoControlReceiver.ACTION_ROTATE)
        btn(R.id.btn_flip,     VideoControlReceiver.ACTION_FLIP)
        btn(R.id.btn_zoom_in,  VideoControlReceiver.ACTION_ZOOM_IN)
        btn(R.id.btn_zoom_out, VideoControlReceiver.ACTION_ZOOM_OUT)
        btn(R.id.btn_up,       VideoControlReceiver.ACTION_ADJUST, 0,   -40)
        btn(R.id.btn_down,     VideoControlReceiver.ACTION_ADJUST, 0,    40)
        btn(R.id.btn_left,     VideoControlReceiver.ACTION_ADJUST, -40,  0)
        btn(R.id.btn_right,    VideoControlReceiver.ACTION_ADJUST, 40,   0)
        btn(R.id.btn_reset,    VideoControlReceiver.ACTION_RESET_TRANSFORM)
        view.findViewById<Button>(R.id.btn_close).setOnClickListener { stop(this) }

        wm.addView(view, lp)
    }

    override fun onDestroy() {
        rootView?.let { runCatching { wm.removeView(it) } }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(NotificationChannel(CHANNEL_ID, "VCam Controls",
                    NotificationManager.IMPORTANCE_LOW))
    }

    private fun buildNotif() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("VCam Active")
        .setContentText("Camera injection running")
        .setSmallIcon(R.drawable.ic_camera)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .build()
}
