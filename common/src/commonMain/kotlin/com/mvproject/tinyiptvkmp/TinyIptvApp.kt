/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
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
        launch {
            preferenceRepository.isChannelsEpgInfoUpdateRequired()
                .collect { isRequired ->
                    if (isRequired) {
                        updateChannelsEpgInfoUseCase()
                    }
                }
        }
        launch {
            preferenceRepository.isEpgUpdateRequired()
                .collect { isRequired ->
                    if (isRequired) {
                        updateEpgUseCase()
                    }
                }
        }
        launch {
            val isEpgInfoDataUpdateRequired = preferenceRepository.isEpgInfoDataUpdateRequired()
            if (isEpgInfoDataUpdateRequired) {
                epgInfoUpdateUseCase()
            }
        }

        launch {
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
            startDestination = AppRoutes.PlaylistGroup.route
        )
    }

}