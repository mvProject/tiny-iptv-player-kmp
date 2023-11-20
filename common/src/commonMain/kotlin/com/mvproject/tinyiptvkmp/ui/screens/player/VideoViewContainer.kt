/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.MainRes
import com.mvproject.tinyiptvkmp.platform.PlayerViewContainer
import com.mvproject.tinyiptvkmp.platform.TwoPaneContainer
import com.mvproject.tinyiptvkmp.ui.components.epg.PlayerEpgContent
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerHorizontalGestures
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerTapGesturesState
import com.mvproject.tinyiptvkmp.ui.components.modifiers.defaultPlayerVerticalGestures
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayChannelInfo
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayChannels
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptvkmp.ui.components.player.PlayerChannelView
import com.mvproject.tinyiptvkmp.ui.components.views.LoadingView
import com.mvproject.tinyiptvkmp.ui.components.views.NoPlaybackView
import com.mvproject.tinyiptvkmp.ui.components.views.VolumeProgressView
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import io.github.skeptick.libres.compose.painterResource

@Composable
fun VideoViewContainer(
    viewModel: VideoViewViewModel,
    channelUrl: String,
    channelGroup: String,
    onNavigateBack: () -> Unit = {}
) {
    val videoViewState by viewModel.videoViewState.collectAsState()

    val fraction = remember(videoViewState.isFullscreen) {
        if (videoViewState.isFullscreen) 1f else 0.5f
    }
    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.TopCenter
    ) {
        if (videoViewState.isFullscreen) {
            PlayerViewContainer(
                modifier = Modifier
                    .defaultPlayerHorizontalGestures(onAction = viewModel::processPlaybackActions)
                    .defaultPlayerVerticalGestures(onAction = viewModel::processPlaybackActions)
                    .defaultPlayerTapGesturesState(onAction = viewModel::processPlaybackActions),
                videoViewState = videoViewState,
                onPlaybackAction = viewModel::processPlaybackActions,
                onPlaybackStateAction = viewModel::processPlaybackStateActions
            ) {
                PlayerChannelView(
                    modifier = Modifier.fillMaxSize(),
                    isVisible = videoViewState.isControlUiVisible,
                    currentChannel = videoViewState.currentChannel,
                    isPlaying = videoViewState.isPlaying,
                    isFullScreen = true,
                    onPlaybackAction = viewModel::processPlaybackActions,
                    onPlaybackClose = onNavigateBack
                )
            }
        } else {
            TwoPaneContainer(
                first = {
                    PlayerViewContainer(
                        modifier = Modifier
                            .defaultPlayerHorizontalGestures(onAction = viewModel::processPlaybackActions)
                            .defaultPlayerVerticalGestures(onAction = viewModel::processPlaybackActions)
                            .defaultPlayerTapGesturesState(onAction = viewModel::processPlaybackActions),
                        videoViewState = videoViewState,
                        onPlaybackAction = viewModel::processPlaybackActions,
                        onPlaybackStateAction = viewModel::processPlaybackStateActions
                    ) {
                        PlayerChannelView(
                            modifier = Modifier.fillMaxSize(),
                            isVisible = videoViewState.isControlUiVisible,
                            currentChannel = videoViewState.currentChannel,
                            isPlaying = videoViewState.isPlaying,
                            isFullScreen = false,
                            onPlaybackAction = viewModel::processPlaybackActions,
                            onPlaybackClose = onNavigateBack
                        )
                    }
                },
                second = {
                    PlayerEpgContent(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        epgList = videoViewState.currentChannel.channelEpg
                    )
                }
            )
        }

        VolumeProgressView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = videoViewState.isVolumeUiVisible,
            value = videoViewState.currentVolume
        )

        OverlayContent(
            isVisible = videoViewState.isEpgVisible,
            onViewTap = viewModel::toggleEpgVisibility
        ) {
            OverlayEpg(
                isFullScreen = videoViewState.isFullscreen,
                currentChannel = videoViewState.currentChannel
            )
        }

        OverlayContent(
            isVisible = videoViewState.isChannelsVisible,
            onViewTap = viewModel::toggleChannelsVisibility,
            contentAlpha = MaterialTheme.dimens.alpha90
        ) {
            OverlayChannels(
                isFullScreen = videoViewState.isFullscreen,
                channels = videoViewState.channels,
                current = videoViewState.mediaPosition,
                group = videoViewState.channelGroup,
                onChannelSelect = viewModel::switchToChannel
            )
        }

        OverlayContent(
            isVisible = videoViewState.isChannelInfoVisible,
            onViewTap = viewModel::toggleChannelInfoVisibility
        ) {
            OverlayChannelInfo(
                isFullScreen = videoViewState.isFullscreen,
                currentChannel = videoViewState.currentChannel
            )
        }

        NoPlaybackView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = !videoViewState.isOnline,
            isFullScreen = videoViewState.isFullscreen,
            text = MainRes.string.msg_no_internet_found,
            logo = MainRes.image.no_network.painterResource()
        )

        NoPlaybackView(
            modifier = Modifier.fillMaxSize(),
            isVisible = !videoViewState.isMediaPlayable,
            isFullScreen = videoViewState.isFullscreen,
            text = MainRes.string.msg_no_playable_media_found,
            logo = MainRes.image.sad_face.painterResource()
        )

        LoadingView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = videoViewState.isBuffering
        )
    }

    DisposableEffect(viewModel) {
        viewModel.initPlayBack(
            channelUrl = channelUrl,
            channelGroup = channelGroup
        )

        onDispose {}
    }
}