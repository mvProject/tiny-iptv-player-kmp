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

@Immutable
data class GroupState(
    val channelGroups: List<ChannelsGroup> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val selectedPlaylist: Playlist = Playlist(),
    val isPlaylistSelectorVisible: Boolean = false,
)
