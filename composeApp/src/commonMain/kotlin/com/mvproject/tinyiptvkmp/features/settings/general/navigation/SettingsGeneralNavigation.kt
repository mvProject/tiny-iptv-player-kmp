/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.general.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralScreen
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToGeneralSettings() {
    this.navigate(AppRoutes.SettingsGeneral)
}

fun NavGraphBuilder.settingsGeneral(
    onNavigateBack: () -> Unit,
    onNavigateToPlayerSettings: () -> Unit,
    onNavigateToPlaylistSettings: () -> Unit
) {
    composable<AppRoutes.SettingsGeneral> {
        val settingsGeneralViewModel = koinViewModel<SettingsGeneralViewModel>()

        SettingsGeneralScreen(
            viewModel = settingsGeneralViewModel,
            onNavigateBack = onNavigateBack,
            onNavigateToPlayerSettings = onNavigateToPlayerSettings,
            onNavigateToPlaylistSettings = onNavigateToPlaylistSettings
        )
    }
}