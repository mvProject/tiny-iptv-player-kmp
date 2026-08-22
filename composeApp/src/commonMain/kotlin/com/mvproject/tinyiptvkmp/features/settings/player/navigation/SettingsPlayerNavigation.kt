/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.player.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerScreen
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<AppRoutes>.settingsPlayer(
    onNavigateBack: () -> Unit
) {
    entry<AppRoutes.SettingsPlayer> {
        val settingsPlayerViewModel = koinViewModel<SettingsPlayerViewModel>()

        SettingsPlayerScreen(
            viewModel = settingsPlayerViewModel,
            onNavigateBack = onNavigateBack
        )
    }
}
