/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val videoAppDarkColorScheme = darkColorScheme(
    primary = videoAppDarkPrimary,
    onPrimary = videoAppDarkOnPrimary,
    background = videoAppDarkBackground,
    surface = videoAppDarkSurface,
    tertiary = videoAppDarkTertiary,
    onTertiary = videoAppDarkOnTertiary,
    onSurface = videoAppDarkOnSurface,
    onSurfaceVariant = videoAppDarkOnSurfaceVariant,
    outline = videoAppDarkOutline
)

private val videoAppLightColorScheme = lightColorScheme(
    primary = videoAppLightPrimary,
    onPrimary = videoAppLightOnPrimary,
    background = videoAppLightBackground,
    surface = videoAppLightSurface,
    tertiary = videoAppLightTertiary,
    onTertiary = videoAppLightOnTertiary,
    onSurface = videoAppLightOnSurface,
    onSurfaceVariant = videoAppLightOnSurfaceVariant,
    outline = videoAppLightOutline,
)

@Composable
fun VideoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val videoAppColorScheme = if (darkTheme)
        videoAppDarkColorScheme
    else videoAppLightColorScheme


    CompositionLocalProvider(
        LocalDimens provides Dimens(),
    ) {
        MaterialTheme(
            colorScheme = videoAppColorScheme,
            typography = getTypography(),
            shapes = shapes,
            content = content
        )
    }
}