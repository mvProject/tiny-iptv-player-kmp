package com.mvproject.tinyiptvkmp.features.player

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType

@Immutable
data class PlayerUiState(
    val channelGroup: String = String.empty,
    val currentChannel: TvChannelWithPrograms = TvChannelWithPrograms(),
    val isControlUiVisible: Boolean = false,
    val isVolumeUiVisible: Boolean = false,
    val isFullscreen: Boolean = false,
    val isPlaying: Boolean = false,
    val currentVolume: Float = 0.5f,
    val isBuffering: Boolean = false,
    val isMediaPlayable: Boolean = true,
    val isOnline: Boolean = true,
    val videoSize: VideoSize = VideoSize.WideScreen,
    val channelIndex: Int = INT_NO_VALUE,
    val groupChannels: List<TvChannelWithPrograms> = emptyList(),
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
    data object ToggleFullScreen : PlayerUiAction
    data object TogglePlayerUi : PlayerUiAction
    data object SelectNext : PlayerUiAction
    data object SelectPrevious : PlayerUiAction
    data object VolumeUp : PlayerUiAction
    data object VolumeDown : PlayerUiAction

    data class SelectChannel(val channel: TvChannelWithPrograms) : PlayerUiAction

    data class OnIsPlayingChanged(val state: Boolean) : PlayerUiAction
    data class OnPlaybackStateChanged(val state: PlayerUiState.PlayerPlaybackState) : PlayerUiAction

    data class UpdateFavorite(val type: FavoriteType) : PlayerUiAction
    data class OpenOsd(val type: PlayerUiState.PlayerOSD) : PlayerUiAction
    data object CloseOsd : PlayerUiAction
}

sealed interface PlayerUiEffect

data class PlayerArgs(
    val channelName: String,
    val group: String,
    val groupType: String,
)