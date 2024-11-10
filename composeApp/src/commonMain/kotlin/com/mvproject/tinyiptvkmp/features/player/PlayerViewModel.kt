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
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.UI_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.common.AppConstants.VOLUME_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelsUtils.mapProgramIds
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelsUtils.mapPrograms
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelsUtils.replaceUpdated
import com.mvproject.tinyiptvkmp.features.player.action.UiActions
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

    private val args = savedStateHandle.toRoute<AppRoutes.Player>()
    private val media = args.channelName
    private val group = args.group
    private val groupType = args.groupType

    init {
        Logger.d("testing VideoViewViewModel init media:$media, group:$group, groupType:$groupType")

        loadGroupChannels()

        initPlayBack(channelName = media)

        refreshGroupChannelsPrograms()
    }

    private fun loadGroupChannels() {
        viewModelScope.launch {
            val channelList = getGroupChannelsUseCase(group, groupType)
            _tvPlayerState.update { current ->
                current.copy(
                    channelGroup = group,
                    groupChannels = channelList
                )
            }
        }
    }

    private fun initPlayBack(channelName: String) {
        viewModelScope.launch {
            val ratioMode = RatioMode.entries[preferenceRepository.getDefaultRatioMode()]
            val resizeMode = ResizeMode.entries[preferenceRepository.getDefaultResizeMode()]
            val isFullscreen = preferenceRepository.getDefaultFullscreenMode()

            _tvPlayerState.update { current ->
                current.copy(
                    isFullscreen = isFullscreen,
                    videoResizeMode = resizeMode,
                    videoRatioMode = ratioMode,
                    videoRatio = ratioMode.ratio,
                )
            }

            val name = tvPlayerState.value.currentChannel.channelName.ifBlank { channelName }

            val currentItemPosition = getCurrentMediaPosition(channelName = name)

            setCurrentChannel(channelIndex = currentItemPosition)
        }
    }

    private fun switchToChannel(channel: TvChannel) {
        viewModelScope.launch {
            val newMediaPosition = getCurrentMediaPosition(channelName = channel.channelName)

            setCurrentChannel(channelIndex = newMediaPosition)
        }
    }

    fun processPlaybackActions(action: UiActions) {
        when (action) {
            UiActions.SelectNext -> switchToNextChannel()
            UiActions.SelectPrevious -> switchToPreviousChannel()
            UiActions.ToggleChannelsUi -> toggleChannelsVisibility()
            UiActions.ToggleProgramsUi -> toggleEpgVisibility()
            UiActions.ToggleFullScreen -> toggleFullScreen()
            UiActions.ChangeVideoSize -> toggleVideoResizeMode()
            UiActions.ChangeVideoRatio -> toggleVideoRatioMode()
            UiActions.ToggleProgramInfoUi -> toggleChannelInfoVisibility()
            UiActions.ToggleChannelFavorite -> toggleChannelFavorite()
            UiActions.TogglePlayback -> togglePlayingState()
            UiActions.TogglePlayerUi -> toggleControlUiState()
            UiActions.VolumeDown -> decreaseVolume()
            UiActions.VolumeUp -> increaseVolume()
            is UiActions.SelectChannel -> switchToChannel(channel = action.channel)
            is UiActions.OnVideoSizeChanged -> changeVideoRatio(ratio = action.videoRatio)
            is UiActions.OnIsPlayingChanged -> changePlayingState(state = action.state)
            is UiActions.OnPlaybackStateChanged -> changePlaybackState(state = action.state)
        }
    }

    private fun changeVideoRatio(ratio: Float) {
        val videoRatio = when {
            tvPlayerState.value.videoRatioMode == RatioMode.Original -> ratio
            else -> tvPlayerState.value.videoRatioMode.ratio
        }

        _tvPlayerState.update { current ->
            current.copy(videoRatio = videoRatio)
        }
    }

    private fun changePlayingState(state: Boolean) {
        _tvPlayerState.update { current ->
            current.copy(isPlaying = state)
        }
    }

    private fun changePlaybackState(state: PlaybackState) {
        var isMediaPlayable = tvPlayerState.value.isMediaPlayable
        val isBuffering = state == PlaybackState.PlaybackBuffering

        when (state) {
            is PlaybackState.PlaybackIdle -> {
                isMediaPlayable = isMediaPlayable(state.errorCode)
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

    private suspend fun loadSelectedChannelEpg() {
        val currentChannel = tvPlayerState.value.currentChannel
        if (currentChannel.programId.isNotBlank()) {
            val channelsEpgData = getChannelsEpgUseCase(channelId = currentChannel.programId)
            val currentChannelWithEpg = currentChannel.copy(programs = channelsEpgData)

            _tvPlayerState.update { state ->
                state.copy(currentChannel = currentChannelWithEpg)
            }

        }
    }

    private fun refreshGroupChannelsPrograms() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentChannels = tvPlayerState.value.groupChannels
            val channelsIds = currentChannels.mapProgramIds()
            if (channelsIds.isNotEmpty()) {
                val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)
                val channelsWithPrograms =
                    currentChannels.mapPrograms(channelEpgMap = channelsEpgData)
                _tvPlayerState.update { current ->
                    current.copy(groupChannels = channelsWithPrograms)
                }
            }
        }
    }

    private fun getCurrentMediaPosition(channelName: String): Int {
        val currentPos = tvPlayerState.value.channelIndex
        val groupChannels = tvPlayerState.value.groupChannels

        val targetPos = groupChannels.indexOfFirst { it.channelName == channelName }

        val mediaPosition = targetPos.coerceAtLeast(currentPos)

        return mediaPosition
    }

    private fun switchToNextChannel() {
        val currentChannelsCount = tvPlayerState.value.groupChannels.count()
        val nextIndex = tvPlayerState.value.channelIndex + INT_VALUE_1
        val newMediaPosition =
            if (nextIndex > currentChannelsCount - INT_VALUE_1) {
                INT_VALUE_ZERO
            } else {
                nextIndex
            }

        viewModelScope.launch {
            setCurrentChannel(channelIndex = newMediaPosition)
        }
    }

    private fun switchToPreviousChannel() {
        val currentChannelsCount = tvPlayerState.value.groupChannels.count()
        val nextIndex = tvPlayerState.value.channelIndex - INT_VALUE_1

        val newMediaPosition =
            if (nextIndex < INT_VALUE_ZERO) {
                currentChannelsCount - INT_VALUE_1
            } else {
                nextIndex
            }

        viewModelScope.launch {
            setCurrentChannel(channelIndex = newMediaPosition)
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

    private suspend fun setCurrentChannel(channelIndex: Int) {
        val currentChannels = tvPlayerState.value.groupChannels
        val currentChannel = currentChannels[channelIndex]

        _tvPlayerState.update { current ->
            current.copy(
                channelIndex = channelIndex,
                currentChannel = currentChannel,
                isChannelsVisible = false,
            )
        }

        loadSelectedChannelEpg()
    }

    private fun toggleChannelFavorite() {
        val currentChannel = tvPlayerState.value.currentChannel

        // todo
        val updatedChannel =
            currentChannel.copy(
                // isInFavorites = !currentChannel.isInFavorites,
            )


        // val channelWithEpg = currentChannel.toggleFavorite(type = type)

        val updatedChannels = tvPlayerState.value.groupChannels
            .replaceUpdated(channel = updatedChannel)

        viewModelScope.launch {
            _tvPlayerState.update { current ->
                current.copy(
                    currentChannel = updatedChannel,
                    groupChannels = updatedChannels,
                )
            }

            toggleFavoriteChannelUseCase(channel = updatedChannel)
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
                    tvPlayerState.value.videoRatio
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
