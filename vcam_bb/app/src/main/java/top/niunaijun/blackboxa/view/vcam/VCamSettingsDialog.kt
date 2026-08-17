package top.niunaijun.blackboxa.view.vcam

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import top.niunaijun.blackboxa.R

/**
 * Bottom sheet that pops from the three-dot menu on each cloned app card.
 * Mirrors VCamera's 4 protect modes:
 *   1 = Disable   2 = Local Video   3 = Network Stream   4 = Local Picture
 */
class VCamSettingsDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "VCamSettingsDialog"
        fun newInstance(pkg: String, label: String) = VCamSettingsDialog().apply {
            arguments = Bundle().apply {
                putString("pkg", pkg)
                putString("label", label)
            }
        }
    }

    private lateinit var pkg: String
    private lateinit var label: String

    private lateinit var spinnerMethod: Spinner
    private lateinit var layoutNetwork: LinearLayout
    private lateinit var editUrl: EditText
    private lateinit var layoutVideo: LinearLayout
    private lateinit var tvVideoPath: TextView
    private lateinit var layoutPicture: LinearLayout
    private lateinit var tvPicPath: TextView
    private lateinit var switchAudio: Switch
    private lateinit var editWidth: EditText
    private lateinit var editHeight: EditText
    private lateinit var editFps: EditText
    private lateinit var btnSave: Button

    private lateinit var videoPicker: ActivityResultLauncher<Intent>
    private lateinit var picturePicker: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pkg   = requireArguments().getString("pkg")!!
        label = requireArguments().getString("label")!!

        videoPicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { r ->
            if (r.resultCode == Activity.RESULT_OK) r.data?.data?.let { uri ->
                runCatching {
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                tvVideoPath.text = uri.toString()
            }
        }

        picturePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { r ->
            if (r.resultCode == Activity.RESULT_OK) r.data?.data?.let { uri ->
                runCatching {
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                tvPicPath.text = uri.toString()
            }
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.dialog_vcam_settings, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.vcam_title).text = "📷 VCamera — $label"

        spinnerMethod = view.findViewById(R.id.vcam_spinner_method)
        layoutNetwork = view.findViewById(R.id.vcam_layout_network)
        editUrl       = view.findViewById(R.id.vcam_edit_url)
        layoutVideo   = view.findViewById(R.id.vcam_layout_video)
        tvVideoPath   = view.findViewById(R.id.vcam_tv_video_path)
        layoutPicture = view.findViewById(R.id.vcam_layout_picture)
        tvPicPath     = view.findViewById(R.id.vcam_tv_pic_path)
        switchAudio   = view.findViewById(R.id.vcam_switch_audio)
        editWidth     = view.findViewById(R.id.vcam_edit_width)
        editHeight    = view.findViewById(R.id.vcam_edit_height)
        editFps       = view.findViewById(R.id.vcam_edit_fps)
        btnSave       = view.findViewById(R.id.vcam_btn_save)

        spinnerMethod.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "1. Disable — real camera",
                "2. Local Video",
                "3. Network Stream (RTSP / HLS / DASH)",
                "4. Local Picture"
            )
        )

        val cfg = VCamPrefs.read(requireContext(), pkg)
        spinnerMethod.setSelection((cfg.method - 1).coerceIn(0, 3))
        editUrl.setText(cfg.netUrl)
        tvVideoPath.text = cfg.videoUri.ifBlank { "No video selected" }
        tvPicPath.text   = cfg.picUri.ifBlank { "No picture selected" }
        switchAudio.isChecked = cfg.audio
        editWidth.setText(cfg.width.toString())
        editHeight.setText(cfg.height.toString())
        editFps.setText(cfg.fps.toString())
        updateMethodUI(cfg.method)

        spinnerMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) =
                updateMethodUI(pos + 1)
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        view.findViewById<Button>(R.id.vcam_btn_pick_video).setOnClickListener {
            videoPicker.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "video/*"
                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            })
        }

        view.findViewById<Button>(R.id.vcam_btn_pick_picture).setOnClickListener {
            picturePicker.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            })
        }

        btnSave.setOnClickListener {
            val method = spinnerMethod.selectedItemPosition + 1
            val cfg = VCamConfig(
                method   = method,
                videoUri = if (tvVideoPath.text == "No video selected") "" else tvVideoPath.text.toString(),
                netUrl   = editUrl.text.toString().trim(),
                picUri   = if (tvPicPath.text == "No picture selected") "" else tvPicPath.text.toString(),
                audio    = switchAudio.isChecked,
                width    = editWidth.text.toString().toIntOrNull() ?: 1280,
                height   = editHeight.text.toString().toIntOrNull() ?: 720,
                fps      = editFps.text.toString().toIntOrNull() ?: 30
            )
            VCamPrefs.write(requireContext(), pkg, cfg)
            Toast.makeText(requireContext(), "VCamera saved for $label", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun updateMethodUI(method: Int) {
        layoutNetwork.visibility = if (method == 3) View.VISIBLE else View.GONE
        layoutVideo.visibility   = if (method == 2) View.VISIBLE else View.GONE
        layoutPicture.visibility = if (method == 4) View.VISIBLE else View.GONE
    }
}
