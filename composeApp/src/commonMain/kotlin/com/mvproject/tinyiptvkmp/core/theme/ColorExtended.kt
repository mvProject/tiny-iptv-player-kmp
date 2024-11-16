package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorSchemeExtended(
    val activeBorder: Color = Color.Unspecified,
    val inactiveBorder: Color = Color.Unspecified,
    val activeProgramTitle: Color = Color.Unspecified,
    val emptyProgramTitle: Color = Color.Unspecified,
    val programTitle: Color = Color.Unspecified,
    val volumeIndicator: Color = Color.Unspecified,
    val timeColor: Color = Color.Unspecified,
    val divider: Color = Color.Unspecified,
    val progress: Color = Color.Unspecified,
    val toolbarTitle: Color = Color.Unspecified,
    val activeInput: Color = Color.Unspecified,

    )

val lightColorSchemeExtended =
    ColorSchemeExtended(
        activeBorder = videoAppLightOnSurface,
        inactiveBorder = whiteDuck,
        activeProgramTitle = videoAppLightOnSurface,
        emptyProgramTitle = offWhite,
        programTitle = whiteDuck,
        volumeIndicator = videoAppLightOnSurface,
        timeColor = offWhite,
        divider = whiteDuck,
        progress = videoAppLightOnSurface,
        toolbarTitle = whiteDuck,
        activeInput = whiteDuck
    )

val darkColorSchemeExtended =
    ColorSchemeExtended(
        activeBorder = videoAppLightOnSurface,
        inactiveBorder = whiteDuck,
        activeProgramTitle = videoAppLightOnSurface,
        divider = videoAppLightOnSurface,
        progress = videoAppLightOnSurface
    )

val LocalColorSchemeExtended = staticCompositionLocalOf { ColorSchemeExtended() }

val MaterialTheme.colorSchemeExtended: ColorSchemeExtended
    @Composable
    @ReadOnlyComposable
    get() = LocalColorSchemeExtended.current