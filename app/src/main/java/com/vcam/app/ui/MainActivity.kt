package com.vcam.app.ui

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.vcam.app.R
import com.vcam.app.engine.VCamPrefs
import com.vcam.app.engine.VideoControlReceiver
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus:       TextView
    private lateinit var switchEnable:   SwitchCompat
    private lateinit var cardMethod:     View
    private lateinit var tvMethodLabel:  TextView
    private lateinit var btnLocal:       View
    private lateinit var btnNetwork:     View
    private lateinit var btnOff:         View
    private lateinit var layoutLocal:    View
    private lateinit var layoutNetwork:  View
    private lateinit var tvVideoPath:    TextView
    private lateinit var btnPickVideo:   Button
    private lateinit var switchAudio:    SwitchCompat
    private lateinit var etNetworkUrl:   EditText
    private lateinit var switchNetAudio: SwitchCompat
    private lateinit var btnSave:        Button
    private lateinit var btnOverlay:     Button
    private lateinit var btnControls:    Button

    private var selectedUri: Uri? = null
    private var currentMethod = VCamPrefs.TYPE_DISABLED

    private val videoPicker = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedUri = it; tvVideoPath.text = File(it.path ?: "").name.ifBlank { it.toString() } }
    }
    private val permLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bind(); requestPerms(); load()
    }

    private fun bind() {
        tvStatus       = findViewById(R.id.tv_status)
        switchEnable   = findViewById(R.id.switch_enable)
        cardMethod     = findViewById(R.id.card_method)
        tvMethodLabel  = findViewById(R.id.tv_method_label)
        btnLocal       = findViewById(R.id.btn_local)
        btnNetwork     = findViewById(R.id.btn_network)
        btnOff         = findViewById(R.id.btn_off)
        layoutLocal    = findViewById(R.id.layout_local)
        layoutNetwork  = findViewById(R.id.layout_network)
        tvVideoPath    = findViewById(R.id.tv_video_path)
        btnPickVideo   = findViewById(R.id.btn_pick_video)
        switchAudio    = findViewById(R.id.switch_audio)
        etNetworkUrl   = findViewById(R.id.et_network_url)
        switchNetAudio = findViewById(R.id.switch_net_audio)
        btnSave        = findViewById(R.id.btn_save)
        btnOverlay     = findViewById(R.id.btn_overlay)
        btnControls    = findViewById(R.id.btn_controls)

        switchEnable.setOnCheckedChangeListener { _, on ->
            cardMethod.visibility = if (on) View.VISIBLE else View.GONE
            if (!on) { VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, false); updateStatus() }
        }
        btnLocal.setOnClickListener   { selectMethod(VCamPrefs.TYPE_LOCAL_VIDEO) }
        btnNetwork.setOnClickListener { selectMethod(VCamPrefs.TYPE_NETWORK) }
        btnOff.setOnClickListener     { selectMethod(VCamPrefs.TYPE_DISABLED) }
        btnPickVideo.setOnClickListener { videoPicker.launch("video/*") }
        btnSave.setOnClickListener    { save() }
        btnOverlay.setOnClickListener { overlay() }
        btnControls.setOnClickListener { showControlsDialog() }
    }

    private fun showControlsDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_controls_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        fun send(action: String, dx: Int = 0, dy: Int = 0) {
            val i = android.content.Intent(action).apply {
                addFlags(android.content.Intent.FLAG_RECEIVER_FOREGROUND)
                if (action == VideoControlReceiver.ACTION_ADJUST) {
                    putExtra("dx", dx); putExtra("dy", dy)
                }
            }
            sendBroadcast(i)
        }

        dialog.findViewById<Button>(R.id.btn_pause).setOnClickListener {
            send(VideoControlReceiver.ACTION_PAUSE)
        }
        dialog.findViewById<Button>(R.id.btn_reload).setOnClickListener {
            send(VideoControlReceiver.ACTION_RELOAD)
        }
        dialog.findViewById<Button>(R.id.btn_rotate).setOnClickListener {
            send(VideoControlReceiver.ACTION_ROTATE)
        }
        dialog.findViewById<Button>(R.id.btn_flip).setOnClickListener {
            send(VideoControlReceiver.ACTION_FLIP)
        }
        dialog.findViewById<Button>(R.id.btn_zoom_in).setOnClickListener {
            send(VideoControlReceiver.ACTION_ZOOM_IN)
        }
        dialog.findViewById<Button>(R.id.btn_zoom_out).setOnClickListener {
            send(VideoControlReceiver.ACTION_ZOOM_OUT)
        }
        dialog.findViewById<Button>(R.id.btn_up).setOnClickListener {
            send(VideoControlReceiver.ACTION_ADJUST, 0, -40)
        }
        dialog.findViewById<Button>(R.id.btn_down).setOnClickListener {
            send(VideoControlReceiver.ACTION_ADJUST, 0, 40)
        }
        dialog.findViewById<Button>(R.id.btn_left).setOnClickListener {
            send(VideoControlReceiver.ACTION_ADJUST, -40, 0)
        }
        dialog.findViewById<Button>(R.id.btn_right).setOnClickListener {
            send(VideoControlReceiver.ACTION_ADJUST, 40, 0)
        }
        dialog.findViewById<Button>(R.id.btn_reset).setOnClickListener {
            send(VideoControlReceiver.ACTION_RESET_TRANSFORM)
        }
        dialog.findViewById<Button>(R.id.btn_close_dialog).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun load() {
        val on = VCamPrefs.isEnabled
        switchEnable.isChecked = on
        cardMethod.visibility  = if (on) View.VISIBLE else View.GONE
        selectMethod(VCamPrefs.methodType, true)
        updateStatus()
    }

    private fun selectMethod(type: Int, fromLoad: Boolean = false) {
        currentMethod = type
        layoutLocal.visibility   = if (type == VCamPrefs.TYPE_LOCAL_VIDEO) View.VISIBLE else View.GONE
        layoutNetwork.visibility = if (type == VCamPrefs.TYPE_NETWORK)     View.VISIBLE else View.GONE
        tvMethodLabel.text = when (type) {
            VCamPrefs.TYPE_LOCAL_VIDEO -> "📁  Local Video"
            VCamPrefs.TYPE_NETWORK     -> "🌐  Network Stream"
            else                       -> "🚫  Off"
        }
        if (fromLoad) when (type) {
            VCamPrefs.TYPE_LOCAL_VIDEO -> {
                tvVideoPath.text      = File(VCamPrefs.videoPath).name.ifBlank { "No video selected" }
                switchAudio.isChecked = VCamPrefs.audioEnable
            }
            VCamPrefs.TYPE_NETWORK -> {
                etNetworkUrl.setText(VCamPrefs.networkUrl)
                switchNetAudio.isChecked = VCamPrefs.getBoolean(VCamPrefs.KEY_NETWORK_AUDIO, true)
            }
        }
    }

    private fun save() {
        when (currentMethod) {
            VCamPrefs.TYPE_DISABLED -> {
                VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, VCamPrefs.TYPE_DISABLED)
                VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, false)
                switchEnable.isChecked = false
                toast("Disabled"); updateStatus()
            }
            VCamPrefs.TYPE_LOCAL_VIDEO -> {
                val uri = selectedUri ?: run {
                    if (VCamPrefs.videoPath.isNotBlank()) {
                        VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, currentMethod)
                        VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                        VCamPrefs.setBoolean(VCamPrefs.KEY_AUDIO_ENABLE, switchAudio.isChecked)
                        toast("Saved ✓"); updateStatus(); return
                    }
                    toast("Pick a video first"); return
                }
                val pd = ProgressDialog(this).apply { setMessage("Copying…"); setCancelable(false); show() }
                Thread {
                    val ok = copyVideo(uri)
                    runOnUiThread {
                        pd.dismiss()
                        if (ok) {
                            VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, currentMethod)
                            VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                            VCamPrefs.setBoolean(VCamPrefs.KEY_AUDIO_ENABLE, switchAudio.isChecked)
                            toast("Saved ✓"); updateStatus()
                        } else toast("Copy failed")
                    }
                }.start()
            }
            VCamPrefs.TYPE_NETWORK -> {
                val url = etNetworkUrl.text.toString().trim()
                if (url.isBlank() || (!url.startsWith("http") && !url.startsWith("rtmp"))) {
                    toast("Enter valid URL"); return
                }
                VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, currentMethod)
                VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                VCamPrefs.setString(VCamPrefs.KEY_NETWORK_URL, url)
                VCamPrefs.setBoolean(VCamPrefs.KEY_NETWORK_AUDIO, switchNetAudio.isChecked)
                toast("Saved ✓"); updateStatus()
            }
        }
    }

    private fun updateStatus() {
        val on = VCamPrefs.isEnabled
        tvStatus.text = if (on) "● Injection ACTIVE" else "○ Injection OFF"
        tvStatus.setTextColor(getColor(if (on) R.color.green else R.color.grey))
    }

    private fun copyVideo(uri: Uri): Boolean = try {
        File(VCamPrefs.videoPath).takeIf { it.exists() }?.delete()
        val ext = contentResolver.getType(uri)?.substringAfterLast('/') ?: "mp4"
        val out = File(filesDir, "vcam_video.$ext")
        contentResolver.openInputStream(uri)!!.use { i -> FileOutputStream(out).use { o -> i.copyTo(o) } }
        VCamPrefs.setString(VCamPrefs.KEY_VIDEO_URI, uri.toString())
        VCamPrefs.setString(VCamPrefs.KEY_VIDEO_PATH, out.absolutePath)
        tvVideoPath.text = out.name; true
    } catch (e: Exception) { false }

    private fun overlay() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this))
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        else toast("Overlay permission already granted ✓")
    }

    private fun requestPerms() {
        val p = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= 33) p += Manifest.permission.READ_MEDIA_VIDEO
        permLauncher.launch(p.toTypedArray())
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
