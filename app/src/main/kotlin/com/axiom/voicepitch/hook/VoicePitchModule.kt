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
            "org.thoughtcrime.securesms",
            "com.jy.x.separation.manager"   // MochieCloner
        )
        private const val TAG = "[AxiomPitch]"
        private var hookFireCount = 0
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName !in TARGETS) return
        XposedBridge.log("$TAG ✅ LOADED into ${lpparam.packageName}")
        hookAllAudioRecordVariants(lpparam)
    }

    private fun hookAllAudioRecordVariants(lpparam: XC_LoadPackage.LoadPackageParam) {
        val ar = XposedHelpers.findClass("android.media.AudioRecord", lpparam.classLoader)

        // Variant 1: read(byte[], int, int)
        XposedHelpers.findAndHookMethod(ar, "read",
            ByteArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    hookFireCount++
                    XposedBridge.log("$TAG 🔥 HOOK FIRED (byte[]) n=$n st=$st fires=$hookFireCount")
                    val buf = param.args[0] as ByteArray
                    val sr  = sampleRate(param.thisObject)
                    val out = PsolaEngine.shiftBytes(buf, n, st, sr)
                    XposedBridge.log("$TAG   shiftBytes in=$n out=${out.size} sr=$sr")
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        // Variant 2: read(short[], int, int)
        XposedHelpers.findAndHookMethod(ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    hookFireCount++
                    XposedBridge.log("$TAG 🔥 HOOK FIRED (short[]) n=$n st=$st fires=$hookFireCount")
                    val buf = param.args[0] as ShortArray
                    val out = PsolaEngine.shiftShorts(buf, n, st)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        // Variant 3: read(ByteBuffer, int)
        XposedHelpers.findAndHookMethod(ar, "read",
            ByteBuffer::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    hookFireCount++
                    XposedBridge.log("$TAG 🔥 HOOK FIRED (ByteBuffer) n=$n st=$st fires=$hookFireCount")
                    val bb  = param.args[0] as ByteBuffer
                    val sr  = sampleRate(param.thisObject)
                    val pos = bb.position()
                    val raw = ByteArray(n)
                    bb.position(pos - n); bb.get(raw)
                    val out = PsolaEngine.shiftBytes(raw, n, st, sr)
                    bb.position(pos - n)
                    bb.put(out, 0, minOf(out.size, n))
                    bb.position(pos)
                }
            })

        // Variant 4: read(byte[], int, int, int) API 23+
        XposedHelpers.findAndHookMethod(ar, "read",
            ByteArray::class.java, Int::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    hookFireCount++
                    XposedBridge.log("$TAG 🔥 HOOK FIRED (byte[],readMode) n=$n st=$st fires=$hookFireCount")
                    val buf = param.args[0] as ByteArray
                    val out = PsolaEngine.shiftBytes(buf, n, st, sampleRate(param.thisObject))
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        // Variant 5: read(short[], int, int, int) API 23+
        XposedHelpers.findAndHookMethod(ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = readSemitones(param.thisObject) ?: return
                    hookFireCount++
                    XposedBridge.log("$TAG 🔥 HOOK FIRED (short[],readMode) n=$n st=$st fires=$hookFireCount")
                    val buf = param.args[0] as ShortArray
                    val out = PsolaEngine.shiftShorts(buf, n, st)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedBridge.log("$TAG ✅ All 5 AudioRecord hooks armed for ${lpparam.packageName}")
    }

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
            val prefs: SharedPreferences = ourCtx.getSharedPreferences(
                PitchPrefs.PREFS_NAME, Context.MODE_PRIVATE
            )
            val enabled = PitchPrefs.isEnabled(prefs)
            val st      = PitchPrefs.getSemitones(prefs)
            XposedBridge.log("$TAG   prefs OK enabled=$enabled semitones=$st")
            if (!enabled) return null
            if (kotlin.math.abs(st) < 0.05f) null else st
        } catch (t: Throwable) {
            XposedBridge.log("$TAG ❌ prefs read FAILED: ${t.message}")
            null
        }
    }

    private fun sampleRate(audioRecord: Any): Int =
        try { XposedHelpers.getIntField(audioRecord, "mSampleRate") }
        catch (_: Throwable) { 16000 }
}
