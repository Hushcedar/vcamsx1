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

class VoicePitchModule : IXposedHookLoadPackage {

    companion object {
        private val TARGETS = setOf(
            "com.whatsapp",
            "com.whatsapp.w4b",
            "org.telegram.messenger",
            "org.telegram.messenger.beta",
            "com.discord",
            "org.thoughtcrime.securesms"
        )
        private const val TAG = "[AxiomPitch]"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName !in TARGETS) return
        XposedBridge.log("$TAG Attaching to ${lpparam.packageName}")
        hookAllAudioRecordVariants(lpparam)
    }

    private fun hookAllAudioRecordVariants(lpparam: XC_LoadPackage.LoadPackageParam) {
        val ar = XposedHelpers.findClass("android.media.AudioRecord", lpparam.classLoader)

        XposedHelpers.findAndHookMethod(ar, "read",
            ByteArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ByteArray
                    val out = PsolaEngine.shiftBytes(buf, n, st, sampleRate(param.thisObject))
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedHelpers.findAndHookMethod(ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ShortArray
                    val out = PsolaEngine.shiftShorts(buf, n, st)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedHelpers.findAndHookMethod(ar, "read",
            ByteBuffer::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    val bb = param.args[0] as ByteBuffer
                    val sr = sampleRate(param.thisObject)
                    val pos = bb.position()
                    val raw = ByteArray(n)
                    bb.position(pos - n); bb.get(raw)
                    val out = PsolaEngine.shiftBytes(raw, n, st, sr)
                    bb.position(pos - n)
                    bb.put(out, 0, minOf(out.size, n))
                    bb.position(pos)
                }
            })

        XposedHelpers.findAndHookMethod(ar, "read",
            ByteArray::class.java, Int::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ByteArray
                    val out = PsolaEngine.shiftBytes(buf, n, st, sampleRate(param.thisObject))
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedHelpers.findAndHookMethod(ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    val buf = param.args[0] as ShortArray
                    val out = PsolaEngine.shiftShorts(buf, n, st)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedBridge.log("$TAG All hooks installed for ${lpparam.packageName}")
    }

    // Hook reads prefs via createPackageContext — MODE_PRIVATE, no world-readable needed
    private fun readSemitones(audioRecord: Any): Float? {
        return try {
            val targetCtx = XposedHelpers.callStaticMethod(
                XposedHelpers.findClass("android.app.ActivityThread", null),
                "currentApplication"
            ) as Context

            val ourCtx = targetCtx.createPackageContext(
                "com.axiom.voicepitch",
                Context.CONTEXT_IGNORE_SECURITY
            )
            // createPackageContext gives us access to our own private prefs
            val prefs: SharedPreferences = ourCtx.getSharedPreferences(
                PitchPrefs.PREFS_NAME, Context.MODE_PRIVATE
            )
            if (!PitchPrefs.isEnabled(prefs)) return null
            val st = PitchPrefs.getSemitones(prefs)
            if (kotlin.math.abs(st) < 0.05f) null else st
        } catch (t: Throwable) {
            XposedBridge.log("$TAG prefs read failed: ${t.message}")
            null
        }
    }

    private fun sampleRate(audioRecord: Any): Int =
        try { XposedHelpers.getIntField(audioRecord, "mSampleRate") }
        catch (_: Throwable) { 16000 }
}
