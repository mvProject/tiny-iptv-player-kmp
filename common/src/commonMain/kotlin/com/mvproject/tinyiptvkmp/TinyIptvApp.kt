/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 09.05.24, 20:51
 *
 */

package com.mvproject.tinyiptvkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.mvproject.tinyiptvkmp.data.helpers.DataUpdateHelper
import com.mvproject.tinyiptvkmp.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavigationHost
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext
import org.koin.compose.rememberKoinInject

@Composable
fun TinyIptvApp() {
    KoinContext {
        PreComposeApp {
            TinyIptvAppContent()
        }
    }
}

@Composable
private fun TinyIptvAppContent() {
    val dataUpdateHelper = rememberKoinInject<DataUpdateHelper>()
    val updateChannelsEpgInfoUseCase = rememberKoinInject<UpdateChannelsEpgInfoUseCase>()
    val epgInfoUpdateUseCase = rememberKoinInject<EpgInfoUpdateUseCase>()
    val updateEpgUseCase = rememberKoinInject<UpdateEpgUseCase>()
    val updateRemotePlaylistChannelsUseCase =
        rememberKoinInject<UpdateRemotePlaylistChannelsUseCase>()

    val scope = rememberCoroutineScope()

    scope.launch(Dispatchers.IO) {
        delay(1000L)
        dataUpdateHelper.appState
            .collect { state ->
                KLog.d("testing state $state")
                if (state.isChannelsInfoRequired) {
                    updateChannelsEpgInfoUseCase()
                }

                if (state.isEpgInfoRequired) {
                    epgInfoUpdateUseCase()
                }

                if (state.isEpgRequired) {
                    updateEpgUseCase()
                }

                state.playlistUpdates.forEach { playlist ->
                    updateRemotePlaylistChannelsUseCase(playlist = playlist)
                }
            }
    }

    VideoAppTheme {
        NavigationHost(
            startDestination = AppRoutes.PlaylistGroup.route,
        )
    }
}
