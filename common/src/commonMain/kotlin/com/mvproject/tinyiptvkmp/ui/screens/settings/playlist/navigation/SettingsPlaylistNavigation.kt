/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.SettingsPlaylistView
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.SettingsPlaylistViewModel
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToSettingsPlaylist() {
    this.navigate(
        AppRoutes.SettingsPlaylist.route
    )
}

fun RouteBuilder.settingsPlaylist(
    onNavigateBack: () -> Unit,
    onNavigatePlaylist: (String) -> Unit,
) {
    scene(
        route = AppRoutes.SettingsPlaylist.route,
        navTransition = NavTransition()
    ) {
        val settingsPlaylistViewModel = koinViewModel(SettingsPlaylistViewModel::class)
        val playlistDataState by settingsPlaylistViewModel.playlistDataState.collectAsState()

        SettingsPlaylistView(
            dataState = playlistDataState,
            onPlaylistAction = settingsPlaylistViewModel::processAction,
            onNavigateBack = onNavigateBack,
            onNavigatePlaylist = onNavigatePlaylist
        )
    }
}