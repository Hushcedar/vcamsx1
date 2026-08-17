package virtual.camera.app.view.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import virtual.camera.app.R
import virtual.camera.app.app.AppManager
import virtual.camera.app.service.VirtualCameraService

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.setting)
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.setting_container, SettingPreferenceFragment())
                .commit()
        }
    }
}

class SettingPreferenceFragment : PreferenceFragmentCompat() {

    private val videoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    requireContext().contentResolver.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) { /* ignore */ }
                AppManager.activeSourceUri = it.toString()
                AppManager.activeSourceType = AppManager.SourceType.LOCAL_VIDEO
                findPreference<Preference>("pref_source_file")?.summary = it.lastPathSegment
                restartCameraService()
                toast("Video source set")
            }
        }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    requireContext().contentResolver.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) { /* ignore */ }
                AppManager.activeSourceUri = it.toString()
                AppManager.activeSourceType = AppManager.SourceType.LOCAL_IMAGE
                findPreference<Preference>("pref_source_file")?.summary = it.lastPathSegment
                restartCameraService()
                toast("Image source set")
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore
        setPreferencesFromResource(R.xml.settings_prefs, rootKey)
        setup()
    }

    private fun setup() {

        // ── Source type ───────────────────────────────────────────────────────
        findPreference<ListPreference>("pref_source_type")?.apply {
            value = AppManager.activeSourceType.name
            summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
            setOnPreferenceChangeListener { _, newValue ->
                AppManager.activeSourceType =
                    AppManager.SourceType.valueOf(newValue as String)
                true
            }
        }

        // ── Pick local file ───────────────────────────────────────────────────
        findPreference<Preference>("pref_source_file")?.apply {
            summary = if (AppManager.activeSourceUri.isNotEmpty())
                Uri.parse(AppManager.activeSourceUri).lastPathSegment
            else "Tap to pick video or image"

            setOnPreferenceClickListener {
                when (AppManager.activeSourceType) {
                    AppManager.SourceType.LOCAL_IMAGE -> imagePickerLauncher.launch("image/*")
                    else                              -> videoPickerLauncher.launch("video/*")
                }
                true
            }
        }

        // ── Network URL ───────────────────────────────────────────────────────
        findPreference<Preference>("pref_network_url")?.apply {
            summary = AppManager.activeSourceUri.ifEmpty { "rtmp:// or https://" }
            setOnPreferenceChangeListener { pref, newValue ->
                AppManager.activeSourceUri = newValue as String
                pref.summary = newValue
                restartCameraService()
                true
            }
        }

        // ── Flip H ────────────────────────────────────────────────────────────
        findPreference<SwitchPreferenceCompat>("pref_flip_h")?.apply {
            isChecked = AppManager.flipHorizontal
            setOnPreferenceChangeListener { _, v ->
                AppManager.flipHorizontal = v as Boolean
                restartCameraService()
                true
            }
        }

        // ── Flip V ────────────────────────────────────────────────────────────
        findPreference<SwitchPreferenceCompat>("pref_flip_v")?.apply {
            isChecked = AppManager.flipVertical
            setOnPreferenceChangeListener { _, v ->
                AppManager.flipVertical = v as Boolean
                restartCameraService()
                true
            }
        }

        // ── Mirror ────────────────────────────────────────────────────────────
        findPreference<SwitchPreferenceCompat>("pref_mirror")?.apply {
            isChecked = AppManager.mirrorMode
            setOnPreferenceChangeListener { _, v ->
                AppManager.mirrorMode = v as Boolean
                restartCameraService()
                true
            }
        }

        // ── Start service ─────────────────────────────────────────────────────
        findPreference<Preference>("pref_start_service")?.setOnPreferenceClickListener {
            if (AppManager.activeSourceUri.isEmpty()) {
                toast("Pick a source first")
            } else {
                VirtualCameraService.start(requireContext())
                AppManager.isServiceRunning = true
                toast("Virtual camera started")
            }
            true
        }

        // ── Stop service ──────────────────────────────────────────────────────
        findPreference<Preference>("pref_stop_service")?.setOnPreferenceClickListener {
            VirtualCameraService.stop(requireContext())
            AppManager.isServiceRunning = false
            toast("Virtual camera stopped")
            true
        }

        // ── Clear source ──────────────────────────────────────────────────────
        findPreference<Preference>("pref_clear_source")?.setOnPreferenceClickListener {
            AppManager.activeSourceUri = ""
            AppManager.activeSourceType = AppManager.SourceType.NONE
            VirtualCameraService.stop(requireContext())
            AppManager.isServiceRunning = false
            findPreference<Preference>("pref_source_file")?.summary = "Tap to pick video or image"
            toast("Source cleared")
            true
        }

        // ── Version ───────────────────────────────────────────────────────────
        findPreference<Preference>("pref_version")?.summary = try {
            requireContext().packageManager
                .getPackageInfo(requireContext().packageName, 0).versionName
        } catch (e: Exception) { "3.5.0" }
    }

    private fun restartCameraService() {
        if (AppManager.isServiceRunning) {
            VirtualCameraService.stop(requireContext())
            VirtualCameraService.start(requireContext())
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}
