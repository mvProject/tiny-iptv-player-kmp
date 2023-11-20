/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mvproject.tinyiptvkmp.data.enums.RatioMode
import com.mvproject.tinyiptvkmp.data.enums.ResizeMode
import com.mvproject.tinyiptvkmp.data.mappers.ListMappers.withRefreshedEpg
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
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

class VideoViewViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
) : ScreenModel {

    private var pollVideoPositionJob: Job? = null
    private var pollVolumeJob: Job? = null

    //time after which [VideoViewState.isControlUiVisible] will be set to false
    private var hideControllerAfterMs: Long = UI_SHOW_DELAY

    //time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    private var _videoViewState = MutableStateFlow(VideoViewState())
    val videoViewState = _videoViewState.asStateFlow()

    private var _videoRatio = FLOAT_VALUE_1

    init {
        screenModelScope.launch {
            _videoViewState.update {
                val ratioMode = RatioMode.entries[preferenceRepository.getDefaultRatioMode()]

                it.copy(
                    isFullscreen = preferenceRepository.getDefaultFullscreenMode(),
                    videoResizeMode = ResizeMode.entries[preferenceRepository.getDefaultResizeMode()],
                    videoRatioMode = ratioMode,
                    videoRatio = ratioMode.ratio
                )
            }
        }
    }

    fun initPlayBack(channelUrl: String, channelGroup: String) {
        screenModelScope.launch {
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
        screenModelScope.launch {
            val channelsRefreshed = videoViewState.value.channels.withRefreshedEpg()

            val newMediaPosition = getCurrentMediaPosition(
                channel.channelUrl,
                channelsRefreshed
            )

            setCurrentChannel(currentMediaPosition = newMediaPosition)
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
        _videoViewState.update { state ->
            state.copy(isRestartRequired = true)
        }
        showControlUi()
    }

    private fun consumeRestart() {
        _videoViewState.update { state ->
            state.copy(isRestartRequired = false)
        }
    }

    private fun switchToNextChannel() {
        val currentChannelsCount = videoViewState.value.channels.count()
        val nextIndex = videoViewState.value.mediaPosition + INT_VALUE_1
        val newMediaPosition =
            if (nextIndex > currentChannelsCount - INT_VALUE_1) INT_VALUE_ZERO else nextIndex

        setCurrentChannel(currentMediaPosition = newMediaPosition)
    }

    private fun switchToPreviousChannel() {
        val currentChannelsCount = videoViewState.value.channels.count()
        val nextIndex = videoViewState.value.mediaPosition - INT_VALUE_1
        val newMediaPosition =
            if (nextIndex < INT_VALUE_ZERO) currentChannelsCount - INT_VALUE_1 else nextIndex

        setCurrentChannel(currentMediaPosition = newMediaPosition)
    }

    private fun increaseVolume() {
        showVolumeUi()
        screenModelScope.launch {
            val targetVolume = videoViewState.value.currentVolume + FLOAT_STEP_VOLUME
            val nextVolume = if (targetVolume > FLOAT_VALUE_1) FLOAT_VALUE_1 else targetVolume
            _videoViewState.update { state ->
                state.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private fun decreaseVolume() {
        showVolumeUi()
        screenModelScope.launch {
            val targetVolume = videoViewState.value.currentVolume - FLOAT_STEP_VOLUME
            val nextVolume = if (targetVolume < FLOAT_VALUE_ZERO) FLOAT_VALUE_ZERO else targetVolume
            _videoViewState.update { state ->
                state.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private fun setCurrentChannel(
        currentMediaPosition: Int
    ) {
        val currentChannels = videoViewState.value.channels.withRefreshedEpg()
        val currentChannel = currentChannels[currentMediaPosition]

        _videoViewState.update { state ->
            state.copy(
                mediaPosition = currentMediaPosition,
                currentChannel = currentChannel,
                channels = currentChannels,
                isChannelsVisible = false
            )
        }
    }

    fun processPlaybackStateActions(action: PlaybackStateActions) {
        when (action) {
            is PlaybackStateActions.OnVideoSizeChanged -> {
                _videoRatio = action.videoRatio

                val ratio = if (videoViewState.value.videoRatioMode == RatioMode.Original) {
                    _videoRatio
                } else
                    videoViewState.value.videoRatioMode.ratio

                _videoViewState.update { state ->
                    state.copy(videoRatio = ratio)
                }
            }

            is PlaybackStateActions.OnIsPlayingChanged -> {
                _videoViewState.update { state ->
                    state.copy(isPlaying = action.state)
                }
            }

            is PlaybackStateActions.OnMediaItemTransition -> {
                triggerRestart()
            }

            is PlaybackStateActions.OnPlaybackStateChanged -> {
                var isMediaPlayable = videoViewState.value.isMediaPlayable
                val isBuffering = action.state == VideoPlaybackState.VideoPlaybackBuffering
                // Napier.e("testing OnPlaybackState isBuffering:$isBuffering")

                when (action.state) {
                    is VideoPlaybackState.VideoPlaybackIdle -> {
                        // Napier.e("testing VideoPlaybackState VideoPlaybackIdle")
                        isMediaPlayable =
                            com.mvproject.tinyiptvkmp.platform.isMediaPlayable(action.state.errorCode)
                    }

                    VideoPlaybackState.VideoPlaybackReady -> {
                        //  Napier.e("testing VideoPlaybackState VideoPlaybackReady")
                        isMediaPlayable = true
                    }

                    else -> {

                    }
                }

                _videoViewState.update { state ->
                    state.copy(
                        isMediaPlayable = isMediaPlayable,
                        isBuffering = isBuffering
                    )
                }
            }
        }
    }

    fun toggleEpgVisibility() {
        val currentEpgVisibleState = videoViewState.value.isEpgVisible

        if (!currentEpgVisibleState) {
            val channelsRefreshed = videoViewState.value.channels.withRefreshedEpg()
            _videoViewState.update { state ->
                state.copy(channels = channelsRefreshed)
            }
        }

        _videoViewState.update { state ->
            state.copy(isEpgVisible = !currentEpgVisibleState)
        }
    }

    fun toggleChannelsVisibility() {
        val currentChannelsVisibleState = videoViewState.value.isChannelsVisible
        _videoViewState.update { state ->
            state.copy(isChannelsVisible = !currentChannelsVisibleState)
        }
    }

    private fun toggleFullScreen() {
        val currentFullscreenState = videoViewState.value.isFullscreen
        _videoViewState.update { state ->
            state.copy(isFullscreen = !currentFullscreenState)
        }
    }

    fun toggleChannelInfoVisibility() {
        val currentChannelInfoVisibleState = videoViewState.value.isChannelInfoVisible
        _videoViewState.update { state ->
            state.copy(isChannelInfoVisible = !currentChannelInfoVisibleState)
        }
    }

    private fun toggleChannelFavorite() {
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

        screenModelScope.launch {
            _videoViewState.update { state ->
                state.copy(
                    currentChannel = currentChannelFavoriteChanged,
                    channels = updatedFavoriteChangedChannels
                )
            }

            toggleFavoriteChannelUseCase(channel = currentChannel)
        }

    }

    private fun toggleVideoResizeMode() {
        val currentMode = videoViewState.value.videoResizeMode
        val nextMode = ResizeMode.toggleResizeMode(current = currentMode)
        _videoViewState.update { state ->
            state.copy(videoResizeMode = nextMode)
        }
    }

    private fun toggleVideoRatioMode() {
        val currentMode = videoViewState.value.videoRatioMode
        val nextMode = RatioMode.toggleRatioMode(current = currentMode)

        val nextRatio = if (nextMode == RatioMode.Original)
            _videoRatio
        else nextMode.ratio

        _videoViewState.update { state ->
            state.copy(
                videoRatioMode = nextMode,
                videoRatio = nextRatio
            )
        }
    }

    private fun toggleControlUiState() {
        val currentUiVisibleState = videoViewState.value.isControlUiVisible
        _videoViewState.update { state ->
            state.copy(isControlUiVisible = !currentUiVisibleState)
        }
    }

    private fun togglePlayingState() {
        val currentPlayingState = videoViewState.value.isPlaying
        _videoViewState.update { state ->
            state.copy(isPlaying = !currentPlayingState)
        }
    }

    private fun showControlUi() {
        _videoViewState.update { state ->
            state.copy(isControlUiVisible = true)
        }
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob = screenModelScope.launch {
            delay(hideControllerAfterMs)
            hideControlUi()
        }
    }

    private fun hideControlUi() {
        // todo temporally always show ui for desktop
        _videoViewState.update { state ->
            //        state.copy(isControlUiVisible = false)
            state.copy(isControlUiVisible = isWindowsDesktop)
        }
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob = null
    }

    private fun showVolumeUi() {
        _videoViewState.update { state ->
            state.copy(isVolumeUiVisible = true)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob = screenModelScope.launch {
            delay(hideVolumeAfterMs)
            hideVolumeUi()
        }
    }

    private fun hideVolumeUi() {
        _videoViewState.update { state ->
            state.copy(isVolumeUiVisible = false)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob = null
    }
}