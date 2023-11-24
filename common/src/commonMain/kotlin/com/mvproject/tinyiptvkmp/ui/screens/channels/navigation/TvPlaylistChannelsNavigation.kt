/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.navigation

import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_TV_PLAYLIST_GROUP
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsView
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsViewModel
import com.mvproject.tinyiptvkmp.utils.KLog
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToTvPlaylistChannels(group: String) {
    this.navigate(
        AppRoutes.TvPlaylistChannels.route + "/$group"
    )
}

fun RouteBuilder.tvPlaylistChannels(
    onNavigateBack: () -> Unit,
    onNavigateSelected: (String, String) -> Unit,
) {
    scene(
        route = AppRoutes.TvPlaylistChannels.route + "/{$ARG_TV_PLAYLIST_GROUP}",
        navTransition = NavTransition()

    ) { backStackEntry ->

        backStackEntry.path<String>(ARG_TV_PLAYLIST_GROUP)?.let { group ->
            val tvPlaylistChannelsViewModel = koinViewModel(TvPlaylistChannelsViewModel::class)

            KLog.w("testing playlistGroupScreen group:$group")
            TvPlaylistChannelsView(
                viewModel = tvPlaylistChannelsViewModel,
                groupSelected = group,
                onAction = tvPlaylistChannelsViewModel::processAction,
                onNavigateBack = onNavigateBack,
                onNavigateSelected = { name ->
                    onNavigateSelected(name, group)
                }
            )
        }
    }
}