/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:53
 *
 */

package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.components.buttons.ControlButton
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.mapper.mapToIcon
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.player.PlayerAction
import com.mvproject.tinyiptvkmp.features.player.PlayerState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.AdditionalMediaPlayerControls

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    videoSize: VideoSize,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isFullScreen: Boolean,
    onAction: (PlayerAction) -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        AdditionalMediaPlayerControls(
            modifier = Modifier,
            onNavigateBack = { onAction(PlayerAction.NavigateBack) },
            onVolumeDown = { onAction(PlayerAction.VolumeDown) },
            onVolumeUp = { onAction(PlayerAction.VolumeUp) },
            onSelectPrevious = { onAction(PlayerAction.SelectPrevious) },
            onSelectNext = { onAction(PlayerAction.SelectNext) },
            onOpenPrograms = { onAction(PlayerAction.OpenOsd(PlayerState.PlayerOSD.ChannelPrograms)) },
            onOpenChannels = { onAction(PlayerAction.OpenOsd(PlayerState.PlayerOSD.GroupChannels)) },
            onOpenInfo = { onAction(PlayerAction.OpenOsd(PlayerState.PlayerOSD.ProgramInfo)) },
        )

        ControlButton(
            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            onClick = { onAction(PlayerAction.TogglePlayback) },
        )

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            ControlButton(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                onClick = { onAction(PlayerAction.OpenOsd(PlayerState.PlayerOSD.ChannelFavorites)) },
            )

            SpacerWidth(width = MaterialTheme.dimensionSize.size8)
            ControlButton(
                imageVector = videoSize.mapToIcon(),
                onClick = { onAction(PlayerAction.ChangeVideoSize) },
            )

            SpacerWidth(width = MaterialTheme.dimensionSize.size8)
            ControlButton(
                imageVector = if (isFullScreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                onClick = { onAction(PlayerAction.ToggleFullScreen) },
            )
        }
    }
}
