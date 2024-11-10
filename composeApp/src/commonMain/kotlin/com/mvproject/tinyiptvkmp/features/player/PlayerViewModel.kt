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
import com.mvproject.tinyiptvkmp.core.common.mvi.MVI
import com.mvproject.tinyiptvkmp.core.common.mvi.mvi
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
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiEffect
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiState
import com.mvproject.tinyiptvkmp.features.player.components.isMediaPlayable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    savedStateHandle: SavedStateHandle,
    private val preferenceRepository: PreferenceRepository,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    private val args = savedStateHandle.toRoute<AppRoutes.Player>()
    private val media = args.channelName
    private val group = args.group
    private val groupType = args.groupType

    private var pollVolumeJob: Job? = null

    // time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    init {
        Logger.d("testing VideoViewViewModel init media:$media, group:$group, groupType:$groupType")

        loadGroupChannels()

        initPlayBack(channelName = media)

        refreshGroupChannelsPrograms()
    }

    private fun loadGroupChannels() {
        viewModelScope.launch {
            val channelList = getGroupChannelsUseCase(group, groupType)

            updateUiState {
                copy(
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

            updateUiState {
                copy(
                    isFullscreen = isFullscreen,
                    videoResizeMode = resizeMode,
                    videoRatioMode = ratioMode,
                    videoRatio = ratioMode.ratio,
                )
            }

            val name = uiState.value.currentChannel.channelName.ifBlank { channelName }

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

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.SelectNext -> switchToNextChannel()
            UiAction.SelectPrevious -> switchToPreviousChannel()
            UiAction.ToggleChannelsUi -> toggleChannelsVisibility()
            UiAction.ToggleProgramsUi -> toggleEpgVisibility()
            UiAction.ToggleFullScreen -> toggleFullScreen()
            UiAction.ChangeVideoSize -> toggleVideoResizeMode()
            UiAction.ChangeVideoRatio -> toggleVideoRatioMode()
            UiAction.ToggleProgramInfoUi -> toggleChannelInfoVisibility()
            UiAction.ToggleChannelFavorite -> toggleChannelFavorite()
            UiAction.TogglePlayback -> togglePlayingState()
            UiAction.TogglePlayerUi -> toggleControlUiState()
            UiAction.VolumeDown -> decreaseVolume()
            UiAction.VolumeUp -> increaseVolume()
            is UiAction.SelectChannel -> switchToChannel(channel = uiAction.channel)
            is UiAction.OnVideoSizeChanged -> changeVideoRatio(ratio = uiAction.videoRatio)
            is UiAction.OnIsPlayingChanged -> changePlayingState(state = uiAction.state)
            is UiAction.OnPlaybackStateChanged -> changePlaybackState(state = uiAction.state)
            UiAction.NavigateBack -> viewModelScope.postUiEffect(UiEffect.OnNavigateBack)
        }
    }

    private fun changeVideoRatio(ratio: Float) {
        val videoRatio = when {
            uiState.value.videoRatioMode == RatioMode.Original -> ratio
            else -> uiState.value.videoRatioMode.ratio
        }

        updateUiState {
            copy(videoRatio = videoRatio)
        }
    }

    private fun changePlayingState(state: Boolean) {
        updateUiState {
            copy(isPlaying = state)
        }
    }

    private fun changePlaybackState(state: PlayerPlaybackState) {
        var isMediaPlayable = uiState.value.isMediaPlayable
        val isBuffering = state == PlayerPlaybackState.PlaybackBuffering

        when (state) {
            is PlayerPlaybackState.PlaybackIdle -> {
                isMediaPlayable = isMediaPlayable(state.errorCode)
            }

            PlayerPlaybackState.PlaybackReady -> {
                isMediaPlayable = true
            }

            else -> {
            }
        }

        updateUiState {
            copy(
                isMediaPlayable = isMediaPlayable,
                isBuffering = isBuffering,
            )
        }
    }

    private suspend fun loadSelectedChannelEpg() {
        val currentChannel = uiState.value.currentChannel
        if (currentChannel.programId.isNotBlank()) {
            val channelsEpgData = getChannelsEpgUseCase(channelId = currentChannel.programId)
            val currentChannelWithEpg = currentChannel.copy(programs = channelsEpgData)

            updateUiState {
                copy(currentChannel = currentChannelWithEpg)
            }

        }
    }

    private fun refreshGroupChannelsPrograms() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentChannels = uiState.value.groupChannels
            val channelsIds = currentChannels.mapProgramIds()
            if (channelsIds.isNotEmpty()) {
                val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)
                val channelsWithPrograms =
                    currentChannels.mapPrograms(channelEpgMap = channelsEpgData)

                updateUiState {
                    copy(groupChannels = channelsWithPrograms)
                }
            }
        }
    }

    private fun getCurrentMediaPosition(channelName: String): Int {
        val currentPos = uiState.value.channelIndex
        val groupChannels = uiState.value.groupChannels

        val targetPos = groupChannels.indexOfFirst { it.channelName == channelName }

        val mediaPosition = targetPos.coerceAtLeast(currentPos)

        return mediaPosition
    }

    private fun switchToNextChannel() {
        val currentChannelsCount = uiState.value.groupChannels.count()
        val nextIndex = uiState.value.channelIndex + INT_VALUE_1
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
        val currentChannelsCount = uiState.value.groupChannels.count()
        val nextIndex = uiState.value.channelIndex - INT_VALUE_1

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
            val targetVolume = uiState.value.currentVolume + FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtMost(FLOAT_VALUE_1)
            updateUiState {
                copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private fun decreaseVolume() {
        showVolumeUi()
        viewModelScope.launch {
            val targetVolume = uiState.value.currentVolume - FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtLeast(FLOAT_VALUE_ZERO)
            updateUiState {
                copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private suspend fun setCurrentChannel(channelIndex: Int) {
        val currentChannels = uiState.value.groupChannels
        val currentChannel = currentChannels[channelIndex]

        updateUiState {
            copy(
                channelIndex = channelIndex,
                currentChannel = currentChannel,
                isChannelsVisible = false,
            )
        }

        loadSelectedChannelEpg()
    }

    private fun toggleChannelFavorite() {
        val currentChannel = uiState.value.currentChannel

        // todo
        val updatedChannel =
            currentChannel.copy(
                // isInFavorites = !currentChannel.isInFavorites,
            )


        // val channelWithEpg = currentChannel.toggleFavorite(type = type)

        val updatedChannels = uiState.value.groupChannels
            .replaceUpdated(channel = updatedChannel)

        viewModelScope.launch {
            updateUiState {
                copy(
                    currentChannel = updatedChannel,
                    groupChannels = updatedChannels,
                )
            }

            toggleFavoriteChannelUseCase(channel = updatedChannel)
        }
    }

    private fun toggleEpgVisibility() {
        if (uiState.value.isFullscreen) {
            val currentEpgVisibleState = uiState.value.isEpgVisible
            updateUiState {
                copy(isEpgVisible = !currentEpgVisibleState)
            }
        }
    }

    private fun toggleChannelsVisibility() {
        val currentChannelsVisibleState = uiState.value.isChannelsVisible
        updateUiState {
            copy(isChannelsVisible = !currentChannelsVisibleState)
        }
    }

    private fun toggleChannelInfoVisibility() {
        val currentChannelInfoVisibleState = uiState.value.isChannelInfoVisible
        updateUiState {
            copy(isChannelInfoVisible = !currentChannelInfoVisibleState)
        }
    }

    private fun toggleFullScreen() {
        val currentFullscreenState = uiState.value.isFullscreen
        updateUiState {
            copy(isFullscreen = !currentFullscreenState)
        }
    }

    private fun toggleControlUiState() {
        viewModelScope.launch {
            if (!uiState.value.isControlUiVisible) {
                updateUiState {
                    copy(isControlUiVisible = true)
                }

                delay(UI_SHOW_DELAY)

                updateUiState {
                    copy(isControlUiVisible = false)
                }
            }
        }
    }

    private fun togglePlayingState() {
        val currentPlayingState = uiState.value.isPlaying
        updateUiState {
            copy(isPlaying = !currentPlayingState)
        }
    }

    private fun toggleVideoResizeMode() {
        val currentMode = uiState.value.videoResizeMode
        val nextMode = ResizeMode.toggleResizeMode(current = currentMode)
        updateUiState {
            copy(videoResizeMode = nextMode)
        }
    }

    private fun toggleVideoRatioMode() {
        val currentMode = uiState.value.videoRatioMode
        val nextMode = RatioMode.toggleRatioMode(current = currentMode)

        val nextRatio =
            if (nextMode == RatioMode.Original) {
                uiState.value.videoRatio
            } else {
                nextMode.ratio
            }

        updateUiState {
            copy(
                videoRatioMode = nextMode,
                videoRatio = nextRatio
            )
        }
    }

    private fun showVolumeUi() {
        updateUiState {
            copy(isVolumeUiVisible = true)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob =
            viewModelScope.launch {
                delay(hideVolumeAfterMs)
                hideVolumeUi()
            }
    }

    private fun hideVolumeUi() {
        updateUiState {
            copy(isVolumeUiVisible = false)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob = null
    }
}
