package com.mvproject.tinyiptvkmp.features.groups.presentation

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist


@Immutable
data class GroupState(
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

sealed interface GroupAction {
    data class SelectPlaylist(val playlistId: String) : GroupAction
    data class NavigateToGroup(val groupKey: String, val groupType: String) : GroupAction
    data object NavigateToSettings : GroupAction
}

sealed interface GroupEffect
