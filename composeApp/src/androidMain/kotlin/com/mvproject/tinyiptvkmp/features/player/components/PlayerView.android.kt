package com.mvproject.tinyiptvkmp.features.player.components

import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import com.mvproject.tinyiptvkmp.core.common.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState
import com.mvproject.tinyiptvkmp.ui.rememberPlayerState

@Composable
actual fun PlayerView(
    modifier: Modifier,
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit
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
        onPlaybackAction = onAction,
    )

    //LaunchedEffect(tvPlayerState.isRestartRequired) {
    //    if (tvPlayerState.isRestartRequired) {
    //        playerState.restartPlayer()
    //        onPlaybackAction(UiActions.Restart)
    //    }
    //}

    LaunchedEffect(uiState.currentVolume) {
        playerState.setVolume(uiState.currentVolume)
    }

    LaunchedEffect(uiState.channelIndex) {
        if (uiState.channelIndex > INT_NO_VALUE) {
            playerState.setPlayerChannel(
                channelUrl = uiState.currentChannel.channelUrl,
            )
            //playerState.restartPlayer()
        }
    }

    LaunchedEffect(uiState.isPlaying) {
        if (playerState.player.isPlaying != uiState.isPlaying) {
            playerState.setPlayingState(uiState.isPlaying)
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