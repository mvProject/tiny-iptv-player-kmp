/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupView
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToPlaylistGroup() {
    this.navigate(
        AppRoutes.PlaylistGroup.route,
    )
}

fun NavGraphBuilder.playlistGroups(
    onNavigateToSettings: () -> Unit,
    onNavigateToGroup: (String, String) -> Unit,
) {
    composable(route = AppRoutes.PlaylistGroup.route) {
        val groupViewModel = koinViewModel<GroupViewModel>()

        val groupState by groupViewModel.groupState.collectAsStateWithLifecycle()
        val groupUiState by groupViewModel.groupUiState.collectAsStateWithLifecycle()

        GroupView(
            state = groupState,
            uiState = groupUiState,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToGroup = onNavigateToGroup,
            onPlaylistAction = groupViewModel::processAction,
        )
    }
}
