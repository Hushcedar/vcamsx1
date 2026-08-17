package com.blackbox.vcam.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.blackbox.vcam.R;
import com.blackbox.vcam.hook.VCamBlackBoxPlugin;
import com.blackbox.vcam.model.VCamConfig;
import com.blackbox.vcam.model.VideoSourceType;
import com.blackbox.vcam.util.VCamLogger;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * BottomSheetVCamMenu
 *
 * Three-dot menu panel for virtual camera settings per BlackBox guest app.
 * Surfaces inside BlackBox's per-app context menu (long press or three-dot on app card).
 *
 * UI layout:
 *   - Toggle: Enable virtual camera for this app
 *   - Source selector: Video File | RTMP/RTSP Stream
 *   - File picker + thumbnail preview
 *   - Stream URL input
 *   - Toggle: Mirror front camera
 *   - Toggle: Auto-loop video
 *   - Apply button
 */
public class BottomSheetVCamMenu extends BottomSheetDialogFragment {

    private static final String TAG            = "VCam_Menu";
    private static final String ARG_PACKAGE    = "arg_package";
    private static final int    REQ_PICK_VIDEO = 1001;

    private String             mGuestPackage;
    private VCamBlackBoxPlugin mPlugin;
    private VCamConfig         mWorkingConfig;
    private Uri                mSelectedVideoUri;

    // Views
    private Switch      mSwitchEnabled;
    private RadioGroup  mRadioSourceGroup;
    private RadioButton mRadioFile;
    private RadioButton mRadioRtmp;
    private Button      mBtnPickFile;
    private TextView    mTvSelectedFile;
    private EditText    mEtStreamUrl;
    private Switch      mSwitchMirror;
    private Switch      mSwitchLoop;
    private Button      mBtnApply;
    private ImageView   mIvVideoThumb;

    // ── Factory ────────────────────────────────────────────────────────────────

