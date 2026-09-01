package com.mvproject.tinyiptvkmp.features.settings.presentation.playlist

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist

@Immutable
data class SettingsPlaylistState(
    val playlistState: PlaylistState = PlaylistState.Empty,
    val isLoading: Boolean = true,
) {
    sealed interface PlaylistState {
        data class Success(val playlists: List<Playlist>) : PlaylistState
        data object Empty : PlaylistState
    }
}

sealed interface SettingsPlaylistAction {
    data class DeletePlaylist(val playlist: Playlist) : SettingsPlaylistAction
    data class DeletePlaylistFailed(val throwable: Throwable) : SettingsPlaylistAction
    data class NavigateToPlaylist(val id: String = String.empty) : SettingsPlaylistAction
    data object DeletePlaylistCompleted : SettingsPlaylistAction
    data object NavigateBack : SettingsPlaylistAction
}

sealed interface SettingsPlaylistEffect {
    data class DeletePlaylist(val playlist: Playlist) : SettingsPlaylistEffect
}
