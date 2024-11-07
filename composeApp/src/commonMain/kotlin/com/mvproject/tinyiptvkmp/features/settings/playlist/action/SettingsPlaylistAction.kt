/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.playlist.action

import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

sealed class SettingsPlaylistAction {
    data class DeletePlaylist(val playlist: Playlist) : SettingsPlaylistAction()
}

