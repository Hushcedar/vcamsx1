package com.wangyiheng.vcamsx.utils

import java.io.File

/**
 * ImageBridge — cross-process shared state via files on disk.
 *
 * VCamSX (UID 11001) and the cloned app hook (UID 10957) are separate
 * processes. In-memory @Volatile fields are NOT shared between them.
 *
 * We use the same directory as copied_video.mp4 — VCamSX's external
 * files dir — which is world-readable on Android 9 and below, and
 * accessible to hooks injected by MochieCloner on Android 10+
 * because MochieCloner runs hooks inside the cloned app's process
 * which shares storage access with the VCamSX module.
 *
 * flagFile present = injection active
 * nv21File         = raw NV21 bytes 720x1280
 */
object ImageBridge {

    private const val NV21_SIZE = 720 * 1280 * 3 / 2  // 1,382,400 bytes

    private var baseDir: File? = null

    private val flagFile get() = File(baseDir, "img_active.flag")
    private val nv21File get() = File(baseDir, "img_nv21.raw")

    /** Call this from ImagePlayer.loadImage() with context.getExternalFilesDir(null) */
    fun init(dir: File) {
        baseDir = dir
    }

    // ── App side (VCamSX process) ─────────────────────────────────────────────

    fun setActive(active: Boolean) {
        try {
            if (active) flagFile.createNewFile()
            else flagFile.delete()
        } catch (_: Exception) {}
    }

    fun writeNV21(nv21: ByteArray) {
        try { nv21File.writeBytes(nv21) } catch (_: Exception) {}
    }

    fun clear() {
        try { flagFile.delete() } catch (_: Exception) {}
        try { nv21File.delete() } catch (_: Exception) {}
    }

    // ── Hook side (cloned app process) ───────────────────────────────────────

    fun isActive(): Boolean = try { flagFile.exists() } catch (_: Exception) { false }

    fun readNV21(): ByteArray? = try {
        val f = nv21File
        if (f.exists() && f.length() >= NV21_SIZE) f.readBytes() else null
    } catch (_: Exception) { null }
}
