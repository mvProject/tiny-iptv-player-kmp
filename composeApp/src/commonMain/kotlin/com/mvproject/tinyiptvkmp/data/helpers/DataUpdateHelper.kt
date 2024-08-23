/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:55
 *
 */

package com.mvproject.tinyiptvkmp.data.helpers

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
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
            preferenceRepository.idForPlaylistContentLoad(),
            preferenceRepository.idForPlaylistContentEpgInfoUpdate(),
            preferenceRepository.isEpgInfoDataExist(),
            preferenceRepository.lastEpgUpdate(),
        ) { playlistContentId, playlistContentEpgInfoId, infoExist, _ ->

            val remote =
                playlistsRepository
                    .getAllPlaylists()
                    .filter { playlist -> playlist.playlistType == PlaylistType.REMOTE }

            val playlistUpdates =
                buildList {
                    remote.forEach { playlist ->
                        val updateDuration = typeToDuration(playlist.updatePeriod.toInt())
                        val isUpdateSet = updateDuration > AppConstants.LONG_VALUE_ZERO
                        val isRequiredUpdate =
                            actualDate - playlist.lastUpdateDate > updateDuration
                        val isUpdateAllowed = isUpdateSet && isRequiredUpdate

                        KLog.w("testing remotePlaylists ${playlist.playlistName} isUpdateAllowed $isUpdateAllowed")
                        if (isUpdateAllowed) {
                            add(playlist)
                        }
                    }
                }

            val isEpgInfoDataUpdateRequired = preferenceRepository.isEpgInfoDataUpdateRequired()

            delay(1000)

            return@combine DataUpdateState(
                playlistContentId = playlistContentId,
                infoExist = infoExist,
                isChannelsInfoRequired = if (infoExist) playlistContentEpgInfoId else LONG_NO_VALUE,
                isEpgInfoRequired = isEpgInfoDataUpdateRequired,
                playlistUpdates = playlistUpdates,
            )
        }
}

@Immutable
data class DataUpdateState(
    val playlistContentId: Long = LONG_NO_VALUE,
    val isChannelsInfoRequired: Long = LONG_NO_VALUE,
    val isEpgInfoRequired: Boolean = false,
    val infoExist: Boolean = false,
    val playlistUpdates: List<Playlist> = emptyList(),
)
