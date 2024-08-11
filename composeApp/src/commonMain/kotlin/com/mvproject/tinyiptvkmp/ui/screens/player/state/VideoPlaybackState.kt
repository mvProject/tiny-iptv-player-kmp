/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player.state

sealed class VideoPlaybackState {
    data object VideoPlaybackReady : VideoPlaybackState()
    data object VideoPlaybackEnded : VideoPlaybackState()
    data object VideoPlaybackBuffering : VideoPlaybackState()
    data class VideoPlaybackIdle(val errorCode: Int?) : VideoPlaybackState()
}
