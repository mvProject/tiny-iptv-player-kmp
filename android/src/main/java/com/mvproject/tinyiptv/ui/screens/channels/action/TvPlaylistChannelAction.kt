/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.09.23, 12:23
 *
 */

package com.mvproject.tinyiptv.ui.screens.channels.action

import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel

sealed class TvPlaylistChannelAction {
    data class ToggleFavourites(val channel: TvPlaylistChannel) : TvPlaylistChannelAction()
    data class SearchTextChange(val text: String) : TvPlaylistChannelAction()
    data class ViewTypeChange(val type: ChannelsViewType) : TvPlaylistChannelAction()
    data object SearchTriggered : TvPlaylistChannelAction()
    data class ToggleEpgState(val channel: TvPlaylistChannel) : TvPlaylistChannelAction()
    data object ToggleEpgVisibility : TvPlaylistChannelAction()
}

