/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 28.10.23, 19:21
 *
 */

package com.mvproject.tinyiptv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.navigator.Navigator
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptv.data.usecases.GetRemotePlaylistsUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptv.navigation.PlaylistDataRoute
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.KLog
import com.mvproject.tinyiptv.utils.TimeUtils
import kotlinx.coroutines.launch
import org.koin.compose.rememberKoinInject

@Composable
fun TinyIptvApp() {
    val scope = rememberCoroutineScope()

    val preferenceRepository = rememberKoinInject<PreferenceRepository>()
    val updateChannelsEpgInfoUseCase = rememberKoinInject<UpdateChannelsEpgInfoUseCase>()
    val getRemotePlaylistsUseCase = rememberKoinInject<GetRemotePlaylistsUseCase>()
    val epgInfoUpdateUseCase = rememberKoinInject<EpgInfoUpdateUseCase>()
    val updateEpgUseCase = rememberKoinInject<UpdateEpgUseCase>()
    val updateRemotePlaylistChannelsUseCase =
        rememberKoinInject<UpdateRemotePlaylistChannelsUseCase>()
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
        Navigator(
            screen = PlaylistDataRoute
        )
    }

}