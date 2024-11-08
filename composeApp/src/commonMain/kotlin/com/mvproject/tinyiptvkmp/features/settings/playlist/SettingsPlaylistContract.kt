package com.mvproject.tinyiptvkmp.features.settings.playlist

import androidx.compose.runtime.Stable
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

interface SettingsPlaylistContract {
    @Stable
    data class UiState(
        val playlistState: PlaylistState = PlaylistState.Empty,
        val isLoading: Boolean = true,
    )

    sealed interface UiAction {
        data class DeletePlaylist(val playlist: Playlist) : UiAction
        data class NavigateToPlaylist(val id: String = String.empty) : UiAction
        data object NavigateBack : UiAction
    }

    sealed interface UiEffect {
        data class NavigateToPlaylist(val id: String) : UiEffect
        data object NavigateBack : UiEffect
    }
}

sealed interface PlaylistState {
    data class Success(val playlists: List<Playlist>) : PlaylistState
    data object Empty : PlaylistState
}