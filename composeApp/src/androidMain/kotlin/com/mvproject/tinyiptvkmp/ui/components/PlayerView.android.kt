package com.mvproject.tinyiptvkmp.ui.components

import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import com.mvproject.tinyiptvkmp.ui.rememberPlayerState
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.utils.AppConstants

@Composable
actual fun PlayerView(
    modifier: Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
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
        onPlaybackStateAction = onPlaybackStateAction,
    )

    LaunchedEffect(videoViewState.isRestartRequired) {
        if (videoViewState.isRestartRequired) {
            playerState.restartPlayer()
            onPlaybackAction(PlaybackActions.OnRestarted)
        }
    }

    LaunchedEffect(videoViewState.currentVolume) {
        playerState.setVolume(videoViewState.currentVolume)
    }

    LaunchedEffect(videoViewState.mediaPosition) {
        if (videoViewState.mediaPosition > AppConstants.INT_NO_VALUE) {
            playerState.setPlayerChannel(
                channelName = videoViewState.currentChannel.channelName,
                channelUrl = videoViewState.currentChannel.channelUrl,
            )
        }
    }

    LaunchedEffect(videoViewState.isPlaying) {
        if (playerState.player.isPlaying != videoViewState.isPlaying) {
            playerState.setPlayingState(videoViewState.isPlaying)
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