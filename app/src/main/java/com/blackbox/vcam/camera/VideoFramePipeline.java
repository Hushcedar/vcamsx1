package com.blackbox.vcam.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import com.blackbox.vcam.util.VCamLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * VideoFramePipeline
 *
 * Hardware-accelerated video decode pipeline. Mirrors VCamera's VideoToFrames class.
 * Decodes video (file or RTMP stream) frame-by-frame using MediaCodec and
 * writes output to either:
 *   a) A Surface (Camera2 / Camera1 direct render path)
 *   b) NV21 byte[] callbacks (Camera1 onPreviewFrame injection path)
 *
 * Pipeline flow:
 *   MediaExtractor → MediaCodec(decoder) → ImageReader → Surface / byte[] callback
 */
public class VideoFramePipeline {

    private static final String TAG          = "VCam_FramePipeline";
    private static final int    MAX_IMAGES   = 3;
    private static final int    TIMEOUT_US   = 10_000;

    private final Context       mContext;
    private final AtomicBoolean mRunning = new AtomicBoolean(false);

    // ──── Codec ────────────────────────────────────────────────────────────────
    private MediaExtractor mExtractor;
    private MediaCodec     mDecoder;
    private Surface        mOutputSurface;
    private ImageReader    mImageReader;

    // ──── Callback ─────────────────────────────────────────────────────────────
    public interface FrameCallback {
        /** Called on decode thread with NV21 bytes or null on error */
        void onFrame(byte[] nv21);
    }
    private FrameCallback mFrameCallback;

    // ──── Thread ───────────────────────────────────────────────────────────────
    private HandlerThread mDecodeThread;
    private Handler       mDecodeHandler;

    // ──── Dimensions (default 1280×720) ────────────────────────────────────────
    private int mWidth  = 1280;
    private int mHeight = 720;

