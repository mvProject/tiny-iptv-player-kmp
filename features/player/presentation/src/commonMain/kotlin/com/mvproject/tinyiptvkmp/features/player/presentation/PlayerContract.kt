package com.mvproject.tinyiptvkmp.features.player.presentation

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms

@Immutable
data class PlayerState(
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

sealed interface PlayerAction {
    data object NavigateBack : PlayerAction
    data object TogglePlayback : PlayerAction
    data object ChangeVideoSize : PlayerAction
    data object ToggleFullScreen : PlayerAction
    data object TogglePlayer : PlayerAction
    data object SelectNext : PlayerAction
    data object SelectPrevious : PlayerAction
    data object VolumeUp : PlayerAction
    data object VolumeDown : PlayerAction

    data class SelectChannel(val channel: TvChannelWithPrograms) : PlayerAction

    data class OnIsPlayingChanged(val state: Boolean) : PlayerAction
    data class OnPlaybackStateChanged(val state: PlayerState.PlayerPlaybackState) : PlayerAction

    data class UpdateFavorite(val type: FavoriteType) : PlayerAction
    data class OpenOsd(val type: PlayerState.PlayerOSD) : PlayerAction
    data object CloseOsd : PlayerAction
}

sealed interface PlayerEffect

data class PlayerArgs(
    val playlistId: String,
    val channelName: String,
    val group: String,
    val groupType: String,
)
