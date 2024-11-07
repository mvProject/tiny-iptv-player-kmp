/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.playlist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistScreen
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToSettingsPlaylist() {
    this.navigate(AppRoutes.SettingsPlaylist)
}

fun NavGraphBuilder.settingsPlaylist(
    onNavigateBack: () -> Unit,
    onNavigatePlaylist: (String) -> Unit,
) {
    composable<AppRoutes.SettingsPlaylist> {
        val settingsPlaylistViewModel = koinViewModel<SettingsPlaylistViewModel>()

        SettingsPlaylistScreen(
            viewModel = settingsPlaylistViewModel,
            onNavigateBack = onNavigateBack,
            onNavigatePlaylist = onNavigatePlaylist
        )
    }
}