    public VideoFramePipeline(Context context) {
        mContext = context;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Public API
    // ──────────────────────────────────────────────────────────────────────────

    /** Start decoding a local file URI, delivering NV21 bytes via callback. */
    public void startFile(Uri fileUri, FrameCallback callback) {
        mFrameCallback = callback;
        startDecodeThread(() -> initFileDecoder(fileUri));
    }

    /** Start an RTMP/RTSP stream, delivering NV21 bytes via callback. */
    public void startStream(String url, FrameCallback callback) {
        mFrameCallback = callback;
        startDecodeThread(() -> initStreamDecoder(url));
    }

    /** Set (or change) the output surface; pipeline renders into it directly. */
    public void setOutputSurface(Surface surface) {
        mOutputSurface = surface;
        VCamLogger.d(TAG, "Output surface updated");
    }

    public void release() {
        mRunning.set(false);
        releaseCodec();
        if (mDecodeThread != null) {
            mDecodeThread.quitSafely();
            mDecodeThread = null;
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Decoder init
    // ──────────────────────────────────────────────────────────────────────────

    private void initFileDecoder(Uri fileUri) {
        try {
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(mContext, fileUri, null);
            int videoTrack = selectVideoTrack(mExtractor);
            if (videoTrack < 0) {
                VCamLogger.e(TAG, "No video track in file: " + fileUri);
                return;
            }
            mExtractor.selectTrack(videoTrack);
            MediaFormat fmt = mExtractor.getTrackFormat(videoTrack);
            mWidth  = fmt.getInteger(MediaFormat.KEY_WIDTH);
            mHeight = fmt.getInteger(MediaFormat.KEY_HEIGHT);
            String mime = fmt.getString(MediaFormat.KEY_MIME);

            initDecoder(mime, fmt);
            driveDecodeLoop();
        } catch (IOException e) {
            VCamLogger.e(TAG, "initFileDecoder failed: " + e.getMessage());
        }
    }

    private void initStreamDecoder(String url) {
        try {
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(url);
            int videoTrack = selectVideoTrack(mExtractor);
            if (videoTrack < 0) {
                VCamLogger.e(TAG, "No video track in stream: " + url);
                return;
            }
            mExtractor.selectTrack(videoTrack);
            MediaFormat fmt = mExtractor.getTrackFormat(videoTrack);
            mWidth  = fmt.containsKey(MediaFormat.KEY_WIDTH)  ? fmt.getInteger(MediaFormat.KEY_WIDTH)  : 1280;
            mHeight = fmt.containsKey(MediaFormat.KEY_HEIGHT) ? fmt.getInteger(MediaFormat.KEY_HEIGHT) : 720;
            String mime = fmt.getString(MediaFormat.KEY_MIME);

            initDecoder(mime, fmt);
            driveDecodeLoop();
        } catch (IOException e) {
            VCamLogger.e(TAG, "initStreamDecoder failed: " + e.getMessage());
        }
    }

    private void initDecoder(String mime, MediaFormat fmt) throws IOException {
        if (mOutputSurface != null) {
            // Render path: decode directly to Surface (zero-copy, preferred)
            mDecoder = MediaCodec.createDecoderByType(mime);
            mDecoder.configure(fmt, mOutputSurface, null, 0);
        } else {
            // Byte path: decode to ImageReader → NV21 callback
            mImageReader = ImageReader.newInstance(mWidth, mHeight, ImageFormat.YUV_420_888, MAX_IMAGES);
            mDecoder = MediaCodec.createDecoderByType(mime);
            mDecoder.configure(fmt, mImageReader.getSurface(), null, 0);
            mImageReader.setOnImageAvailableListener(this::onImageAvailable, mDecodeHandler);
        }
        mDecoder.start();
        VCamLogger.d(TAG, "Decoder started: " + mime + " " + mWidth + "×" + mHeight);
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Decode loop (runs on decode thread)
    // ──────────────────────────────────────────────────────────────────────────

    private void driveDecodeLoop() {
        mRunning.set(true);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        boolean inputEOS = false;

        while (mRunning.get()) {
            // ── Feed input ──────────────────────────────────────────────────
            if (!inputEOS) {
                int inputIdx = mDecoder.dequeueInputBuffer(TIMEOUT_US);
                if (inputIdx >= 0) {
                    ByteBuffer buf = mDecoder.getInputBuffer(inputIdx);
                    if (buf != null) {
                        int sampleSize = mExtractor.readSampleData(buf, 0);
                        if (sampleSize < 0) {
                            // EOS → loop: seek back to start
                            mDecoder.queueInputBuffer(inputIdx, 0, 0, 0,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            mExtractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
                            inputEOS = false; // loop
                        } else {
                            long pts = mExtractor.getSampleTime();
                            mDecoder.queueInputBuffer(inputIdx, 0, sampleSize, pts, 0);
                            mExtractor.advance();
                        }
                    }
                }
            }

            // ── Drain output ────────────────────────────────────────────────
            int outputIdx = mDecoder.dequeueOutputBuffer(info, TIMEOUT_US);
            if (outputIdx >= 0) {
                boolean renderToSurface = (mOutputSurface != null);
                mDecoder.releaseOutputBuffer(outputIdx, renderToSurface);
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    inputEOS = false; // already seeked above, keep going
                }
            }

            // Throttle to ~30fps if rendering to surface (no sync otherwise)
            if (mOutputSurface != null) {
                try { Thread.sleep(33); } catch (InterruptedException e) { break; }
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  ImageReader callback → NV21
    // ──────────────────────────────────────────────────────────────────────────

    private void onImageAvailable(ImageReader reader) {
        try (Image image = reader.acquireLatestImage()) {
            if (image == null || mFrameCallback == null) return;
            byte[] nv21 = yuv420_888ToNv21(image);
            mFrameCallback.onFrame(nv21);
        } catch (Exception e) {
            VCamLogger.w(TAG, "onImageAvailable error: " + e.getMessage());
        }
    }

    /**
     * Convert YUV_420_888 Image to NV21 byte[] suitable for Camera1 onPreviewFrame.
     * NV21 layout: Y plane (w*h bytes) followed by interleaved VU (w*h/2 bytes).
     */
    private byte[] yuv420_888ToNv21(Image image) {
        Image.Plane[] planes = image.getPlanes();
        int w = image.getWidth();
        int h = image.getHeight();
        int ySize  = w * h;
        int uvSize = ySize / 2;
        byte[] nv21 = new byte[ySize + uvSize];

        // ── Y plane ──────────────────────────────────────────────────────────
        ByteBuffer yBuf    = planes[0].getBuffer();
        int        yStride = planes[0].getRowStride();
        if (yStride == w) {
            yBuf.get(nv21, 0, ySize);
        } else {
            for (int row = 0; row < h; row++) {
                yBuf.position(row * yStride);
                yBuf.get(nv21, row * w, w);
            }
        }

        // ── U / V planes → interleaved VU ────────────────────────────────────
        ByteBuffer uBuf    = planes[1].getBuffer();
        ByteBuffer vBuf    = planes[2].getBuffer();
        int        uvStride   = planes[1].getRowStride();
        int        uvPixelStr = planes[1].getPixelStride();

        if (uvPixelStr == 2 && planes[2].getPixelStride() == 2) {
            // Already interleaved (most devices): copy V plane which overlaps U
            vBuf.get(nv21, ySize, uvSize);
        } else {
            // Interleave manually
            int pos = ySize;
            for (int row = 0; row < h / 2; row++) {
                for (int col = 0; col < w / 2; col++) {
                    int uIdx = row * uvStride + col * uvPixelStr;
                    int vIdx = row * planes[2].getRowStride() + col * planes[2].getPixelStride();
                    nv21[pos++] = vBuf.get(vIdx);
                    nv21[pos++] = uBuf.get(uIdx);
                }
            }
        }

        return nv21;
    }

    // ──────────────────────────────────────────────────────────────────────────
    //  Helpers
    // ──────────────────────────────────────────────────────────────────────────

    private int selectVideoTrack(MediaExtractor extractor) {
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            MediaFormat fmt = extractor.getTrackFormat(i);
            String mime = fmt.getString(MediaFormat.KEY_MIME);
            if (mime != null && mime.startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }

    private void startDecodeThread(Runnable initTask) {
        mDecodeThread = new HandlerThread("VCam_DecodeThread");
        mDecodeThread.start();
        mDecodeHandler = new Handler(mDecodeThread.getLooper());
        mDecodeHandler.post(initTask);
    }

    private void releaseCodec() {
        if (mDecoder != null) {
            try { mDecoder.stop(); mDecoder.release(); } catch (Exception ignored) {}
            mDecoder = null;
        }
        if (mExtractor != null) {
            mExtractor.release();
            mExtractor = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }
}
