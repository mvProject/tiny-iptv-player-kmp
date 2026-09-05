package com.mvproject.tinyiptvkmp.features.channels.presentation.nav

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator
import com.mvproject.tinyiptvkmp.core.navigation.NavigationOptions

interface GroupChannelsNavigator : AppNavigator {
    suspend fun navigateToPlayer(
        playlistId: String,
        name: String,
        url: String,
        group: String,
        groupType: String,
        options: NavigationOptions = NavigationOptions.Default,
    )
}
