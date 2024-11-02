/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.playlist.PlaylistView
import com.mvproject.tinyiptvkmp.ui.screens.playlist.PlaylistViewModel
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToPlaylistDetail(id: String = String.empty) {
    val route = AppRoutes.PlaylistDetail(id = id)
    this.navigate(route)
}

fun NavGraphBuilder.playlistDetail(onNavigateBack: () -> Unit) {
    composable<AppRoutes.PlaylistDetail> {
        val playlistViewModel = koinViewModel<PlaylistViewModel>()

        PlaylistView(
            viewModel = playlistViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
