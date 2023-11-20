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
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsView
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsViewModel

object SettingsRoute : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val settingsViewModel: SettingsViewModel = getScreenModel()
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