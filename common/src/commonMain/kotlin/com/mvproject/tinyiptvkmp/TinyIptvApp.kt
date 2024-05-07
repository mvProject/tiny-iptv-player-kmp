/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:11
 *
 */

package com.mvproject.tinyiptvkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetRemotePlaylistsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavigationHost
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils
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
    val preferenceRepository = rememberKoinInject<PreferenceRepository>()
    val updateChannelsEpgInfoUseCase = rememberKoinInject<UpdateChannelsEpgInfoUseCase>()
    val getRemotePlaylistsUseCase = rememberKoinInject<GetRemotePlaylistsUseCase>()
    val epgInfoUpdateUseCase = rememberKoinInject<EpgInfoUpdateUseCase>()
    val updateEpgUseCase = rememberKoinInject<UpdateEpgUseCase>()
    val updateRemotePlaylistChannelsUseCase =
        rememberKoinInject<UpdateRemotePlaylistChannelsUseCase>()

    val scope = rememberCoroutineScope()

    scope.apply {
        launch(Dispatchers.IO) {
            delay(2000L)
            preferenceRepository.isChannelsEpgInfoUpdateRequired()
                .collect { isRequired ->
                    KLog.d("testing isChannelsEpgInfoUpdateRequired $isRequired")
                    if (isRequired) {
                        updateChannelsEpgInfoUseCase()
                    }
                }
        }

        launch(Dispatchers.IO) {
            delay(1000L)
            // updateEpgUseCase()
            preferenceRepository.isEpgUpdateRequired2()
                .collect { isRequired ->
                    KLog.d("testing isEpgUpdateRequired2 $isRequired")
                    if (isRequired) {
                        // updateEpgUseCase()
                    }
                }
        }

        launch(Dispatchers.IO) {
            val isEpgInfoDataUpdateRequired = preferenceRepository.isEpgInfoDataUpdateRequired()
            KLog.e("testing isEpgInfoDataUpdateRequired $isEpgInfoDataUpdateRequired")
            if (isEpgInfoDataUpdateRequired) {
                epgInfoUpdateUseCase()
            }
        }

        launch(Dispatchers.IO) {
            delay(4000L)

            val remotePlaylists = getRemotePlaylistsUseCase()

            remotePlaylists.forEach { playlist ->
                val updateDuration = TimeUtils.typeToDuration(playlist.updatePeriod.toInt())
                val isUpdateSet = updateDuration > AppConstants.LONG_VALUE_ZERO
                val isRequiredUpdate =
                    TimeUtils.actualDate - playlist.lastUpdateDate > updateDuration
                val isUpdateAllowed = isUpdateSet && isRequiredUpdate

                KLog.w("testing remotePlaylists ${playlist.playlistTitle} isUpdateAllowed $isUpdateAllowed")
                if (isUpdateAllowed) {
                    updateRemotePlaylistChannelsUseCase(playlist = playlist)
                }
            }
        }
    }

    VideoAppTheme {
        NavigationHost(
            startDestination = AppRoutes.PlaylistGroup.route,
        )
    }
}
