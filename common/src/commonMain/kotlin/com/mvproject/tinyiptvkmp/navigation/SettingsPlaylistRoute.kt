/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.SettingsPlaylistView
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.SettingsPlaylistViewModel

object SettingsPlaylistRoute : Screen {

    @Composable
    override fun Content() {
        val settingsPlaylistViewModel: SettingsPlaylistViewModel = getScreenModel()
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