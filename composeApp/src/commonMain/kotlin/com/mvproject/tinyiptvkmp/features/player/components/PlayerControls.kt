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
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.Crop
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
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.buttons.ControlButton
import com.mvproject.tinyiptvkmp.core.ui.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.features.player.action.PlaybackActions

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isFullScreen: Boolean,
    onPlaybackAction: (PlaybackActions) -> Unit = {},
    onPlaybackClose: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        AdditionalPlayerControls(
            modifier = Modifier,
            action = onPlaybackClose,
            onPlaybackAction = onPlaybackAction,
        )

        ControlButton(
            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            onClick = { onPlaybackAction(PlaybackActions.OnPlaybackToggle) },
        )

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            ControlButton(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                onClick = { onPlaybackAction(PlaybackActions.OnFavoriteToggle) },
            )

            SpacerWidth(width = MaterialTheme.dimens.size8)
            ControlButton(
                imageVector = Icons.Rounded.AspectRatio,
                onClick = { onPlaybackAction(PlaybackActions.OnVideoRatioToggle) },
            )

            SpacerWidth(width = MaterialTheme.dimens.size8)
            ControlButton(
                imageVector = Icons.Rounded.Crop,
                onClick = { onPlaybackAction(PlaybackActions.OnVideoResizeToggle) },
            )

            SpacerWidth(width = MaterialTheme.dimens.size8)
            ControlButton(
                imageVector = if (isFullScreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                onClick = { onPlaybackAction(PlaybackActions.OnFullScreenToggle) },
            )
        }
    }
}
