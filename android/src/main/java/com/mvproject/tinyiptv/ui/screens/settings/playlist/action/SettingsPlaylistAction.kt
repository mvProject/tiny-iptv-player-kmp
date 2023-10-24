/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.09.23, 18:07
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.playlist.action

import com.mvproject.tinyiptv.data.models.playlist.Playlist

sealed class SettingsPlaylistAction {
    data class DeletePlaylist(val playlist: Playlist) : SettingsPlaylistAction()
}

