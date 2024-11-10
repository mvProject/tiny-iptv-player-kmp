/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptvkmp.features.player.action

import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.player.state.PlaybackState

sealed class UiActions {
    data object TogglePlayback : UiActions()
    data object ChangeVideoSize : UiActions()
    data object ChangeVideoRatio : UiActions()
    data object ToggleFullScreen : UiActions()
    data object ToggleChannelFavorite : UiActions()
    data object TogglePlayerUi : UiActions()
    data object ToggleProgramsUi : UiActions()
    data object ToggleChannelsUi : UiActions()
    data object ToggleProgramInfoUi : UiActions()
    data object SelectNext : UiActions()
    data object SelectPrevious : UiActions()
    data object VolumeUp : UiActions()
    data object VolumeDown : UiActions()

    //data object Restart : UiActions()
    data class SelectChannel(val channel: TvChannel) : UiActions()
    data class OnVideoSizeChanged(val height: Int, val width: Int, val videoRatio: Float) :
        UiActions()

    data class OnIsPlayingChanged(val state: Boolean) : UiActions()
    data class OnPlaybackStateChanged(val state: PlaybackState) : UiActions()
}