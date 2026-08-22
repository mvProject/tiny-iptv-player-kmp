/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 16:34
 *
 */

package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class DimensionSize(
    val size1: Dp = 1.dp,
    val size2: Dp = 2.dp,
    val size4: Dp = 4.dp,
    val size8: Dp = 8.dp,
    val size12: Dp = 12.dp,
    val size16: Dp = 16.dp,
    val size22: Dp = 22.dp,
    val size24: Dp = 24.dp,
    val size32: Dp = 32.dp,
    val size42: Dp = 42.dp,
    val size48: Dp = 48.dp,
    val size78: Dp = 78.dp,
    val size96: Dp = 96.dp,
    val size140: Dp = 140.dp,
    val size180: Dp = 180.dp,
    val size200: Dp = 200.dp,
    val size310: Dp = 310.dp,
)

internal val LocalDimensionSize = staticCompositionLocalOf { DimensionSize() }

val MaterialTheme.dimensionSize: DimensionSize
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensionSize.current
