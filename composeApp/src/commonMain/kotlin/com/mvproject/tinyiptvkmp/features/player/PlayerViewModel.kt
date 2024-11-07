/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 14:06
 *
 */

package com.mvproject.tinyiptvkmp.features.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants.DELAY_50
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_STEP_VOLUME
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.UI_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.common.AppConstants.VOLUME_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.features.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.features.player.components.isMediaPlayable
import com.mvproject.tinyiptvkmp.features.player.state.PlaybackState
import com.mvproject.tinyiptvkmp.features.player.state.TvPlayerState
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(
    savedStateHandle: SavedStateHandle,
    private val preferenceRepository: PreferenceRepository,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
) : ViewModel() {
    private var pollVolumeJob: Job? = null

    // time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    private var _tvPlayerState = MutableStateFlow(TvPlayerState())
    val tvPlayerState: StateFlow<TvPlayerState> = _tvPlayerState

    private var _tvChannelState = MutableStateFlow<List<TvChannel>>(emptyList())
    val tvChannelState: StateFlow<List<TvChannel>> = _tvChannelState

    private var _videoRatio = FLOAT_VALUE_1

    private val args = savedStateHandle.toRoute<AppRoutes.VideoView>()

    init {
        val media = args.mediaName
        val group = args.mediaGroup
        Logger.d("testing VideoViewViewModel init media:$media, group:$group")

        viewModelScope.launch {
            _tvPlayerState.update { current ->
                val ratioMode = RatioMode.entries[preferenceRepository.getDefaultRatioMode()]

                current.copy(
                    isFullscreen = preferenceRepository.getDefaultFullscreenMode(),
                    videoResizeMode = ResizeMode.entries[preferenceRepository.getDefaultResizeMode()],
                    videoRatioMode = ratioMode,
                    videoRatio = ratioMode.ratio,
                )
            }
        }

        initPlayBack(
            channelName = media,
            channelGroup = group,
        )
    }

    private fun initPlayBack(
        channelName: String,
        channelGroup: String,
    ) {
        viewModelScope.launch {
            val channelList = getGroupChannelsUseCase(channelGroup, String.empty)
            val currentPlaying = tvPlayerState.value.currentChannel.channelName

            val name = currentPlaying.ifBlank { channelName }

            val currentItemPosition =
                getCurrentMediaPosition(
                    channelName = name,
                    channels = channelList,
                )
            val currentChannel = channelList[currentItemPosition]

            _tvPlayerState.update { current ->
                current.copy(
                    channelGroup = channelGroup,
                    mediaPosition = currentItemPosition,
                    currentChannel = currentChannel,
                )
            }

            _tvChannelState.value = channelList

            loadSelectedChannelEpg()

            loadAvailableChannelsEpg()
        }
    }

    private fun switchToChannel(channel: TvChannel) {
        viewModelScope.launch {
            //   val channelsRefreshed = videoViewState.value.channels.items.withRefreshedEpg()
            val currentChannels = tvChannelState.value

            val newMediaPosition =
                getCurrentMediaPosition(
                    channelName = channel.channelName,
                    channels = currentChannels,
                )

            setCurrentChannel(currentMediaPosition = newMediaPosition)
        }
    }

    fun processPlaybackActions(action: PlaybackActions) {
        when (action) {
            PlaybackActions.OnNextSelected -> switchToNextChannel()
            PlaybackActions.OnPreviousSelected -> switchToPreviousChannel()
            PlaybackActions.OnChannelsUiToggle -> toggleChannelsVisibility()
            PlaybackActions.OnEpgUiToggle -> toggleEpgVisibility()
            PlaybackActions.OnFullScreenToggle -> toggleFullScreen()
            PlaybackActions.OnVideoResizeToggle -> toggleVideoResizeMode()
            PlaybackActions.OnVideoRatioToggle -> toggleVideoRatioMode()
            PlaybackActions.OnChannelInfoUiToggle -> toggleChannelInfoVisibility()
            PlaybackActions.OnFavoriteToggle -> toggleChannelFavorite()
            PlaybackActions.OnPlaybackToggle -> togglePlayingState()
            PlaybackActions.OnPlayerUiToggle -> toggleControlUiState()
            PlaybackActions.OnVolumeDown -> decreaseVolume()
            PlaybackActions.OnVolumeUp -> increaseVolume()
            PlaybackActions.OnRestarted -> consumeRestart()
            is PlaybackActions.OnChannelSelected -> switchToChannel(channel = action.channel)
        }
    }

    fun processPlaybackStateActions(action: PlaybackStateActions) {
        when (action) {
            is PlaybackStateActions.OnVideoSizeChanged -> {
                _videoRatio = action.videoRatio

                val ratio =
                    if (tvPlayerState.value.videoRatioMode == RatioMode.Original) {
                        _videoRatio
                    } else {
                        tvPlayerState.value.videoRatioMode.ratio
                    }

                _tvPlayerState.update { current ->
                    current.copy(videoRatio = ratio)
                }
            }

            is PlaybackStateActions.OnIsPlayingChanged -> {
                _tvPlayerState.update { current ->
                    current.copy(isPlaying = action.state)
                }
            }

            is PlaybackStateActions.OnMediaItemTransition -> {
                triggerRestart()
            }

            is PlaybackStateActions.OnPlaybackStateChanged -> {
                var isMediaPlayable = tvPlayerState.value.isMediaPlayable
                val isBuffering = action.state == PlaybackState.PlaybackBuffering

                when (action.state) {
                    is PlaybackState.PlaybackIdle -> {
                        isMediaPlayable = isMediaPlayable(action.state.errorCode)
                    }

                    PlaybackState.PlaybackReady -> {
                        isMediaPlayable = true
                    }

                    else -> {
                    }
                }

                _tvPlayerState.update { current ->
                    current.copy(
                        isMediaPlayable = isMediaPlayable,
                        isBuffering = isBuffering,
                    )
                }
            }
        }
    }

    private suspend fun loadSelectedChannelEpg() {
        val currentChannel = tvPlayerState.value.currentChannel
        if (currentChannel.epgId.isNotBlank()) {
            val channelsEpgData = getChannelsEpgUseCase(channelId = currentChannel.epgId)

            val currentChannelWithEpg =
                currentChannel.copy(
                    programs = channelsEpgData,
                )

            _tvPlayerState.update { state ->
                state.copy(currentChannel = currentChannelWithEpg)
            }
        }
    }

    private suspend fun loadAvailableChannelsEpg() {
        withContext(Dispatchers.IO) {
            val currentChannels = tvChannelState.value
            if (currentChannels.isNotEmpty()) {
                val channelsIds =
                    currentChannels
                        .map { it.epgId }
                        .filter { it.isNotBlank() }

                val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)

                val channelsWithPrograms = currentChannels.map { ch ->
                    val programs = channelsEpgData[ch.epgId] ?: emptyList()
                    ch.copy(programs = programs)
                }

                _tvChannelState.value = channelsWithPrograms
            }
        }
    }

    private fun getCurrentMediaPosition(
        channelName: String,
        channels: List<TvChannel>,
    ): Int {
        val currentPos = tvPlayerState.value.mediaPosition

        val targetPos = channels.indexOfFirst { it.channelName == channelName }

        val mediaPosition =
            if (targetPos > INT_NO_VALUE) {
                targetPos
            } else {
                currentPos
            }

        return mediaPosition
    }

    private fun triggerRestart() {
        _tvPlayerState.update { current ->
            current.copy(isRestartRequired = true)
        }
        toggleControlUiState()
    }

    private fun consumeRestart() {
        _tvPlayerState.update { current ->
            current.copy(isRestartRequired = false)
        }
    }

    private fun switchToNextChannel() {
        val currentChannelsCount = tvChannelState.value.count()
        val nextIndex = tvPlayerState.value.mediaPosition + INT_VALUE_1
        val newMediaPosition =
            if (nextIndex > currentChannelsCount - INT_VALUE_1) {
                INT_VALUE_ZERO
            } else {
                nextIndex
            }

        viewModelScope.launch {
            setCurrentChannel(currentMediaPosition = newMediaPosition)
        }
    }

    private fun switchToPreviousChannel() {
        val currentChannelsCount = tvChannelState.value.count()
        val nextIndex = tvPlayerState.value.mediaPosition - INT_VALUE_1
        val newMediaPosition =
            if (nextIndex < INT_VALUE_ZERO) {
                currentChannelsCount - INT_VALUE_1
            } else {
                nextIndex
            }

        viewModelScope.launch {
            setCurrentChannel(currentMediaPosition = newMediaPosition)
        }
    }

    private fun increaseVolume() {
        showVolumeUi()
        viewModelScope.launch {
            val targetVolume = tvPlayerState.value.currentVolume + FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtMost(FLOAT_VALUE_1)
            _tvPlayerState.update { current ->
                current.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private fun decreaseVolume() {
        showVolumeUi()
        viewModelScope.launch {
            val targetVolume = tvPlayerState.value.currentVolume - FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtLeast(FLOAT_VALUE_ZERO)
            _tvPlayerState.update { current ->
                current.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private suspend fun setCurrentChannel(currentMediaPosition: Int) {
        val currentChannels = tvChannelState.value
        val currentChannel = currentChannels[currentMediaPosition]

        _tvPlayerState.update { current ->
            current.copy(
                mediaPosition = currentMediaPosition,
                currentChannel = currentChannel,
                // channels = TvPlaylistChannels(items = currentChannels),
                isChannelsVisible = false,
            )
        }

        loadSelectedChannelEpg()
    }

    private fun toggleChannelFavorite() {
        val currentChannel = tvPlayerState.value.currentChannel
        val currentChannels = tvChannelState.value
        val currentIndex = tvPlayerState.value.mediaPosition

        // todo
        val currentChannelFavoriteChanged =
            currentChannel.copy(
                // isInFavorites = !currentChannel.isInFavorites,
            )

        val updatedFavoriteChangedChannels =
            currentChannels.mapIndexed { index, channel ->
                if (index == currentIndex) {
                    currentChannelFavoriteChanged
                } else {
                    channel
                }
            }

        viewModelScope.launch {
            _tvPlayerState.update { current ->
                current.copy(
                    currentChannel = currentChannelFavoriteChanged,
                )
            }

            _tvChannelState.value = updatedFavoriteChangedChannels

            toggleFavoriteChannelUseCase(channel = currentChannel)
        }
    }

    private fun toggleEpgVisibility() {
        if (tvPlayerState.value.isFullscreen) {
            _tvPlayerState.update { current ->
                val currentEpgVisibleState = current.isEpgVisible
                current.copy(isEpgVisible = !currentEpgVisibleState)
            }
        }
    }

    private fun toggleChannelsVisibility() {
        _tvPlayerState.update { current ->
            val currentChannelsVisibleState = current.isChannelsVisible
            current.copy(isChannelsVisible = !currentChannelsVisibleState)
        }
    }

    private fun toggleChannelInfoVisibility() {
        _tvPlayerState.update { current ->
            val currentChannelInfoVisibleState = current.isChannelInfoVisible
            current.copy(isChannelInfoVisible = !currentChannelInfoVisibleState)
        }
    }

    private fun toggleFullScreen() {
        _tvPlayerState.update { current ->
            val currentFullscreenState = current.isFullscreen
            current.copy(isFullscreen = !currentFullscreenState)
        }
    }

    private fun toggleControlUiState() {
        viewModelScope.launch {
            if (!tvPlayerState.value.isControlUiVisible) {
                _tvPlayerState.update { current ->
                    current.copy(isControlUiVisible = true)
                }

                delay(UI_SHOW_DELAY)

                _tvPlayerState.update { current ->
                    current.copy(isControlUiVisible = false)
                }
            }
        }
    }

    private fun togglePlayingState() {
        _tvPlayerState.update { current ->
            val currentPlayingState = current.isPlaying
            current.copy(isPlaying = !currentPlayingState)
        }
    }

    private fun toggleVideoResizeMode() {
        _tvPlayerState.update { current ->
            val currentMode = current.videoResizeMode
            val nextMode = ResizeMode.toggleResizeMode(current = currentMode)
            current.copy(videoResizeMode = nextMode)
        }
    }

    private fun toggleVideoRatioMode() {
        _tvPlayerState.update { current ->
            val currentMode = current.videoRatioMode
            val nextMode = RatioMode.toggleRatioMode(current = currentMode)

            val nextRatio =
                if (nextMode == RatioMode.Original) {
                    _videoRatio
                } else {
                    nextMode.ratio
                }
            current.copy(
                videoRatioMode = nextMode,
                videoRatio = nextRatio,
            )
        }
    }

    private fun showVolumeUi() {
        _tvPlayerState.update { current ->
            current.copy(isVolumeUiVisible = true)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob =
            viewModelScope.launch {
                delay(hideVolumeAfterMs)
                hideVolumeUi()
            }
    }

    private fun hideVolumeUi() {
        _tvPlayerState.update { current ->
            current.copy(isVolumeUiVisible = false)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob = null
    }
}
