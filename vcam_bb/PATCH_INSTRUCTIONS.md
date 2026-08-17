# VCamera × BlackBox — Patch Instructions

## Files to drop into BlackBox source (github.com/niunaijun/BlackBoxes)

```
app/src/main/java/top/niunaijun/blackboxa/view/vcam/
  VCamConfig.kt              ← data class
  VCamPrefs.kt               ← per-package SharedPrefs storage
  VCamSettingsDialog.kt      ← bottom sheet UI (4 protect modes)
  VCamCameraHook.kt          ← camera feed injector

app/src/main/res/layout/
  dialog_vcam_settings.xml   ← bottom sheet layout
```

---

## 1. Wire the dialog into the three-dot popup menu

In `AppListFragment.kt` (or wherever BB shows the per-app popup), find the
three-dot click handler and add a "Virtual Camera" item:

```kotlin
// Inside showAppMenu(appInfo: AppInfo) or similar:
popupMenu.menu.add("Virtual Camera").setOnMenuItemClickListener {
    VCamSettingsDialog
        .newInstance(appInfo.packageName, appInfo.label)
        .show(childFragmentManager, VCamSettingsDialog.TAG)
    true
}
```

If the app uses a BottomSheetDialog or AlertDialog instead of PopupMenu, same idea —
just add the item and call the dialog.

---

## 2. Wire the camera hook into BlackBox's camera intercept

Search the BlackBox source for `openCamera` — it will be in one of:
- `core/.../BActivityManagerSender.kt`
- `core/.../BlackBoxCore.kt`
- `core/.../proxy/ProxyManifest.kt`

At the point where BB is about to hand the camera to the cloned app, add:

```kotlin
val hook = VCamCameraHook(context, callerPackageName)
if (hook.isActive()) {
    hook.start(virtualSurface)   // virtualSurface = the Surface BB already creates
    return   // skip real camera open
}
// else fall through to normal camera open
```

`callerPackageName` = the package name of the cloned app currently opening the camera.
`virtualSurface`    = whatever Surface BlackBox already constructs for the preview —
                      you're just redirecting the feed into it.

---

## 3. Add a camera icon badge on active apps (optional)

In your app list adapter's `onBindViewHolder`, after binding the app info:

```kotlin
val isVCamOn = VCamPrefs.isActive(itemView.context, appInfo.packageName)
badgeIcon.visibility = if (isVCamOn) View.VISIBLE else View.GONE
```

---

## 4. Permissions to add in AndroidManifest.xml (if not already present)

```xml
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.READ_MEDIA_VIDEO"/>
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_CAMERA"/>
```
