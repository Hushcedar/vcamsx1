package com.blackbox.vcam.model;

/**
 * VCamConfig
 *
 * Data model representing the virtual camera configuration for a single
 * BlackBox guest application. Persisted via VCamPreferences (JSON/SharedPrefs).
 *
 * Mirrors VCamera's VideoStatues + VideoInfo models combined into one clean object.
 */
public class VCamConfig {

    // ── Fields ─────────────────────────────────────────────────────────────────
    private boolean mEnabled;
    private @VideoSourceType int mSourceType;
    private String  mVideoUri;       // file:// or rtmp:// or rtsp://
    private String  mGuestPackage;
    private int     mTargetWidth;
    private int     mTargetHeight;
    private boolean mMirrorFrontCamera;
    private boolean mAutoLoop;
    private float   mPlaybackSpeed;

    // ── Constructor ────────────────────────────────────────────────────────────
    public VCamConfig() {
        mEnabled           = false;
        mSourceType        = VideoSourceType.FILE;
        mTargetWidth       = 1280;
        mTargetHeight      = 720;
        mMirrorFrontCamera = false;
        mAutoLoop          = true;
        mPlaybackSpeed     = 1.0f;
    }

    // ── Builder pattern ────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final VCamConfig cfg = new VCamConfig();

        public Builder enabled(boolean v)              { cfg.mEnabled = v;           return this; }
        public Builder sourceType(@VideoSourceType int t) { cfg.mSourceType = t;     return this; }
        public Builder videoUri(String uri)            { cfg.mVideoUri = uri;        return this; }
        public Builder guestPackage(String pkg)        { cfg.mGuestPackage = pkg;    return this; }
        public Builder targetSize(int w, int h)        { cfg.mTargetWidth = w; cfg.mTargetHeight = h; return this; }
        public Builder mirrorFront(boolean v)          { cfg.mMirrorFrontCamera = v; return this; }
        public Builder autoLoop(boolean v)             { cfg.mAutoLoop = v;          return this; }
        public Builder playbackSpeed(float v)          { cfg.mPlaybackSpeed = v;     return this; }
        public VCamConfig build()                      { return cfg; }
    }

    // ── Getters / setters ──────────────────────────────────────────────────────
    public boolean isEnabled()               { return mEnabled; }
    public void    setEnabled(boolean v)     { mEnabled = v; }

    public @VideoSourceType int getSourceType()             { return mSourceType; }
    public void                 setSourceType(@VideoSourceType int t) { mSourceType = t; }

    public String getVideoUri()              { return mVideoUri; }
    public void   setVideoUri(String uri)    { mVideoUri = uri; }

    public String getGuestPackage()          { return mGuestPackage; }
    public void   setGuestPackage(String p)  { mGuestPackage = p; }

    public int  getTargetWidth()             { return mTargetWidth; }
    public int  getTargetHeight()            { return mTargetHeight; }
    public void setTargetSize(int w, int h)  { mTargetWidth = w; mTargetHeight = h; }

    public boolean isMirrorFrontCamera()         { return mMirrorFrontCamera; }
    public void    setMirrorFrontCamera(boolean v) { mMirrorFrontCamera = v; }

    public boolean isAutoLoop()              { return mAutoLoop; }
    public void    setAutoLoop(boolean v)    { mAutoLoop = v; }

    public float getPlaybackSpeed()          { return mPlaybackSpeed; }
    public void  setPlaybackSpeed(float v)   { mPlaybackSpeed = v; }

    @Override
    public String toString() {
        return "VCamConfig{" +
                "enabled=" + mEnabled +
                ", source=" + mSourceType +
                ", uri='" + mVideoUri + '\'' +
                ", pkg='" + mGuestPackage + '\'' +
                ", size=" + mTargetWidth + "×" + mTargetHeight +
                '}';
    }
}
