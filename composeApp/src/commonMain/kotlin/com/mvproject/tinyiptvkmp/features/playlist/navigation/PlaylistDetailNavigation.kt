/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistScreen
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToPlaylistDetail(id: String = String.empty) {
    val route = AppRoutes.PlaylistDetail(id = id)
    this.navigate(route)
}

fun NavGraphBuilder.playlistDetail(onNavigateBack: () -> Unit) {
    composable<AppRoutes.PlaylistDetail> {
        val playlistViewModel = koinViewModel<PlaylistViewModel>()

        PlaylistScreen(
            viewModel = playlistViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
