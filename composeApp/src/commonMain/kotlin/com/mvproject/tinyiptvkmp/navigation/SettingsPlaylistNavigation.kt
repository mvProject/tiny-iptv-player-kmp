/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistScreen
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<AppRoutes>.settingsPlaylist(
    onNavigateBack: () -> Unit,
    onNavigatePlaylist: (String) -> Unit,
) {
    entry<AppRoutes.SettingsPlaylist> {
        val settingsPlaylistViewModel = koinViewModel<SettingsPlaylistViewModel>()

        SettingsPlaylistScreen(
            viewModel = settingsPlaylistViewModel,
            onNavigateBack = onNavigateBack,
            onNavigatePlaylist = onNavigatePlaylist
        )
    }
}
