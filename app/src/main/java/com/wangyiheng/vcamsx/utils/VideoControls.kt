package com.wangyiheng.vcamsx.utils

import androidx.compose.runtime.mutableStateOf

object VideoControls {
    val isPaused       = mutableStateOf(false)
    val isImageEnabled = mutableStateOf(false)
    val rotation       = mutableStateOf(0)
    val isFlipped      = mutableStateOf(false)
    val scale          = mutableStateOf(1.0f)

    // Speed cycles: 0.5 → 1 → 1.5 → 2 → 3 → back to 0.5
    val speedSteps     = listOf(0.5f, 1.0f, 1.5f, 2.0f, 3.0f)
    val speedIndex     = mutableStateOf(1)   // default = 1× (index 1)
    val speed get()    = speedSteps[speedIndex.value]
}
