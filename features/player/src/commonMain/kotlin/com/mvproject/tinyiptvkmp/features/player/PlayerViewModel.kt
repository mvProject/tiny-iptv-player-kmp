/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 14:06
 *
 */

package com.mvproject.tinyiptvkmp.features.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.foundation.common.DELAY_50
import com.mvproject.tinyiptvkmp.core.foundation.common.DELAY_500
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_STEP_VOLUME
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.common.UI_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.foundation.common.VOLUME_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapProgramIds
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.replaceUpdated
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.withPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.features.player.PlayerState.PlayerOSD
import com.mvproject.tinyiptvkmp.features.player.nav.PlayerNavigator
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import com.mvproject.tinyiptvkmp.platform.mediaplayer.isMediaPlayable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    @InjectedParam args: PlayerArgs,
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
) : ViewModel(),
    KoinComponent,
    MviCore<PlayerState, PlayerAction, PlayerUiEffect> by mviCore(PlayerState()) {

    private val media = args.channelName
    private val group = args.group
    private val groupType = args.groupType

    private val navigator: PlayerNavigator by inject()

    private var pollVolumeJob: Job? = null

    private val logger by injectLogger()

    // time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    init {
        logger.d { "testing VideoViewViewModel init media:$media, group:$group, groupType:$groupType" }

        viewModelScope.launch {
            loadGroupChannels()

            initPlayBack(channelName = media)
        }

        refreshGroupChannelsPrograms()
    }

    private suspend fun loadGroupChannels() {
        val channelList = getGroupChannelsUseCase(group, groupType)

        updateUiState {
            copy(
                channelGroup = group,
                groupChannels = channelList.withPrograms()
            )
        }
    }

    private suspend fun initPlayBack(channelName: String) {
        val preferences = preferencesStore.data.first()
        val videoSize = VideoSize.entries[preferences.defaultVideoSizeMode]
        val isFullscreen = preferences.defaultFullscreenMode

        updateUiState {
            copy(
                isFullscreen = isFullscreen,
                videoSize = videoSize,
            )
        }

        val name = uiState.value.currentChannel.channelName.ifBlank { channelName }

        val currentItemPosition = getCurrentMediaPosition(channelName = name)

        setCurrentChannel(channelIndex = currentItemPosition)
    }

    private fun switchToChannel(channel: TvChannelWithPrograms) {
        viewModelScope.launch {
            val newMediaPosition = getCurrentMediaPosition(channelName = channel.channelName)

            setCurrentChannel(channelIndex = newMediaPosition)
        }
    }

    override fun onAction(uiAction: PlayerAction) {
        when (uiAction) {
            PlayerAction.SelectNext -> switchToNextChannel()
            PlayerAction.SelectPrevious -> switchToPreviousChannel()
            PlayerAction.ToggleFullScreen -> toggleFullScreen()
            PlayerAction.ChangeVideoSize -> toggleVideoSizeMode()
            PlayerAction.TogglePlayback -> togglePlayingState()
            PlayerAction.TogglePlayer -> toggleControlUiState()
            PlayerAction.VolumeDown -> decreaseVolume()
            PlayerAction.VolumeUp -> increaseVolume()
            is PlayerAction.SelectChannel -> switchToChannel(channel = uiAction.channel)
            is PlayerAction.OnIsPlayingChanged -> changePlayingState(state = uiAction.state)
            is PlayerAction.OnPlaybackStateChanged -> changePlaybackState(state = uiAction.state)
            PlayerAction.NavigateBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }

            is PlayerAction.OpenOsd -> openOsd(type = uiAction.type)
            PlayerAction.CloseOsd -> closeOsd()
            is PlayerAction.UpdateFavorite -> toggleChannelFavorite(type = uiAction.type)
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

    private fun changePlayingState(state: Boolean) {
        updateUiState {
            copy(isPlaying = state)
        }
    }

    private fun changePlaybackState(state: PlayerState.PlayerPlaybackState) {
        var isMediaPlayable = uiState.value.isMediaPlayable
        val isBuffering = state == PlayerState.PlayerPlaybackState.PlaybackBuffering

        when (state) {
            is PlayerState.PlayerPlaybackState.PlaybackIdle -> {
                isMediaPlayable = isMediaPlayable(state.errorCode)
            }

            PlayerState.PlayerPlaybackState.PlaybackReady -> {
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
        viewModelScope.launch {
            delay(DELAY_500.milliseconds)
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

        return if (targetPos >= INT_VALUE_ZERO) {
            targetPos
        } else {
            currentPos.coerceAtLeast(INT_VALUE_ZERO)
        }
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
            delay(DELAY_50.milliseconds)
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
            delay(DELAY_50.milliseconds)
        }
    }

    private suspend fun setCurrentChannel(channelIndex: Int) {
        val currentChannels = uiState.value.groupChannels
        val currentChannel = currentChannels.getOrNull(channelIndex) ?: return

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

        if (currentChannel.favoriteType != type.name) {

            val updatedChannel = currentChannel.copy(
                channel = currentChannel.channel.copy(favoriteType = type.name),
            )

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
                toggleFavoriteChannelUseCase(channel = currentChannel.channel, type = type.name)
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

                delay(UI_SHOW_DELAY.milliseconds)

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

    private fun toggleVideoSizeMode() {
        val currentMode = uiState.value.videoSize
        val nextMode = VideoSize.toggleVideoSize(current = currentMode)

        updateUiState { copy(videoSize = nextMode) }
    }

    private fun showVolumeUi() {
        updateUiState {
            copy(isVolumeUiVisible = true)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob =
            viewModelScope.launch {
                delay(hideVolumeAfterMs.milliseconds)
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


