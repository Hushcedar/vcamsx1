package com.wangyiheng.vcamsx.components

import android.content.Context
import android.view.SurfaceHolder
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.wangyiheng.vcamsx.modules.home.controllers.HomeController

@Composable
fun VideoPlayerDialog(homeController: HomeController, context: Context, videoPath: String) {
    if (homeController.isVideoDisplay.value) {
        Dialog(onDismissRequest = {
            homeController.isVideoDisplay.value = false
            homeController.release()
        }) {
            AndroidView(
                factory = { ctx ->
                    android.view.SurfaceView(ctx).apply {
                        holder.addCallback(object : SurfaceHolder.Callback {
                            override fun surfaceCreated(h: SurfaceHolder) {
                                homeController.playVideo(h, videoPath)
                            }
                            override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, ht: Int) {}
                            override fun surfaceDestroyed(h: SurfaceHolder) {
                                homeController.release()
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
        }
    }
}
