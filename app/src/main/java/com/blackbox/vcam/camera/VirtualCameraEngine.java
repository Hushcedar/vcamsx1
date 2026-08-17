package com.blackbox.vcam.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.blackbox.vcam.model.VCamConfig;
import com.blackbox.vcam.model.VideoSourceType;
import com.blackbox.vcam.util.VCamLogger;

/**
 * VirtualCameraEngine
 *
 * Intercepts Camera1 and Camera2 API calls within a BlackBox guest process,
 * replacing the real camera feed with a user-selected video file or RTMP stream.
 *
 * Architecture (mirrors VCamera's MainHook + VideoPlayer pipeline):
 *   ┌─────────────────────────────────────────────────────┐
 *   │  Guest App (cloned inside BlackBox)                  │
 *   │    calls Camera.open() / CameraManager.openCamera() │
 *   └──────────────────┬──────────────────────────────────┘
 *                       │  BlackBoxHookBridge intercepts
 *   ┌──────────────────▼──────────────────────────────────┐
 *   │  VirtualCameraEngine                                 │
 *   │    MediaPlayer / VideoToFramesPipeline               │
 *   │    → SurfaceTexture (fake texture)                   │
 *   │    → Surface (fed back to guest as camera surface)   │
 *   └─────────────────────────────────────────────────────┘
 */
public class VirtualCameraEngine {

    private static final String TAG = "VCam_Engine";
    private static volatile VirtualCameraEngine sInstance;

    // ──── State ────────────────────────────────────────────────────────────────
    private final Context mContext;
    private final AtomicBoolean mIsActive       = new AtomicBoolean(false);
    private final AtomicBoolean mNeedsRecreate  = new AtomicBoolean(false);

    // ──── Video pipeline ───────────────────────────────────────────────────────
    private MediaPlayer        mMediaPlayer;
    private VideoFramePipeline mFramePipeline;   // hw-decode path (mirrors VideoToFrames)
    private volatile byte[]    mDataBuffer = {0};

    // ──── Camera1 surfaces ─────────────────────────────────────────────────────
    private SurfaceTexture mC1FakeTexture;
    private Surface        mC1FakeSurface;
    private SurfaceTexture mOriginalC1PreviewTexture;
    private Surface        mOriginalPreviewSurface;
    private Camera         mOriginPreviewCamera;

    // ──── Camera2 surfaces ─────────────────────────────────────────────────────
    private SurfaceTexture        mC2VirtualSurfaceTexture;
    private Surface               mC2ReaderSurface;
    private OutputConfiguration   mOutputConfiguration;
    private SessionConfiguration  mSessionConfiguration;
    private SessionConfiguration  mFakeSessionConfiguration;
    private CameraDevice.StateCallback mC2StateCallback;

    // ──── Handler ──────────────────────────────────────────────────────────────
    private HandlerThread mCameraThread;
    private Handler       mCameraHandler;

    // ──── Config ───────────────────────────────────────────────────────────────
    private VCamConfig mConfig;

    // ──────────────────────────────────────────────────────────────────────────
    private VirtualCameraEngine(Context context) {
        mContext = context.getApplicationContext();
        startCameraHandlerThread();
    }

