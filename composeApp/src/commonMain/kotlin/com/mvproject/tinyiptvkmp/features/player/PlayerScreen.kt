/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 15:44
 *
 */

package com.mvproject.tinyiptvkmp.features.player

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.base.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.components.adaptive.PlayerProgramsPlacement
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.indicators.VolumeIndicator
import com.mvproject.tinyiptvkmp.core.components.overlay.OnScreenDisplay
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.core.ui.epg.ChannelPrograms
import com.mvproject.tinyiptvkmp.features.channels.components.ChannelFavoriteSelector
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState.PlayerOSD
import com.mvproject.tinyiptvkmp.features.player.components.NoPlaybackView
import com.mvproject.tinyiptvkmp.features.player.components.PlayerChannels
import com.mvproject.tinyiptvkmp.features.player.components.PlayerContainer
import com.mvproject.tinyiptvkmp.features.player.components.PlayerToolbar
import com.mvproject.tinyiptvkmp.features.player.components.ProgramInfo
import com.mvproject.tinyiptvkmp.features.player.components.handleHorizontalGestures
import com.mvproject.tinyiptvkmp.features.player.components.handleTapGestures
import com.mvproject.tinyiptvkmp.features.player.components.handleVerticalGestures
import com.mvproject.tinyiptvkmp.features.player.utils.programDescription
import com.mvproject.tinyiptvkmp.features.player.utils.programTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_internet_found
import tinyiptvkmp.composeapp.generated.resources.msg_no_playable_media_found
import tinyiptvkmp.composeapp.generated.resources.no_network
import tinyiptvkmp.composeapp.generated.resources.sad_face

@Composable
internal fun PlayerScreen(
    viewModel: PlayerViewModel,
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            PlayerUiEffect.OnNavigateBack -> onNavigateBack()
        }
    }
    PlayerScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun PlayerScreen(
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()

    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim)
            .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.TopCenter,
    ) {

        if (adaptiveLayoutState.playerProgramsPlacement == PlayerProgramsPlacement.Side) {
            Row(modifier = Modifier.fillMaxSize()) {
                PlayerContent(
                    modifier = Modifier.weight(MaterialTheme.dimensionWeight.weight2),
                    uiState = uiState,
                    onAction = onAction
                )

                if (!uiState.isFullscreen) {
                    ChannelPrograms(
                        modifier = Modifier
                            .weight(MaterialTheme.dimensionWeight.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        programs = uiState.currentChannel.programs,
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                PlayerContent(
                    modifier = Modifier.weight(MaterialTheme.dimensionWeight.weight2),
                    uiState = uiState,
                    onAction = onAction
                )

                if (!uiState.isFullscreen) {
                    ChannelPrograms(
                        modifier = Modifier
                            .weight(MaterialTheme.dimensionWeight.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        programs = uiState.currentChannel.programs,
                    )
                }
            }
        }

        OnScreenDisplay(
            isVisible = uiState.osdType != null,
            onViewTap = { onAction(PlayerUiAction.CloseOsd) }
        ) {
            uiState.osdType?.let { osdType ->
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
                            title = uiState.currentChannel.channelName,
                            programs = uiState.currentChannel.programs,
                        )
                    }

                    PlayerOSD.GroupChannels -> {
                        PlayerChannels(
                            channels = uiState.groupChannels,
                            current = uiState.channelIndex,
                            group = uiState.channelGroup,
                            onChannelSelect = { chn -> onAction(PlayerUiAction.SelectChannel(chn)) }
                        )
                    }

                    PlayerOSD.ProgramInfo -> {
                        ProgramInfo(
                            channelName = uiState.currentChannel.channelName,
                            programName = uiState.currentChannel.programTitle,
                            description = uiState.currentChannel.programDescription,
                        )
                    }

                    PlayerOSD.ChannelFavorites -> {
                        ChannelFavoriteSelector(
                            favoriteType = uiState.currentChannel.favoriteType,
                            onSelectFavorite = { favType ->
                                onAction(PlayerUiAction.UpdateFavorite(favType))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit
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
