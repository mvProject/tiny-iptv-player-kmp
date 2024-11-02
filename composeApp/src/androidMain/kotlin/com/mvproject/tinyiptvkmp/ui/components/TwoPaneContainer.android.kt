package com.mvproject.tinyiptvkmp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.mvproject.tinyiptvkmp.ui.findActivity
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun TwoPaneContainer(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val windowSizeClass = calculateWindowSizeClass()
    val displayFeatures = calculateDisplayFeatures(activity)

    TwoPane(
        first = {
            first()
        },
        second = {
            second()
        },
        strategy =
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> VerticalTwoPaneStrategy(MaterialTheme.dimens.fraction50)
            WindowWidthSizeClass.Medium -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction60)
            else -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction70)
        },
        displayFeatures = displayFeatures,
    )
}