/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.general.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsGeneralView
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsViewModel
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToSettingsGeneral() {
    this.navigate(
        AppRoutes.SettingsGeneral.route
    )
}

fun RouteBuilder.settingsGeneral(
    onNavigateBack: () -> Unit,
    onNavigatePlayerSettings: () -> Unit,
    onNavigatePlaylistSettings: () -> Unit
) {
    scene(
        route = AppRoutes.SettingsGeneral.route,
        navTransition = NavTransition()
    ) {
        val settingsViewModel = koinViewModel(SettingsViewModel::class)
        val settingsState by settingsViewModel.state.collectAsState()

        SettingsGeneralView(
            state = settingsState,
            onSettingsAction = settingsViewModel::processAction,
            onNavigateBack = onNavigateBack,
            onNavigatePlayerSettings = onNavigatePlayerSettings,
            onNavigatePlaylistSettings = onNavigatePlaylistSettings
        )
    }
}