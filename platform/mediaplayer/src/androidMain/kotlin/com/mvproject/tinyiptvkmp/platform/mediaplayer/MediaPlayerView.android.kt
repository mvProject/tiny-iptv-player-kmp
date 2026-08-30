package com.mvproject.tinyiptvkmp.platform.mediaplayer

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.state.rememberCurrentMediaItemState
import androidx.media3.ui.compose.state.rememberErrorState
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_NO_VALUE

@OptIn(UnstableApi::class)
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
    val currentMediaItemState = rememberCurrentMediaItemState(player = playerState.player)
    val errorState = rememberErrorState(player = playerState.player)

    //LaunchedEffect(tvPlayerState.isRestartRequired) {
    //    if (tvPlayerState.isRestartRequired) {
    //        playerState.restartPlayer()
    //        onPlaybackAction(UiActions.Restart)
    //    }
    //}

    LaunchedEffect(state.volume) {
        playerState.setVolume(state.volume)
    }

    LaunchedEffect(
        state.channelKey,
        state.url,
        currentMediaItemState.mediaItem,
    ) {
        val currentUrl = currentMediaItemState.mediaItem
            ?.localConfiguration
            ?.uri
            ?.toString()

        if (state.channelKey > INT_NO_VALUE && currentUrl != state.url) {
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

    LaunchedEffect(errorState.error?.errorCode) {
        val error = errorState.error ?: return@LaunchedEffect
        onEvent(
            MediaPlayerEvent.PlaybackStateChanged(
                MediaPlaybackState.Idle(errorCode = error.errorCode)
            )
        )
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

    PlayerSurface(
        player = playerState.player,
        modifier = modifier.fillMaxSize(),
        surfaceType = SURFACE_TYPE_SURFACE_VIEW,
    )

}
