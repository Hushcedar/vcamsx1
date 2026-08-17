#!/bin/bash
cd /storage/emulated/0/Podcasts/VCameraRevived2
DL=~/storage/downloads

echo "Deploying all files..."

# AppsViewModel — correct method names matching AppsFragment exactly
cp $DL/AppsViewModel.kt \
    app/src/main/java/virtual/camera/app/view/apps/AppsViewModel.kt

# Factories — correct constructors
cp $DL/AppsFactory.kt \
    app/src/main/java/virtual/camera/app/view/apps/AppsFactory.kt

cp $DL/ListFactory.kt \
    app/src/main/java/virtual/camera/app/view/list/ListFactory.kt

cp $DL/GmsFactory.kt \
    app/src/main/java/virtual/camera/app/view/gms/GmsFactory.kt

# InjectionUtil — wired correctly
cp $DL/InjectionUtil.kt \
    app/src/main/java/virtual/camera/app/util/InjectionUtil.kt

# SettingActivity — has companion object start(context)
cp $DL/SettingActivity_fix.kt \
    app/src/main/java/virtual/camera/app/view/setting/SettingActivity.kt

# AppManager — all fields
cp $DL/AppManager.kt \
    app/src/main/java/virtual/camera/app/app/AppManager.kt

# App.kt — calls AppManager.init
cp $DL/App.kt \
    app/src/main/java/virtual/camera/app/app/App.kt

# VirtualCameraService
mkdir -p app/src/main/java/virtual/camera/app/service
cp $DL/VirtualCameraService.kt \
    app/src/main/java/virtual/camera/app/service/VirtualCameraService.kt

# XML resources
cp $DL/settings_prefs.xml  app/src/main/res/xml/settings_prefs.xml
cp $DL/arrays_fix.xml      app/src/main/res/values/arrays.xml
cp $DL/activity_setting.xml app/src/main/res/layout/activity_setting.xml

echo "Patching manifest..."
python3 << 'PYEOF'
path = "app/src/main/AndroidManifest.xml"
with open(path) as f:
    content = f.read()

changed = False

if "VirtualCameraService" not in content:
    content = content.replace("</application>",
        """        <service
            android:name=".service.VirtualCameraService"
            android:exported="false"
            android:foregroundServiceType="camera"/>
    </application>""")
    changed = True
    print("Added VirtualCameraService")

if "SettingActivity" not in content:
    content = content.replace("</application>",
        """        <activity
            android:name=".view.setting.SettingActivity"
            android:exported="false"/>
    </application>""")
    changed = True
    print("Added SettingActivity")

if changed:
    with open(path, "w") as f:
        f.write(content)
else:
    print("Manifest already up to date")
PYEOF

echo "Committing..."
git add -A
git commit -m "Fix: AppsViewModel correct method names, factories fixed, SettingActivity.start(), AppManager fields"
git push
echo "DONE boss man — push complete"
