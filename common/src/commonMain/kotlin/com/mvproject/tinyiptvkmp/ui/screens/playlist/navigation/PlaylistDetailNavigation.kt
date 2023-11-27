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
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_PLAYLIST_DETAIL
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_PLAYLIST_DETAIL_NEW
import com.mvproject.tinyiptvkmp.ui.screens.playlist.PlaylistView
import com.mvproject.tinyiptvkmp.ui.screens.playlist.PlaylistViewModel
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToPlaylistDetail(id: String) {
    val route = AppRoutes.PlaylistDetail.route + "/${id.ifEmpty { ARG_PLAYLIST_DETAIL_NEW }}"
    this.navigate(route)
}

fun RouteBuilder.playlistDetail(
    onNavigateBack: () -> Unit
) {
    scene(
        route = AppRoutes.PlaylistDetail.route + "/{$ARG_PLAYLIST_DETAIL}",
        navTransition = NavTransition()
    ) { backStackEntry ->

        backStackEntry.path<String>(ARG_PLAYLIST_DETAIL)?.let { id ->
            val playlistViewModel = koinViewModel(PlaylistViewModel::class)
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