package com.voicechangerx

import android.content.Context
import android.media.AudioRecord
import com.voicechangerx.audio.AudioProcessor
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {

    companion object {
        const val TAG = "VoiceChangerX"
        val TARGET_APPS = setOf(
            "com.whatsapp",
            "com.whatsapp.w4b",
            "org.telegram.messenger",
            "org.telegram.messenger.web",
            "com.discord",
            "org.thoughtcrime.securesms",  // Signal
            "com.viber.voip",
            "com.skype.raider",
            "com.microsoft.teams",
            "us.zoom.videomeetings",
        )
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.voicechangerx") {
            // Hook own process to allow UI to read prefs from hook context
            return
        }
        if (lpparam.packageName !in TARGET_APPS) return

        XposedBridge.log("$TAG hooking: ${lpparam.packageName}")

        try {
            hookAudioRecord(lpparam)
        } catch (e: Exception) {
            XposedBridge.log("$TAG hook error: ${e.message}")
        }
    }

    private fun hookAudioRecord(lpparam: XC_LoadPackage.LoadPackageParam) {
        val cl = lpparam.classLoader

        // ── AudioRecord.read(byte[], int, int) ────────────────────────────────
        XposedHelpers.findAndHookMethod(
            "android.media.AudioRecord", cl,
            "read", ByteArray::class.java, Int::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val bytesRead = param.result as? Int ?: return
                    if (bytesRead <= 0) return
                    val buffer  = param.args[0] as? ByteArray ?: return
                    val offset  = param.args[1] as? Int ?: 0
                    val sampleRate = (param.thisObject as? AudioRecord)?.sampleRate ?: 16000

                    val ctx = getContext(param.thisObject) ?: return
                    AudioProcessor.init(ctx)

                    val chunk  = buffer.copyOfRange(offset, offset + bytesRead)
                    val processed = AudioProcessor.process(chunk, sampleRate)
                    System.arraycopy(processed, 0, buffer, offset, minOf(processed.size, bytesRead))
                }
            }
        )

        // ── AudioRecord.read(byte[], int, int, int) — with readMode ───────────
        try {
            XposedHelpers.findAndHookMethod(
                "android.media.AudioRecord", cl,
                "read", ByteArray::class.java, Int::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val bytesRead = param.result as? Int ?: return
                        if (bytesRead <= 0) return
                        val buffer = param.args[0] as? ByteArray ?: return
                        val offset = param.args[1] as? Int ?: 0
                        val sampleRate = (param.thisObject as? AudioRecord)?.sampleRate ?: 16000

                        val ctx = getContext(param.thisObject) ?: return
                        AudioProcessor.init(ctx)

                        val chunk = buffer.copyOfRange(offset, offset + bytesRead)
                        val processed = AudioProcessor.process(chunk, sampleRate)
                        System.arraycopy(processed, 0, buffer, offset, minOf(processed.size, bytesRead))
                    }
                }
            )
        } catch (_: Exception) {}

        // ── AudioRecord.read(short[], int, int) ───────────────────────────────
        try {
            XposedHelpers.findAndHookMethod(
                "android.media.AudioRecord", cl,
                "read", ShortArray::class.java, Int::class.java, Int::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val samplesRead = param.result as? Int ?: return
                        if (samplesRead <= 0) return
                        val buffer = param.args[0] as? ShortArray ?: return
                        val offset = param.args[1] as? Int ?: 0
                        val sampleRate = (param.thisObject as? AudioRecord)?.sampleRate ?: 16000

                        val ctx = getContext(param.thisObject) ?: return
                        AudioProcessor.init(ctx)

                        // Convert short→byte, process, convert back
                        val bytes = ByteArray(samplesRead * 2)
                        for (i in 0 until samplesRead) {
                            val s = buffer[offset + i]
                            bytes[i * 2]     = (s.toInt() and 0xFF).toByte()
                            bytes[i * 2 + 1] = (s.toInt() shr 8).toByte()
                        }
                        val processed = AudioProcessor.process(bytes, sampleRate)
                        for (i in 0 until samplesRead) {
                            buffer[offset + i] = ((processed[i * 2 + 1].toInt() shl 8) or
                                    (processed[i * 2].toInt() and 0xFF)).toShort()
                        }
                    }
                }
            )
        } catch (_: Exception) {}

        XposedBridge.log("$TAG AudioRecord hooks installed for ${lpparam.packageName}")
    }

    private fun getContext(audioRecord: Any?): android.content.Context? {
        return try {
            android.app.ActivityThread.currentApplication()
        } catch (_: Exception) { null }
    }
}
