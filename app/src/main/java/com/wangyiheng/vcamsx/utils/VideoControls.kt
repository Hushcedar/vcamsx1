package com.wangyiheng.vcamsx.utils

import androidx.compose.runtime.mutableStateOf

/**
 * Shared mutable state for all video transform controls.
 *
 * rotation default = 180 — fixes the "upside-down on first open" bug.
 * Camera sensor feed is expected head-up; our injected video arrives
 * inverted relative to that convention, so 180° pre-correction is the
 * right baseline. User can still adjust via the floating controls.
 */
object VideoControls {
    val isPaused        = mutableStateOf(false)
    val isImageEnabled  = mutableStateOf(false)
    val rotation  = mutableStateOf(0)   // ← was 0, caused upside-down default
    val isFlipped = mutableStateOf(false)
    val scale     = mutableStateOf(1.0f)
}
