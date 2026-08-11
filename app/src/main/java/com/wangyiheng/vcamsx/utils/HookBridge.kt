package com.wangyiheng.vcamsx.utils

import android.graphics.SurfaceTexture
import android.view.Surface

object HookBridge {

    private fun hookClass(): Class<*>? = try {
        Class.forName(
            "com.wangyiheng.vcamsx.MainHook",
            false,
            HookBridge::class.java.classLoader
        )
    } catch (_: Throwable) { null }

    fun setField(field: String, value: Any?) {
        try { hookClass()?.getField(field)?.set(null, value) } catch (_: Throwable) {}
    }

    private fun getField(field: String): Any? = try {
        hookClass()?.getField(field)?.get(null)
    } catch (_: Throwable) { null }

    fun getVirtualSurface(): Surface? =
        getField("c2_virtual_surface") as? Surface

    fun getFakeSurfaceTexture(): SurfaceTexture? =
        getField("fake_SurfaceTexture") as? SurfaceTexture

    fun getPreviewSurface(): Surface? =
        getField("original_preview_Surface") as? Surface

    fun getC1PreviewSurfaceTexture(): SurfaceTexture? =
        getField("original_c1_preview_SurfaceTexture") as? SurfaceTexture

    fun getOriHolder(): android.view.SurfaceHolder? =
        getField("oriHolder") as? android.view.SurfaceHolder
}
