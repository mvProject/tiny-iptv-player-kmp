/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 28.10.23, 15:06
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptv.ui.screens.playlist.PlaylistView
import com.mvproject.tinyiptv.ui.screens.playlist.PlaylistViewModel
import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.KLog

data class PlaylistDetailRoute(
    val id: String = AppConstants.EMPTY_STRING
) : Screen {

    @Composable
    override fun Content() {
        val playlistViewModel: PlaylistViewModel = getScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(key1 = id) {
            KLog.w("testing PlaylistDetailRoute playlistId:$id")
            playlistViewModel.setPlaylistMode(id)
        }

        val state by playlistViewModel.state.collectAsState()

        PlaylistView(
            state = state,
            onPlaylistAction = playlistViewModel::processAction,
            onNavigateBack = {
                navigator.pop()
            }
        )
    }
}




