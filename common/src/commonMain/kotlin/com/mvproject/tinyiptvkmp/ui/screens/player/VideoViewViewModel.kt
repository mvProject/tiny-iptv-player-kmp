/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:31
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player

import com.mvproject.tinyiptvkmp.data.enums.RatioMode
import com.mvproject.tinyiptvkmp.data.enums.ResizeMode
import com.mvproject.tinyiptvkmp.data.mappers.ListMappers.withRefreshedEpg
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.ui.data.TvPlaylistChannels
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
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
import com.mvproject.tinyiptvkmp.utils.CommonUtils.isWindowsDesktop
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class VideoViewViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
) : ViewModel() {
    private var pollVideoPositionJob: Job? = null
    private var pollVolumeJob: Job? = null

    // time after which [VideoViewState.isControlUiVisible] will be set to false
    private var hideControllerAfterMs: Long = UI_SHOW_DELAY

    // time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    private var _videoViewState = MutableStateFlow(VideoViewState())
    val videoViewState = _videoViewState.asStateFlow()

    private var _videoRatio = FLOAT_VALUE_1

    init {
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
            val channelList = getGroupChannelsUseCase(channelGroup)
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
                    channels = TvPlaylistChannels(items = channelList),
                    currentChannel = currentChannel,
                )
            }
        }
    }

    fun switchToChannel(channel: TvPlaylistChannel) {
        viewModelScope.launch {
            val channelsRefreshed = videoViewState.value.channels.items.withRefreshedEpg()

            val newMediaPosition =
                getCurrentMediaPosition(
                    channelName = channel.channelName,
                    channels = channelsRefreshed,
                )

            setCurrentChannel(currentMediaPosition = newMediaPosition)
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

    private fun triggerRestart() {
        _videoViewState.update { current ->
            current.copy(isRestartRequired = true)
        }
        showControlUi()
    }

    private fun consumeRestart() {
        _videoViewState.update { current ->
            current.copy(isRestartRequired = false)
        }
    }

    private fun switchToNextChannel() {
        val currentChannelsCount = videoViewState.value.channels.items.count()
        val nextIndex = videoViewState.value.mediaPosition + INT_VALUE_1
        val newMediaPosition =
            if (nextIndex > currentChannelsCount - INT_VALUE_1) INT_VALUE_ZERO else nextIndex

        setCurrentChannel(currentMediaPosition = newMediaPosition)
    }

    private fun switchToPreviousChannel() {
        val currentChannelsCount = videoViewState.value.channels.items.count()
        val nextIndex = videoViewState.value.mediaPosition - INT_VALUE_1
        val newMediaPosition =
            if (nextIndex < INT_VALUE_ZERO) currentChannelsCount - INT_VALUE_1 else nextIndex

        setCurrentChannel(currentMediaPosition = newMediaPosition)
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

    private fun setCurrentChannel(currentMediaPosition: Int) {
        val currentChannels = videoViewState.value.channels.items.withRefreshedEpg()
        val currentChannel = currentChannels[currentMediaPosition]

        _videoViewState.update { current ->
            current.copy(
                mediaPosition = currentMediaPosition,
                currentChannel = currentChannel,
                channels = TvPlaylistChannels(items = currentChannels),
                isChannelsVisible = false,
            )
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
                            com.mvproject.tinyiptvkmp.platform.isMediaPlayable(action.state.errorCode)
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

    fun toggleEpgVisibility() {
        val currentEpgVisibleState = videoViewState.value.isEpgVisible

        if (!currentEpgVisibleState) {
            val channelsRefreshed = videoViewState.value.channels.items.withRefreshedEpg()
            _videoViewState.update { current ->
                current.copy(channels = TvPlaylistChannels(items = channelsRefreshed))
            }
        }

        _videoViewState.update { current ->
            current.copy(isEpgVisible = !currentEpgVisibleState)
        }
    }

    fun toggleChannelsVisibility() {
        val currentChannelsVisibleState = videoViewState.value.isChannelsVisible
        _videoViewState.update { current ->
            current.copy(isChannelsVisible = !currentChannelsVisibleState)
        }
    }

    private fun toggleFullScreen() {
        val currentFullscreenState = videoViewState.value.isFullscreen
        _videoViewState.update { current ->
            current.copy(isFullscreen = !currentFullscreenState)
        }
    }

    fun toggleChannelInfoVisibility() {
        val currentChannelInfoVisibleState = videoViewState.value.isChannelInfoVisible
        _videoViewState.update { current ->
            current.copy(isChannelInfoVisible = !currentChannelInfoVisibleState)
        }
    }

    private fun toggleChannelFavorite() {
        val currentChannel = videoViewState.value.currentChannel
        val currentChannels = videoViewState.value.channels
        val currentIndex = videoViewState.value.mediaPosition

        val currentChannelFavoriteChanged =
            currentChannel.copy(
                isInFavorites = !currentChannel.isInFavorites,
            )

        val updatedFavoriteChangedChannels =
            currentChannels.items.mapIndexed { index, channel ->
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
                    channels = TvPlaylistChannels(items = updatedFavoriteChangedChannels),
                )
            }

            toggleFavoriteChannelUseCase(channel = currentChannel)
        }
    }

    private fun toggleVideoResizeMode() {
        val currentMode = videoViewState.value.videoResizeMode
        val nextMode = ResizeMode.toggleResizeMode(current = currentMode)
        _videoViewState.update { current ->
            current.copy(videoResizeMode = nextMode)
        }
    }

    private fun toggleVideoRatioMode() {
        val currentMode = videoViewState.value.videoRatioMode
        val nextMode = RatioMode.toggleRatioMode(current = currentMode)

        val nextRatio =
            if (nextMode == RatioMode.Original) {
                _videoRatio
            } else {
                nextMode.ratio
            }

        _videoViewState.update { current ->
            current.copy(
                videoRatioMode = nextMode,
                videoRatio = nextRatio,
            )
        }
    }

    private fun toggleControlUiState() {
        val currentUiVisibleState = videoViewState.value.isControlUiVisible
        _videoViewState.update { current ->
            current.copy(isControlUiVisible = !currentUiVisibleState)
        }
    }

    private fun togglePlayingState() {
        val currentPlayingState = videoViewState.value.isPlaying
        _videoViewState.update { current ->
            current.copy(isPlaying = !currentPlayingState)
        }
    }

    private fun showControlUi() {
        _videoViewState.update { current ->
            current.copy(isControlUiVisible = true)
        }
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob =
            viewModelScope.launch {
                delay(hideControllerAfterMs)
                hideControlUi()
            }
    }

    private fun hideControlUi() {
        // todo temporally always show ui for desktop
        _videoViewState.update { current ->
            //        current.copy(isControlUiVisible = false)
            current.copy(isControlUiVisible = isWindowsDesktop)
        }
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob = null
    }

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
