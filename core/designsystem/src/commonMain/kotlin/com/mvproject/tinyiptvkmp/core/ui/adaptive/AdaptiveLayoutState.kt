package com.mvproject.tinyiptvkmp.core.ui.adaptive

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

@Immutable
data class AdaptiveLayoutState(
    val windowSizeClass: WindowSizeClass,
    val widthType: AdaptiveWidthType,
    val heightType: AdaptiveHeightType,
    val contentHorizontalPadding: Dp,
    val maxContentWidth: Dp,
    val channelGridMinCellWidth: Dp,
    val overlayWidthFraction: Float,
    val overlayHeightFraction: Float,
    val playerProgramsPlacement: PlayerProgramsPlacement,
) {
    val isCompactWidth: Boolean = widthType == AdaptiveWidthType.Compact
    val isCompactHeight: Boolean = heightType == AdaptiveHeightType.Compact
}

enum class AdaptiveWidthType {
    Compact,
    Medium,
    Expanded,
    Large,
    ExtraLarge,
}

enum class AdaptiveHeightType {
    Compact,
    Medium,
    Expanded,
}

enum class PlayerProgramsPlacement {
    Side,
    Bottom,
}

@Composable
fun rememberAdaptiveLayoutState(): AdaptiveLayoutState {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val windowSizeClass = windowAdaptiveInfo.windowSizeClass

    return remember(windowSizeClass) {
        val widthType = windowSizeClass.adaptiveWidthType()
        val heightType = windowSizeClass.adaptiveHeightType()
        val isCompactWidth = widthType == AdaptiveWidthType.Compact
        val isCompactHeight = heightType == AdaptiveHeightType.Compact

        AdaptiveLayoutState(
            windowSizeClass = windowSizeClass,
            widthType = widthType,
            heightType = heightType,
            contentHorizontalPadding = when (widthType) {
                AdaptiveWidthType.Compact -> 8.dp
                AdaptiveWidthType.Medium -> 16.dp
                else -> 24.dp
            },
            maxContentWidth = when (widthType) {
                AdaptiveWidthType.Compact -> 600.dp
                AdaptiveWidthType.Medium -> 720.dp
                else -> 960.dp
            },
            channelGridMinCellWidth = when (widthType) {
                AdaptiveWidthType.Compact -> 160.dp
                AdaptiveWidthType.Medium -> 180.dp
                AdaptiveWidthType.Expanded -> 200.dp
                else -> 220.dp
            },
            overlayWidthFraction = when {
                isCompactWidth -> 0.92f
                widthType == AdaptiveWidthType.Medium -> 0.72f
                else -> 0.56f
            },
            overlayHeightFraction = if (isCompactHeight) 0.92f else 0.82f,
            // Wide desktop/tablet windows should keep EPG beside the player even when height is not compact.
            playerProgramsPlacement = when {
                widthType >= AdaptiveWidthType.Expanded -> PlayerProgramsPlacement.Side
                widthType == AdaptiveWidthType.Medium && isCompactHeight -> PlayerProgramsPlacement.Side
                else -> PlayerProgramsPlacement.Bottom
            },
        )
    }
}

fun Modifier.adaptiveContentWidth(adaptiveLayoutState: AdaptiveLayoutState): Modifier =
    widthIn(max = adaptiveLayoutState.maxContentWidth).fillMaxWidth()

private fun WindowSizeClass.adaptiveWidthType(): AdaptiveWidthType =
    when {
        isWidthAtLeastBreakpoint(WIDTH_DP_EXTRA_LARGE_LOWER_BOUND) -> {
            AdaptiveWidthType.ExtraLarge
        }

        isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND) -> {
            AdaptiveWidthType.Large
        }

        isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
            AdaptiveWidthType.Expanded
        }

        isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            AdaptiveWidthType.Medium
        }

        else -> AdaptiveWidthType.Compact
    }

private const val WIDTH_DP_LARGE_LOWER_BOUND = 1200
private const val WIDTH_DP_EXTRA_LARGE_LOWER_BOUND = 1600

private fun WindowSizeClass.adaptiveHeightType(): AdaptiveHeightType =
    when {
        isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND) -> {
            AdaptiveHeightType.Expanded
        }

        isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND) -> {
            AdaptiveHeightType.Medium
        }

        else -> AdaptiveHeightType.Compact
    }
