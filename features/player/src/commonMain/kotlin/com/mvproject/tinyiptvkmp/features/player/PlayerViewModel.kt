/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 14:06
 *
 */

package com.mvproject.tinyiptvkmp.features.player

import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
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
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.ObservePlayerSettingsUseCase
import com.mvproject.tinyiptvkmp.features.player.nav.PlayerNavigator
import com.mvproject.tinyiptvkmp.platform.mediaplayer.isMediaPlayable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    @InjectedParam args: PlayerArgs,
    private val observePlayerSettings: ObservePlayerSettingsUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
) : MviViewModel<PlayerState, PlayerAction, PlayerEffect>() {

    private val media = args.channelName
    private val group = args.group
    private val groupType = args.groupType
    private val playlistId = args.playlistId

    private val navigator: PlayerNavigator by inject()

    private var pollVolumeJob: Job? = null

    // time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    override fun createStore() = createStore(
        initialState = PlayerState(),
        invokeOnStart = {
            loadGroupChannels()
            initPlayBack(channelName = media)
            refreshGroupChannelsPrograms()
        },
    )

    override fun onIntent(intent: PlayerAction) {
        when (intent) {
            PlayerAction.ChangeVideoSize -> toggleVideoSizeMode()
            PlayerAction.CloseOsd -> closeOsd()
            PlayerAction.NavigateBack -> launch { navigator.navigateUp() }
            is PlayerAction.OnIsPlayingChanged -> changePlayingState(state = intent.state)
            is PlayerAction.OnPlaybackStateChanged -> changePlaybackState(playerState = intent.state)
            is PlayerAction.OpenOsd -> openOsd(type = intent.type)
            is PlayerAction.SelectChannel -> launch { switchToChannel(channel = intent.channel) }
            PlayerAction.SelectNext -> launch { switchToNextChannel() }
            PlayerAction.SelectPrevious -> launch { switchToPreviousChannel() }
            PlayerAction.ToggleFullScreen -> toggleFullScreen()
            PlayerAction.TogglePlayback -> togglePlayingState()
            PlayerAction.TogglePlayer -> launch { toggleControlUiState() }
            is PlayerAction.UpdateFavorite -> launch { toggleChannelFavorite(type = intent.type) }
            PlayerAction.VolumeDown -> launch { decreaseVolume() }
            PlayerAction.VolumeUp -> launch { increaseVolume() }
        }
    }


    init {
        logger.d { "testing VideoViewViewModel init media:$media, group:$group, groupType:$groupType" }
        /*
                viewModelScope.launch {
                    loadGroupChannels()

                    initPlayBack(channelName = media)
                }

                refreshGroupChannelsPrograms()*/
    }

    private suspend fun loadGroupChannels() {
        val channelList = getGroupChannelsUseCase(
            playlistId = playlistId,
            group = group,
            groupType = groupType,
        )

        setState {
            copy(
                channelGroup = group,
                groupChannels = channelList.withPrograms()
            )
        }
    }

    private suspend fun loadPlayerSettings() {
        observePlayerSettings()
            .distinctUntilChanged()
            .collect { settings ->
                val videoSize = VideoSize.entries[settings.videoSize]
                val isFullscreen = settings.isFullscreenEnabled

                setState {
                    copy(
                        isFullscreen = isFullscreen,
                        videoSize = videoSize,
                    )
                }
            }
    }


    private suspend fun initPlayBack(channelName: String) {
        val settings = observePlayerSettings().first()
        val videoSize = VideoSize.entries[settings.videoSize]
        val isFullscreen = settings.isFullscreenEnabled

        setState {
            copy(
                isFullscreen = isFullscreen,
                videoSize = videoSize,
            )
        }

        val name = state.value.currentChannel.channelName.ifBlank { channelName }

        val currentItemPosition = getCurrentMediaPosition(channelName = name)

        setCurrentChannel(channelIndex = currentItemPosition)
    }

    private suspend fun switchToChannel(channel: TvChannelWithPrograms) {
        val newMediaPosition = getCurrentMediaPosition(channelName = channel.channelName)
        setCurrentChannel(channelIndex = newMediaPosition)
    }

    private fun openOsd(type: PlayerOSD) {
        if (type == PlayerOSD.ChannelPrograms && !state.value.isFullscreen) {
            return
        }
        setState { copy(osdType = type) }
    }

    private fun closeOsd() {
        setState { copy(osdType = null) }
    }

    private fun changePlayingState(state: Boolean) {
        setState { copy(isPlaying = state) }
    }

    private fun changePlaybackState(playerState: PlayerState.PlayerPlaybackState) {
        var isMediaPlayable = state.value.isMediaPlayable
        val isBuffering = playerState == PlayerState.PlayerPlaybackState.PlaybackBuffering

        when (playerState) {
            is PlayerState.PlayerPlaybackState.PlaybackIdle -> {
                isMediaPlayable = isMediaPlayable(playerState.errorCode)
            }

            PlayerState.PlayerPlaybackState.PlaybackReady -> {
                isMediaPlayable = true
            }

            else -> {
            }
        }

        setState {
            copy(
                isMediaPlayable = isMediaPlayable,
                isBuffering = isBuffering,
            )
        }
    }

    private suspend fun loadSelectedChannelEpg() {
        val currentChannel = state.value.currentChannel
        if (currentChannel.programId.isNotBlank()) {
            val channelsEpgData = getChannelsEpgUseCase(channelId = currentChannel.programId)
            val currentChannelWithEpg = currentChannel.copy(programs = channelsEpgData)

            setState { copy(currentChannel = currentChannelWithEpg) }
        }
    }

    private suspend fun refreshGroupChannelsPrograms() {
        delay(DELAY_500.milliseconds)
        val currentChannels = state.value.groupChannels
        val channelsIds = currentChannels.mapProgramIds()

        if (channelsIds.isNotEmpty()) {
            val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)
            val channelsWithPrograms =
                currentChannels.mapPrograms(channelEpgMap = channelsEpgData)

            setState { copy(groupChannels = channelsWithPrograms) }
        }
    }

    private fun getCurrentMediaPosition(channelName: String): Int {
        val currentPos = state.value.channelIndex
        val groupChannels = state.value.groupChannels

        val targetPos = groupChannels.indexOfFirst { it.channelName == channelName }

        return if (targetPos >= INT_VALUE_ZERO) {
            targetPos
        } else {
            currentPos.coerceAtLeast(INT_VALUE_ZERO)
        }
    }

    private suspend fun switchToNextChannel() {
        val currentChannelsCount = state.value.groupChannels.count()
        val nextIndex = state.value.channelIndex + INT_VALUE_1
        val newMediaPosition =
            if (nextIndex > currentChannelsCount - INT_VALUE_1) {
                INT_VALUE_ZERO
            } else {
                nextIndex
            }

        setCurrentChannel(channelIndex = newMediaPosition)
    }

    private suspend fun switchToPreviousChannel() {
        val currentChannelsCount = state.value.groupChannels.count()
        val nextIndex = state.value.channelIndex - INT_VALUE_1

        val newMediaPosition =
            if (nextIndex < INT_VALUE_ZERO) {
                currentChannelsCount - INT_VALUE_1
            } else {
                nextIndex
            }

        setCurrentChannel(channelIndex = newMediaPosition)

    }

    private suspend fun increaseVolume() {
        showVolumeUi()
        val targetVolume = state.value.currentVolume + FLOAT_STEP_VOLUME
        val nextVolume = targetVolume.coerceAtMost(FLOAT_VALUE_1)
        setState { copy(currentVolume = nextVolume) }
        delay(DELAY_50.milliseconds)

    }

    private suspend fun decreaseVolume() {
        showVolumeUi()
        val targetVolume = state.value.currentVolume - FLOAT_STEP_VOLUME
        val nextVolume = targetVolume.coerceAtLeast(FLOAT_VALUE_ZERO)
        setState { copy(currentVolume = nextVolume) }
        delay(DELAY_50.milliseconds)
    }

    private suspend fun setCurrentChannel(channelIndex: Int) {
        val currentChannels = state.value.groupChannels
        val currentChannel = currentChannels.getOrNull(channelIndex) ?: return

        setState {
            copy(
                channelIndex = channelIndex,
                currentChannel = currentChannel,
                osdType = null
            )
        }

        loadSelectedChannelEpg()
    }

    private suspend fun toggleChannelFavorite(type: FavoriteType) {
        val currentChannel = state.value.currentChannel

        if (currentChannel.favoriteType != type.name) {

            val updatedChannel = currentChannel.copy(
                channel = currentChannel.channel.copy(favoriteType = type.name),
            )

            val updatedChannels = state.value.groupChannels
                .replaceUpdated(channel = updatedChannel)


            setState {
                copy(
                    currentChannel = updatedChannel,
                    groupChannels = updatedChannels,
                    osdType = null
                )
            }
            toggleFavoriteChannelUseCase(
                playlistId = playlistId,
                channel = currentChannel.channel,
                type = type.name,
            )

        }
    }

    private fun toggleFullScreen() {
        val currentFullscreenState = state.value.isFullscreen
        setState { copy(isFullscreen = !currentFullscreenState) }
    }

    private suspend fun toggleControlUiState() {
        if (!state.value.isControlUiVisible) {
            setState { copy(isControlUiVisible = true) }

            delay(UI_SHOW_DELAY.milliseconds)

            setState { copy(isControlUiVisible = false) }
        }
    }

    private fun togglePlayingState() {
        val currentPlayingState = state.value.isPlaying
        setState { copy(isPlaying = !currentPlayingState) }
    }

    private fun toggleVideoSizeMode() {
        val currentMode = state.value.videoSize
        val nextMode = VideoSize.toggleVideoSize(current = currentMode)

        setState { copy(videoSize = nextMode) }
    }

    private fun showVolumeUi() {
        setState { copy(isVolumeUiVisible = true) }
        pollVolumeJob?.cancel()
        pollVolumeJob =
            viewModelScope.launch {
                delay(hideVolumeAfterMs.milliseconds)
                hideVolumeUi()
            }
    }

    private fun hideVolumeUi() {
        setState { copy(isVolumeUiVisible = false) }
        pollVolumeJob?.cancel()
        pollVolumeJob = null
    }
}
