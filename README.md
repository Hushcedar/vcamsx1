# Axiom Voice Pitch — LSPosed Module

Real-time voice pitch shifter for WhatsApp, Telegram, Discord, and Signal.  
Hooks `AudioRecord` at the framework level. Pure-Kotlin PSOLA phase-vocoder — no native libs.

---

## Targets

| App | Package |
|-----|---------|
| WhatsApp | `com.whatsapp` |
| WhatsApp Business | `com.whatsapp.w4b` |
| Telegram | `org.telegram.messenger` |
| Telegram Beta | `org.telegram.messenger.beta` |
| Discord | `com.discord` |
| Signal | `org.thoughtcrime.securesms` |

---

## Requirements

- Rooted Android device (Magisk)
- LSPosed framework installed
- Android 9+ (API 28+)

---

## Build

```bash
git clone https://github.com/yourname/VoicePitchModule.git
cd VoicePitchModule
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

---

## Install

1. Install the APK on your device
2. Open **LSPosed Manager**
3. Enable **Axiom Voice Pitch** module
4. Scope it to your target app (WhatsApp, Discord, etc.)
5. Force-stop the target app
6. Reopen the target app — hooks are active

---

## Usage

Open **Axiom Voice Pitch** from your launcher.

| Control | Description |
|---------|-------------|
| Enable toggle | Turns pitch shift on/off globally |
| Slider | –12 to +12 semitones continuous |
| Robot preset | –12 st (deep robotic) |
| Deep preset | –6 st |
| Normal preset | 0 st (bypass) |
| High preset | +6 st |
| Chipmunk preset | +12 st |
| Reset | Returns slider to 0 |

> **Note:** Force-stop the target app after changing settings.

---

## Architecture

```
hook/
  VoicePitchModule.kt   ← LSPosed entry point, hooks all AudioRecord.read() variants
engine/
  PsolaEngine.kt        ← PSOLA phase vocoder + Cooley-Tukey FFT (pure Kotlin)
prefs/
  PitchPrefs.kt         ← World-readable SharedPrefs bridge between UI and hook
ui/
  SettingsActivity.kt   ← Launcher UI with slider and presets
```

### How it works

1. LSPosed loads `VoicePitchModule` into the target app's process
2. All 5 `AudioRecord.read()` overloads are hooked with `afterHookedMethod`
3. After the system fills the audio buffer with mic data, we intercept it
4. `PsolaEngine` runs a windowed PSOLA transform using a Hann-windowed FFT phase vocoder
5. Shifted audio replaces the original buffer before the app reads it
6. The app (WhatsApp, Discord, etc.) receives already-shifted audio — completely transparent

---

## License

MIT
