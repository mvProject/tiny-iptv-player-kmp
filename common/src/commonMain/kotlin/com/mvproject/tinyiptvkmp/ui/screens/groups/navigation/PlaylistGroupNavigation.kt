/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.platform.ExecuteOnResume
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupView
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupViewModel
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToPlaylistGroup() {
    this.navigate(
        AppRoutes.PlaylistGroup.route,
    )
}

fun RouteBuilder.playlistGroups(
    onNavigateToSettings: () -> Unit,
    onNavigateToGroup: (String, String) -> Unit,
) {
    scene(
        route = AppRoutes.PlaylistGroup.route,
        navTransition = NavTransition(),
    ) {
        val groupViewModel = koinViewModel(GroupViewModel::class)
        val playlistDataState by groupViewModel.groupState.collectAsState()

        ExecuteOnResume {
            groupViewModel.refresh()
        }

        GroupView(
            dataState = playlistDataState,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToGroup = onNavigateToGroup,
            onPlaylistAction = groupViewModel::processAction,
        )
    }
}