    public static VirtualCameraEngine getInstance(Context context) {
        if (sInstance == null) {
            synchronized (VirtualCameraEngine.class) {
                if (sInstance == null) {
                    sInstance = new VirtualCameraEngine(context);
                }
            }
        }
        return sInstance;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Public API called by BlackBoxHookBridge
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Apply config and start pumping frames. Called when user enables virtual
     * camera for a specific guest app inside BlackBox.
     */
    public synchronized void applyConfig(VCamConfig config) {
        mConfig = config;
        if (config == null || !config.isEnabled()) {
            stopEngine();
            return;
        }
        startEngine();
    }

    public VCamConfig getCurrentConfig() {
        return mConfig;
    }

    public boolean isActive() {
        return mIsActive.get();
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Camera1 Hook handlers
    //  These are called by BlackBoxHookBridge after it intercepts Camera1 calls
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Called when guest's Camera.setPreviewTexture / setPreviewDisplay is intercepted.
     * We store the original surface and swap in a fake one backed by our video pipeline.
     */
    public void onCamera1SetPreview(Camera camera, SurfaceTexture originalTexture) {
        if (!mIsActive.get()) return;
        VCamLogger.d(TAG, "onCamera1SetPreview intercepted");
        mOriginPreviewCamera    = camera;
        mOriginalC1PreviewTexture = originalTexture;

        ensureC1FakeTexture();
        try {
            camera.setPreviewTexture(mC1FakeTexture);
        } catch (IOException e) {
            VCamLogger.e(TAG, "setPreviewTexture failed: " + e.getMessage());
        }
    }

    public void onCamera1SetPreviewSurface(Camera camera, Surface originalSurface) {
        if (!mIsActive.get()) return;
        mOriginalPreviewSurface = originalSurface;

        ensureC1FakeSurface();
        try {
            camera.setPreviewDisplay(null); // clear it; we drive via SurfaceTexture
        } catch (IOException e) {
            VCamLogger.e(TAG, "clear previewDisplay failed");
        }
        // Feed frames to original surface ourselves
        redirectC1FramesToSurface(originalSurface);
    }

    /**
     * Called on Camera.startPreview interception.
     * We tell the camera to render to our fake texture, then the pipeline
     * copies decoded video frames onto the surface the guest expects.
     */
    public void onCamera1StartPreview(Camera camera) {
        if (!mIsActive.get()) return;
        VCamLogger.d(TAG, "onCamera1StartPreview — redirecting feed");
        ensureC1FakeTexture();
        try {
            camera.setPreviewTexture(mC1FakeTexture);
        } catch (IOException ignored) {}

        startVideoFeedToSurface(mC1FakeSurface);
    }

    /**
     * Called on Camera.onPreviewFrame callback interception.
     * Replaces NV21 byte[] with our own synthesized frame bytes derived
     * from the current decoded video frame.
     */
    public byte[] interceptPreviewFrame(byte[] originalFrame, int width, int height) {
        if (!mIsActive.get()) return originalFrame;
        byte[] synth = mDataBuffer;
        if (synth != null && synth.length == width * height * 3 / 2) {
            return synth;
        }
        return originalFrame;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Camera2 Hook handlers
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Called when CameraDevice.createCaptureSession is intercepted.
     * Swaps the output surfaces with our virtual ones.
     */
    public SessionConfiguration buildFakeSessionConfiguration(
            SessionConfiguration original,
            CameraDevice device) {

        if (!mIsActive.get()) return original;

        VCamLogger.d(TAG, "buildFakeSessionConfiguration — injecting virtual surface");

        // Create a virtual SurfaceTexture to receive rendered frames
        mC2VirtualSurfaceTexture = new SurfaceTexture(0);
        mC2VirtualSurfaceTexture.detachFromGLContext();
        Surface virtualSurface = new Surface(mC2VirtualSurfaceTexture);

        mC2ReaderSurface = virtualSurface;

        OutputConfiguration fakeOutput = new OutputConfiguration(virtualSurface);
        mOutputConfiguration = fakeOutput;

        Executor callbackExecutor = Executors.newSingleThreadExecutor();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mFakeSessionConfiguration = new SessionConfiguration(
                    SessionConfiguration.SESSION_REGULAR,
                    Collections.singletonList(fakeOutput),
                    callbackExecutor,
                    buildC2SessionCallback(device)
            );
            startVideoFeedToSurface(virtualSurface);
            return mFakeSessionConfiguration;
        }

        return original; // fallback: pre-P cameras handled via Camera1 path
    }

    /**
     * Build a synthetic CameraCaptureSession.StateCallback that redirects
     * completed captures into our virtual surface.
     */
    private CameraCaptureSession.StateCallback buildC2SessionCallback(CameraDevice device) {
        return new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                try {
                    CaptureRequest.Builder builder =
                            device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    builder.addTarget(mC2ReaderSurface);
                    session.setRepeatingRequest(builder.build(), null, mCameraHandler);
                    VCamLogger.d(TAG, "Camera2 repeating request set on virtual surface");
                } catch (Exception e) {
                    VCamLogger.e(TAG, "Camera2 capture setup error: " + e.getMessage());
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
                VCamLogger.e(TAG, "Camera2 session configure failed");
            }
        };
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Internal Engine Lifecycle
    // ──────────────────────────────────────────────────────────────────────────

    private void startEngine() {
        if (mIsActive.compareAndSet(false, true)) {
            VCamLogger.d(TAG, "Engine starting — source: " + (mConfig != null ? mConfig.getSourceType() : "null"));
            initVideoSource();
        }
    }

    private void stopEngine() {
        if (mIsActive.compareAndSet(true, false)) {
            VCamLogger.d(TAG, "Engine stopping");
            releaseVideoSource();
            releaseC1Surfaces();
            releaseC2Surfaces();
        }
    }

    private void initVideoSource() {
        if (mConfig == null) return;

        switch (mConfig.getSourceType()) {
            case VideoSourceType.FILE:
                initMediaPlayer(Uri.parse(mConfig.getVideoUri()));
                break;
            case VideoSourceType.RTMP:
            case VideoSourceType.RTSP:
                initRtmpPlayer(mConfig.getVideoUri());
                break;
            default:
                VCamLogger.w(TAG, "Unknown source type: " + mConfig.getSourceType());
        }
    }

    private void initMediaPlayer(Uri videoUri) {
        releaseVideoSource();
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mContext, videoUri);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
                VCamLogger.e(TAG, "MediaPlayer error what=" + what + " extra=" + extra);
                mNeedsRecreate.set(true);
                return true;
            });
            mMediaPlayer.setOnPreparedListener(mp -> {
                VCamLogger.d(TAG, "MediaPlayer prepared, starting");
                mp.start();
            });
            mMediaPlayer.prepareAsync();
            VCamLogger.d(TAG, "MediaPlayer init for URI: " + videoUri);
        } catch (IOException e) {
            VCamLogger.e(TAG, "MediaPlayer init failed: " + e.getMessage());
        }
    }

    private void initRtmpPlayer(String url) {
        // IjkMediaPlayer path for RTMP/RTSP (same as VCamera's VideoPlayer.initRTMPStreamPlayer)
        // For compile-time simplicity we delegate to the FramePipeline
        mFramePipeline = new VideoFramePipeline(mContext);
        mFramePipeline.startStream(url, bytes -> {
            if (bytes != null && bytes.length > 0) {
                mDataBuffer = bytes;
            }
        });
    }

    private void startVideoFeedToSurface(Surface targetSurface) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(targetSurface);
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
            VCamLogger.d(TAG, "MediaPlayer surface set and playing");
        } else if (mFramePipeline != null) {
            mFramePipeline.setOutputSurface(targetSurface);
        }
    }

    private void redirectC1FramesToSurface(Surface targetSurface) {
        // For Camera1 surface-holder path: render our fake texture onto targetSurface
        if (mC1FakeTexture != null) {
            mCameraHandler.post(() -> {
                VCamLogger.d(TAG, "Redirecting Camera1 frames to real surface");
                startVideoFeedToSurface(targetSurface);
            });
        }
    }

    private void releaseVideoSource() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception ignored) {}
            mMediaPlayer = null;
        }
        if (mFramePipeline != null) {
            mFramePipeline.release();
            mFramePipeline = null;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Surface helpers
    // ──────────────────────────────────────────────────────────────────────────

    private void ensureC1FakeTexture() {
        if (mC1FakeTexture == null) {
            mC1FakeTexture = new SurfaceTexture(0);
            mC1FakeTexture.detachFromGLContext();
            mC1FakeSurface = new Surface(mC1FakeTexture);
            VCamLogger.d(TAG, "C1 fake texture/surface created");
        }
    }

    private void ensureC1FakeSurface() {
        ensureC1FakeTexture();
    }

    private void releaseC1Surfaces() {
        if (mC1FakeSurface != null)  { mC1FakeSurface.release();  mC1FakeSurface  = null; }
        if (mC1FakeTexture != null)  { mC1FakeTexture.release();  mC1FakeTexture  = null; }
        mOriginalPreviewSurface       = null;
        mOriginalC1PreviewTexture     = null;
        mOriginPreviewCamera          = null;
    }

    private void releaseC2Surfaces() {
        if (mC2ReaderSurface != null)          { mC2ReaderSurface.release();          mC2ReaderSurface = null; }
        if (mC2VirtualSurfaceTexture != null)  { mC2VirtualSurfaceTexture.release();  mC2VirtualSurfaceTexture = null; }
        mOutputConfiguration      = null;
        mSessionConfiguration     = null;
        mFakeSessionConfiguration = null;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Handler thread
    // ──────────────────────────────────────────────────────────────────────────

    private void startCameraHandlerThread() {
        mCameraThread = new HandlerThread("VCam_CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    public Handler getCameraHandler() { return mCameraHandler; }

    /**
     * Update synthetic NV21 frame buffer from a decoded RGBA frame.
     * Called by VideoFramePipeline on each decoded frame callback.
     */
    public void updateDataBuffer(byte[] nv21Bytes) {
        mDataBuffer = nv21Bytes;
    }

    public byte[] getDataBuffer() { return mDataBuffer; }
}
