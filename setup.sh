#!/bin/bash
# VCam — one shot setup and build
# Run: bash setup.sh
set -e

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; NC='\033[0m'
ok()   { echo -e "${GREEN}[OK]${NC} $1"; }
info() { echo -e "${YELLOW}[..] $1${NC}"; }
fail() { echo -e "${RED}[!!] $1${NC}"; exit 1; }

cd "$(dirname "$0")"
ROOT="$(pwd)"

echo ""
echo "================================"
echo "  VCam Build"
echo "================================"
echo ""

# Tools check
command -v curl  >/dev/null 2>&1 || fail "curl missing — pkg install curl"
command -v java  >/dev/null 2>&1 || fail "Java missing — pkg install openjdk-17"
ok "Tools OK"

# Android SDK
[ -z "$ANDROID_HOME" ] && [ -d "$HOME/android-sdk" ] && export ANDROID_HOME="$HOME/android-sdk"
[ -z "$ANDROID_HOME" ] && fail "ANDROID_HOME not set. Set up Android SDK first."
export ANDROID_SDK_ROOT="$ANDROID_HOME"
ok "SDK: $ANDROID_HOME"

# Keystore
info "Creating keystore..."
mkdir -p "$ROOT/keystore"
if [ ! -f "$ROOT/keystore/debug.keystore" ]; then
  keytool -genkey -v \
    -keystore "$ROOT/keystore/debug.keystore" \
    -storepass android -alias androiddebugkey -keypass android \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -dname "CN=Android Debug,O=Android,C=US" >/dev/null 2>&1
fi
ok "Keystore ready"

# Build
info "Building APK..."
chmod +x gradlew
./gradlew assembleDebug --no-daemon 2>&1

APK=$(find . -name "app-debug.apk" | head -1)
if [ -n "$APK" ]; then
  echo ""
  echo "================================"
  echo -e "${GREEN}  BUILD SUCCESSFUL${NC}"
  echo "================================"
  echo "APK : $APK"
  echo "Size: $(du -sh "$APK" | cut -f1)"
  echo ""
  echo "Install:"
  echo "  adb install -r $APK"
  echo "  -- or --"
  echo "  cp \"$APK\" /sdcard/VCam.apk"
else
  echo ""
  echo "================================"
  echo -e "${RED}  BUILD FAILED${NC}"
  echo "================================"
  echo "Check output above for errors."
fi
