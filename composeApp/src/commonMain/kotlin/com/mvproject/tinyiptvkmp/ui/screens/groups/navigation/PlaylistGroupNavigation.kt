/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupScreen
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToPlaylistGroup() {
    this.navigate(AppRoutes.PlaylistGroup)
}

fun NavGraphBuilder.playlistGroups(
    onNavigateToSettings: () -> Unit,
    onNavigateToGroup: (String, String) -> Unit,
) {
    composable<AppRoutes.PlaylistGroup> {
        val groupViewModel = koinViewModel<GroupViewModel>()

        GroupScreen(
            viewModel = groupViewModel,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToGroup = onNavigateToGroup,
        )
    }
}
