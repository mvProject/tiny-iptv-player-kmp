/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.mappers.ListMappers.withRefreshedEpg
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.network.NetworkConnectivityObserver
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptv.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.events.PlaybackEvents
import com.mvproject.tinyiptv.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptv.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptv.utils.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptv.utils.isMediaPlayable
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoViewViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
) : ViewModel() {

    private var _videoViewState = MutableStateFlow(VideoViewState())
    val videoViewState = _videoViewState.asStateFlow()

    private val _playbackEffects: Channel<PlaybackEvents> = Channel()
    val playbackEffects = _playbackEffects.receiveAsFlow()

    init {
        Napier.i("testing VideoViewViewModel init")
        viewModelScope.launch {
            _videoViewState.update {
                it.copy(
                    isFullscreen = preferenceRepository.getDefaultFullscreenMode(),
                    videoResizeMode = ResizeMode.entries[preferenceRepository.getDefaultResizeMode()]
                )
            }
        }
        viewModelScope.launch {
            networkConnectivityObserver.observe()
                .collect { status ->
                    val isNetworkOn = status == NetworkConnectivityObserver.Status.Available
                    if (isNetworkOn) {
                        restartPlayingIfNeeded()
                    }
                    _videoViewState.update {
                        it.copy(
                            isOnline = isNetworkOn
                        )
                    }
                }
        }
    }

    private fun restartPlayingIfNeeded() {
        viewModelScope.launch {
            _playbackEffects.send(PlaybackEvents.OnPlaybackRestart)
        }
    }

    fun initPlayBack(channelUrl: String, channelGroup: String) {
        viewModelScope.launch {
            val channelList = getGroupChannelsUseCase(channelGroup)
            val currentItemPosition = getCurrentMediaPosition(channelUrl, channelList)
            val currentChannel = channelList[currentItemPosition]

            _videoViewState.update { state ->
                state.copy(
                    channelGroup = channelGroup,
                    mediaPosition = currentItemPosition,
                    channels = channelList,
                    currentChannel = currentChannel
                )
            }
        }
    }

    fun switchToChannel(channel: TvPlaylistChannel) {
        viewModelScope.launch {
            val channelsRefreshed = videoViewState.value.channels.withRefreshedEpg()

            val newMediaPosition = getCurrentMediaPosition(
                channel.channelUrl,
                channelsRefreshed
            )

            _videoViewState.update { state ->
                state.copy(
                    mediaPosition = newMediaPosition,
                    currentChannel = channel,
                    channels = channelsRefreshed,
                    isChannelsVisible = false
                )
            }

            viewModelScope.launch {
                _playbackEffects.send(PlaybackEvents.OnSpecifiedSelected)
            }
        }
    }

    private fun getCurrentMediaPosition(
        channelUrl: String,
        channels: List<TvPlaylistChannel>
    ): Int {
        val currentPos = videoViewState.value.mediaPosition

        val targetPos = channels
            .indexOfFirst { it.channelUrl == channelUrl }

        val mediaPosition = if (targetPos > INT_NO_VALUE) {
            targetPos
        } else {
            currentPos
        }

        return mediaPosition
    }


    fun processPlaybackActions(action: PlaybackActions) {
        val effect = when (action) {
            PlaybackActions.OnChannelsUiToggle -> PlaybackEvents.OnChannelsUiToggle
            PlaybackActions.OnEpgUiToggle -> PlaybackEvents.OnEpgUiToggle
            PlaybackActions.OnFullScreenToggle -> PlaybackEvents.OnFullScreenToggle
            PlaybackActions.OnNextSelected -> PlaybackEvents.OnNextSelected
            PlaybackActions.OnPlaybackToggle -> PlaybackEvents.OnPlaybackToggle
            PlaybackActions.OnPlayerUiToggle -> PlaybackEvents.OnPlayerUiToggle
            PlaybackActions.OnPreviousSelected -> PlaybackEvents.OnPreviousSelected
            PlaybackActions.OnVideoResizeToggle -> PlaybackEvents.OnVideoResizeToggle
            PlaybackActions.OnVolumeDown -> PlaybackEvents.OnVolumeDown
            PlaybackActions.OnVolumeUp -> PlaybackEvents.OnVolumeUp
            PlaybackActions.OnChannelInfoUiToggle -> PlaybackEvents.OnChannelInfoUiToggle
            PlaybackActions.OnFavoriteToggle -> PlaybackEvents.OnFavoriteToggle
        }
        viewModelScope.launch {
            _playbackEffects.send(effect)
        }

    }

    fun processPlaybackStateActions(action: PlaybackStateActions) {
        var isMediaPlayable = videoViewState.value.isMediaPlayable
        var isBuffering = videoViewState.value.isBuffering

        when (action) {
            is PlaybackStateActions.OnMediaItemTransition -> {

                val currentMediaPosition = action.index
                val current = videoViewState.value.channels[currentMediaPosition]
                val channelsRefreshed = videoViewState.value.channels.withRefreshedEpg()
                _videoViewState.update { state ->
                    state.copy(
                        mediaPosition = currentMediaPosition,
                        currentChannel = current,
                        channels = channelsRefreshed
                    )
                }

                restartPlayingIfNeeded()
            }

            is PlaybackStateActions.OnPlaybackState -> {
                isBuffering = action.state == VideoPlaybackState.VideoPlaybackBuffering
                Napier.e("testing OnPlaybackState isBuffering:$isBuffering")

                when (action.state) {
                    is VideoPlaybackState.VideoPlaybackIdle -> {
                        Napier.e("testing VideoPlaybackState VideoPlaybackIdle")
                        isMediaPlayable = isMediaPlayable(action.state.errorCode)
                    }

                    VideoPlaybackState.VideoPlaybackReady -> {
                        Napier.e("testing VideoPlaybackState VideoPlaybackReady")
                        isMediaPlayable = true
                    }

                    else -> {

                    }
                }

            }
        }

        _videoViewState.update { state ->
            state.copy(
                isMediaPlayable = isMediaPlayable,
                isBuffering = isBuffering
            )
        }
    }

    fun toggleEpgVisibility() {
        val currentEpgVisibleState = videoViewState.value.isEpgVisible

        if (!currentEpgVisibleState) {
            val channelsRefreshed = videoViewState.value.channels.withRefreshedEpg()
            _videoViewState.update { state ->
                state.copy(
                    channels = channelsRefreshed
                )
            }
        }

        _videoViewState.update { state ->
            state.copy(
                isEpgVisible = !currentEpgVisibleState
            )
        }
    }

    fun toggleChannelsVisibility() {
        val currentChannelsVisibleState = videoViewState.value.isChannelsVisible

        _videoViewState.update { state ->
            state.copy(
                isChannelsVisible = !currentChannelsVisibleState
            )
        }
    }

    fun toggleChannelInfoVisibility() {
        val currentChannelInfoVisibleState = videoViewState.value.isChannelInfoVisible

        _videoViewState.update { state ->
            state.copy(
                isChannelInfoVisible = !currentChannelInfoVisibleState
            )
        }
    }

    fun toggleChannelFavorite() {
        val currentChannel = videoViewState.value.currentChannel
        val currentChannels = videoViewState.value.channels
        val currentIndex = videoViewState.value.mediaPosition

        val currentChannelFavoriteChanged = currentChannel.copy(
            isInFavorites = !currentChannel.isInFavorites
        )

        val updatedFavoriteChangedChannels = currentChannels.mapIndexed { index, channel ->
            if (index == currentIndex) {
                currentChannelFavoriteChanged
            } else
                channel
        }

        viewModelScope.launch {
            _videoViewState.update { state ->
                state.copy(
                    currentChannel = currentChannelFavoriteChanged,
                    channels = updatedFavoriteChangedChannels
                )
            }

            toggleFavoriteChannelUseCase(channel = currentChannel)
        }

    }
}