package com.mvproject.tinyiptvkmp.features.channels.nav

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator

interface GroupChannelsNavigator : AppNavigator {
    suspend fun navigateToPlayer(name: String, group: String, groupType: String)

}