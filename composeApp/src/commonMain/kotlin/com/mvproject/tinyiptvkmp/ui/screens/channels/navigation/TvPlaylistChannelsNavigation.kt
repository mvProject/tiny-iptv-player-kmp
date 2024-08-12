/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_TV_PLAYLIST_GROUP
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_TV_PLAYLIST_TYPE
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsScreen
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToTvPlaylistChannels(
    group: String,
    groupType: String,
) {
    val route = AppRoutes.TvPlaylistChannels.route + "/$group/$groupType"
    this.navigate(route)
}

internal class TvPlaylistChannelsArgs(
    val group: String,
    val type: String,
) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            group = checkNotNull(savedStateHandle[ARG_TV_PLAYLIST_GROUP]) as String,
            type = checkNotNull(savedStateHandle[ARG_TV_PLAYLIST_TYPE]) as String,
        )
}

fun NavGraphBuilder.tvPlaylistChannels(
    onNavigateBack: () -> Unit,
    onNavigateSelected: NavigationGroup,
) {
    composable(
        route = AppRoutes.TvPlaylistChannels.route + "/{$ARG_TV_PLAYLIST_GROUP}/{$ARG_TV_PLAYLIST_TYPE}",
    ) {
        val tvPlaylistChannelsViewModel = koinViewModel<TvPlaylistChannelsViewModel>()

        TvPlaylistChannelsScreen(
            viewModel = tvPlaylistChannelsViewModel,
            onAction = tvPlaylistChannelsViewModel::processAction,
            onNavigateBack = onNavigateBack,
            onNavigateSelected = onNavigateSelected,
        )
    }
}

typealias NavigationGroup = (String, String) -> Unit
