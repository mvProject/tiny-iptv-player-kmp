/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.09.23, 18:08
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.playlist.state

import com.mvproject.tinyiptv.data.model.playlist.Playlist

data class SettingsPlaylistState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = true,
) {
    val dataIsEmpty
        get() = !isLoading && playlists.isEmpty()
}