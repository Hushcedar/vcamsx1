package com.vcam.app.ui

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

    private lateinit var spinnerMethod:  Spinner
    private lateinit var layoutLocal:    View
    private lateinit var layoutNetwork:  View
    private lateinit var layoutImage:    View
    private lateinit var tvVideoPath:    TextView
    private lateinit var tvImagePath:    TextView
    private lateinit var etNetworkUrl:   EditText
    private lateinit var switchAudio:    SwitchCompat
    private lateinit var btnPickVideo:   Button
    private lateinit var btnPickImage:   Button
    private lateinit var btnSave:        Button
    private lateinit var btnControls:    Button

    private var selectedVideoUri: Uri? = null
    private var selectedImageUri: Uri? = null

    private val videoPicker = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedVideoUri = it; tvVideoPath.text = it.lastPathSegment ?: it.toString() }
    }
    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedImageUri = it; tvImagePath.text = it.lastPathSegment ?: it.toString() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bind()
        load()
        requestOverlayIfNeeded()
    }

    private fun bind() {
        spinnerMethod = findViewById(R.id.spinner_method)
        layoutLocal   = findViewById(R.id.layout_local)
        layoutNetwork = findViewById(R.id.layout_network)
        layoutImage   = findViewById(R.id.layout_image)
        tvVideoPath   = findViewById(R.id.tv_video_path)
        tvImagePath   = findViewById(R.id.tv_image_path)
        etNetworkUrl  = findViewById(R.id.et_network_url)
        switchAudio   = findViewById(R.id.switch_audio)
        btnPickVideo  = findViewById(R.id.btn_pick_video)
        btnPickImage  = findViewById(R.id.btn_pick_image)
        btnSave       = findViewById(R.id.btn_save)
        btnControls   = findViewById(R.id.btn_controls)

        // Spinner adapter matching screenshot options
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.method_options, android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spinnerMethod.adapter = adapter

        spinnerMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                layoutLocal.visibility   = if (pos == 1) View.VISIBLE else View.GONE
                layoutNetwork.visibility = if (pos == 2) View.VISIBLE else View.GONE
                layoutImage.visibility   = if (pos == 3) View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        btnPickVideo.setOnClickListener  { videoPicker.launch("video/*") }
        btnPickImage.setOnClickListener  { imagePicker.launch("image/*") }
        btnSave.setOnClickListener       { save() }
        btnControls.setOnClickListener   { showChannelDialog() }
    }

    private fun load() {
        val method = VCamPrefs.methodType
        spinnerMethod.setSelection(method)
        switchAudio.isChecked = VCamPrefs.audioEnable
        tvVideoPath.text = File(VCamPrefs.videoPath).name.ifBlank { "No file selected" }
        tvImagePath.text = File(VCamPrefs.imagePath).name.ifBlank { "No file selected" }
        etNetworkUrl.setText(VCamPrefs.networkUrl)
    }

    private fun save() {
        val method = spinnerMethod.selectedItemPosition
        when (method) {
            VCamPrefs.TYPE_DISABLED -> {
                VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, method)
                VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, false)
                toast("Disabled — real camera will be used")
            }
            VCamPrefs.TYPE_LOCAL_VIDEO -> {
                val uri = selectedVideoUri ?: run {
                    if (VCamPrefs.videoPath.isNotBlank()) {
                        VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, method)
                        VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                        VCamPrefs.setBoolean(VCamPrefs.KEY_AUDIO_ENABLE, switchAudio.isChecked)
                        toast("Saved"); return
                    }
                    toast("Please select a video file first"); return
                }
                val pd = ProgressDialog(this).apply { setMessage("Copying video…"); show() }
                Thread {
                    val ok = copyFile(uri, "vcam_video", "mp4", VCamPrefs.KEY_VIDEO_PATH, VCamPrefs.KEY_VIDEO_URI)
                    runOnUiThread {
                        pd.dismiss()
                        if (ok) {
                            VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, method)
                            VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                            VCamPrefs.setBoolean(VCamPrefs.KEY_AUDIO_ENABLE, switchAudio.isChecked)
                            toast("Saved")
                        } else toast("Failed to copy video")
                    }
                }.start()
            }
            VCamPrefs.TYPE_NETWORK -> {
                val url = etNetworkUrl.text.toString().trim()
                if (url.isBlank()) { toast("Enter a stream URL"); return }
                VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, method)
                VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                VCamPrefs.setString(VCamPrefs.KEY_NETWORK_URL, url)
                VCamPrefs.setBoolean(VCamPrefs.KEY_AUDIO_ENABLE, switchAudio.isChecked)
                toast("Saved")
            }
            VCamPrefs.TYPE_LOCAL_IMAGE -> {
                val uri = selectedImageUri ?: run {
                    if (VCamPrefs.imagePath.isNotBlank()) {
                        VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, method)
                        VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                        toast("Saved"); return
                    }
                    toast("Please select an image file first"); return
                }
                val pd = ProgressDialog(this).apply { setMessage("Copying image…"); show() }
                Thread {
                    val ok = copyFile(uri, "vcam_image", "jpg", VCamPrefs.KEY_IMAGE_PATH, VCamPrefs.KEY_IMAGE_URI)
                    runOnUiThread {
                        pd.dismiss()
                        if (ok) {
                            VCamPrefs.setInt(VCamPrefs.KEY_METHOD_TYPE, method)
                            VCamPrefs.setBoolean(VCamPrefs.KEY_IS_ENABLED, true)
                            toast("Saved")
                        } else toast("Failed to copy image")
                    }
                }.start()
            }
        }
    }

    private fun copyFile(uri: Uri, name: String, ext: String, keyPath: String, keyUri: String): Boolean {
        return try {
            val old = VCamPrefs.getString(keyPath)
            if (old.isNotBlank()) File(old).delete()
            val out = File(filesDir, "$name.$ext")
            contentResolver.openInputStream(uri)!!.use { i ->
                FileOutputStream(out).use { o -> i.copyTo(o) }
            }
            VCamPrefs.setString(keyPath, out.absolutePath)
            VCamPrefs.setString(keyUri, uri.toString())
            true
        } catch (e: Exception) { false }
    }

    // ── Channel controls dialog — replicates the original VCamera UI ──────────
    private fun showChannelDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_channel_settings)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Channel spinner
        val spinnerChannel = dialog.findViewById<Spinner>(R.id.spinner_channel)
        val channels = arrayOf(
            "Channel 1 : Preview",
            "Channel 2 : Take Picture",
            "Channel 3: Video Call",
            "Channel 4: Others"
        )
        spinnerChannel.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, channels).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        var currentChannel = 1

        // Seekbars
        val seekSize = dialog.findViewById<SeekBar>(R.id.seek_size)
        val seekZoom = dialog.findViewById<SeekBar>(R.id.seek_zoom)

        fun loadChannel(ch: Int) {
            seekSize.progress = VCamPrefs.getInt(VCamPrefs.keySize(ch), 100)
            seekZoom.progress = VCamPrefs.getInt(VCamPrefs.keyZoom(ch), 100)
        }
        loadChannel(1)

        spinnerChannel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                currentChannel = pos + 1; loadChannel(currentChannel)
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        // Flip spinner
        val spinnerFlip = dialog.findViewById<Spinner>(R.id.spinner_flip)
        spinnerFlip.adapter = ArrayAdapter.createFromResource(
            this, R.array.flip_options, android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spinnerFlip.setSelection(VCamPrefs.getInt(VCamPrefs.keyFlip(1), 0))

        // Size buttons
        dialog.findViewById<android.widget.ImageButton>(R.id.btn_size_minus).setOnClickListener {
            seekSize.progress = (seekSize.progress - 5).coerceAtLeast(0)
        }
        dialog.findViewById<android.widget.ImageButton>(R.id.btn_size_plus).setOnClickListener {
            seekSize.progress = (seekSize.progress + 5).coerceAtMost(200)
        }

        // Zoom buttons
        dialog.findViewById<android.widget.ImageButton>(R.id.btn_zoom_minus).setOnClickListener {
            seekZoom.progress = (seekZoom.progress - 5).coerceAtLeast(0)
            sendBroadcast(Intent(VideoControlReceiver.ACTION_ZOOM_OUT))
        }
        dialog.findViewById<android.widget.ImageButton>(R.id.btn_zoom_plus).setOnClickListener {
            seekZoom.progress = (seekZoom.progress + 5).coerceAtMost(200)
            sendBroadcast(Intent(VideoControlReceiver.ACTION_ZOOM_IN))
        }

        // Rotate
        dialog.findViewById<android.widget.ImageButton>(R.id.btn_rotate).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_ROTATE))
        }

        // Flip change
        spinnerFlip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                VCamPrefs.setInt(VCamPrefs.keyFlip(currentChannel), pos)
                if (pos == 1 || pos == 3) sendBroadcast(Intent(VideoControlReceiver.ACTION_FLIP))
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        // D-pad move
        dialog.findViewById<Button>(R.id.btn_up).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_ADJUST).apply { putExtra("dx", 0); putExtra("dy", -40) })
        }
        dialog.findViewById<Button>(R.id.btn_down).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_ADJUST).apply { putExtra("dx", 0); putExtra("dy", 40) })
        }
        dialog.findViewById<Button>(R.id.btn_left).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_ADJUST).apply { putExtra("dx", -40); putExtra("dy", 0) })
        }
        dialog.findViewById<Button>(R.id.btn_right).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_ADJUST).apply { putExtra("dx", 40); putExtra("dy", 0) })
        }
        dialog.findViewById<Button>(R.id.btn_center).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_RESET_TRANSFORM))
        }

        // Pause / Reload
        dialog.findViewById<Button>(R.id.btn_pause).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_PAUSE))
        }
        dialog.findViewById<Button>(R.id.btn_reload).setOnClickListener {
            sendBroadcast(Intent(VideoControlReceiver.ACTION_RELOAD))
        }

        // Save channel prefs on seekbar change
        seekSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar, p: Int, u: Boolean) {
                VCamPrefs.setInt(VCamPrefs.keySize(currentChannel), p)
                val scale = p / 100f
                sendBroadcast(Intent(VideoControlReceiver.ACTION_ZOOM_IN).apply {
                    putExtra("scale_abs", scale)
                })
            }
            override fun onStartTrackingTouch(s: SeekBar) {}
            override fun onStopTrackingTouch(s: SeekBar) {}
        })

        seekZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar, p: Int, u: Boolean) {
                VCamPrefs.setInt(VCamPrefs.keyZoom(currentChannel), p)
            }
            override fun onStartTrackingTouch(s: SeekBar) {}
            override fun onStopTrackingTouch(s: SeekBar) {}
        })

        dialog.findViewById<Button>(R.id.btn_dismiss).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun requestOverlayIfNeeded() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")))
        }
    }

    private fun toast(m: String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
