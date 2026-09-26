/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptvkmp.core.theme

import androidx.compose.ui.graphics.Color

val videoAppLightPrimary = Color(0xFF0E101A)
val videoAppLightBackground = Color(0xFF0E101A)
val videoAppLightSurface = Color(0xFF171b2b)
val videoAppLightOnPrimary = Color(0xFFFFFFFF)
val videoAppLightTertiary = Color(0xFFFFB4A1)
val videoAppLightOnTertiary = Color(0xFF5E1704)
val videoAppLightOnSurface = Color(0xFFE4D093)
val videoAppLightOnSurfaceVariant = Color(0xFFFFBF00)
val videoAppLightOutline = Color(0xFF636363)
val videoAppLightInverseSurface = Color(0xFF171f2b)


val videoAppDarkPrimary = Color(0xFF0E101A)
val videoAppDarkBackground = Color(0xFF0E101A)
val videoAppDarkSurface = Color(0xFF171b2b)
val videoAppDarkOnPrimary = Color(0xFFFFFFFF)
val videoAppDarkTertiary = Color(0xFFFFB4A1)
val videoAppDarkOnTertiary = Color(0xFF5E1704)
val videoAppDarkOnSurface = Color(0xFFE4D093)
val videoAppDarkOnSurfaceVariant = Color(0xFFFFBF00)
val videoAppDarkOutline = Color(0xFF636363)
val videoAppDarkInverseSurface = Color(0xFF171f2b)


val perl = Color(0xFFF6F6F6)
val cream = Color(0xFFF3F2ED)
val wing = Color(0xFFEAEFF3)
val pudr = Color(0xFFF7F6F1)
val offWhite = Color(0xFFEAEAEA)
val whiteDuck = Color(0xFFE5DFD3)
val porce = Color(0xFFEFF1F0)
val zalizo = Color(0xFF322D31)
val woodVugilya = Color(0xFF232023)
val smokeBlack = Color(0xFF100D08)
val galka = Color(0xFF333333)
val shadow = Color(0xFF373737)
val blackwood = Color(0xFF242F35)
val midnightGray = Color(0xFF101420)
val vugilnoGray = Color(0xFF0D1717)
val blackOil = Color(0xFF0C0C0C)
val hueBlack = Color(0xFF020D19)
val obsidian = Color(0xFF0B1215)
val hueBlue = Color(0xFF011222)
val blackCarbor = Color(0xFF0C0A00)

/**
 * Tones backing the M3 [androidx.compose.material3.ColorScheme] roles that the app does not
 * already source from the palette above.
 *
 * Grouped in an object rather than declared as loose top-level vals because most of these names
 * collide with the `ColorScheme` constructor parameter they feed, and `role = role` reads as
 * self-assignment even though it resolves to this property.
 */
internal object AppTones {
    // Surface tonal ramp, anchored on the warm-black baseline. Ordered darkest -> lightest so the
    // scale reads the way M3 does.
    val surfaceContainerLowest = Color(0xFF090702)
    val surfaceContainerLow = Color(0xFF131109)
    val surfaceContainer = Color(0xFF1A1812)
    val surfaceContainerHigh = Color(0xFF221F18)
    val surfaceContainerHighest = Color(0xFF2B271E)
    val surfaceBright = Color(0xFF2B271E)
    val surfaceDim = Color(0xFF0C0A00)
    val surfaceVariant = Color(0xFF302C22)
    val onSurfaceVariant = Color(0xFFC9C3B6)

    // Outline ramp, replacing the ad-hoc "divider = onSurface" styling.
    val outline = Color(0xFF8A857A)
    val outlineVariant = Color(0xFF3A362C)

    // Primary family. `primary`/`onPrimary` stay on the black-cream pair the app already ships;
    // the container tones carry the gold accent instead.
    val primaryContainer = Color(0xFF2A2415)
    val onPrimaryContainer = Color(0xFFFFDF9E)
    val inversePrimary = Color(0xFFFFBF00)

    // Secondary/tertiary families, carrying the app's existing gold/amber accents.
    val secondary = Color(0xFFC7B77E)
    val onSecondary = Color(0xFF2A2415)
    val secondaryContainer = Color(0xFF3A3319)
    val onSecondaryContainer = Color(0xFFE4D093)
    val tertiary = Color(0xFFFFBF00)
    val onTertiary = Color(0xFF3A2E00)
    val tertiaryContainer = Color(0xFF4A3C00)
    val onTertiaryContainer = Color(0xFFFFDF9E)

    // Error family, giving destructive actions a real role instead of a hardcoded color.
    val error = Color(0xFFFFB4AB)
    val onError = Color(0xFF690005)
    val errorContainer = Color(0xFF93000A)
    val onErrorContainer = Color(0xFFFFDAD6)

    // Inverse roles, used by snackbars and tooltips.
    val inverseSurface = Color(0xFF31302C)
    val inverseOnSurface = Color(0xFFF4EFE4)

    // Fixed roles, kept in step with the families above so no default (purple) baseline tone can
    // leak in from a component that uses them.
    val primaryFixed = Color(0xFF221F18)
    val primaryFixedDim = Color(0xFF0C0A00)
    val onPrimaryFixed = Color(0xFFE5DFD3)
    val onPrimaryFixedVariant = Color(0xFFC9C3B6)
    val secondaryFixed = Color(0xFF3A3319)
    val secondaryFixedDim = Color(0xFF1A1812)
    val onSecondaryFixed = Color(0xFFE4D093)
    val onSecondaryFixedVariant = Color(0xFFD4C58C)
    val tertiaryFixed = Color(0xFF4A3C00)
    val tertiaryFixedDim = Color(0xFF1A1812)
    val onTertiaryFixed = Color(0xFFFFDF9E)
    val onTertiaryFixedVariant = Color(0xFFF0D17A)
}
