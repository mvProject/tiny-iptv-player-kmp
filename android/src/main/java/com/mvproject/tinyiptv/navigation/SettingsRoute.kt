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
import com.mvproject.tinyiptv.ui.screens.settings.general.SettingsView
import com.mvproject.tinyiptv.ui.screens.settings.general.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

object SettingsRoute : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val settingsViewModel: SettingsViewModel = koinViewModel()
        val settingsState by settingsViewModel.state.collectAsState()

        SettingsView(
            state = settingsState,
            onSettingsAction = settingsViewModel::processAction,
            onNavigateBack = {
                navigator.pop()
            },
            onNavigatePlayerSettings = {
                navigator.push(
                    SettingsPlayerRoute
                )
            },
            onNavigatePlaylistSettings = {
                navigator.push(
                    SettingsPlaylistRoute
                )
            }
        )
    }
}