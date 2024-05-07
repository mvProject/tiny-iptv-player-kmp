/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:24
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist

data class SettingsPlaylistState(
    val playlists: Playlists = Playlists(),
    val isLoading: Boolean = true,
) {
    val dataIsEmpty
        get() = !isLoading && playlists.items.isEmpty()
}

@Immutable
data class Playlists(
    val items: List<Playlist> = emptyList(),
)
