/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 15:44
 *
 */

package com.mvproject.tinyiptvkmp.features.player.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.adaptive.PlayerProgramsPlacement
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelProgramUiModel
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.indicators.VolumeIndicator
import com.mvproject.tinyiptvkmp.core.components.overlay.OnScreenDisplay
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.player.presentation.components.NoPlaybackView
import com.mvproject.tinyiptvkmp.features.player.presentation.components.PlayerContainer
import com.mvproject.tinyiptvkmp.features.player.presentation.components.PlayerOsdContent
import com.mvproject.tinyiptvkmp.features.player.presentation.components.PlayerToolbar
import com.mvproject.tinyiptvkmp.features.player.presentation.components.handlePlayerGestures
import com.mvproject.tinyiptvkmp.features.player.presentation.components.toChannelProgramUiModel
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.msg_no_internet_found
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.msg_no_playable_media_found
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.no_network
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.sad_face
import com.mvproject.tinyiptvkmp.platform.mediaplayer.MediaPlayerState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlayerScreen(
        state = state,
        onAction = viewModel::onIntent
    )
}

@Composable
private fun PlayerScreen(
    state: PlayerState,
    onAction: (PlayerAction) -> Unit
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()
    val currentProgramsUiModels =
        remember(state.currentChannel.programs) {
            state.currentChannel.programs.map { program -> program.toChannelProgramUiModel() }
        }
    val mediaPlayerState = remember(
        state.currentChannel.channelUrl,
        state.channelIndex,
        state.currentVolume,
        state.isPlaying,
    ) {
        MediaPlayerState(
            url = state.currentChannel.channelUrl,
            channelKey = state.channelIndex,
            volume = state.currentVolume,
            isPlaying = state.isPlaying,
        )
    }
    val playerContent =
        remember {
            movableContentOf<Modifier, PlayerContentState, (PlayerAction) -> Unit>(
                content = { modifier, contentState, action ->
                    PlayerContent(
                        modifier = modifier,
                        state = contentState,
                        onAction = action,
                    )
                },
            )
        }
    val programsContent =
        remember {
            movableContentOf<Modifier, List<ChannelProgramUiModel>> { modifier, programs ->
                ChannelPrograms(
                    modifier = modifier,
                    programs = programs,
                )
            }
        }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim)
                .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.TopCenter,
    ) {
        PlayerAdaptiveContentLayout(
            programsPlacement = adaptiveLayoutState.playerProgramsPlacement,
            showPrograms = !state.isFullscreen,
            modifier = Modifier.fillMaxSize(),
            playerContent = { modifier ->
                playerContent(
                    modifier,
                    state.toPlayerContentState(mediaPlayerState),
                    onAction,
                )
            },
            programsContent = { modifier -> programsContent(modifier, currentProgramsUiModels) },
        )

        OnScreenDisplay(
            isVisible = state.osdType != null,
            onViewTap = { onAction(PlayerAction.CloseOsd) },
        ) {
            PlayerOsdContent(
                osdType = state.osdType,
                currentChannel = state.currentChannel,
                currentPrograms = currentProgramsUiModels,
                groupChannels = state.groupChannels,
                currentChannelIndex = state.channelIndex,
                channelGroup = state.channelGroup,
                overlayHeightFraction = adaptiveLayoutState.overlayHeightFraction,
                overlayWidthFraction = adaptiveLayoutState.overlayWidthFraction,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun PlayerAdaptiveContentLayout(
    programsPlacement: PlayerProgramsPlacement,
    showPrograms: Boolean,
    modifier: Modifier = Modifier,
    playerContent: @Composable (Modifier) -> Unit,
    programsContent: @Composable (Modifier) -> Unit,
) {
    if (programsPlacement == PlayerProgramsPlacement.Side) {
        Row(modifier = modifier) {
            playerContent(Modifier.weight(MaterialTheme.dimensionWeight.weight2))

            if (showPrograms) {
                programsContent(
                    Modifier
                        .weight(MaterialTheme.dimensionWeight.weight1)
                        .background(color = MaterialTheme.colorScheme.primary),
                )
            }
        }
    } else {
        Column(modifier = modifier) {
            playerContent(Modifier.weight(MaterialTheme.dimensionWeight.weight2))

            if (showPrograms) {
                programsContent(
                    Modifier
                        .weight(MaterialTheme.dimensionWeight.weight1)
                        .background(color = MaterialTheme.colorScheme.primary),
                )
            }
        }
    }
}

private data class PlayerContentState(
    val videoSize: VideoSize,
    val mediaPlayerState: MediaPlayerState,
    val isOnline: Boolean,
    val isMediaPlayable: Boolean,
    val isVolumeUiVisible: Boolean,
    val currentVolume: Float,
    val isBuffering: Boolean,
    val isControlUiVisible: Boolean,
    val currentChannel: TvChannelWithPrograms,
    val isPlaying: Boolean,
    val isFullscreen: Boolean,
)

private fun PlayerState.toPlayerContentState(mediaPlayerState: MediaPlayerState) =
    PlayerContentState(
        videoSize = videoSize,
        mediaPlayerState = mediaPlayerState,
        isOnline = isOnline,
        isMediaPlayable = isMediaPlayable,
        isVolumeUiVisible = isVolumeUiVisible,
        currentVolume = currentVolume,
        isBuffering = isBuffering,
        isControlUiVisible = isControlUiVisible,
        currentChannel = currentChannel,
        isPlaying = isPlaying,
        isFullscreen = isFullscreen,
    )

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    state: PlayerContentState,
    onAction: (PlayerAction) -> Unit
) {
    PlayerContainer(
        modifier = modifier.handlePlayerGestures(onAction = onAction),
        videoSize = state.videoSize,
        mediaPlayerState = state.mediaPlayerState,
        onAction = onAction,
    ) {

        NoPlaybackView(
            isVisible = !state.isOnline,
            text = stringResource(Res.string.msg_no_internet_found),
            logo = painterResource(Res.drawable.no_network),
        )

        NoPlaybackView(
            isVisible = !state.isMediaPlayable,
            text = stringResource(Res.string.msg_no_playable_media_found),
            logo = painterResource(Res.drawable.sad_face),
        )

        VolumeIndicator(
            modifier = Modifier.fillMaxSize(),
            isVisible = state.isVolumeUiVisible,
            value = state.currentVolume,
        )

        LoadingIndicator(isVisible = state.isBuffering)

        PlayerToolbar(
            modifier = Modifier.fillMaxSize(),
            isVisible = state.isControlUiVisible,
            videoSize = state.videoSize,
            currentChannel = state.currentChannel,
            isPlaying = state.isPlaying,
            isFullScreen = state.isFullscreen,
            onAction = onAction,
        )
    }
}
