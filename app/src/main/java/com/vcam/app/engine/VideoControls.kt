package com.vcam.app.engine

object VideoControls {
    @Volatile var isPaused        = false
    @Volatile var rotation        = 0
    @Volatile var isFlipped       = false
    @Volatile var scale           = 1.0f
    @Volatile var offsetX         = 0f
    @Volatile var offsetY         = 0f
}
