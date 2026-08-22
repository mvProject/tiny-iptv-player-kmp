package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class DimensionText(
    val font10: TextUnit = 10.sp,
    val font12: TextUnit = 12.sp,
    val font14: TextUnit = 14.sp,
    val font16: TextUnit = 16.sp,
    val font18: TextUnit = 18.sp,
    val font20: TextUnit = 20.sp,
)

internal val LocalDimensionText = staticCompositionLocalOf { DimensionText() }

val MaterialTheme.dimensionText: DimensionText
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensionText.current