/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist.state

import com.mvproject.tinyiptvkmp.data.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class PlaylistState(
    val selectedId: Long = AppConstants.LONG_VALUE_ZERO,
    val listName: String = String.empty,
    val url: String = String.empty,
    val uri: String = String.empty,
    val localName: String = String.empty,
    val isLocal: Boolean = false,
    val updatePeriod: Int = UpdatePeriod.NO_UPDATE.value,
    val lastUpdateDate: Long = AppConstants.LONG_VALUE_ZERO,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val isComplete: Boolean = false,
) {
    val isReadyToSave: Boolean
        get() {
            return if (isEdit) {
                true
            } else if (isLocal) {
                listName.isNotEmpty() && uri.isNotEmpty()
            } else {
                listName.isNotEmpty() && url.isNotEmpty()
            }
        }

    fun toPlaylist() =
        with(this) {
            Playlist(
                id = selectedId,
                playlistTitle = listName,
                playlistUrl = url,
                playlistLocalName = localName,
                lastUpdateDate = lastUpdateDate,
                updatePeriod = updatePeriod.toLong(),
                isLocalSource = isLocal,
            )
        }
}
