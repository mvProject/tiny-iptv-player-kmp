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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.mvproject.tinyiptvkmp.core.common.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.epg.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.ui.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.ui.indicators.VolumeIndicator
import com.mvproject.tinyiptvkmp.core.ui.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.features.player.components.NoPlaybackView
import com.mvproject.tinyiptvkmp.features.player.components.PlayerChannels
import com.mvproject.tinyiptvkmp.features.player.components.PlayerContainer
import com.mvproject.tinyiptvkmp.features.player.components.PlayerToolbar
import com.mvproject.tinyiptvkmp.features.player.components.ProgramInfo
import com.mvproject.tinyiptvkmp.features.player.components.handleHorizontalGestures
import com.mvproject.tinyiptvkmp.features.player.components.handleTapGestures
import com.mvproject.tinyiptvkmp.features.player.components.handleVerticalGestures
import com.mvproject.tinyiptvkmp.features.player.utils.PlayerUtils.programDescription
import com.mvproject.tinyiptvkmp.features.player.utils.PlayerUtils.programTitle
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
        onUiAction = viewModel::onAction
    )
}

@Composable
private fun PlayerScreen(
    uiState: PlayerUiState,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onUiAction: (PlayerUiAction) -> Unit
) {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim)
            .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.TopCenter,
    ) {

        if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT
            && windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT
        ) {
            Row {
                PlayerContent(
                    modifier = Modifier.weight(MaterialTheme.dimens.weight2),
                    uiState = uiState,
                    onUiAction = onUiAction
                )

                if (!uiState.isFullscreen) {
                    ChannelPrograms(
                        modifier = Modifier
                            .weight(MaterialTheme.dimens.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        programs = uiState.currentChannel.programs,
                    )
                }
            }
        } else {
            Column {
                PlayerContent(
                    modifier = Modifier.weight(MaterialTheme.dimens.weight2),
                    uiState = uiState,
                    onUiAction = onUiAction
                )

                if (!uiState.isFullscreen) {
                    ChannelPrograms(
                        modifier = Modifier
                            .weight(MaterialTheme.dimens.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        programs = uiState.currentChannel.programs,
                    )
                }
            }
        }

        OverlayContent(
            isVisible = uiState.isEpgVisible,
            onViewTap = { onUiAction(PlayerUiAction.ToggleProgramsUi) }
        ) {
            ChannelPrograms(
                modifier =
                Modifier
                    .fillMaxHeight(MaterialTheme.dimens.fraction90)
                    .fillMaxWidth(MaterialTheme.dimens.fraction80)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape =
                        RoundedCornerShape(
                            bottomStart = MaterialTheme.dimens.size8,
                            bottomEnd = MaterialTheme.dimens.size8,
                        ),
                    ),
                title = uiState.currentChannel.channelName,
                programs = uiState.currentChannel.programs,
            )
        }

        OverlayContent(
            isVisible = uiState.isChannelsVisible,
            onViewTap = { onUiAction(PlayerUiAction.ToggleChannelsUi) },
            contentAlpha = MaterialTheme.dimens.alpha90,
        ) {
            PlayerChannels(
                channels = uiState.groupChannels,
                current = uiState.channelIndex,
                group = uiState.channelGroup,
                onChannelSelect = { chn -> onUiAction(PlayerUiAction.SelectChannel(chn)) }
            )
        }

        OverlayContent(
            isVisible = uiState.isChannelInfoVisible,
            onViewTap = { onUiAction(PlayerUiAction.ToggleProgramInfoUi) },
        ) {
            ProgramInfo(
                channelName = uiState.currentChannel.channelName,
                programName = uiState.currentChannel.programTitle,
                description = uiState.currentChannel.programDescription,
            )
        }
    }
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    uiState: PlayerUiState,
    onUiAction: (PlayerUiAction) -> Unit
) {
    PlayerContainer(
        modifier = modifier
            .handleHorizontalGestures(onAction = onUiAction)
            .handleVerticalGestures(onAction = onUiAction)
            .handleTapGestures(onAction = onUiAction),
        uiState = uiState,
        onUiAction = onUiAction,
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
            currentChannel = uiState.currentChannel,
            isPlaying = uiState.isPlaying,
            isFullScreen = true,
            onAction = onUiAction
        )
    }
}