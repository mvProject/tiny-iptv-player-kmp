package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

class DimensionFraction(
    val fractionNone: Float = 0f,
    val fraction10: Float = 0.1f,
    val fraction20: Float = 0.2f,
    val fraction30: Float = 0.3f,
    val fraction40: Float = 0.4f,
    val fraction50: Float = 0.5f,
    val fraction60: Float = 0.6f,
    val fraction70: Float = 0.7f,
    val fraction80: Float = 0.8f,
    val fraction90: Float = 0.9f,
    val fraction100: Float = 1f,
)

internal val LocalDimensionFraction = staticCompositionLocalOf { DimensionFraction() }

val MaterialTheme.dimensionFraction: DimensionFraction
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensionFraction.current