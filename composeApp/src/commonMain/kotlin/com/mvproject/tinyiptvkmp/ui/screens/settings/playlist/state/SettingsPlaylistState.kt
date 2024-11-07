/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:24
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.state

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

@Immutable
data class SettingsPlaylistState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = true,
) {
    val dataIsEmpty
        get() = !isLoading && playlists.isEmpty()
}
