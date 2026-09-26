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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/**
 * The app ships a single, dark, warm-black palette, so light and dark resolve to the same scheme.
 * That is deliberate rather than accidental: a separate light palette is a follow-up, and until it
 * lands, branching here would only hide the fact that `darkTheme` has no effect.
 *
 * Add a real `lightColorScheme` beside this one when that follow-up starts.
 *
 * Every role is set explicitly so no component can fall back to the M3 baseline purple.
 */
private val appColorScheme = darkColorScheme(
    primary = blackCarbor,
    onPrimary = porce,
    primaryContainer = AppTones.primaryContainer,
    onPrimaryContainer = AppTones.onPrimaryContainer,
    inversePrimary = AppTones.inversePrimary,
    secondary = AppTones.secondary,
    onSecondary = AppTones.onSecondary,
    secondaryContainer = AppTones.secondaryContainer,
    onSecondaryContainer = AppTones.onSecondaryContainer,
    tertiary = AppTones.tertiary,
    onTertiary = AppTones.onTertiary,
    tertiaryContainer = AppTones.tertiaryContainer,
    onTertiaryContainer = AppTones.onTertiaryContainer,
    background = blackCarbor,
    onBackground = porce,
    surface = blackOil,
    onSurface = whiteDuck,
    surfaceVariant = AppTones.surfaceVariant,
    onSurfaceVariant = AppTones.onSurfaceVariant,
    surfaceTint = blackOil,
    inverseSurface = AppTones.inverseSurface,
    inverseOnSurface = AppTones.inverseOnSurface,
    error = AppTones.error,
    onError = AppTones.onError,
    errorContainer = AppTones.errorContainer,
    onErrorContainer = AppTones.onErrorContainer,
    outline = AppTones.outline,
    outlineVariant = AppTones.outlineVariant,
    scrim = Color.Black,
    surfaceBright = AppTones.surfaceBright,
    surfaceContainer = AppTones.surfaceContainer,
    surfaceContainerHigh = AppTones.surfaceContainerHigh,
    surfaceContainerHighest = AppTones.surfaceContainerHighest,
    surfaceContainerLow = AppTones.surfaceContainerLow,
    surfaceContainerLowest = AppTones.surfaceContainerLowest,
    surfaceDim = AppTones.surfaceDim,
    primaryFixed = AppTones.primaryFixed,
    primaryFixedDim = AppTones.primaryFixedDim,
    onPrimaryFixed = AppTones.onPrimaryFixed,
    onPrimaryFixedVariant = AppTones.onPrimaryFixedVariant,
    secondaryFixed = AppTones.secondaryFixed,
    secondaryFixedDim = AppTones.secondaryFixedDim,
    onSecondaryFixed = AppTones.onSecondaryFixed,
    onSecondaryFixedVariant = AppTones.onSecondaryFixedVariant,
    tertiaryFixed = AppTones.tertiaryFixed,
    tertiaryFixedDim = AppTones.tertiaryFixedDim,
    onTertiaryFixed = AppTones.onTertiaryFixed,
    onTertiaryFixedVariant = AppTones.onTertiaryFixedVariant,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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
            colorScheme = appColorScheme,
            typography = getTypography(),
            shapes = shapes,
            content = content
        )
    }
}
