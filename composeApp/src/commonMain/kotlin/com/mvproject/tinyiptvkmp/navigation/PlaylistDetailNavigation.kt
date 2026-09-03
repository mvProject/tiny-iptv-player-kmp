/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.playlist.presentation.PlaylistDetailArgs
import com.mvproject.tinyiptvkmp.features.playlist.presentation.PlaylistScreen
import com.mvproject.tinyiptvkmp.features.playlist.presentation.PlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<AppRoutes>.playlistDetail() {
    entry<AppRoutes.PlaylistDetail> { key ->
        val playlistViewModel =
            koinViewModel<PlaylistViewModel>(
                parameters = { parametersOf(PlaylistDetailArgs(playlistId = key.id)) },
            )

        PlaylistScreen(viewModel = playlistViewModel)
    }
}
