package com.mvproject.tinyiptvkmp.platform.mediaplayer

import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_NO_VALUE

@Composable
actual fun MediaPlayerView(
    modifier: Modifier,
    state: MediaPlayerState,
    onEvent: (MediaPlayerEvent) -> Unit
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
        onEvent = onEvent,
    )

    //LaunchedEffect(tvPlayerState.isRestartRequired) {
    //    if (tvPlayerState.isRestartRequired) {
    //        playerState.restartPlayer()
    //        onPlaybackAction(UiActions.Restart)
    //    }
    //}

    LaunchedEffect(state.volume) {
        playerState.setVolume(state.volume)
    }

    LaunchedEffect(state.channelKey) {
        if (state.channelKey > INT_NO_VALUE) {
            playerState.setPlayerChannel(
                channelUrl = state.url,
            )
            //playerState.restartPlayer()
        }
    }

    LaunchedEffect(state.isPlaying) {
        if (playerState.player.isPlaying != state.isPlaying) {
            playerState.setPlayingState(state.isPlaying)
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
