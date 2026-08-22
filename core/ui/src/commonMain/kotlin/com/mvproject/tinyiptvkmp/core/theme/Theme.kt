/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val darkColorScheme = darkColorScheme(
    primary = blackCarbor,
    onPrimary = porce,
    background = blackCarbor,
    onBackground = porce,
    surface = blackOil,
    onSurface = whiteDuck
)

private val lightColorScheme = lightColorScheme(
    primary = blackCarbor,
    onPrimary = porce,
    background = blackCarbor,
    onBackground = porce,
    surface = blackOil,
    onSurface = whiteDuck
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme)
        darkColorScheme
    else
        lightColorScheme

    val colorSchemeExtended = if (darkTheme)
        darkColorSchemeExtended
    else
        lightColorSchemeExtended

    CompositionLocalProvider(
        LocalDimensionSize provides DimensionSize(),
        LocalDimensionFraction provides DimensionFraction(),
        LocalDimensionOpacity provides DimensionOpacity(),
        LocalDimensionSpacing provides DimensionSpacing(),
        LocalDimensionWeight provides DimensionWeight(),
        LocalDimensionText provides DimensionText(),
        LocalColorSchemeExtended provides colorSchemeExtended,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = getTypography(),
            shapes = shapes,
            content = content
        )
    }
}