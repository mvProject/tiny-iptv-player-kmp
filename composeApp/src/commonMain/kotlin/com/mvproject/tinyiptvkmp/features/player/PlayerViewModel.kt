/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 14:06
 *
 */

package com.mvproject.tinyiptvkmp.features.player

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.AppConstants.DELAY_50
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_STEP_VOLUME
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.UI_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.common.AppConstants.VOLUME_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.common.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.common.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
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
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState.PlayerOSD
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
) : ViewModel(),
    MviCore<PlayerUiState, PlayerUiAction, PlayerUiEffect> by mviCore(PlayerUiState()) {

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

    override fun onAction(uiAction: PlayerUiAction) {
        when (uiAction) {
            PlayerUiAction.SelectNext -> switchToNextChannel()
            PlayerUiAction.SelectPrevious -> switchToPreviousChannel()
            PlayerUiAction.ToggleFullScreen -> toggleFullScreen()
            PlayerUiAction.ChangeVideoSize -> toggleVideoResizeMode()
            PlayerUiAction.ChangeVideoRatio -> toggleVideoRatioMode()
            PlayerUiAction.TogglePlayback -> togglePlayingState()
            PlayerUiAction.TogglePlayerUi -> toggleControlUiState()
            PlayerUiAction.VolumeDown -> decreaseVolume()
            PlayerUiAction.VolumeUp -> increaseVolume()
            is PlayerUiAction.SelectChannel -> switchToChannel(channel = uiAction.channel)
            is PlayerUiAction.OnVideoSizeChanged -> changeVideoRatio(ratio = uiAction.videoRatio)
            is PlayerUiAction.OnIsPlayingChanged -> changePlayingState(state = uiAction.state)
            is PlayerUiAction.OnPlaybackStateChanged -> changePlaybackState(state = uiAction.state)
            PlayerUiAction.NavigateBack -> viewModelScope.postUiEffect(PlayerUiEffect.OnNavigateBack)
            is PlayerUiAction.OpenOsd -> openOsd(type = uiAction.type)
            PlayerUiAction.CloseOsd -> closeOsd()
            is PlayerUiAction.UpdateFavorite -> toggleChannelFavorite(type = uiAction.type)
        }
    }

    private fun openOsd(type: PlayerOSD) {
        if (type == PlayerOSD.ChannelPrograms && !uiState.value.isFullscreen) {
            return
        }
        updateUiState {
            copy(osdType = type)
        }
    }

    private fun closeOsd() {
        updateUiState {
            copy(osdType = null)
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

    private fun changePlaybackState(state: PlayerUiState.PlayerPlaybackState) {
        var isMediaPlayable = uiState.value.isMediaPlayable
        val isBuffering = state == PlayerUiState.PlayerPlaybackState.PlaybackBuffering

        when (state) {
            is PlayerUiState.PlayerPlaybackState.PlaybackIdle -> {
                isMediaPlayable = isMediaPlayable(state.errorCode)
            }

            PlayerUiState.PlayerPlaybackState.PlaybackReady -> {
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
                osdType = null
            )
        }

        loadSelectedChannelEpg()
    }

    private fun toggleChannelFavorite(type: FavoriteType) {
        val currentChannel = uiState.value.currentChannel

        if (currentChannel.favoriteType != type) {

            val updatedChannel = currentChannel.copy(favoriteType = type)

            val updatedChannels = uiState.value.groupChannels
                .replaceUpdated(channel = updatedChannel)

            viewModelScope.launch {
                updateUiState {
                    copy(
                        currentChannel = updatedChannel,
                        groupChannels = updatedChannels,
                        osdType = null
                    )
                }
                toggleFavoriteChannelUseCase(channel = currentChannel, type = type)
            }
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

@Immutable
data class PlayerUiState(
    val channelGroup: String = String.empty,
    val currentChannel: TvChannel = TvChannel(),
    val isControlUiVisible: Boolean = false,
    val isVolumeUiVisible: Boolean = false,
    val isFullscreen: Boolean = false,
    val isPlaying: Boolean = false,
    val currentVolume: Float = 0.5f,
    val isBuffering: Boolean = false,
    val isMediaPlayable: Boolean = true,
    val isOnline: Boolean = true,
    val videoRatioMode: RatioMode = RatioMode.WideScreen,
    val videoRatio: Float = RatioMode.WideScreen.ratio,
    val videoResizeMode: ResizeMode = ResizeMode.Fit,
    val channelIndex: Int = AppConstants.INT_NO_VALUE,
    val groupChannels: List<TvChannel> = emptyList(),
    val osdType: PlayerOSD? = null,
) {
    sealed interface PlayerPlaybackState {
        data object PlaybackReady : PlayerPlaybackState
        data object PlaybackEnded : PlayerPlaybackState
        data object PlaybackBuffering : PlayerPlaybackState
        data class PlaybackIdle(val errorCode: Int?) : PlayerPlaybackState
    }

    sealed interface PlayerOSD {
        data object GroupChannels : PlayerOSD
        data object ChannelPrograms : PlayerOSD
        data object ProgramInfo : PlayerOSD
        data object ChannelFavorites : PlayerOSD
    }
}

sealed interface PlayerUiAction {
    data object NavigateBack : PlayerUiAction
    data object TogglePlayback : PlayerUiAction
    data object ChangeVideoSize : PlayerUiAction
    data object ChangeVideoRatio : PlayerUiAction
    data object ToggleFullScreen : PlayerUiAction
    data object TogglePlayerUi : PlayerUiAction
    data object SelectNext : PlayerUiAction
    data object SelectPrevious : PlayerUiAction
    data object VolumeUp : PlayerUiAction
    data object VolumeDown : PlayerUiAction

    data class SelectChannel(val channel: TvChannel) : PlayerUiAction
    data class OnVideoSizeChanged(val height: Int, val width: Int, val videoRatio: Float) :
        PlayerUiAction

    data class OnIsPlayingChanged(val state: Boolean) : PlayerUiAction
    data class OnPlaybackStateChanged(val state: PlayerUiState.PlayerPlaybackState) : PlayerUiAction

    data class UpdateFavorite(val type: FavoriteType) : PlayerUiAction
    data class OpenOsd(val type: PlayerOSD) : PlayerUiAction
    data object CloseOsd : PlayerUiAction
}

sealed interface PlayerUiEffect {
    data object OnNavigateBack : PlayerUiEffect
}