    public static BottomSheetVCamMenu show(FragmentManager fm, String packageName,
                                           VCamBlackBoxPlugin plugin) {
        BottomSheetVCamMenu sheet = new BottomSheetVCamMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PACKAGE, packageName);
        sheet.setArguments(args);
        sheet.mPlugin = plugin;
        sheet.show(fm, "vcam_menu");
        return sheet;
    }

    // ── Lifecycle ──────────────────────────────────────────────────────────────

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGuestPackage = getArguments().getString(ARG_PACKAGE);
        }
        if (mPlugin != null && mGuestPackage != null) {
            mWorkingConfig = mPlugin.getVCamConfig(mGuestPackage);
        }
        if (mWorkingConfig == null) {
            mWorkingConfig = VCamConfig.builder()
                    .guestPackage(mGuestPackage)
                    .enabled(false)
                    .sourceType(VideoSourceType.FILE)
                    .autoLoop(true)
                    .build();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_vcam_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        bindViews(root);
        populate();
        attachListeners();
    }

    // ── View binding ───────────────────────────────────────────────────────────

    private void bindViews(View root) {
        mSwitchEnabled    = root.findViewById(R.id.switch_vcam_enabled);
        mRadioSourceGroup = root.findViewById(R.id.rg_source_type);
        mRadioFile        = root.findViewById(R.id.rb_source_file);
        mRadioRtmp        = root.findViewById(R.id.rb_source_rtmp);
        mBtnPickFile      = root.findViewById(R.id.btn_pick_video);
        mTvSelectedFile   = root.findViewById(R.id.tv_selected_file);
        mEtStreamUrl      = root.findViewById(R.id.et_stream_url);
        mSwitchMirror     = root.findViewById(R.id.switch_mirror_front);
        mSwitchLoop       = root.findViewById(R.id.switch_auto_loop);
        mBtnApply         = root.findViewById(R.id.btn_apply_vcam);
        mIvVideoThumb     = root.findViewById(R.id.iv_video_thumb);
    }

    private void populate() {
        mSwitchEnabled.setChecked(mWorkingConfig.isEnabled());
        mSwitchMirror.setChecked(mWorkingConfig.isMirrorFrontCamera());
        mSwitchLoop.setChecked(mWorkingConfig.isAutoLoop());

        boolean isFile = mWorkingConfig.getSourceType() == VideoSourceType.FILE;
        if (isFile) mRadioFile.setChecked(true);
        else        mRadioRtmp.setChecked(true);
        flipSourceUI(isFile);

        String uri = mWorkingConfig.getVideoUri();
        if (uri != null) {
            if (isFile) {
                mSelectedVideoUri = Uri.parse(uri);
                mTvSelectedFile.setText(lastSegment(mSelectedVideoUri));
            } else {
                mEtStreamUrl.setText(uri);
            }
        }
    }

    private void attachListeners() {
        mRadioSourceGroup.setOnCheckedChangeListener((g, id) ->
                flipSourceUI(id == R.id.rb_source_file));

        mBtnPickFile.setOnClickListener(v -> pickVideo());
        mBtnApply.setOnClickListener(v -> applyAndClose());
    }

    // ── Source UI toggle ───────────────────────────────────────────────────────

    private void flipSourceUI(boolean isFile) {
        mBtnPickFile.setVisibility(isFile    ? View.VISIBLE : View.GONE);
        mTvSelectedFile.setVisibility(isFile ? View.VISIBLE : View.GONE);
        mIvVideoThumb.setVisibility(isFile   ? View.VISIBLE : View.GONE);
        mEtStreamUrl.setVisibility(isFile    ? View.GONE    : View.VISIBLE);
    }

    // ── Video picker ───────────────────────────────────────────────────────────

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQ_PICK_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_VIDEO
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {

            mSelectedVideoUri = data.getData();
            // Persist read permission across reboots
            requireContext().getContentResolver().takePersistableUriPermission(
                    mSelectedVideoUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            mTvSelectedFile.setText(lastSegment(mSelectedVideoUri));
            VCamLogger.d(TAG, "Video picked: " + mSelectedVideoUri);
            loadThumb(mSelectedVideoUri);
        }
    }

    private void loadThumb(Uri uri) {
        new Thread(() -> {
            try {
                android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
                mmr.setDataSource(requireContext(), uri);
                android.graphics.Bitmap bmp = mmr.getFrameAtTime(1_000_000L);
                mmr.release();
                if (bmp != null && getView() != null) {
                    requireActivity().runOnUiThread(() -> {
                        mIvVideoThumb.setImageBitmap(bmp);
                        mIvVideoThumb.setVisibility(View.VISIBLE);
                    });
                }
            } catch (Exception e) {
                VCamLogger.w(TAG, "Thumbnail load error: " + e.getMessage());
            }
        }).start();
    }

    // ── Apply ──────────────────────────────────────────────────────────────────

    private void applyAndClose() {
        // Validate and collect source
        if (mRadioRtmp.isChecked()) {
            String url = mEtStreamUrl.getText().toString().trim();
            if (url.isEmpty()) {
                mEtStreamUrl.setError("Stream URL is required");
                return;
            }
            if (!url.startsWith("rtmp://") && !url.startsWith("rtsp://")) {
                mEtStreamUrl.setError("URL must start with rtmp:// or rtsp://");
                return;
            }
            mWorkingConfig.setSourceType(VideoSourceType.RTMP);
            mWorkingConfig.setVideoUri(url);
        } else {
            if (mSelectedVideoUri == null) {
                Toast.makeText(requireContext(), "Please select a video file", Toast.LENGTH_SHORT).show();
                return;
            }
            mWorkingConfig.setSourceType(VideoSourceType.FILE);
            mWorkingConfig.setVideoUri(mSelectedVideoUri.toString());
        }

        mWorkingConfig.setEnabled(mSwitchEnabled.isChecked());
        mWorkingConfig.setMirrorFrontCamera(mSwitchMirror.isChecked());
        mWorkingConfig.setAutoLoop(mSwitchLoop.isChecked());
        mWorkingConfig.setGuestPackage(mGuestPackage);

        if (mPlugin != null) {
            mPlugin.applyVCamConfig(mGuestPackage, mWorkingConfig);
            String msg = "Virtual camera " + (mWorkingConfig.isEnabled() ? "ENABLED" : "DISABLED");
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            VCamLogger.d(TAG, "Applied: " + mWorkingConfig);
        }
        dismiss();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private String lastSegment(Uri uri) {
        if (uri == null) return "";
        String seg = uri.getLastPathSegment();
        return seg != null ? seg : uri.toString();
    }

    public void setPlugin(VCamBlackBoxPlugin plugin) {
        mPlugin = plugin;
    }
}
