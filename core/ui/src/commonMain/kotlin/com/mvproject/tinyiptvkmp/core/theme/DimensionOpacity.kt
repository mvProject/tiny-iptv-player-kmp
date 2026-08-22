package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

class DimensionOpacity(
    val opacityNone: Float = 0f,
    val opacity10: Float = 0.1f,
    val opacity20: Float = 0.2f,
    val opacity30: Float = 0.3f,
    val opacity50: Float = 0.5f,
    val opacity60: Float = 0.6f,
    val opacity70: Float = 0.7f,
    val opacity80: Float = 0.8f,
    val opacity90: Float = 0.9f,
    val opacityDefault: Float = 1f
)

internal val LocalDimensionOpacity = staticCompositionLocalOf { DimensionOpacity() }

val MaterialTheme.dimensionOpacity: DimensionOpacity
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensionOpacity.current