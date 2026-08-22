package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class DimensionSpacing(
    val spacing1: Dp = 1.dp,
    val spacing2: Dp = 2.dp,
    val spacing3: Dp = 3.dp,
    val spacing4: Dp = 4.dp,
    val spacing5: Dp = 5.dp,
    val spacing6: Dp = 6.dp,
    val spacing8: Dp = 8.dp,
    val spacing10: Dp = 10.dp,
    val spacing12: Dp = 12.dp,
    val spacing14: Dp = 14.dp,
)

internal val LocalDimensionSpacing = staticCompositionLocalOf { DimensionSpacing() }

val MaterialTheme.dimensionSpacing: DimensionSpacing
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensionSpacing.current