/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist.state

import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.data.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

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
