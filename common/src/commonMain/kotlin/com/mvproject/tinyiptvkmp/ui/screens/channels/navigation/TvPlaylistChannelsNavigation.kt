/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.navigation

import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_TV_PLAYLIST_GROUP
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_TV_PLAYLIST_TYPE
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsView
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsViewModel
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToTvPlaylistChannels(
    group: String,
    groupType: String,
) {
    val route = AppRoutes.TvPlaylistChannels.route + "/$group/$groupType"
    this.navigate(route)
}

fun RouteBuilder.tvPlaylistChannels(
    onNavigateBack: () -> Unit,
    onNavigateSelected: (String, String) -> Unit,
) {
    scene(
        route = AppRoutes.TvPlaylistChannels.route + "/{$ARG_TV_PLAYLIST_GROUP}/{$ARG_TV_PLAYLIST_TYPE}",
        navTransition = NavTransition(),
    ) { backStackEntry ->
        val selectedGroup = backStackEntry.path<String>(ARG_TV_PLAYLIST_GROUP)
        val type = backStackEntry.path<String>(ARG_TV_PLAYLIST_TYPE)
        selectedGroup?.let { group ->
            val tvPlaylistChannelsViewModel = koinViewModel(TvPlaylistChannelsViewModel::class)

            TvPlaylistChannelsView(
                viewModel = tvPlaylistChannelsViewModel,
                selectedGroup = group,
                selectedGroupType = type ?: GroupType.ALL.name,
                onAction = tvPlaylistChannelsViewModel::processAction,
                onNavigateBack = onNavigateBack,
                onNavigateSelected = { name ->
                    onNavigateSelected(name, group)
                },
            )
        }
    }
}
