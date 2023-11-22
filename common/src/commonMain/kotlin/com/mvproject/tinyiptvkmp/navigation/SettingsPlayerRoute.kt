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
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.SettingsPlayerView
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.SettingsPlayerViewModel

object SettingsPlayerRoute : Screen {

    @Composable
    override fun Content() {
        val settingsPlayerViewModel: SettingsPlayerViewModel = getScreenModel()
        val state by settingsPlayerViewModel.settingsPlayerState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        SettingsPlayerView(
            state = state,
            onSettingsPlayerAction = settingsPlayerViewModel::processAction,
            onNavigateBack = {
                navigator.pop()
            }
        )
    }
}