/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player.action

import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoPlaybackState

sealed class PlaybackStateActions {
    data class OnVideoSizeChanged(val height: Int, val width: Int, val videoRatio: Float) :
        PlaybackStateActions()

    data class OnIsPlayingChanged(val state: Boolean) : PlaybackStateActions()
    data class OnPlaybackStateChanged(val state: VideoPlaybackState) : PlaybackStateActions()
    data class OnMediaItemTransition(val mediaTitle: String, val index: Int) :
        PlaybackStateActions()
}