/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:12
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.action

import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel

sealed class TvPlaylistChannelAction {
    data class ToggleFavourites(val channel: TvPlaylistChannel) : TvPlaylistChannelAction()

    data class SearchTextChange(val text: String) : TvPlaylistChannelAction()

    data class ViewTypeChange(val type: ChannelsViewType) : TvPlaylistChannelAction()

    data object SearchTriggered : TvPlaylistChannelAction()

    data object ToggleEpgVisibility : TvPlaylistChannelAction()
}
