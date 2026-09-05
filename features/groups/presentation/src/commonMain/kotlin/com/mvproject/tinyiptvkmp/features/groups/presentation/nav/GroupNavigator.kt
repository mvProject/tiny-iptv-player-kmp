package com.mvproject.tinyiptvkmp.features.groups.presentation.nav

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator
import com.mvproject.tinyiptvkmp.core.navigation.NavigationOptions

interface GroupNavigator : AppNavigator {
    suspend fun navigateToSettings(options: NavigationOptions = NavigationOptions.Default)
    suspend fun navigateToPlaylist(
        playlistId: String,
        groupKey: String,
        groupType: String,
        options: NavigationOptions = NavigationOptions.Default,
    )
}
