/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:55
 *
 */

package com.mvproject.tinyiptvkmp.data.helpers

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate
import com.mvproject.tinyiptvkmp.utils.TimeUtils.typeToDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine

class DataUpdateHelper(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
) {
    val appState =
        combine(
            preferenceRepository.isChannelsEpgInfoUpdateRequired(),
            preferenceRepository.isEpgInfoDataExist(),
            preferenceRepository.lastEpgUpdate(),
        ) { isChannelsInfoRequired, infoExist, _ ->

            val remote =
                playlistsRepository
                    .getAllPlaylistsRoom()
                    .filter { playlist ->
                        !playlist.isLocalSource
                    }

            val playlistUpdates =
                buildList {
                    remote.forEach { playlist ->
                        val updateDuration = typeToDuration(playlist.updatePeriod.toInt())
                        val isUpdateSet = updateDuration > AppConstants.LONG_VALUE_ZERO
                        val isRequiredUpdate =
                            actualDate - playlist.lastUpdateDate > updateDuration
                        val isUpdateAllowed = isUpdateSet && isRequiredUpdate

                        KLog.w("testing remotePlaylists ${playlist.playlistTitle} isUpdateAllowed $isUpdateAllowed")
                        if (isUpdateAllowed) {
                            add(playlist)
                        }
                    }
                }

            val isEpgInfoDataUpdateRequired = preferenceRepository.isEpgInfoDataUpdateRequired()

            delay(1000)

            return@combine DataUpdateState(
                infoExist = infoExist,
                isChannelsInfoRequired = infoExist && isChannelsInfoRequired,
                isEpgInfoRequired = isEpgInfoDataUpdateRequired,
                playlistUpdates = playlistUpdates,
            )
        }
}

@Immutable
data class DataUpdateState(
    val isChannelsInfoRequired: Boolean = false,
    val isEpgInfoRequired: Boolean = false,
    val infoExist: Boolean = false,
    val playlistUpdates: List<Playlist> = emptyList(),
)
