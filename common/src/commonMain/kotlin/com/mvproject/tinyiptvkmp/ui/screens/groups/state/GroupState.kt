/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups.state

import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.utils.AppConstants


data class GroupState(
    val groups: List<ChannelsGroup> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val playlistNames: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val playlistSelectedIndex: Int = AppConstants.INT_NO_VALUE,
    val isPlaylistSelectorVisible: Boolean = false
) {
    val dataIsEmpty: Boolean
        get() {
            return !isLoading && groups.isEmpty()
        }
}