/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.general.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsGeneralScreen
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToSettingsGeneral() {
    this.navigate(AppRoutes.SettingsGeneral)
}

fun NavGraphBuilder.settingsGeneral(
    onNavigateBack: () -> Unit,
    onNavigatePlayerSettings: () -> Unit,
    onNavigatePlaylistSettings: () -> Unit
) {
    composable<AppRoutes.SettingsGeneral> {
        val settingsViewModel = koinViewModel<SettingsViewModel>()

        SettingsGeneralScreen(
            viewModel = settingsViewModel,
            onNavigateBack = onNavigateBack,
            onNavigatePlayerSettings = onNavigatePlayerSettings,
            onNavigatePlaylistSettings = onNavigatePlaylistSettings
        )
    }
}