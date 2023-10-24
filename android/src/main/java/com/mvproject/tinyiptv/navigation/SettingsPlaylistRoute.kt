/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptv.ui.screens.settings.playlist.SettingsPlaylistView
import com.mvproject.tinyiptv.ui.screens.settings.playlist.SettingsPlaylistViewModel
import org.koin.androidx.compose.koinViewModel

object SettingsPlaylistRoute : Screen {

    @Composable
    override fun Content() {
        val settingsPlaylistViewModel: SettingsPlaylistViewModel = koinViewModel()
        val playlistDataState by settingsPlaylistViewModel.playlistDataState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        SettingsPlaylistView(
            dataState = playlistDataState,
            onPlaylistAction = settingsPlaylistViewModel::processAction,
            onNavigatePlaylist = { playlistId ->
                navigator.push(PlaylistDetailRoute(id = playlistId))
            },
            onNavigateBack = {
                navigator.pop()
            }
        )
    }
}