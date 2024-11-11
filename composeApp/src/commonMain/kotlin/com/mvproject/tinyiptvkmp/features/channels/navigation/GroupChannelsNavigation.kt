/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsScreen
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToGroupChannels(
    group: String,
    groupType: String,
) {
    this.navigate(AppRoutes.TvPlaylistChannels(group = group, groupType = groupType))
}

fun NavGraphBuilder.groupChannels(
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: (String, String, String) -> Unit,
) {
    composable<AppRoutes.TvPlaylistChannels> {
        val groupChannelsViewModel = koinViewModel<GroupChannelsViewModel>()

        GroupChannelsScreen(
            viewModel = groupChannelsViewModel,
            onNavigateBack = onNavigateBack,
            onNavigateToPlayer = onNavigateToPlayer,
        )
    }
}
