/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.playlist.action

import com.mvproject.tinyiptv.data.model.playlist.Playlist

sealed class SettingsPlaylistAction {
    data class DeletePlaylist(val playlist: Playlist) : SettingsPlaylistAction()
}

