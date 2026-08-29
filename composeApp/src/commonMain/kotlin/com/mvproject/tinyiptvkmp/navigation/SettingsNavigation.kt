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
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerScreen
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerViewModel
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistScreen
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<AppRoutes>.settingsGeneral() {
    entry<AppRoutes.SettingsGeneral> {
        val settingsGeneralViewModel = koinViewModel<SettingsGeneralViewModel>()

        SettingsGeneralScreen(viewModel = settingsGeneralViewModel)
    }
}

fun EntryProviderScope<AppRoutes>.settingsPlayer() {
    entry<AppRoutes.SettingsPlayer> {
        val settingsPlayerViewModel = koinViewModel<SettingsPlayerViewModel>()

        SettingsPlayerScreen(viewModel = settingsPlayerViewModel)
    }
}

fun EntryProviderScope<AppRoutes>.settingsPlaylist() {
    entry<AppRoutes.SettingsPlaylist> {
        val settingsPlaylistViewModel = koinViewModel<SettingsPlaylistViewModel>()

        SettingsPlaylistScreen(viewModel = settingsPlaylistViewModel)
    }
}
