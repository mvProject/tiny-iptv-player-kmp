package com.mvproject.tinyiptvkmp.features.player

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

interface PlayerContract {
    @Immutable
    data class UiState(
        val channelGroup: String = String.empty,
        val currentChannel: TvChannel = TvChannel(),
        val isControlUiVisible: Boolean = false,
        val isVolumeUiVisible: Boolean = false,
        val isEpgVisible: Boolean = false,
        val isChannelsVisible: Boolean = false,
        val isChannelInfoVisible: Boolean = false,
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
    )

    sealed interface UiAction {
        data object NavigateBack : UiAction
        data object TogglePlayback : UiAction
        data object ChangeVideoSize : UiAction
        data object ChangeVideoRatio : UiAction
        data object ToggleFullScreen : UiAction
        data object ToggleChannelFavorite : UiAction
        data object TogglePlayerUi : UiAction
        data object ToggleProgramsUi : UiAction
        data object ToggleChannelsUi : UiAction
        data object ToggleProgramInfoUi : UiAction
        data object SelectNext : UiAction
        data object SelectPrevious : UiAction
        data object VolumeUp : UiAction
        data object VolumeDown : UiAction

        data class SelectChannel(val channel: TvChannel) : UiAction
        data class OnVideoSizeChanged(val height: Int, val width: Int, val videoRatio: Float) :
            UiAction

        data class OnIsPlayingChanged(val state: Boolean) : UiAction
        data class OnPlaybackStateChanged(val state: PlayerPlaybackState) : UiAction
    }

    sealed interface UiEffect {
        data object OnNavigateBack : UiEffect
    }
}

sealed interface PlayerPlaybackState {
    data object PlaybackReady : PlayerPlaybackState
    data object PlaybackEnded : PlayerPlaybackState
    data object PlaybackBuffering : PlayerPlaybackState
    data class PlaybackIdle(val errorCode: Int?) : PlayerPlaybackState
}