/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.player.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.SettingsPlayerView
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.SettingsPlayerViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToSettingsPlayer() {
    this.navigate(
        AppRoutes.SettingsPlayer.route
    )
}

fun NavGraphBuilder.settingsPlayer(
    onNavigateBack: () -> Unit
) {
    composable(route = AppRoutes.SettingsPlayer.route) {

        val settingsPlayerViewModel = koinViewModel<SettingsPlayerViewModel>()
        val state by settingsPlayerViewModel.settingsPlayerState.collectAsState()

        SettingsPlayerView(
            state = state,
            onSettingsPlayerAction = settingsPlayerViewModel::processAction,
            onNavigateBack = onNavigateBack
        )
    }
}