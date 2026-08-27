package com.axiom.voicepitch.hook

import com.axiom.voicepitch.engine.PsolaEngine
import com.axiom.voicepitch.prefs.PitchPrefs
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.nio.ByteBuffer

class VoicePitchModule : IXposedHookLoadPackage, IXposedHookZygoteInit {

    companion object {
        private const val TAG = "[AxiomPitch]"

        private val TARGETS = setOf(
            "com.jy.x.separation.manager",  // MochieCloner — covers ALL cloned apps inside
            "com.whatsapp",
            "com.whatsapp.w4b",
            "org.telegram.messenger",
            "org.telegram.messenger.beta",
            "com.discord",
            "org.thoughtcrime.securesms"
        )

        private var xprefs: XSharedPreferences? = null
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        xprefs = XSharedPreferences("com.axiom.voicepitch", PitchPrefs.PREFS_NAME)
        xprefs?.makeWorldReadable()
        XposedBridge.log("$TAG Zygote init OK — prefs path: ${xprefs?.file?.absolutePath}")
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val isMochie   = lpparam.packageName == "com.jy.x.separation.manager"
        val isTarget   = lpparam.packageName in TARGETS
        val isVirtual  = lpparam.processName.startsWith("com.jy.x.separation.manager")

        if (!isTarget && !isVirtual) return

        XposedBridge.log("$TAG ✅ LOADED pkg=${lpparam.packageName} proc=${lpparam.processName} mochie=$isMochie virtual=$isVirtual")
        installAudioHooks(lpparam)
    }

    private fun installAudioHooks(lpparam: XC_LoadPackage.LoadPackageParam) {
        val ar = XposedHelpers.findClass("android.media.AudioRecord", lpparam.classLoader)

        XposedHelpers.findAndHookMethod(ar, "read",
            ByteArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = semitones() ?: return
                    XposedBridge.log("$TAG 🔥 byte[] n=$n st=$st proc=${lpparam.processName}")
                    val buf = param.args[0] as ByteArray
                    val out = PsolaEngine.shiftBytes(buf, n, st, sr(param.thisObject))
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedHelpers.findAndHookMethod(ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = semitones() ?: return
                    XposedBridge.log("$TAG 🔥 short[] n=$n st=$st proc=${lpparam.processName}")
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
                    val st = semitones() ?: return
                    XposedBridge.log("$TAG 🔥 ByteBuffer n=$n st=$st proc=${lpparam.processName}")
                    val bb  = param.args[0] as ByteBuffer
                    val pos = bb.position()
                    val raw = ByteArray(n)
                    bb.position(pos - n); bb.get(raw)
                    val out = PsolaEngine.shiftBytes(raw, n, st, sr(param.thisObject))
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
                    val st = semitones() ?: return
                    val buf = param.args[0] as ByteArray
                    val out = PsolaEngine.shiftBytes(buf, n, st, sr(param.thisObject))
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedHelpers.findAndHookMethod(ar, "read",
            ShortArray::class.java, Int::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = semitones() ?: return
                    val buf = param.args[0] as ShortArray
                    val out = PsolaEngine.shiftShorts(buf, n, st)
                    System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                }
            })

        XposedBridge.log("$TAG ✅ All hooks armed for proc=${lpparam.processName}")
    }

    private fun semitones(): Float? {
        return try {
            val p = xprefs ?: return null
            p.reload()
            val enabled   = p.getBoolean(PitchPrefs.KEY_ENABLED, true)
            val semitones = p.getFloat(PitchPrefs.KEY_SEMITONES, 0f)
            if (!enabled) return null
            if (kotlin.math.abs(semitones) < 0.05f) null else semitones
        } catch (t: Throwable) {
            XposedBridge.log("$TAG ❌ xprefs failed: ${t.message}")
            null
        }
    }

    private fun sr(audioRecord: Any): Int =
        try { XposedHelpers.getIntField(audioRecord, "mSampleRate") }
        catch (_: Throwable) { 16000 }
}
