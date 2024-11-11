/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.11.23, 14:10
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoutes {
    @Serializable
    data class Player(
        val channelName: String,
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