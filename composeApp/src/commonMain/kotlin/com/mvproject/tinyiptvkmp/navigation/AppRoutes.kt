/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.11.23, 14:10
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes : NavKey {
    @Serializable
    data class Player(
        val playlistId: String,
        val channelName: String,
        val channelUrl: String,
        val group: String,
        val groupType: String,
    ) : AppRoutes

    @Serializable
    data object PlaylistGroup : AppRoutes

    @Serializable
    data class PlaylistDetail(
        val id: String
    ) : AppRoutes

    @Serializable
    data class TvPlaylistChannels(
        val playlistId: String,
        val group: String,
        val groupType: String,
    ) : AppRoutes

    @Serializable
    data object SettingsPlayer : AppRoutes

    @Serializable
    data object SettingsPlaylist : AppRoutes

    @Serializable
    data object SettingsGeneral : AppRoutes
}
