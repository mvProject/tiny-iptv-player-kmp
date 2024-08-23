/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 14:58
 *
 */

package com.mvproject.tinyiptvkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.mvproject.tinyiptvkmp.data.helpers.DataUpdateHelper
import com.mvproject.tinyiptvkmp.data.helpers.DataUpdateState
import com.mvproject.tinyiptvkmp.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptvkmp.data.usecases.SavePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavigationHost
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.ImageUtils.getAsyncImageLoader
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TinyIptvApp() {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    KoinContext {
        TinyIptvAppContent()
    }
}

@Composable
private fun TinyIptvAppContent() {
    val dataUpdateHelper = koinInject<DataUpdateHelper>()
    val updateChannelsEpgInfoUseCase = koinInject<UpdateChannelsEpgInfoUseCase>()
    val epgInfoUpdateUseCase = koinInject<EpgInfoUpdateUseCase>()
    val savePlaylistContentUseCase = koinInject<SavePlaylistContentUseCase>()
    val updateEpgUseCase = koinInject<UpdateEpgUseCase>()
    val updateRemotePlaylistChannelsUseCase =
        koinInject<UpdateRemotePlaylistChannelsUseCase>()

    val appState by dataUpdateHelper.appState.collectAsStateWithLifecycle(DataUpdateState())

    LaunchedEffect(appState.isChannelsInfoRequired) {
        withContext(Dispatchers.IO) {
            if (appState.isChannelsInfoRequired != LONG_NO_VALUE) {
                delay(500)
                updateChannelsEpgInfoUseCase(playlistId = appState.isChannelsInfoRequired)
            }
        }
    }

    LaunchedEffect(appState.isEpgInfoRequired) {
        withContext(Dispatchers.IO) {
            if (appState.isEpgInfoRequired) {
                KLog.d("testing Launched epgInfoUpdateUseCase")
                delay(1000)
                epgInfoUpdateUseCase()
            }
        }
    }

    LaunchedEffect(appState.playlistUpdates) {
        withContext(Dispatchers.IO) {
            delay(500)
            appState.playlistUpdates.forEach { playlist ->
                KLog.d("testing Launched updateRemotePlaylistChannelsUseCase")
                updateRemotePlaylistChannelsUseCase(playlist = playlist)
            }
        }
    }

    LaunchedEffect(appState.infoExist) {
        withContext(Dispatchers.IO) {
            if (appState.infoExist) {
                KLog.d("testing Launched updateEpgUseCase")
                delay(2000)
                //    updateEpgUseCase()
            }
        }
    }

    LaunchedEffect(appState.playlistContentId) {
        withContext(Dispatchers.IO) {
            if (appState.playlistContentId != LONG_NO_VALUE) {
                KLog.d("testing Launched savePlaylistContentUseCase")
                savePlaylistContentUseCase(playlistId = appState.playlistContentId)
            }
        }
    }

    VideoAppTheme {
        NavigationHost(
            startDestination = AppRoutes.PlaylistGroup.route,
        )
    }
}
