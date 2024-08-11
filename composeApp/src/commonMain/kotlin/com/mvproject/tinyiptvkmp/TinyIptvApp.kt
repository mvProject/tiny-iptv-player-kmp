/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 14:58
 *
 */

package com.mvproject.tinyiptvkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mvproject.tinyiptvkmp.data.helpers.DataUpdateHelper
import com.mvproject.tinyiptvkmp.data.helpers.DataUpdateState
import com.mvproject.tinyiptvkmp.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavigationHost
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.delay
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun TinyIptvApp() {
    KoinContext {
        //PreComposeApp {
            TinyIptvAppContent()
       // }
    }
}

@Composable
private fun TinyIptvAppContent() {
    val dataUpdateHelper = koinInject<DataUpdateHelper>()
    val updateChannelsEpgInfoUseCase = koinInject<UpdateChannelsEpgInfoUseCase>()
    val epgInfoUpdateUseCase = koinInject<EpgInfoUpdateUseCase>()
    val updateEpgUseCase = koinInject<UpdateEpgUseCase>()
    val updateRemotePlaylistChannelsUseCase =
        koinInject<UpdateRemotePlaylistChannelsUseCase>()

    val appState by dataUpdateHelper.appState.collectAsState(DataUpdateState())

    LaunchedEffect(appState) {
        KLog.d("testing LaunchedEffect appState $appState")
        if (appState.isChannelsInfoRequired) {
            delay(500)
            updateChannelsEpgInfoUseCase()
        }

        if (appState.isEpgInfoRequired) {
            delay(1000)
            epgInfoUpdateUseCase()
        }
    }

    LaunchedEffect(appState.playlistUpdates) {
        KLog.d("testing LaunchedEffect appState playlistUpdates ${appState.playlistUpdates}")
        delay(500)
        appState.playlistUpdates.forEach { playlist ->
            updateRemotePlaylistChannelsUseCase(playlist = playlist)
        }
    }

    LaunchedEffect(appState.infoExist) {
        KLog.d("testing LaunchedEffect appState infoExist ${appState.infoExist}")
        if (appState.infoExist) {
            delay(2000)
            //    updateEpgUseCase()
        }
    }

    VideoAppTheme {
        NavigationHost(
            startDestination = AppRoutes.PlaylistGroup.route,
        )
    }
}
