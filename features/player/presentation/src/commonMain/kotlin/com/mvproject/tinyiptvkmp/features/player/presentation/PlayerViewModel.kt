/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 14:06
 *
 */

package com.mvproject.tinyiptvkmp.features.player.presentation

import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.core.foundation.common.DELAY_50
import com.mvproject.tinyiptvkmp.core.foundation.common.DELAY_500
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_STEP_VOLUME
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.common.UI_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.foundation.common.VOLUME_SHOW_DELAY
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.withPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapProgramIds
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.replaceUpdated
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.toggleFavorite
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.withPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelGroupSelection
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.features.player.api.domain.usecase.GetPlayerSettingsUseCase
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerState.PlayerOSD
import com.mvproject.tinyiptvkmp.features.player.presentation.nav.PlayerNavigator
import com.mvproject.tinyiptvkmp.platform.mediaplayer.isMediaPlayable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    @InjectedParam args: PlayerArgs,
    private val getPlayerSettings: GetPlayerSettingsUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
) : MviViewModel<PlayerState, PlayerAction, PlayerEffect>() {

    private val media = args.channelName
    private val mediaUrl = args.channelUrl
    private val group = args.group
    private val groupType = args.groupType
    private val selection = ChannelGroupSelection.fromRoute(
        group = args.group,
        groupType = args.groupType,
    )
    private val playlistId = args.playlistId

    private val navigator: PlayerNavigator by inject()

    private var pollVolumeJob: Job? = null

    // time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    override fun createStore() = createStore(
        initialState = PlayerState(),
        invokeOnStart = {
            loadPlayerSettings()
            loadInitialSelectedChannel()
            scheduleFullGroupChannelsLoad()
        },
    )

    override fun onIntent(intent: PlayerAction) {
        when (intent) {
            PlayerAction.ChangeVideoSize -> toggleVideoSizeMode()
            PlayerAction.CloseOsd -> closeOsd()
            PlayerAction.NavigateBack -> launch { navigator.navigateUp() }
            is PlayerAction.OnIsPlayingChanged -> changePlayingState(state = intent.state)
            is PlayerAction.OnPlaybackStateChanged -> changePlaybackState(playerState = intent.state)
            is PlayerAction.OpenOsd -> launch { openOsd(type = intent.type) }
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
        logger.d { "VideoViewViewModel init groupType=$groupType" }
    }

    private suspend fun loadPlayerSettings() {
        val settings = getPlayerSettings()
        val videoSize = settings.videoSize
        val isFullscreen = settings.isFullscreenEnabled
        setState {
            copy(
                isFullscreen = isFullscreen,
                videoSize = videoSize,
            )
        }
    }

    private fun loadInitialSelectedChannel() {
        val selectedChannel =
            TvChannel(
                channelName = media,
                channelUrl = mediaUrl,
            ).withPrograms()

        setState {
            copy(
                channelGroup = group,
                channelIndex = INT_NO_VALUE,
                currentChannel = selectedChannel,
                groupChannels = listOf(selectedChannel),
            )
        }
    }

    private fun scheduleFullGroupChannelsLoad() {
        launch {
            delay(DELAY_500.milliseconds)
            loadFullGroupChannelsAndRefresh()
        }
    }

    private suspend fun switchToChannel(channel: TvChannelWithPrograms) {
        val newMediaPosition = state.value.groupChannels.indexOfFirst {
            it.channelUrl == channel.channelUrl
        }
        if (newMediaPosition >= INT_VALUE_ZERO) {
            setCurrentChannel(
                localChannelIndex = newMediaPosition,
                absoluteChannelIndex = newMediaPosition,
            )
        }
    }

    private suspend fun openOsd(type: PlayerOSD) {
        if (type == PlayerOSD.ChannelPrograms && !state.value.isFullscreen) {
            return
        }
        if (type == PlayerOSD.GroupChannels && state.value.channelIndex < INT_VALUE_ZERO) {
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
        val currentChannels = state.value.groupChannels
        val channelsIds = currentChannels.mapProgramIds()

        if (channelsIds.isNotEmpty()) {
            val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)
            val channelsWithPrograms =
                currentChannels.mapPrograms(channelEpgMap = channelsEpgData)

            setState { copy(groupChannels = channelsWithPrograms) }
        }
    }

    private suspend fun switchToNextChannel() {
        val nextIndex = state.value.channelIndex + INT_VALUE_1
        if (state.value.channelIndex >= INT_VALUE_ZERO) {
            setCurrentChannel(
                localChannelIndex = nextIndex,
                absoluteChannelIndex = nextIndex,
            )
        }
    }

    private suspend fun switchToPreviousChannel() {
        val previousIndex = state.value.channelIndex - INT_VALUE_1
        if (state.value.channelIndex >= INT_VALUE_ZERO) {
            setCurrentChannel(
                localChannelIndex = previousIndex,
                absoluteChannelIndex = previousIndex,
            )
        }
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

    private suspend fun setCurrentChannel(
        localChannelIndex: Int,
        absoluteChannelIndex: Int,
    ) {
        val currentChannels = state.value.groupChannels
        val currentChannel = currentChannels.getOrNull(localChannelIndex) ?: return

        setState {
            copy(
                channelIndex = absoluteChannelIndex,
                currentChannel = currentChannel,
                osdType = null
            )
        }

        loadSelectedChannelEpg()
    }

    private suspend fun loadFullGroupChannelsAndRefresh() {
        val loadedChannels = mutableListOf<TvChannelWithPrograms>()
        var nextOffset = INT_VALUE_ZERO
        var hasMore: Boolean
        do {
            val page =
                getGroupChannelsUseCase(
                    playlistId = playlistId,
                    selection = selection,
                    offset = nextOffset,
                    limit = GetGroupChannelsUseCase.DEFAULT_PAGE_SIZE,
                )
            loadedChannels += page.channels.withPrograms()
            nextOffset = page.nextOffset
            hasMore = page.hasMore
        } while (hasMore)

        if (loadedChannels.isEmpty()) return

        val currentState = state.value
        val currentChannelIndex =
            loadedChannels
                .indexOfFirst { channel -> channel.channelUrl == currentState.currentChannel.channelUrl }
                .takeIf { index -> index >= INT_VALUE_ZERO }
                ?: INT_VALUE_ZERO
        val currentChannel =
            loadedChannels[currentChannelIndex].let { channel ->
                if (channel.channelUrl == currentState.currentChannel.channelUrl) {
                    channel.copy(programs = currentState.currentChannel.programs)
                } else {
                    channel
                }
            }
        loadedChannels[currentChannelIndex] = currentChannel

        setState {
            copy(
                channelIndex = currentChannelIndex,
                currentChannel = currentChannel,
                groupChannels = loadedChannels,
            )
        }
        loadSelectedChannelEpg()
        refreshGroupChannelsPrograms()
    }

    private suspend fun toggleChannelFavorite(type: FavoriteType) {
        val currentChannel = state.value.currentChannel

        val updatedChannel = currentChannel.toggleFavorite(type = type)

        val updatedChannels = state.value.groupChannels
            .replaceUpdated(channel = updatedChannel)


        setState {
            copy(
                currentChannel = updatedChannel,
                groupChannels = updatedChannels,
                osdType = null,
            )
        }
        toggleFavoriteChannelUseCase(
            playlistId = playlistId,
            channel = currentChannel.channel,
            type = type,
        )
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
