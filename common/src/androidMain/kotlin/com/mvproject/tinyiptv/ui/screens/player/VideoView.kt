/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 28.10.23, 15:55
 *
 */

package com.mvproject.tinyiptv.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.Player
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mvproject.tinyiptv.common.R
import com.mvproject.tinyiptv.data.mappers.AndroidListMappers.createMediaItems
import com.mvproject.tinyiptv.ui.components.ConnectionState
import com.mvproject.tinyiptv.ui.components.epg.PlayerEpgContent
import com.mvproject.tinyiptv.ui.components.networkConnectionState
import com.mvproject.tinyiptv.ui.components.overlay.OverlayChannelInfo
import com.mvproject.tinyiptv.ui.components.overlay.OverlayChannels
import com.mvproject.tinyiptv.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptv.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptv.ui.components.player.PlayerView
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.components.views.NoPlaybackView
import com.mvproject.tinyiptv.ui.screens.player.events.PlaybackEvents
import com.mvproject.tinyiptv.ui.screens.player.state.rememberVideoPlayerState
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.PLAYBACK_START_POSITION
import com.mvproject.tinyiptv.utils.findActivity
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun VideoView(
    viewModel: VideoViewViewModel,
    channelUrl: String,
    channelGroup: String
) {
    val context = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass()
    val displayFeatures = calculateDisplayFeatures(activity = context.findActivity())

    val connection by networkConnectionState()
    when (connection) {
        ConnectionState.Available -> Napier.w("testing connectivity is available")
        ConnectionState.Unavailable -> Napier.w("testing connectivity is unavailable")
    }

    val videoViewState by viewModel.videoViewState.collectAsState()

    val playerState = rememberVideoPlayerState(
        isPlayerFullscreen = videoViewState.isFullscreen,
        videoResizeMode = videoViewState.videoResizeMode,
        onPlaybackStateAction = viewModel::processPlaybackStateActions
    )
    val systemUIController = rememberSystemUiController()

    systemUIController.isNavigationBarVisible = !playerState.isFullscreen.value
    systemUIController.isStatusBarVisible = !playerState.isFullscreen.value

    LaunchedEffect(Unit) {
        viewModel.playbackEffects.onEach { effect ->
            when (effect) {
                PlaybackEvents.OnPlaybackRestart -> {
                    Napier.e("testing playbackEffects OnPlaybackRestart")
                    if (playerState.playerState.value == Player.STATE_IDLE) {
                        playerState.player.apply {
                            prepare()
                            playWhenReady = true
                        }
                    }
                }

                PlaybackEvents.OnChannelsUiToggle -> {
                    viewModel.toggleChannelsVisibility()
                }

                PlaybackEvents.OnChannelInfoUiToggle -> {
                    viewModel.toggleChannelInfoVisibility()
                }

                PlaybackEvents.OnEpgUiToggle -> {
                    viewModel.toggleEpgVisibility()
                }

                PlaybackEvents.OnFullScreenToggle -> {
                    playerState.toggleFullscreen()
                }

                PlaybackEvents.OnNextSelected -> {
                    playerState.next()
                }

                PlaybackEvents.OnPlaybackToggle -> {
                    playerState.togglePlayingState()
                }

                PlaybackEvents.OnPlayerUiToggle -> {
                    playerState.toggleControlUiState()
                }

                PlaybackEvents.OnPreviousSelected -> {
                    playerState.previous()
                }

                PlaybackEvents.OnVideoResizeToggle -> {
                    playerState.toggleVideoResizeMode()
                }

                PlaybackEvents.OnVolumeDown -> {
                    playerState.volumeDown()
                }

                PlaybackEvents.OnVolumeUp -> {
                    playerState.volumeUp()
                }

                PlaybackEvents.OnSpecifiedSelected -> {
                    playerState.player.stop()
                    playerState.player.apply {
                        setMediaItems(
                            videoViewState.channels.createMediaItems(),
                            videoViewState.mediaPosition,
                            PLAYBACK_START_POSITION
                        )
                        repeatMode = Player.REPEAT_MODE_ALL
                        prepare()
                        playWhenReady = true
                    }
                }

                PlaybackEvents.OnFavoriteToggle -> {
                    viewModel.toggleChannelFavorite()
                }
            }
        }.collect {}
    }

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.TopCenter
    ) {
        if (playerState.isFullscreen.value) {
            PlayerView(
                playerState = playerState,
                currentChannel = videoViewState.currentChannel,
                onPlaybackAction = viewModel::processPlaybackActions
            )
        } else {
            TwoPane(
                first = {
                    PlayerView(
                        playerState = playerState,
                        currentChannel = videoViewState.currentChannel,
                        onPlaybackAction = viewModel::processPlaybackActions
                    )
                },
                second = {
                    PlayerEpgContent(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        epgList = videoViewState.currentChannel.channelEpg
                    )
                },
                strategy = when (windowSizeClass.widthSizeClass) {
                    Compact -> VerticalTwoPaneStrategy(MaterialTheme.dimens.fraction50)
                    Medium -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction60)
                    else -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction70)
                },
                displayFeatures = displayFeatures
            )
        }

        AnimatedVisibility(
            visible = videoViewState.isEpgVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OverlayContent(
                onViewTap = viewModel::toggleEpgVisibility
            ) {
                OverlayEpg(
                    isFullScreen = videoViewState.isFullscreen,
                    currentChannel = videoViewState.currentChannel
                )
            }
        }

        AnimatedVisibility(
            visible = videoViewState.isChannelsVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OverlayContent(
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
        }

        AnimatedVisibility(
            visible = videoViewState.isChannelInfoVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OverlayContent(
                onViewTap = viewModel::toggleChannelInfoVisibility
            ) {
                OverlayChannelInfo(
                    isFullScreen = videoViewState.isFullscreen,
                    currentChannel = videoViewState.currentChannel
                )
            }
        }

        AnimatedVisibility(
            visible = !videoViewState.isOnline,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
            ) {
                NoPlaybackView(
                    textRes = R.string.msg_no_internet_found,
                    iconRes = R.drawable.ic_no_network
                )
            }
        }

        AnimatedVisibility(
            visible = !videoViewState.isMediaPlayable,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(0.5f),
                contentAlignment = Alignment.Center
            ) {
                NoPlaybackView(
                    textRes = R.string.msg_no_playable_media_found,
                    iconRes = R.drawable.ic_sad_face
                )
            }
        }

        AnimatedVisibility(
            visible = videoViewState.isBuffering,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingView()
        }
    }

    LaunchedEffect(videoViewState.channels.count()) {
        playerState.player.apply {
            setMediaItems(
                videoViewState.channels.createMediaItems(),
                videoViewState.mediaPosition,
                PLAYBACK_START_POSITION
            )
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(viewModel) {
        viewModel.initPlayBack(
            channelUrl = channelUrl,
            channelGroup = channelGroup
        )

        onDispose {
            playerState.player.stop()
        }
    }
}