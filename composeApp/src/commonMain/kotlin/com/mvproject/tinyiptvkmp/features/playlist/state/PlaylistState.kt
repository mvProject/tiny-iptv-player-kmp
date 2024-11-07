/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist.state

import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

data class PlaylistState(
    val selectedId: Long = LONG_VALUE_ZERO,
    val playlistName: String = String.empty,
    val playlistSource: String = String.empty,
    val playlistType: PlaylistType = PlaylistType.REMOTE,
    val updatePeriod: Int = UpdatePeriod.NO_UPDATE.value,
    val lastUpdateDate: Long = LONG_VALUE_ZERO,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val isComplete: Boolean = false,
) {
    val isReadyToSave: Boolean
        get() = playlistName.isNotBlank() && playlistName.isNotBlank()

    fun toPlaylist() =
        with(this) {
            Playlist(
                id = selectedId,
                playlistName = playlistName,
                playlistSource = playlistSource,
                playlistType = playlistType,
                lastUpdateDate = lastUpdateDate,
                updatePeriod = updatePeriod.toLong(),
            )
        }
}
