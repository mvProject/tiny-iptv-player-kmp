/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.SettingsPlaylistView
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.SettingsPlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

fun NavHostController.navigateToSettingsPlaylist() {
    this.navigate(
        AppRoutes.SettingsPlaylist.route
    )
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.settingsPlaylist(
    onNavigateBack: () -> Unit,
    onNavigatePlaylist: (String) -> Unit,
) {
    composable(
        route = AppRoutes.SettingsPlaylist.route,
    ) {
        val settingsPlaylistViewModel = koinViewModel<SettingsPlaylistViewModel>()
        val playlistDataState by settingsPlaylistViewModel.playlistDataState.collectAsState()

        SettingsPlaylistView(
            dataState = playlistDataState,
            onPlaylistAction = settingsPlaylistViewModel::processAction,
            onNavigateBack = onNavigateBack,
            onNavigatePlaylist = onNavigatePlaylist
        )
    }
}