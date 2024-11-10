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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.epg.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.ui.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.ui.indicators.VolumeIndicator
import com.mvproject.tinyiptvkmp.core.ui.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.features.player.action.UiActions
import com.mvproject.tinyiptvkmp.features.player.components.NoPlaybackView
import com.mvproject.tinyiptvkmp.features.player.components.PlayerChannels
import com.mvproject.tinyiptvkmp.features.player.components.PlayerContainer
import com.mvproject.tinyiptvkmp.features.player.components.PlayerToolbar
import com.mvproject.tinyiptvkmp.features.player.components.ProgramInfo
import com.mvproject.tinyiptvkmp.features.player.components.handleHorizontalGestures
import com.mvproject.tinyiptvkmp.features.player.components.handleTapGestures
import com.mvproject.tinyiptvkmp.features.player.components.handleVerticalGestures
import com.mvproject.tinyiptvkmp.features.player.state.TvPlayerState
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
    val tvPlayerState by viewModel.tvPlayerState.collectAsState()

    PlayerScreen(
        tvPlayerState = tvPlayerState,
        onUiAction = viewModel::processPlaybackActions,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun PlayerScreen(
    tvPlayerState: TvPlayerState,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onUiAction: (UiActions) -> Unit,
    onNavigateBack: () -> Unit = {},
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
                    tvPlayerState = tvPlayerState,
                    onUiAction = onUiAction,
                    onNavigateBack = onNavigateBack
                )

                if (!tvPlayerState.isFullscreen) {
                    ChannelPrograms(
                        modifier = Modifier
                            .weight(MaterialTheme.dimens.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        programs = tvPlayerState.currentChannel.programs,
                    )
                }
            }
        } else {
            Column {
                PlayerContent(
                    modifier = Modifier.weight(MaterialTheme.dimens.weight2),
                    tvPlayerState = tvPlayerState,
                    onUiAction = onUiAction,
                    onNavigateBack = onNavigateBack
                )

                if (!tvPlayerState.isFullscreen) {
                    ChannelPrograms(
                        modifier = Modifier
                            .weight(MaterialTheme.dimens.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        programs = tvPlayerState.currentChannel.programs,
                    )
                }
            }
        }

        OverlayContent(
            isVisible = tvPlayerState.isEpgVisible,
            onViewTap = { onUiAction(UiActions.ToggleProgramsUi) }
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
                title = tvPlayerState.currentChannel.channelName,
                programs = tvPlayerState.currentChannel.programs,
            )
        }

        OverlayContent(
            isVisible = tvPlayerState.isChannelsVisible,
            onViewTap = { onUiAction(UiActions.ToggleChannelsUi) },
            contentAlpha = MaterialTheme.dimens.alpha90,
        ) {
            PlayerChannels(
                channels = tvPlayerState.groupChannels,
                current = tvPlayerState.channelIndex,
                group = tvPlayerState.channelGroup,
                onChannelSelect = { chn -> onUiAction(UiActions.SelectChannel(chn)) }
            )
        }

        OverlayContent(
            isVisible = tvPlayerState.isChannelInfoVisible,
            onViewTap = { onUiAction(UiActions.ToggleProgramInfoUi) },
        ) {
            ProgramInfo(
                channelName = tvPlayerState.currentChannel.channelName,
                programName = tvPlayerState.currentChannel.programTitle,
                description = tvPlayerState.currentChannel.programDescription,
            )
        }
    }
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    tvPlayerState: TvPlayerState,
    onUiAction: (UiActions) -> Unit,
    onNavigateBack: () -> Unit = {},
) {
    PlayerContainer(
        modifier = modifier
            .handleHorizontalGestures(onAction = onUiAction)
            .handleVerticalGestures(onAction = onUiAction)
            .handleTapGestures(onAction = onUiAction),
        tvPlayerState = tvPlayerState,
        onUiAction = onUiAction,
    ) {

        NoPlaybackView(
            isVisible = !tvPlayerState.isOnline,
            text = stringResource(Res.string.msg_no_internet_found),
            logo = painterResource(Res.drawable.no_network),
        )

        NoPlaybackView(
            isVisible = !tvPlayerState.isMediaPlayable,
            text = stringResource(Res.string.msg_no_playable_media_found),
            logo = painterResource(Res.drawable.sad_face),
        )

        VolumeIndicator(
            modifier = Modifier.fillMaxSize(),
            isVisible = tvPlayerState.isVolumeUiVisible,
            value = tvPlayerState.currentVolume,
        )

        LoadingIndicator(isVisible = tvPlayerState.isBuffering)

        PlayerToolbar(
            modifier = Modifier.fillMaxSize(),
            isVisible = tvPlayerState.isControlUiVisible,
            currentChannel = tvPlayerState.currentChannel,
            isPlaying = tvPlayerState.isPlaying,
            isFullScreen = true,
            onUiAction = onUiAction,
            onPlaybackClose = onNavigateBack,
        )
    }
}