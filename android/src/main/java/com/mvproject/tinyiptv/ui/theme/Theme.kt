/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

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
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val videoAppColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }

        darkTheme -> videoAppDarkColorScheme
        else -> videoAppLightColorScheme
    }

    CompositionLocalProvider(
        LocalDimens provides Dimens(),
    ) {
        MaterialTheme(
            colorScheme = videoAppColorScheme,
            typography = videoAppTypography,
            shapes = shapes,
            content = content
        )
    }
}