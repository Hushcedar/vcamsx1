# BlackBox VCam — Integration Guide

## What This Is

A virtual camera module that plugs into BlackBox's existing app-cloning
infrastructure and adds the same camera-replacement capability that VCamera
provides — but controlled from inside BlackBox's own three-dot menu per app,
with no separate VCamera app required.

---

## Architecture Overview

```
BlackBox Host App
  └── VCamApplication.onCreate()
        └── VCamBlackBoxPlugin (registered as BlackBox plugin)
              ├── onAppStarted(pkg) → VirtualCameraEngine.applyConfig()
              │                         └── MediaPlayer / VideoFramePipeline
              │                               └── SurfaceTexture (fake)
              │                                     └── Surface → guest Camera
              └── BlackBoxHookBridge
                    ├── hookCamera1SetPreviewTexture()  ← Camera1 path
                    ├── hookCamera1StartPreview()
                    ├── hookCamera1PreviewFrame()       ← NV21 injection
                    └── hookCamera2CreateCaptureSession() ← Camera2 path
```

---

## Step 1: Add as a Module to BlackBox

Clone this repo inside your BlackBox source tree:

```
BlackBox/
  app/              ← BlackBox's existing host app
  vcam/             ← This module (rename if needed)
  settings.gradle   ← Add: include ':vcam'
```

In BlackBox's `app/build.gradle`:
```groovy
implementation project(':vcam')
```

---

## Step 2: Wire up VCamApplication

In BlackBox's existing `Application` class (or its `onCreate`), add:

```java
// After BlackBox initialises:
VCamBlackBoxPlugin vcamPlugin = new VCamBlackBoxPlugin(this);
// Register so we receive guest app lifecycle events:
BlackBoxCore.get().registerPlugin(new IAppPlugin() {
    public void onAppInstalled(String pkg, int uid)   { vcamPlugin.onAppInstalled(pkg, uid); }
    public void onAppUninstalled(String pkg, int uid) { vcamPlugin.onAppUninstalled(pkg, uid); }
    public void onAppStarted(String pkg, int uid)     { vcamPlugin.onAppStarted(pkg, uid); }
    public void onAppStopped(String pkg, int uid)     { vcamPlugin.onAppStopped(pkg, uid); }
});

// Store reference for use in UI:
BlackBoxApp.sVCamPlugin = vcamPlugin;
```

---

## Step 3: Inject the Three-Dot Menu Item

Find where BlackBox builds its per-app popup menu (typically in the host
fragment that shows the installed-apps RecyclerView). Look for a method like
`showAppMenu(String packageName)` or the adapter's `onCreateContextMenu`.

Add:

```java
// In the method that creates the per-app PopupMenu:
VCamMenuInjector.inject(popup.getMenu(), packageName, vcamPlugin);

// In the MenuItemClickListener:
popup.setOnMenuItemClickListener(item -> {
    // Your existing BlackBox item handling...
    if (VCamMenuInjector.handleSelection(item, packageName, activity, vcamPlugin)) {
        return true;
    }
    // ...
    return false;
});
```

Or use the convenience method for the three-dot button directly:

```java
VCamMenuInjector.attachToAppCard(threeDotButton, packageName, activity, vcamPlugin);
```

---

## Step 4: Camera Hook Calls

BlackBox already intercepts Camera calls for guest processes.
Find BlackBox's Camera proxy layer (usually in `com.lody.virtual.client.hook.proxies`
or a similar path depending on the BlackBox fork you're using).

### Camera1

In the Camera service proxy, after the real method runs:

```java
// After Camera.setPreviewTexture():
vcamPlugin.getBridge().hookCamera1SetPreviewTexture(camera, surfaceTexture);

// After Camera.startPreview():
vcamPlugin.getBridge().hookCamera1StartPreview(camera);

// Inside onPreviewFrame callback:
byte[] frame = vcamPlugin.getBridge().hookCamera1PreviewFrame(originalFrame, camera, width, height);
// use frame instead of originalFrame
```

### Camera2

In the CameraDevice proxy:

```java
// Before forwarding createCaptureSession():
if (Build.VERSION.SDK_INT >= 28) {
    sessionConfig = vcamPlugin.getBridge()
            .hookCamera2CreateCaptureSession(device, sessionConfig);
}
// then call device.createCaptureSession(sessionConfig)
```

---

## Step 5: The User Flow

1. User opens BlackBox → sees cloned apps grid
2. User taps ⋮ (three-dot) on a cloned app card
3. Menu shows: Clone / Clear Data / Force Stop ... **Virtual Camera** ← (new)
4. Tapping "Virtual Camera" opens `BottomSheetVCamMenu`
5. User toggles ON, picks a video file from gallery or enters RTMP URL
6. Taps Apply
7. Next time the cloned app opens its camera, it sees the selected video instead

---

## File Map

```
vcam/
  app/src/main/
    java/com/blackbox/vcam/
      VCamApplication.java           ← Application class, bootstrap
      camera/
        VirtualCameraEngine.java     ← Core engine (Camera1 + Camera2)
        VideoFramePipeline.java      ← HW decode, NV21 output
        VCamContentProvider.java     ← File access for guest processes
      hook/
        BlackBoxHookBridge.java      ← Wires hook calls to engine
        VCamBlackBoxPlugin.java      ← BlackBox IAppPlugin implementation
      model/
        VCamConfig.java              ← Per-app config model
        VideoSourceType.java         ← File / RTMP / RTSP enum
      service/
        VCamForegroundService.java   ← Keeps engine alive in foreground
      ui/
        BottomSheetVCamMenu.java     ← Three-dot settings panel
        VCamMenuInjector.java        ← Injects menu item into BlackBox UI
      util/
        VCamLogger.java
        VCamPreferences.java         ← Persists configs as JSON
    res/
      layout/bottom_sheet_vcam_menu.xml
      values/colors.xml
    AndroidManifest.xml
  build.gradle
INTEGRATION_GUIDE.md
```

---

## Dependencies

| Library | Purpose |
|---------|---------|
| `com.google.code.gson:gson:2.10.1` | Config serialization |
| `com.google.android.material:material:1.12.0` | BottomSheetDialogFragment |
| `tv.danmaku.ijk.media:ijkplayer-*` | RTMP/RTSP streaming (optional) |
| BlackBox as project module | Guest app lifecycle callbacks |

---

## Notes

- **No Xposed needed.** BlackBox is itself the hook layer. We just extend it.
- **Camera1 + Camera2 both covered.** Most social apps use Camera2; Camera1
  path handles legacy apps.
- **RTMP streaming** requires IjkMediaPlayer. If you only need local file
  playback, the built-in `MediaPlayer` path covers it with zero extra deps.
- **Config persists** across BlackBox and device restarts via `VCamPreferences`.
- Tested against BlackBox 4.0.0 (the APK you provided). Internal class paths
  may differ in forks — adjust the reflection in `BlackBoxHookBridge` and
  `VCamApplication` accordingly.
