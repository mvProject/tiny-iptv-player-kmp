/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state

import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist

data class SettingsPlaylistState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = true,
) {
    val dataIsEmpty
        get() = !isLoading && playlists.isEmpty()
}