/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.features.groups

import androidx.compose.runtime.Stable
import com.mvproject.tinyiptvkmp.core.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

interface GroupContract {
    @Stable
    data class UiState(
        val groupState: GroupState = GroupState.Empty,
        val playlists: List<Playlist> = emptyList(),
        val selectedPlaylist: Playlist = Playlist(),
        val isPlaylistSelectorVisible: Boolean = false,
        val isLoading: Boolean = false,
    )

    sealed interface UiAction {
        data class SelectPlaylist(
            val playlist: Playlist,
        ) : UiAction

        data class NavigateToGroup(val title: String, val group: String) : UiAction
        data object NavigateToSettings : UiAction
        data object RefreshPlaylist : UiAction
    }

    sealed interface UiEffect {
        data object NavigateToSettings : UiEffect
        data class NavigateToGroup(val title: String, val group: String) : UiEffect
    }
}

sealed interface GroupState {
    data class Success(val groups: List<ChannelsGroup>) : GroupState
    data object Empty : GroupState
}

