package com.vcam.app.engine

object VideoControls {
    class StateVal<T>(var value: T)

    val isPaused  = StateVal(false)
    val rotation  = StateVal(0)
    val isFlipped = StateVal(false)
    val scale     = StateVal(1.0f)
    val offsetX   = StateVal(0f)
    val offsetY   = StateVal(0f)

    fun resetTransform() {
        scale.value   = 1f
        offsetX.value = 0f
        offsetY.value = 0f
    }
}
