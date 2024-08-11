/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_PLAYLIST_DETAIL
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_PLAYLIST_DETAIL_NEW
import com.mvproject.tinyiptvkmp.ui.screens.playlist.PlaylistView
import com.mvproject.tinyiptvkmp.ui.screens.playlist.PlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToPlaylistDetail(id: String) {
    val route = AppRoutes.PlaylistDetail.route + "/${id.ifEmpty { ARG_PLAYLIST_DETAIL_NEW }}"
    this.navigate(route)
}

internal class PlaylistDetailArgs(val id: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(id = checkNotNull(savedStateHandle[ARG_PLAYLIST_DETAIL]) as String)
}

fun NavGraphBuilder.playlistDetail(
    onNavigateBack: () -> Unit
) {
    composable(
        route = AppRoutes.PlaylistDetail.route + "/{$ARG_PLAYLIST_DETAIL}",
    ) { backStackEntry ->

        val detailId = backStackEntry.arguments?.getString(ARG_PLAYLIST_DETAIL)

        detailId?.let { id ->
            val playlistViewModel = koinViewModel<PlaylistViewModel>()
            val state by playlistViewModel.state.collectAsState()

            LaunchedEffect(key1 = id) {
                playlistViewModel.setPlaylistMode(id)
            }

            PlaylistView(
                state = state,
                onPlaylistAction = playlistViewModel::processAction,
                onNavigateBack = onNavigateBack
            )
        }
    }
}