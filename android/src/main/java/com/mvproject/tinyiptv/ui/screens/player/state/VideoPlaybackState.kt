/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 01.09.23, 19:12
 *
 */

package com.mvproject.tinyiptv.ui.screens.player.state

sealed class VideoPlaybackState {
    data object VideoPlaybackReady : VideoPlaybackState()
    data object VideoPlaybackEnded : VideoPlaybackState()
    data object VideoPlaybackBuffering : VideoPlaybackState()
    data class VideoPlaybackIdle(val errorCode: Int?) : VideoPlaybackState()
}
