package com.mvproject.tinyiptvkmp.features.groups

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist


@Immutable
data class GroupUiState(
    val groupState: GroupState = GroupState.Empty,
    val playlists: List<Playlist> = emptyList(),
    val selectedPlaylist: Playlist = Playlist(),
    val isPlaylistSelectorVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val progress: Float = FLOAT_VALUE_ZERO,
) {
    sealed interface GroupState {
        data class Success(val groups: List<ChannelsGroup>) : GroupState
        data object Empty : GroupState
    }
}

sealed interface GroupUiAction {
    data class SelectPlaylist(val playlist: Playlist) : GroupUiAction
    data class NavigateToGroup(val title: String, val group: String) : GroupUiAction
    data object NavigateToSettings : GroupUiAction
    data object RefreshPlaylist : GroupUiAction
}

sealed interface GroupUiEffect