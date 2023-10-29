/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptv.ui.screens.player.action

import com.mvproject.tinyiptv.ui.screens.player.state.VideoPlaybackState

sealed class PlaybackStateActions {
    data class OnPlaybackState(val state: VideoPlaybackState) : PlaybackStateActions()
    data class OnMediaItemTransition(val mediaTitle: String, val index: Int) :
        PlaybackStateActions()
}