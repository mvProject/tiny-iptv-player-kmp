/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.player.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.SettingsPlayerView
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.SettingsPlayerViewModel
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToSettingsPlayer() {
    this.navigate(
        AppRoutes.SettingsPlayer.route
    )
}

fun RouteBuilder.settingsPlayer(
    onNavigateBack: () -> Unit
) {
    scene(
        route = AppRoutes.SettingsPlayer.route,
        navTransition = NavTransition()
    ) {

        val settingsPlayerViewModel = koinViewModel(SettingsPlayerViewModel::class)
        val state by settingsPlayerViewModel.settingsPlayerState.collectAsState()

        SettingsPlayerView(
            state = state,
            onSettingsPlayerAction = settingsPlayerViewModel::processAction,
            onNavigateBack = onNavigateBack
        )
    }
}