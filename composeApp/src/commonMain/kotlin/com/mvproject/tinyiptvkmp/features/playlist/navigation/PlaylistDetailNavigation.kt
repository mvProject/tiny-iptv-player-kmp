/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistScreen
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<AppRoutes>.playlistDetail(onNavigateBack: () -> Unit) {
    entry<AppRoutes.PlaylistDetail> { key ->
        val playlistViewModel = koinViewModel<PlaylistViewModel>(
            parameters = { parametersOf(key) }
        )

        PlaylistScreen(
            viewModel = playlistViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
