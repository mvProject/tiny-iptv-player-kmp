/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.general.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsGeneralView
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

fun NavHostController.navigateToSettingsGeneral() {
    this.navigate(
        AppRoutes.SettingsGeneral.route
    )
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.settingsGeneral(
    onNavigateBack: () -> Unit,
    onNavigatePlayerSettings: () -> Unit,
    onNavigatePlaylistSettings: () -> Unit
) {
    composable(
        route = AppRoutes.SettingsGeneral.route,
          ) {
        val settingsViewModel = koinViewModel<SettingsViewModel>()
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