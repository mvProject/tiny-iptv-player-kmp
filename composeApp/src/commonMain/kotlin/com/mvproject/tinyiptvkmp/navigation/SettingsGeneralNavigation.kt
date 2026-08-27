/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralScreen
import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralViewModel
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<AppRoutes>.settingsGeneral(
    onNavigateBack: () -> Unit,
    onNavigateToPlayerSettings: () -> Unit,
    onNavigateToPlaylistSettings: () -> Unit
) {
    entry<AppRoutes.SettingsGeneral> {
        val settingsGeneralViewModel = koinViewModel<SettingsGeneralViewModel>()

        SettingsGeneralScreen(
            viewModel = settingsGeneralViewModel,
            onNavigateBack = onNavigateBack,
            onNavigateToPlayerSettings = onNavigateToPlayerSettings,
            onNavigateToPlaylistSettings = onNavigateToPlaylistSettings
        )
    }
}
