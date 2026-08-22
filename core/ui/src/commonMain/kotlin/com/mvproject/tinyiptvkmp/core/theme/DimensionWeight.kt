package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

class DimensionWeight(
    val weight1: Float = 1f,
    val weight2: Float = 2f,
    val weight4: Float = 4f,
    val weight5: Float = 5f,
    val weight6: Float = 6f,
    val weight7: Float = 7f,
    val weight8: Float = 8f,
    val weight9: Float = 9f,
    val weight10: Float = 10f,
)

internal val LocalDimensionWeight = staticCompositionLocalOf { DimensionWeight() }

val MaterialTheme.dimensionWeight: DimensionWeight
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensionWeight.current