/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptvkmp.features.player.state

sealed class PlaybackState {
    data object PlaybackReady : PlaybackState()
    data object PlaybackEnded : PlaybackState()
    data object PlaybackBuffering : PlaybackState()
    data class PlaybackIdle(val errorCode: Int?) : PlaybackState()
}
