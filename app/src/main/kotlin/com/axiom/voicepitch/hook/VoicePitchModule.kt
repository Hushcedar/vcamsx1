package com.axiom.voicepitch.hook

import android.content.Context
import android.content.SharedPreferences
import com.axiom.voicepitch.engine.PsolaEngine
import com.axiom.voicepitch.prefs.PitchPrefs
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.nio.ByteBuffer

/**
 * LSPosed entry point.
 * Hooks AudioRecord.read() in all three overloads across every supported app.
 * Audio is intercepted post-read and pitch-shifted via PsolaEngine before
 * the calling app sees the samples.
 */
class VoicePitchModule : IXposedHookLoadPackage {

    companion object {
        private val TARGETS = setOf(
            "com.whatsapp",
            "com.whatsapp.w4b",
            "org.telegram.messenger",
            "org.telegram.messenger.beta",
            "com.discord",
            "org.thoughtcrime.securesms"          // Signal
        )

        private const val TAG = "[AxiomPitch]"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName !in TARGETS) return
        XposedBridge.log("$TAG Attaching to ${lpparam.packageName}")
        hookAllAudioRecordVariants(lpparam)
    }

    // ────────────────────────────────────────────────────────────────────
    //  Hook installation
    // ────────────────────────────────────────────────────────────────────

    private fun hookAllAudioRecordVariants(lpparam: XC_LoadPackage.LoadPackageParam) {
        val ar = XposedHelpers.findClass("android.media.AudioRecord", lpparam.classLoader)

        // ── Variant 1: read(byte[], int, int) ───────────────────────────
        XposedHelpers.findAndHookMethod(
            ar, "read",
            ByteArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val bytesRead = param.result as? Int ?: return
                    if (bytesRead <= 0) return
                    val semitones = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ByteArray
                    val sr  = sampleRate(param.thisObject)
                    val out = PsolaEngine.shiftBytes(buf, bytesRead, semitones, sr)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, bytesRead))
                }
            }
        )

        // ── Variant 2: read(short[], int, int) ──────────────────────────
        XposedHelpers.findAndHookMethod(
            ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val samplesRead = param.result as? Int ?: return
                    if (samplesRead <= 0) return
                    val semitones = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ShortArray
                    val out = PsolaEngine.shiftShorts(buf, samplesRead, semitones)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, samplesRead))
                }
            }
        )

        // ── Variant 3: read(ByteBuffer, int) — WebRTC / modern codecs ───
        XposedHelpers.findAndHookMethod(
            ar, "read",
            ByteBuffer::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val bytesRead = param.result as? Int ?: return
                    if (bytesRead <= 0) return
                    val semitones = readSemitones(param.thisObject) ?: return
                    val bb  = param.args[0] as ByteBuffer
                    val sr  = sampleRate(param.thisObject)
                    val pos = bb.position()
                    val raw = ByteArray(bytesRead)
                    bb.position(pos - bytesRead)
                    bb.get(raw)
                    val out = PsolaEngine.shiftBytes(raw, bytesRead, semitones, sr)
                    bb.position(pos - bytesRead)
                    bb.put(out, 0, minOf(out.size, bytesRead))
                    bb.position(pos)
                }
            }
        )

        // ── Variant 4: read(byte[], int, int, int) — API 23+ read mode ──
        XposedHelpers.findAndHookMethod(
            ar, "read",
            ByteArray::class.java, Int::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val bytesRead = param.result as? Int ?: return
                    if (bytesRead <= 0) return
                    val semitones = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ByteArray
                    val sr  = sampleRate(param.thisObject)
                    val out = PsolaEngine.shiftBytes(buf, bytesRead, semitones, sr)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, bytesRead))
                }
            }
        )

        // ── Variant 5: read(short[], int, int, int) — API 23+ read mode ─
        XposedHelpers.findAndHookMethod(
            ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val samplesRead = param.result as? Int ?: return
                    if (samplesRead <= 0) return
                    val semitones = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ShortArray
                    val out = PsolaEngine.shiftShorts(buf, samplesRead, semitones)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, samplesRead))
                }
            }
        )

        XposedBridge.log("$TAG All AudioRecord hooks installed for ${lpparam.packageName}")
    }

    // ────────────────────────────────────────────────────────────────────
    //  Helpers
    // ────────────────────────────────────────────────────────────────────

    /**
     * Read semitone shift from world-readable prefs.
     * Returns null if module is disabled or shift is effectively zero.
     */
    private fun readSemitones(audioRecord: Any): Float? {
        return try {
            val ctx = XposedHelpers.callStaticMethod(
                XposedHelpers.findClass("android.app.ActivityThread", null),
                "currentApplication"
            ) as Context
            val prefs: SharedPreferences = ctx.createPackageContext(
                "com.axiom.voicepitch",
                Context.CONTEXT_IGNORE_SECURITY
            ).getSharedPreferences(PitchPrefs.PREFS_NAME, Context.MODE_PRIVATE)

            if (!PitchPrefs.isEnabled(prefs)) return null
            val semitones = PitchPrefs.getSemitones(prefs)
            if (kotlin.math.abs(semitones) < 0.05f) null else semitones
        } catch (t: Throwable) {
            XposedBridge.log("$TAG prefs read failed: ${t.message}")
            null
        }
    }

    private fun sampleRate(audioRecord: Any): Int =
        try { XposedHelpers.getIntField(audioRecord, "mSampleRate") }
        catch (_: Throwable) { 16000 }
}
