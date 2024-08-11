/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups.state

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE

data class GroupState(
    val allGroup: ChannelsGroup = ChannelsGroup(),
    val favorites: ChannelsGroups = ChannelsGroups(),
    val groups: ChannelsGroups = ChannelsGroups(),
    val playlists: Playlists = Playlists(),
    val playlistNames: PlaylistNames = PlaylistNames(),
    val isLoading: Boolean = true,
    val playlistSelectedIndex: Int = INT_NO_VALUE,
    val playlistSelectedId: Long = LONG_NO_VALUE,
    val isPlaylistSelectorVisible: Boolean = false,
) {
    val dataIsEmpty: Boolean
        get() {
            return !isLoading && groups.items.isEmpty()
        }
}

@Immutable
data class ChannelsGroups(
    val items: List<ChannelsGroup> = emptyList(),
)

@Immutable
data class Playlists(
    val items: List<Playlist> = emptyList(),
)

@Immutable
data class PlaylistNames(
    val items: List<String> = emptyList(),
)
