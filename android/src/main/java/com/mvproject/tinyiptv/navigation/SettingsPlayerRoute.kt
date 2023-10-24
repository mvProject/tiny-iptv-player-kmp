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
import com.mvproject.tinyiptv.ui.screens.settings.player.SettingsPlayerView
import com.mvproject.tinyiptv.ui.screens.settings.player.SettingsPlayerViewModel
import org.koin.androidx.compose.koinViewModel

object SettingsPlayerRoute : Screen {

    @Composable
    override fun Content() {
        val settingsPlayerViewModel: SettingsPlayerViewModel = koinViewModel()
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