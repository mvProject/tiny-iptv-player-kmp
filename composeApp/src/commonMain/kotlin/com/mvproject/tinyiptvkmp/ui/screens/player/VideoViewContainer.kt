/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 15:44
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.ui.components.PlayerViewContainer
import com.mvproject.tinyiptvkmp.ui.components.TwoPaneContainer
import com.mvproject.tinyiptvkmp.ui.components.epg.PlayerEpgContent
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerHorizontalGestures
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerTapGesturesState
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerVerticalGestures
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptvkmp.ui.components.views.LoadingView
import com.mvproject.tinyiptvkmp.ui.components.views.NoPlaybackView
import com.mvproject.tinyiptvkmp.ui.components.views.VolumeProgressView
import com.mvproject.tinyiptvkmp.ui.data.TvPlaylistChannels
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.components.OverlayChannelInfo
import com.mvproject.tinyiptvkmp.ui.screens.player.components.OverlayChannels
import com.mvproject.tinyiptvkmp.ui.screens.player.components.PlayerChannelView
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.ui.theme.dimens
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
    videoViewChannelsState: TvPlaylistChannels,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
    onNavigateBack: () -> Unit = {},
) {
    val fraction =
        remember(videoViewState.isFullscreen) {
            if (videoViewState.isFullscreen) 1f else 0.5f
        }

    Box(
        modifier =
        Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.TopCenter,
    ) {
        if (videoViewState.isFullscreen) {
            PlayerViewContainer(
                modifier =
                Modifier
                    .defaultPlayerHorizontalGestures(onAction = onPlaybackAction)
                    .defaultPlayerVerticalGestures(onAction = onPlaybackAction)
                    .defaultPlayerTapGesturesState(onAction = onPlaybackAction),
                videoViewState = videoViewState,
                onPlaybackAction = onPlaybackAction,
                onPlaybackStateAction = onPlaybackStateAction,
            ) {
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
        } else {
            TwoPaneContainer(
                first = {
                    PlayerViewContainer(
                        modifier =
                        Modifier
                            .defaultPlayerHorizontalGestures(onAction = onPlaybackAction)
                            .defaultPlayerVerticalGestures(onAction = onPlaybackAction)
                            .defaultPlayerTapGesturesState(onAction = onPlaybackAction),
                        videoViewState = videoViewState,
                        onPlaybackAction = onPlaybackAction,
                        onPlaybackStateAction = onPlaybackStateAction,
                    ) {
                        PlayerChannelView(
                            modifier = Modifier.fillMaxSize(),
                            isVisible = videoViewState.isControlUiVisible,
                            currentChannel = videoViewState.currentChannel,
                            isPlaying = videoViewState.isPlaying,
                            isFullScreen = false,
                            onPlaybackAction = onPlaybackAction,
                            onPlaybackClose = onNavigateBack,
                        )
                    }
                },
                second = {
                    PlayerEpgContent(
                        modifier =
                        Modifier.background(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        epgList = videoViewState.currentChannel.programs,
                    )
                },
            )
        }

        VolumeProgressView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = videoViewState.isVolumeUiVisible,
            value = videoViewState.currentVolume,
        )

        OverlayContent(
            isVisible = videoViewState.isEpgVisible,
            onViewTap = { onPlaybackAction(PlaybackActions.OnEpgUiToggle) }
        ) {
            OverlayEpg(
                isFullScreen = videoViewState.isFullscreen,
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
                isFullScreen = videoViewState.isFullscreen,
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
                isFullScreen = videoViewState.isFullscreen,
                currentChannel = videoViewState.currentChannel,
            )
        }

        NoPlaybackView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = !videoViewState.isOnline,
            isFullScreen = videoViewState.isFullscreen,
            text = stringResource(Res.string.msg_no_internet_found),
            logo = painterResource(Res.drawable.no_network),
        )

        NoPlaybackView(
            modifier = Modifier.fillMaxSize(),
            isVisible = !videoViewState.isMediaPlayable,
            isFullScreen = videoViewState.isFullscreen,
            text = stringResource(Res.string.msg_no_playable_media_found),
            logo = painterResource(Res.drawable.sad_face),
        )

        LoadingView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = videoViewState.isBuffering,
        )
    }
}
