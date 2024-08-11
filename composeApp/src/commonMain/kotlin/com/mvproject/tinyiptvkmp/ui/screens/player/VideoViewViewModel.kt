/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 14:06
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.data.enums.RatioMode
import com.mvproject.tinyiptvkmp.data.enums.ResizeMode
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.usecases.GetChannelsEpg
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsEpg
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.ui.data.TvPlaylistChannels
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.ChannelEpg
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelEpg
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.TvPlaylistChannelsArgs
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.navigation.VideoViewArgs
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.utils.AppConstants.DELAY_50
import com.mvproject.tinyiptvkmp.utils.AppConstants.FLOAT_STEP_VOLUME
import com.mvproject.tinyiptvkmp.utils.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.AppConstants.UI_SHOW_DELAY
import com.mvproject.tinyiptvkmp.utils.AppConstants.VOLUME_SHOW_DELAY
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoViewViewModel(
    savedStateHandle: SavedStateHandle,
    private val preferenceRepository: PreferenceRepository,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val getChannelsEpg: GetChannelsEpg,
    private val getGroupChannelsEpg: GetGroupChannelsEpg,
) : ViewModel() {
    private var pollVolumeJob: Job? = null

    // time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    private var _videoViewState = MutableStateFlow(VideoViewState())
    val videoViewState = _videoViewState.asStateFlow()

    private var _videoViewChannelsState = MutableStateFlow(TvPlaylistChannels())
    val videoViewChannelsState = _videoViewChannelsState.asStateFlow()

    private var _videoRatio = FLOAT_VALUE_1

    private val args = VideoViewArgs(savedStateHandle)

    init {
        val media = args.media
        val group = args.group
        KLog.d("testing VideoViewViewModel init media:$media, group:$group")

        viewModelScope.launch {
            _videoViewState.update { current ->
                val ratioMode = RatioMode.entries[preferenceRepository.getDefaultRatioMode()]

                current.copy(
                    isFullscreen = preferenceRepository.getDefaultFullscreenMode(),
                    videoResizeMode = ResizeMode.entries[preferenceRepository.getDefaultResizeMode()],
                    videoRatioMode = ratioMode,
                    videoRatio = ratioMode.ratio,
                )
            }
        }
    }

    fun initPlayBack(
        channelName: String,
        channelGroup: String,
    ) {
        viewModelScope.launch {
            val channelList = getGroupChannelsUseCase(channelGroup, "")
            val currentPlaying = videoViewState.value.currentChannel.channelName

            val name = currentPlaying.ifBlank { channelName }

            val currentItemPosition =
                getCurrentMediaPosition(
                    channelName = name,
                    channels = channelList,
                )
            val currentChannel = channelList[currentItemPosition]

            _videoViewState.update { current ->
                current.copy(
                    channelGroup = channelGroup,
                    mediaPosition = currentItemPosition,
                    currentChannel = currentChannel,
                )
            }

            _videoViewChannelsState.value = TvPlaylistChannels(items = channelList)

            loadSelectedChannelEpg()

            loadAvailableChannelsEpg()
        }
    }

    fun switchToChannel(channel: TvPlaylistChannel) {
        viewModelScope.launch {
            //   val channelsRefreshed = videoViewState.value.channels.items.withRefreshedEpg()
            val currentChannels = videoViewChannelsState.value.items

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
        }
    }

    fun processPlaybackStateActions(action: PlaybackStateActions) {
        when (action) {
            is PlaybackStateActions.OnVideoSizeChanged -> {
                _videoRatio = action.videoRatio

                val ratio =
                    if (videoViewState.value.videoRatioMode == RatioMode.Original) {
                        _videoRatio
                    } else {
                        videoViewState.value.videoRatioMode.ratio
                    }

                _videoViewState.update { current ->
                    current.copy(videoRatio = ratio)
                }
            }

            is PlaybackStateActions.OnIsPlayingChanged -> {
                _videoViewState.update { current ->
                    current.copy(isPlaying = action.state)
                }
            }

            is PlaybackStateActions.OnMediaItemTransition -> {
                triggerRestart()
            }

            is PlaybackStateActions.OnPlaybackStateChanged -> {
                var isMediaPlayable = videoViewState.value.isMediaPlayable
                val isBuffering = action.state == VideoPlaybackState.VideoPlaybackBuffering

                when (action.state) {
                    is VideoPlaybackState.VideoPlaybackIdle -> {
                        isMediaPlayable =
                            com.mvproject.tinyiptvkmp.platform
                                .isMediaPlayable(action.state.errorCode)
                    }

                    VideoPlaybackState.VideoPlaybackReady -> {
                        isMediaPlayable = true
                    }

                    else -> {
                    }
                }

                _videoViewState.update { current ->
                    current.copy(
                        isMediaPlayable = isMediaPlayable,
                        isBuffering = isBuffering,
                    )
                }
            }
        }
    }

    private suspend fun loadSelectedChannelEpg() {
        val currentChannel = videoViewState.value.currentChannel
        if (currentChannel.epgId.isNotBlank()) {
            val channelsEpgData = getChannelsEpg(channelId = currentChannel.epgId)

            val currentChannelWithEpg =
                currentChannel.copy(
                    channelEpg = TvPlaylistChannelEpg(items = channelsEpgData),
                )

            _videoViewState.update { state ->
                state.copy(currentChannel = currentChannelWithEpg)
            }
        }
    }

    private suspend fun loadAvailableChannelsEpg() {
        val currentChannels = videoViewChannelsState.value.items
        if (currentChannels.isNotEmpty()) {
            val channelsData =
                currentChannels
                    .filter { it.epgId.isNotBlank() }
                    .map {
                        ChannelEpg(
                            channelName = it.channelName,
                            channelEpgId = it.epgId,
                        )
                    }

            val channelsEpgData = getGroupChannelsEpg(channels = channelsData)

            channelsEpgData.forEach { data ->
                delay(200)
                applyEpg(data = data)
            }
        }
    }

    private fun applyEpg(data: ChannelEpg) {
        KLog.d("testing channelsEpgData id = ${data.channelEpgId}, count = ${data.programs.count()}")
        val channels = videoViewChannelsState.value.items
        val channelIndex = channels.indexOfFirst { it.channelName == data.channelName }

        val channelWithEpg =
            updateChannelWithEpg(
                index = channelIndex,
                programsData = data.programs,
            )

        updateChannel(
            index = channelIndex,
            channel = channelWithEpg,
        )
    }

    private fun updateChannelWithEpg(
        index: Int,
        programsData: List<EpgProgram>,
    ): TvPlaylistChannel {
        val current = videoViewChannelsState.value.items[index]
        val programs = TvPlaylistChannelEpg(items = programsData)
        val updated = current.copy(channelEpg = programs)
        return updated
    }

    private fun updateChannel(
        index: Int,
        channel: TvPlaylistChannel,
    ) {
        val current = videoViewChannelsState.value.items

        val updatedList =
            current
                .toMutableList()
                .apply {
                    set(index, channel)
                }

        _videoViewChannelsState.update { state ->
            state.copy(items = updatedList)
        }
    }

    private fun getCurrentMediaPosition(
        channelName: String,
        channels: List<TvPlaylistChannel>,
    ): Int {
        val currentPos = videoViewState.value.mediaPosition

        val targetPos =
            channels
                .indexOfFirst { it.channelName == channelName }

        val mediaPosition =
            if (targetPos > INT_NO_VALUE) {
                targetPos
            } else {
                currentPos
            }

        return mediaPosition
    }

    private fun triggerRestart() {
        _videoViewState.update { current ->
            current.copy(isRestartRequired = true)
        }
        toggleControlUiState()
    }

    private fun consumeRestart() {
        _videoViewState.update { current ->
            current.copy(isRestartRequired = false)
        }
    }

    private fun switchToNextChannel() {
        val currentChannelsCount = videoViewChannelsState.value.items.count()
        val nextIndex = videoViewState.value.mediaPosition + INT_VALUE_1
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
        val currentChannelsCount = videoViewChannelsState.value.items.count()
        val nextIndex = videoViewState.value.mediaPosition - INT_VALUE_1
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
            val targetVolume = videoViewState.value.currentVolume + FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtMost(FLOAT_VALUE_1)
            _videoViewState.update { current ->
                current.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private fun decreaseVolume() {
        showVolumeUi()
        viewModelScope.launch {
            val targetVolume = videoViewState.value.currentVolume - FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtLeast(FLOAT_VALUE_ZERO)
            _videoViewState.update { current ->
                current.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private suspend fun setCurrentChannel(currentMediaPosition: Int) {
        val currentChannels = videoViewChannelsState.value.items
        val currentChannel = currentChannels[currentMediaPosition]

        _videoViewState.update { current ->
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
        val currentChannel = videoViewState.value.currentChannel
        val currentChannels = videoViewChannelsState.value.items
        val currentIndex = videoViewState.value.mediaPosition

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
            _videoViewState.update { current ->
                current.copy(
                    currentChannel = currentChannelFavoriteChanged,
                )
            }

            _videoViewChannelsState.update { state ->
                state.copy(items = updatedFavoriteChangedChannels)
            }

            toggleFavoriteChannelUseCase(channel = currentChannel)
        }
    }

    fun toggleEpgVisibility() {
        _videoViewState.update { current ->
            val currentEpgVisibleState = current.isEpgVisible
            current.copy(isEpgVisible = !currentEpgVisibleState)
        }
    }

    fun toggleChannelsVisibility() {
        _videoViewState.update { current ->
            val currentChannelsVisibleState = current.isChannelsVisible
            current.copy(isChannelsVisible = !currentChannelsVisibleState)
        }
    }

    fun toggleChannelInfoVisibility() {
        _videoViewState.update { current ->
            val currentChannelInfoVisibleState = current.isChannelInfoVisible
            current.copy(isChannelInfoVisible = !currentChannelInfoVisibleState)
        }
    }

    private fun toggleFullScreen() {
        _videoViewState.update { current ->
            val currentFullscreenState = current.isFullscreen
            current.copy(isFullscreen = !currentFullscreenState)
        }
    }

    private fun toggleControlUiState() {
        viewModelScope.launch {
            if (!videoViewState.value.isControlUiVisible) {
                _videoViewState.update { current ->
                    current.copy(isControlUiVisible = true)
                }

                delay(UI_SHOW_DELAY)

                _videoViewState.update { current ->
                    current.copy(isControlUiVisible = false)
                }
            }
        }
    }

    private fun togglePlayingState() {
        _videoViewState.update { current ->
            val currentPlayingState = current.isPlaying
            current.copy(isPlaying = !currentPlayingState)
        }
    }

    private fun toggleVideoResizeMode() {
        _videoViewState.update { current ->
            val currentMode = current.videoResizeMode
            val nextMode = ResizeMode.toggleResizeMode(current = currentMode)
            current.copy(videoResizeMode = nextMode)
        }
    }

    private fun toggleVideoRatioMode() {
        _videoViewState.update { current ->
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

    // private fun showControlUi() {
    //     _videoViewState.update { current ->
    //         current.copy(isControlUiVisible = true)
    //     }
    //     pollVideoPositionJob?.cancel()
    //     pollVideoPositionJob =
    //         viewModelScope.launch {
    //             delay(hideControllerAfterMs)
    //             hideControlUi()
    //         }
    // }

    //  private fun hideControlUi() {
    //      // todo temporally always show ui for desktop
    //      _videoViewState.update { current ->
    //          //        current.copy(isControlUiVisible = false)
    //          current.copy(isControlUiVisible = isWindowsDesktop)
    //      }
    //      pollVideoPositionJob?.cancel()
    //      pollVideoPositionJob = null
    //  }

    private fun showVolumeUi() {
        _videoViewState.update { current ->
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
        _videoViewState.update { current ->
            current.copy(isVolumeUiVisible = false)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob = null
    }
}
