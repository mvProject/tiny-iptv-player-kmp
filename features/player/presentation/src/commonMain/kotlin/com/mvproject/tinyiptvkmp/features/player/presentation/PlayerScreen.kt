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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelFavoriteSelector
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelProgramUiModel
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.indicators.VolumeIndicator
import com.mvproject.tinyiptvkmp.core.components.overlay.OnScreenDisplay
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerState.PlayerOSD
import com.mvproject.tinyiptvkmp.features.player.presentation.components.NoPlaybackView
import com.mvproject.tinyiptvkmp.features.player.presentation.components.PlayerChannels
import com.mvproject.tinyiptvkmp.features.player.presentation.components.PlayerContainer
import com.mvproject.tinyiptvkmp.features.player.presentation.components.PlayerToolbar
import com.mvproject.tinyiptvkmp.features.player.presentation.components.ProgramInfo
import com.mvproject.tinyiptvkmp.features.player.presentation.components.favoriteOptionsUiModels
import com.mvproject.tinyiptvkmp.features.player.presentation.components.handleHorizontalGestures
import com.mvproject.tinyiptvkmp.features.player.presentation.components.handleTapGestures
import com.mvproject.tinyiptvkmp.features.player.presentation.components.handleVerticalGestures
import com.mvproject.tinyiptvkmp.features.player.presentation.components.toChannelProgramUiModel
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.msg_no_internet_found
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.msg_no_playable_media_found
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.no_network
import com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources.sad_face
import com.mvproject.tinyiptvkmp.features.player.presentation.utils.programDescription
import com.mvproject.tinyiptvkmp.features.player.presentation.utils.programTitle
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
    val playerContent =
        remember {
            movableContentOf<Modifier, PlayerState, (PlayerAction) -> Unit>(
                content = { modifier, uiState, action ->
                    PlayerContent(
                        modifier = modifier,
                        uiState = uiState,
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
            playerContent = { modifier -> playerContent(modifier, state, onAction) },
            programsContent = { modifier -> programsContent(modifier, currentProgramsUiModels) },
        )

        OnScreenDisplay(
            isVisible = state.osdType != null,
            onViewTap = { onAction(PlayerAction.CloseOsd) }
        ) {
            state.osdType?.let { osdType ->
                when (osdType) {
                    PlayerOSD.ChannelPrograms -> {
                        ChannelPrograms(
                            modifier =
                                Modifier
                                    .fillMaxHeight(adaptiveLayoutState.overlayHeightFraction)
                                    .fillMaxWidth(adaptiveLayoutState.overlayWidthFraction)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape =
                                            RoundedCornerShape(
                                                bottomStart = MaterialTheme.dimensionSize.size8,
                                                bottomEnd = MaterialTheme.dimensionSize.size8,
                                            ),
                                    ),
                            title = state.currentChannel.channelName,
                            programs = currentProgramsUiModels,
                        )
                    }

                    PlayerOSD.GroupChannels -> {
                        PlayerChannels(
                            channels = state.groupChannels,
                            current = state.channelIndex,
                            group = state.channelGroup,
                            onChannelSelect = { chn -> onAction(PlayerAction.SelectChannel(chn)) }
                        )
                    }

                    PlayerOSD.ProgramInfo -> {
                        ProgramInfo(
                            channelName = state.currentChannel.channelName,
                            programName = state.currentChannel.programTitle,
                            description = state.currentChannel.programDescription,
                        )
                    }

                    PlayerOSD.ChannelFavorites -> {
                        ChannelFavoriteSelector(
                            options = favoriteOptionsUiModels(state.currentChannel.favoriteType),
                            onSelectFavorite = { option ->
                                onAction(PlayerAction.UpdateFavorite(FavoriteType.valueOf(option.id)))
                            }
                        )
                    }
                }
            }
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

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    uiState: PlayerState,
    onAction: (PlayerAction) -> Unit
) {
    PlayerContainer(
        modifier = modifier
            .handleHorizontalGestures(onAction = onAction)
            .handleVerticalGestures(onAction = onAction)
            .handleTapGestures(onAction = onAction),
        uiState = uiState,
        onAction = onAction,
    ) {

        NoPlaybackView(
            isVisible = !uiState.isOnline,
            text = stringResource(Res.string.msg_no_internet_found),
            logo = painterResource(Res.drawable.no_network),
        )

        NoPlaybackView(
            isVisible = !uiState.isMediaPlayable,
            text = stringResource(Res.string.msg_no_playable_media_found),
            logo = painterResource(Res.drawable.sad_face),
        )

        VolumeIndicator(
            modifier = Modifier.fillMaxSize(),
            isVisible = uiState.isVolumeUiVisible,
            value = uiState.currentVolume,
        )

        LoadingIndicator(isVisible = uiState.isBuffering)

        PlayerToolbar(
            modifier = Modifier.fillMaxSize(),
            isVisible = uiState.isControlUiVisible,
            videoSize = uiState.videoSize,
            currentChannel = uiState.currentChannel,
            isPlaying = uiState.isPlaying,
            isFullScreen = uiState.isFullscreen,
            onAction = onAction
        )
    }
}
