package com.mvproject.tinyiptvkmp.features.settings.presentation.nav

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator
import com.mvproject.tinyiptvkmp.core.navigation.NavigationOptions

interface SettingsNavigator : AppNavigator {
    suspend fun navigateToPlaylistSettings(options: NavigationOptions = NavigationOptions.Default)
    suspend fun navigateToPlayerSettings(options: NavigationOptions = NavigationOptions.Default)
    suspend fun navigateToPlaylist(
        id: String,
        options: NavigationOptions = NavigationOptions.Default,
    )
}
