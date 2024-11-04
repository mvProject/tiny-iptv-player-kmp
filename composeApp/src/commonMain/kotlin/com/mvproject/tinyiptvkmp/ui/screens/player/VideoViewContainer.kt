/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 15:44
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.mvproject.tinyiptvkmp.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.components.epg.PlayerEpgContent
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerHorizontalGestures
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerTapGesturesState
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerVerticalGestures
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptvkmp.ui.components.views.LoadingView
import com.mvproject.tinyiptvkmp.ui.components.views.NoPlaybackView
import com.mvproject.tinyiptvkmp.ui.components.views.VolumeProgressView
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.components.OverlayChannelInfo
import com.mvproject.tinyiptvkmp.ui.screens.player.components.OverlayChannels
import com.mvproject.tinyiptvkmp.ui.screens.player.components.PlayerChannelView
import com.mvproject.tinyiptvkmp.ui.screens.player.components.PlayerViewContainer
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_internet_found
import tinyiptvkmp.composeapp.generated.resources.msg_no_playable_media_found
import tinyiptvkmp.composeapp.generated.resources.no_network
import tinyiptvkmp.composeapp.generated.resources.sad_face


@Composable
internal fun PlayerScreen(
    viewModel: VideoViewViewModel,
    onNavigateBack: () -> Unit = {},
) {
    val videoViewState by viewModel.videoViewState.collectAsState()
    val videoViewChannelsState by viewModel.videoViewChannelsState.collectAsState()

    PlayerScreen(
        videoViewState = videoViewState,
        videoViewChannelsState = videoViewChannelsState,
        onPlaybackAction = viewModel::processPlaybackActions,
        onPlaybackStateAction = viewModel::processPlaybackStateActions,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun PlayerScreen(
    videoViewState: VideoViewState,
    videoViewChannelsState: List<TvPlaylistChannel> = emptyList(),
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
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
                    videoViewState = videoViewState,
                    onPlaybackAction = onPlaybackAction,
                    onPlaybackStateAction = onPlaybackStateAction,
                    onNavigateBack = onNavigateBack
                )

                if (!videoViewState.isFullscreen) {
                    PlayerEpgContent(
                        modifier = Modifier
                            .weight(MaterialTheme.dimens.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        epgList = videoViewState.currentChannel.programs,
                    )
                }
            }
        } else {
            Column {
                PlayerContent(
                    modifier = Modifier.weight(MaterialTheme.dimens.weight2),
                    videoViewState = videoViewState,
                    onPlaybackAction = onPlaybackAction,
                    onPlaybackStateAction = onPlaybackStateAction,
                    onNavigateBack = onNavigateBack
                )

                if (!videoViewState.isFullscreen) {
                    PlayerEpgContent(
                        modifier = Modifier
                            .weight(MaterialTheme.dimens.weight1)
                            .background(color = MaterialTheme.colorScheme.primary),
                        epgList = videoViewState.currentChannel.programs,
                    )
                }
            }
        }

        VolumeProgressView(
            modifier = Modifier.fillMaxSize(0.7f),
            isVisible = videoViewState.isVolumeUiVisible,
            value = videoViewState.currentVolume,
        )

        OverlayContent(
            isVisible = videoViewState.isEpgVisible,
            onViewTap = { onPlaybackAction(PlaybackActions.OnEpgUiToggle) }
        ) {
            OverlayEpg(
                title = videoViewState.currentChannel.channelName,
                programs = videoViewState.currentChannel.programs,
            )
        }

        OverlayContent(
            isVisible = videoViewState.isChannelsVisible,
            onViewTap = { onPlaybackAction(PlaybackActions.OnChannelsUiToggle) },
            contentAlpha = MaterialTheme.dimens.alpha90,
        ) {
            OverlayChannels(
                channels = videoViewChannelsState,
                current = videoViewState.mediaPosition,
                group = videoViewState.channelGroup,
                onChannelSelect = { chn -> onPlaybackAction(PlaybackActions.OnChannelSelected(chn)) }
            )
        }

        OverlayContent(
            isVisible = videoViewState.isChannelInfoVisible,
            onViewTap = { onPlaybackAction(PlaybackActions.OnChannelInfoUiToggle) },
        ) {
            OverlayChannelInfo(
                channelName = videoViewState.currentChannel.channelName,
                channelDescription = videoViewState.currentChannel.programs.toActual().firstOrNull()
                    ?.description ?: String.empty,
            )
        }
    }
}

@Composable
fun PlayerContent(
    modifier: Modifier = Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
    onNavigateBack: () -> Unit = {},
) {
    PlayerViewContainer(
        modifier =
        modifier
            .defaultPlayerHorizontalGestures(onAction = onPlaybackAction)
            .defaultPlayerVerticalGestures(onAction = onPlaybackAction)
            .defaultPlayerTapGesturesState(onAction = onPlaybackAction),
        videoViewState = videoViewState,
        onPlaybackAction = onPlaybackAction,
        onPlaybackStateAction = onPlaybackStateAction,
    ) {

        NoPlaybackView(
            isVisible = !videoViewState.isOnline,
            text = stringResource(Res.string.msg_no_internet_found),
            logo = painterResource(Res.drawable.no_network),
        )

        NoPlaybackView(
            isVisible = !videoViewState.isMediaPlayable,
            text = stringResource(Res.string.msg_no_playable_media_found),
            logo = painterResource(Res.drawable.sad_face),
        )

        LoadingView(isVisible = videoViewState.isBuffering)

        PlayerChannelView(
            modifier = Modifier.fillMaxSize(),
            isVisible = videoViewState.isControlUiVisible,
            currentChannel = videoViewState.currentChannel,
            isPlaying = videoViewState.isPlaying,
            isFullScreen = true,
            onPlaybackAction = onPlaybackAction,
            onPlaybackClose = onNavigateBack,
        )
    }
}