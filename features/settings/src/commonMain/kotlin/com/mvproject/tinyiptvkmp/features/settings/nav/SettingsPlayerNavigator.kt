package com.mvproject.tinyiptvkmp.features.settings.nav

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator

interface SettingsNavigator : AppNavigator {
    suspend fun navigateToPlaylistSettings()
    suspend fun navigateToPlayerSettings()
    suspend fun navigateToPlaylist(id: String)
}