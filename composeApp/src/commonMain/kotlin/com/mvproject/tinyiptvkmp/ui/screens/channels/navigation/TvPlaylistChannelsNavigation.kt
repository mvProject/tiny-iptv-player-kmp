/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsScreen
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToTvPlaylistChannels(
    group: String,
    groupType: String,
) {
    this.navigate(AppRoutes.TvPlaylistChannels(group = group, groupType = groupType))
}

fun NavGraphBuilder.tvPlaylistChannels(
    onNavigateBack: () -> Unit,
    onNavigateSelected: NavigationGroup,
) {
    composable<AppRoutes.TvPlaylistChannels> {
        val tvPlaylistChannelsViewModel = koinViewModel<TvPlaylistChannelsViewModel>()

        TvPlaylistChannelsScreen(
            viewModel = tvPlaylistChannelsViewModel,
            onNavigateBack = onNavigateBack,
            onNavigateSelected = onNavigateSelected,
        )
    }
}

typealias NavigationGroup = (String, String) -> Unit
