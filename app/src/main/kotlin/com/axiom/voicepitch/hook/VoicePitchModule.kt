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
        private const val TAG        = "[AxiomPitch]"
        private const val MOCHIE_PKG = "com.jy.x.separation.manager"

        private val DIRECT_TARGETS = setOf(
            "com.whatsapp",
            "com.whatsapp.w4b",
            "org.telegram.messenger",
            "org.telegram.messenger.beta",
            "com.discord",
            "org.thoughtcrime.securesms"
        )

        private var xprefs: XSharedPreferences? = null
        private var hooksInstalled = false
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        xprefs = XSharedPreferences("com.axiom.voicepitch", PitchPrefs.PREFS_NAME)
        xprefs?.makeWorldReadable()
        XposedBridge.log("$TAG Zygote init — xprefs: ${xprefs?.file?.absolutePath}")
        hookActivityThread()
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val pkg  = lpparam.packageName
        val proc = lpparam.processName

        when {
            pkg in DIRECT_TARGETS -> {
                XposedBridge.log("$TAG ✅ Direct target: $pkg")
                installAudioHooks(lpparam.classLoader)
            }
            pkg == MOCHIE_PKG -> {
                XposedBridge.log("$TAG ✅ MochieCloner main process")
                installAudioHooks(lpparam.classLoader)
            }
            proc.startsWith(MOCHIE_PKG) -> {
                XposedBridge.log("$TAG ✅ MochieCloner child via proc: $proc pkg: $pkg")
                installAudioHooks(lpparam.classLoader)
            }
        }
    }

    private fun hookActivityThread() {
        try {
            val activityThreadClass = XposedHelpers.findClass(
                "android.app.ActivityThread", null
            )

            XposedHelpers.findAndHookMethod(
                activityThreadClass,
                "handleBindApplication",
                "android.app.ActivityThread\$AppBindData",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        try {
                            val appBindData  = param.args[0]
                            val appInfo      = XposedHelpers.getObjectField(appBindData, "appInfo")
                                ?: return
                            val packageName  = XposedHelpers.getObjectField(appInfo, "packageName")
                                as? String ?: return
                            val processName  = XposedHelpers.getObjectField(appBindData, "processName")
                                as? String ?: return

                            XposedBridge.log("$TAG handleBindApplication pkg=$packageName proc=$processName")

                            val isMochieChild = processName.startsWith(MOCHIE_PKG) ||
                                                packageName.startsWith(MOCHIE_PKG)

                            if (isMochieChild || packageName in DIRECT_TARGETS) {
                                XposedBridge.log("$TAG 🎯 Injecting into: pkg=$packageName proc=$processName")
                                val cl = Thread.currentThread().contextClassLoader
                                    ?: VoicePitchModule::class.java.classLoader
                                    ?: return
                                installAudioHooks(cl)
                            }
                        } catch (t: Throwable) {
                            XposedBridge.log("$TAG handleBindApplication error: ${t.message}")
                        }
                    }
                }
            )
            XposedBridge.log("$TAG ActivityThread.handleBindApplication hooked ✅")
        } catch (t: Throwable) {
            XposedBridge.log("$TAG Failed to hook ActivityThread: ${t.message}")
        }
    }

    private fun installAudioHooks(classLoader: ClassLoader?) {
        if (hooksInstalled) {
            XposedBridge.log("$TAG hooks already installed in this process, skipping")
            return
        }
        hooksInstalled = true

        try {
            val ar = XposedHelpers.findClass("android.media.AudioRecord", classLoader)

            val hook = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val n  = param.result as? Int ?: return
                    if (n <= 0) return
                    val st = semitones() ?: return
                    XposedBridge.log("$TAG 🔥 AudioRecord fired n=$n st=$st")
                    when (val buf = param.args[0]) {
                        is ByteArray -> {
                            val out = PsolaEngine.shiftBytes(buf, n, st, sr(param.thisObject))
                            System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                        }
                        is ShortArray -> {
                            val out = PsolaEngine.shiftShorts(buf, n, st)
                            System.arraycopy(out, 0, buf, 0, minOf(out.size, n))
                        }
                        is ByteBuffer -> {
                            val pos = buf.position()
                            val raw = ByteArray(n)
                            buf.position(pos - n); buf.get(raw)
                            val out = PsolaEngine.shiftBytes(raw, n, st, sr(param.thisObject))
                            buf.position(pos - n)
                            buf.put(out, 0, minOf(out.size, n))
                            buf.position(pos)
                        }
                    }
                }
            }

            listOf(
                arrayOf(ByteArray::class.java,  Int::class.java, Int::class.java),
                arrayOf(ShortArray::class.java, Int::class.java, Int::class.java),
                arrayOf(ByteBuffer::class.java, Int::class.java),
                arrayOf(ByteArray::class.java,  Int::class.java, Int::class.java, Int::class.java),
                arrayOf(ShortArray::class.java, Int::class.java, Int::class.java, Int::class.java),
            ).forEach { sig ->
                try {
                    XposedHelpers.findAndHookMethod(ar, "read", *sig, hook)
                } catch (_: Throwable) {}
            }

            // MediaRecorder
            try {
                val mr = XposedHelpers.findClass("android.media.MediaRecorder", classLoader)
                var outputPath: String? = null

                XposedHelpers.findAndHookMethod(mr, "setOutputFile",
                    String::class.java, object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            outputPath = param.args[0] as? String
                            XposedBridge.log("$TAG MediaRecorder output: $outputPath")
                        }
                    })

                XposedHelpers.findAndHookMethod(mr, "stop",
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            val path = outputPath ?: return
                            val st   = semitones() ?: return
                            XposedBridge.log("$TAG 🔥 MediaRecorder.stop — shifting $path st=$st")
                            try { PsolaEngine.shiftAudioFile(path, st) }
                            catch (t: Throwable) {
                                XposedBridge.log("$TAG shiftAudioFile failed: ${t.message}")
                            }
                        }
                    })
            } catch (t: Throwable) {
                XposedBridge.log("$TAG MediaRecorder hook failed: ${t.message}")
            }

            XposedBridge.log("$TAG ✅ All audio hooks installed")

        } catch (t: Throwable) {
            XposedBridge.log("$TAG ❌ installAudioHooks failed: ${t.message}")
            hooksInstalled = false
        }
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
            XposedBridge.log("$TAG xprefs error: ${t.message}")
            null
        }
    }

    private fun sr(rec: Any): Int =
        try { XposedHelpers.getIntField(rec, "mSampleRate") }
        catch (_: Throwable) { 48000 }
}
