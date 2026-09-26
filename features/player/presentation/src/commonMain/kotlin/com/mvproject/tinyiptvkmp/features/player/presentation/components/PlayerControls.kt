/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:53
 *
 */

package com.mvproject.tinyiptvkmp.features.player.presentation.components

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
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_change_video_size
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_exit_fullscreen
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_favorites
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_fullscreen
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_pause
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_play
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.mapper.mapToIcon
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerAction
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerState
import com.mvproject.tinyiptvkmp.platform.mediaplayer.AdditionalMediaPlayerControls
import org.jetbrains.compose.resources.stringResource

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
            contentDescription = stringResource(
                if (isPlaying) Res.string.action_pause else Res.string.action_play
            ),
            onClick = { onAction(PlayerAction.TogglePlayback) },
        )

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            ControlButton(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = stringResource(Res.string.action_favorites),
                onClick = { onAction(PlayerAction.OpenOsd(PlayerState.PlayerOSD.ChannelFavorites)) },
            )

            SpacerWidth(width = MaterialTheme.dimensionSize.size8)
            ControlButton(
                imageVector = videoSize.mapToIcon(),
                contentDescription = stringResource(Res.string.action_change_video_size),
                onClick = { onAction(PlayerAction.ChangeVideoSize) },
            )

            SpacerWidth(width = MaterialTheme.dimensionSize.size8)
            ControlButton(
                imageVector = if (isFullScreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                contentDescription = stringResource(
                    if (isFullScreen) Res.string.action_exit_fullscreen else Res.string.action_fullscreen
                ),
                onClick = { onAction(PlayerAction.ToggleFullScreen) },
            )
        }
    }
}
