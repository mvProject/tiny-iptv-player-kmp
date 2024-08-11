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
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.navigateToTvPlaylistChannels
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.tvPlaylistChannels
import com.mvproject.tinyiptvkmp.ui.screens.groups.navigation.playlistGroups
import com.mvproject.tinyiptvkmp.ui.screens.player.navigation.navigateToVideoView
import com.mvproject.tinyiptvkmp.ui.screens.player.navigation.videoView
import com.mvproject.tinyiptvkmp.ui.screens.playlist.navigation.navigateToPlaylistDetail
import com.mvproject.tinyiptvkmp.ui.screens.playlist.navigation.playlistDetail
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.navigation.navigateToSettingsGeneral
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.navigation.settingsGeneral
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.navigation.navigateToSettingsPlayer
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.navigation.settingsPlayer
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.navigation.navigateToSettingsPlaylist
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.navigation.settingsPlaylist

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {

        playlistGroups(
            onNavigateToSettings = navController::navigateToSettingsGeneral,
            onNavigateToGroup = navController::navigateToTvPlaylistChannels
        )

        tvPlaylistChannels(
            onNavigateBack = navController::navigateUp,
            onNavigateSelected = navController::navigateToVideoView
        )

        playlistDetail(
            onNavigateBack = navController::navigateUp
        )

        videoView(
            onNavigateBack = navController::navigateUp
        )

        settingsGeneral(
            onNavigateBack = navController::navigateUp,
            onNavigatePlaylistSettings = navController::navigateToSettingsPlaylist,
            onNavigatePlayerSettings = navController::navigateToSettingsPlayer
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