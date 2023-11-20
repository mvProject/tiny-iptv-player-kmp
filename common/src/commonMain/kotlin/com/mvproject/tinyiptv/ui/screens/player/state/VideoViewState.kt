/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 11.10.23, 10:16
 *
 */

package com.mvproject.tinyiptv.ui.screens.player.state

import com.mvproject.tinyiptv.data.enums.RatioMode
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.utils.AppConstants

data class VideoViewState(
    val channelGroup: String = AppConstants.EMPTY_STRING,
    val currentChannel: TvPlaylistChannel = TvPlaylistChannel(),
    val isRestartRequired: Boolean = false,
    val isUseSubtitle: Boolean = false,
    val isTracksAvailable: Boolean = false,
    val isControlUiVisible: Boolean = false,
    val isVolumeUiVisible: Boolean = false,
    val isEpgVisible: Boolean = false,
    val isChannelsVisible: Boolean = false,
    val isChannelInfoVisible: Boolean = false,
    val isFullscreen: Boolean = false,
    val isPlaying: Boolean = false,
    val currentVolume: Float = 0.5f,
    val isBuffering: Boolean = false,
    val isMediaPlayable: Boolean = true,
    val isOnline: Boolean = true,
    val videoRatioMode: RatioMode = RatioMode.WideScreen,
    val videoRatio: Float = RatioMode.WideScreen.ratio,
    val videoResizeMode: ResizeMode = ResizeMode.Fit,
    val mediaPosition: Int = AppConstants.INT_NO_VALUE,
    val channels: List<TvPlaylistChannel> = emptyList()
)