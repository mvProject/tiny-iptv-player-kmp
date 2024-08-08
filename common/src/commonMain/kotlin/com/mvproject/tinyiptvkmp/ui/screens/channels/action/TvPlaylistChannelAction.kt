/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 17:47
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.action

import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel

sealed class TvPlaylistChannelAction {
    data class ToggleFavourites(
        val channel: TvPlaylistChannel,
        val type: FavoriteType,
    ) : TvPlaylistChannelAction()

    data class SearchTextChange(
        val text: String,
    ) : TvPlaylistChannelAction()

    data class ViewTypeChange(
        val type: ChannelsViewType,
    ) : TvPlaylistChannelAction()

    data object SearchTriggered : TvPlaylistChannelAction()

    data object ToggleEpgVisibility : TvPlaylistChannelAction()
}
