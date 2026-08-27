package com.vcam.app.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.util.Log
import android.view.Surface

object ImagePlayer {
    private const val TAG = "VCam-ImagePlayer"

    private var bitmap: Bitmap? = null
    private var surface: Surface? = null
    @Volatile private var running = false
    private var thread: Thread? = null

    fun start(ctx: Context, imageUri: String, outputSurface: Surface) {
        stop()
        surface = outputSurface
        try {
            bitmap = if (imageUri.startsWith("content://") || imageUri.startsWith("file://")) {
                ctx.contentResolver.openInputStream(Uri.parse(imageUri))?.use {
                    BitmapFactory.decodeStream(it)
                }
            } else {
                BitmapFactory.decodeFile(imageUri)
            }
        } catch (e: Exception) {
            Log.e(TAG, "load image: ${e.message}"); return
        }
        if (bitmap == null) { Log.e(TAG, "bitmap null"); return }
        running = true
        thread = Thread({
            val rect = android.graphics.Rect(0, 0, 1280, 720)
            while (running) {
                try {
                    val canvas: Canvas = outputSurface.lockCanvas(null)
                    bitmap?.let { canvas.drawBitmap(it, null, rect, null) }
                    outputSurface.unlockCanvasAndPost(canvas)
                    Thread.sleep(33)
                } catch (e: Exception) {
                    Log.e(TAG, "draw: ${e.message}"); break
                }
            }
        }, "ImagePlayer").also { it.start() }
    }

    fun stop() {
        running = false
        thread?.join(1000); thread = null
        bitmap?.recycle(); bitmap = null
        surface = null
    }

    fun isRunning() = running
}
