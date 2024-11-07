/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player.action

import com.mvproject.tinyiptvkmp.core.domain.model.TvPlaylistChannel

sealed class PlaybackActions {

    data object OnPlaybackToggle : PlaybackActions()
    data object OnVideoResizeToggle : PlaybackActions()
    data object OnVideoRatioToggle : PlaybackActions()
    data object OnFullScreenToggle : PlaybackActions()
    data object OnFavoriteToggle : PlaybackActions()
    data object OnPlayerUiToggle : PlaybackActions()
    data object OnEpgUiToggle : PlaybackActions()
    data object OnChannelsUiToggle : PlaybackActions()
    data object OnChannelInfoUiToggle : PlaybackActions()
    data object OnNextSelected : PlaybackActions()
    data object OnPreviousSelected : PlaybackActions()
    data class OnChannelSelected(val channel: TvPlaylistChannel) : PlaybackActions()
    data object OnVolumeUp : PlaybackActions()
    data object OnVolumeDown : PlaybackActions()
    data object OnRestarted : PlaybackActions()
}