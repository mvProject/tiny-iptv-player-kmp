/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsView
import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsViewModel

data class PlaylistGroupScreenRoute(
    val group: String
) : Screen {

    @Composable
    override fun Content() {
        val tvPlaylistChannelsViewModel: TvPlaylistChannelsViewModel = getScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        TvPlaylistChannelsView(
            viewModel = tvPlaylistChannelsViewModel,
            groupSelected = group,
            onAction = tvPlaylistChannelsViewModel::processAction,
            onNavigateBack = {
                navigator.pop()
            },
            onNavigateSelected = { url ->
                navigator.push(
                    PlayerScreenRoute(
                        mediaUrl = url,
                        mediaGroup = group
                    )
                )
            }
        )
    }
}