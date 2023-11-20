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
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupView
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupViewModel

object PlaylistDataRoute : Screen {

    @Composable
    override fun Content() {
        val groupViewModel: GroupViewModel = getScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val playlistDataState by groupViewModel.playlistDataState.collectAsState()

        GroupView(
            dataState = playlistDataState,
            onNavigateToSettings = {
                navigator.push(SettingsRoute)
            },
            onNavigateToGroup = {
                navigator.push(
                    PlaylistGroupScreenRoute(group = it)
                )
            },
            onPlaylistAction = groupViewModel::processAction
        )
    }
}