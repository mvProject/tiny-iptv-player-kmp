/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.11.23, 14:10
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import com.mvproject.tinyiptvkmp.navigation.NavConstants.ROUTE_PLAYLIST_DETAIL
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ROUTE_PLAYLIST_GROUP
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ROUTE_SETTINGS_GENERAL
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ROUTE_SETTINGS_PLAYER
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ROUTE_SETTINGS_PLAYLIST
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ROUTE_TV_PLAYLIST_CHANNELS
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ROUTE_VIDEO_VIEW

sealed class AppRoutes(val route: String) {
    data object VideoView : AppRoutes(ROUTE_VIDEO_VIEW)
    data object PlaylistGroup : AppRoutes(ROUTE_PLAYLIST_GROUP)
    data object PlaylistDetail : AppRoutes(ROUTE_PLAYLIST_DETAIL)
    data object TvPlaylistChannels : AppRoutes(ROUTE_TV_PLAYLIST_CHANNELS)
    data object SettingsPlayer : AppRoutes(ROUTE_SETTINGS_PLAYER)
    data object SettingsPlaylist : AppRoutes(ROUTE_SETTINGS_PLAYLIST)
    data object SettingsGeneral : AppRoutes(ROUTE_SETTINGS_GENERAL)
}