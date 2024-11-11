/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.11.23, 14:18
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mvproject.tinyiptvkmp.features.channels.navigation.groupChannels
import com.mvproject.tinyiptvkmp.features.channels.navigation.navigateToGroupChannels
import com.mvproject.tinyiptvkmp.features.groups.navigation.playlistGroups
import com.mvproject.tinyiptvkmp.features.player.navigation.navigateToPlayer
import com.mvproject.tinyiptvkmp.features.player.navigation.player
import com.mvproject.tinyiptvkmp.features.playlist.navigation.navigateToPlaylistDetail
import com.mvproject.tinyiptvkmp.features.playlist.navigation.playlistDetail
import com.mvproject.tinyiptvkmp.features.settings.general.navigation.navigateToGeneralSettings
import com.mvproject.tinyiptvkmp.features.settings.general.navigation.settingsGeneral
import com.mvproject.tinyiptvkmp.features.settings.player.navigation.navigateToPlayerSettings
import com.mvproject.tinyiptvkmp.features.settings.player.navigation.settingsPlayer
import com.mvproject.tinyiptvkmp.features.settings.playlist.navigation.navigateToPlaylistSettings
import com.mvproject.tinyiptvkmp.features.settings.playlist.navigation.settingsPlaylist

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    startDestination: AppRoutes
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {

        playlistGroups(
            onNavigateToSettings = navController::navigateToGeneralSettings,
            onNavigateToGroup = navController::navigateToGroupChannels
        )

        groupChannels(
            onNavigateBack = navController::navigateUp,
            onNavigateToPlayer = navController::navigateToPlayer
        )

        playlistDetail(
            onNavigateBack = navController::navigateUp
        )

        player(
            onNavigateBack = navController::navigateUp
        )

        settingsGeneral(
            onNavigateBack = navController::navigateUp,
            onNavigateToPlaylistSettings = navController::navigateToPlaylistSettings,
            onNavigateToPlayerSettings = navController::navigateToPlayerSettings
        )

        settingsPlaylist(
            onNavigateBack = navController::navigateUp,
            onNavigatePlaylist = navController::navigateToPlaylistDetail
        )

        settingsPlayer(
            onNavigateBack = navController::navigateUp
        )

    }
}