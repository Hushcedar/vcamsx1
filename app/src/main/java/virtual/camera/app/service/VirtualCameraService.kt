package virtual.camera.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.util.Size
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import virtual.camera.app.R
import virtual.camera.app.app.AppManager
import virtual.camera.app.app.AppManager.SourceType

class VirtualCameraService : LifecycleService() {

    companion object {
        private const val TAG        = "VirtualCamSvc"
        private const val CHANNEL_ID = "vcamera_channel"
        private const val NOTIF_ID   = 1001

        const val ACTION_START  = "action.START"
        const val ACTION_STOP   = "action.STOP"
        const val ACTION_UPDATE = "action.UPDATE"

        fun start(context: Context) {
            val i = Intent(context, VirtualCameraService::class.java).apply { action = ACTION_START }
            ContextCompat.startForegroundService(context, i)
        }

        fun stop(context: Context) {
            val i = Intent(context, VirtualCameraService::class.java).apply { action = ACTION_STOP }
            context.startService(i)
        }
    }

    private var exoPlayer: ExoPlayer? = null
    private var staticImageJob: Job?  = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIF_ID, buildNotification("Initializing…"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_START, ACTION_UPDATE -> {
                lifecycleScope.launch(Dispatchers.IO) { setupPipeline() }
            }
            ACTION_STOP -> {
                teardown()
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        teardown()
        AppManager.isServiceRunning = false
        super.onDestroy()
    }

    private fun setupPipeline() {
        teardown()
        when (AppManager.activeSourceType) {
            SourceType.LOCAL_VIDEO,
            SourceType.NETWORK_RTMP,
            SourceType.NETWORK_HLS,
            SourceType.NETWORK_RTSP -> setupVideoSource()
            SourceType.LOCAL_IMAGE  -> setupImageSource()
            SourceType.REAL_CAMERA  -> updateNotification("Real camera passthrough active")
            SourceType.NONE         -> updateNotification("No source configured")
        }
        AppManager.isServiceRunning = true
    }

    private fun setupVideoSource() {
        val uri = Uri.parse(AppManager.activeSourceUri)
        lifecycleScope.launch(Dispatchers.Main) {
            exoPlayer = ExoPlayer.Builder(this@VirtualCameraService).build().apply {
                setMediaItem(MediaItem.fromUri(uri))
                repeatMode = Player.REPEAT_MODE_ALL
                playWhenReady = true
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_READY    -> updateNotification("Streaming active")
                            Player.STATE_BUFFERING-> updateNotification("Buffering…")
                            else -> {}
                        }
                    }
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        Log.e(TAG, "Player error: ${error.message}")
                        updateNotification("Stream error — retrying…")
                        lifecycleScope.launch(Dispatchers.IO) {
                            delay(3000)
                            setupVideoSource()
                        }
                    }
                })
                prepare()
            }
            updateNotification("Video source active")
        }
    }

    private fun setupImageSource() {
        staticImageJob = lifecycleScope.launch(Dispatchers.IO) {
            val uri = Uri.parse(AppManager.activeSourceUri)
            val bitmap = try {
                contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                }
            } catch (e: Exception) { null }

            if (bitmap == null) {
                updateNotification("Image load failed")
                return@launch
            }
            updateNotification("Static image active")
            // Hold service alive — image is fed via Camera2 surface in real impl
            while (isActive) { delay(1000L) }
        }
    }

    private fun teardown() {
        staticImageJob?.cancel()
        staticImageJob = null
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "Virtual Camera", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Keeps virtual camera feed active"
            setShowBadge(false)
        }
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun buildNotification(status: String): Notification {
        val openIntent = PendingIntent.getActivity(
            this, 0,
            packageManager.getLaunchIntentForPackage(packageName),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val stopIntent = PendingIntent.getService(
            this, 1,
            Intent(this, VirtualCameraService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentTitle("VCamera Active")
            .setContentText(status)
            .setContentIntent(openIntent)
            .addAction(0, "Stop", stopIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun updateNotification(status: String) {
        val mgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mgr.notify(NOTIF_ID, buildNotification(status))
    }
}
