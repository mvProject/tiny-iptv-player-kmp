/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptv.ui.screens.channels.TvPlaylistChannelsView
import com.mvproject.tinyiptv.ui.screens.channels.TvPlaylistChannelsViewModel

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