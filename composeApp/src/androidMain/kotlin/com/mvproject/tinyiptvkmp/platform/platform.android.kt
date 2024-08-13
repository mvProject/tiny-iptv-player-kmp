/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:13
 *
 */

package com.mvproject.tinyiptvkmp.platform

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.PlaybackException
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.mvproject.tinyiptvkmp.ui.PlayerView
import com.mvproject.tinyiptvkmp.ui.findActivity
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.KLog
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

actual fun createPlatformHttpClient(): HttpClient = HttpClient(Android)

@Composable
actual fun PlayerViewContainer(
    modifier: Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
    controls: @Composable () -> Unit,
) {
    PlayerView(
        modifier = modifier,
        videoViewState = videoViewState,
        onPlaybackAction = onPlaybackAction,
        onPlaybackStateAction = onPlaybackStateAction,
        controls = controls,
    )
}

@Composable
actual fun AdditionalPlayerControls(
    modifier: Modifier,
    action: () -> Unit,
    onPlaybackAction: (PlaybackActions) -> Unit,
) {
    // no need yet
}

actual fun isMediaPlayable(errorCode: Int?): Boolean {
    val isMediaPlayable =
        when (errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> false
            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> false
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> false
            else -> true
        }
    KLog.e("testing errorCode:$errorCode, isMediaPlayable:$isMediaPlayable")
    return isMediaPlayable
}

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
