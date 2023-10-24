/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 11.10.23, 10:16
 *
 */

package com.mvproject.tinyiptv.ui.screens.player.state

import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.utils.AppConstants

data class VideoViewState(
    val channelGroup: String = AppConstants.EMPTY_STRING,
    val currentChannel: TvPlaylistChannel = TvPlaylistChannel(),
    val isUseSubtitle: Boolean = false,
    val isTracksAvailable: Boolean = false,
    val isUiVisible: Boolean = false,
    val isEpgVisible: Boolean = false,
    val isChannelsVisible: Boolean = false,
    val isChannelInfoVisible: Boolean = false,
    val isFullscreen: Boolean = false,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isMediaPlayable: Boolean = true,
    val isOnline: Boolean = true,
    val videoSizeRatio: Float = 1.7777778f,
    val videoResizeMode: ResizeMode = ResizeMode.Fit,
    val mediaPosition: Int = AppConstants.INT_NO_VALUE,
    val channels: List<TvPlaylistChannel> = emptyList()
)