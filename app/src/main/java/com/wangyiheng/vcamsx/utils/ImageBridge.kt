package com.wangyiheng.vcamsx.utils

/**
 * ImageBridge — shared buffer between ImagePlayer (app process)
 * and MainHook/VideoPlayer (hook process).
 *
 * No Xposed imports — safe to reference from both sides.
 * MainHook reads data_buffer directly from here.
 * VideoPlayer.startWriterLoop reads from here too.
 */
object ImageBridge {
    @Volatile @JvmField var data_buffer: ByteArray = byteArrayOf()
    @Volatile @JvmField var isImageActive: Boolean = false
}
