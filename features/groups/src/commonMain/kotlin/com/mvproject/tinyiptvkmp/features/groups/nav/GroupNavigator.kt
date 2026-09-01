package com.mvproject.tinyiptvkmp.features.groups.nav

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator

interface GroupNavigator : AppNavigator {
    suspend fun navigateToSettings()
    suspend fun navigateToPlaylist(playlistId: String, title: String, group: String)
}
