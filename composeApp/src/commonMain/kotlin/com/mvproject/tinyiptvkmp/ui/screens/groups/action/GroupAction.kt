/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.groups.action

import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

sealed class GroupAction {
    data class SelectPlaylist(
        val playlist: Playlist,
    ) : GroupAction()

    data object RefreshPlaylist : GroupAction()
}
