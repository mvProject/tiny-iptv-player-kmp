/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.05.24, 19:17
 *
 */

package com.mvproject.tinyiptvkmp.features.player.state

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

@Immutable
data class TvPlayerState(
    val channelGroup: String = String.empty,
    val currentChannel: TvChannel = TvChannel(),
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
    val channelIndex: Int = AppConstants.INT_NO_VALUE,
    val groupChannels: List<TvChannel> = emptyList(),
)
