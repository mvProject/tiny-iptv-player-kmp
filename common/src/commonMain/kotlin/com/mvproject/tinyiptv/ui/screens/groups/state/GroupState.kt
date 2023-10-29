/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.09.23, 11:56
 *
 */

package com.mvproject.tinyiptv.ui.screens.groups.state

import com.mvproject.tinyiptv.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptv.data.model.playlist.Playlist
import com.mvproject.tinyiptv.utils.AppConstants

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