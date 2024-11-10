package com.mvproject.tinyiptvkmp.features.player.components

import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.features.player.action.UiActions
import com.mvproject.tinyiptvkmp.features.player.state.TvPlayerState
import com.mvproject.tinyiptvkmp.ui.rememberPlayerState

@Composable
actual fun PlayerView(
    modifier: Modifier,
    tvPlayerState: TvPlayerState,
    onUiAction: (UiActions) -> Unit
) {
    // todo network Available check
    // val connection by networkConnectionState()
    // val activity = LocalContext.current as Activity
    // val systemUIController = rememberSystemUIController()

    // when (connection) {
    //     ConnectionState.Available -> KLog.i("testing connectivity is available")
    //     ConnectionState.Unavailable -> KLog.e("testing connectivity is unavailable")
    // }

    val playerState = rememberPlayerState(
        onPlaybackAction = onUiAction,
    )

    //LaunchedEffect(tvPlayerState.isRestartRequired) {
    //    if (tvPlayerState.isRestartRequired) {
    //        playerState.restartPlayer()
    //        onPlaybackAction(UiActions.Restart)
    //    }
    //}

    LaunchedEffect(tvPlayerState.currentVolume) {
        playerState.setVolume(tvPlayerState.currentVolume)
    }

    LaunchedEffect(tvPlayerState.channelIndex) {
        if (tvPlayerState.channelIndex > AppConstants.INT_NO_VALUE) {
            playerState.setPlayerChannel(
                channelUrl = tvPlayerState.currentChannel.channelUrl,
            )
            //playerState.restartPlayer()
        }
    }

    LaunchedEffect(tvPlayerState.isPlaying) {
        if (playerState.player.isPlaying != tvPlayerState.isPlaying) {
            playerState.setPlayingState(tvPlayerState.isPlaying)
        }
    }

    // LaunchedEffect(videoViewState.isFullscreen) {
    //     systemUIController.isSystemBarsVisible = !videoViewState.isFullscreen
//
    //     if (videoViewState.isFullscreen) {
    //         activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    //     } else {
    //         activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    //     }
    // }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            SurfaceView(context)
                .apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    keepScreenOn = true
                }.also { view ->
                    playerState.player.setVideoSurfaceView(view)
                }
        },
    )

    LifecycleStartEffect(
        key1 = playerState,
    ) {
        onStopOrDispose {
            playerState.player.stop()
            //   systemUIController.isSystemBarsVisible = true
            //     activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}