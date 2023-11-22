/*
 *  Created by Medvediev Viktor [mvproject] 
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptvkmp.platform.AdditionalPlayerControls
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
fun PlayerControlView(
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
        horizontalArrangement = Arrangement.Start
    ) {

        AdditionalPlayerControls(
            modifier = Modifier,
            action = onPlaybackClose,
            onPlaybackAction = onPlaybackAction
        )

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnPlaybackToggle)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            contentDescription = "PLAYBACK_TOGGLE",
            tint = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                modifier = Modifier
                    .clickable {
                        onPlaybackAction(PlaybackActions.OnFavoriteToggle)
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(MaterialTheme.dimens.size8),
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = "FAVORITE_TOGGLE",
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Icon(
                modifier = Modifier
                    .clickable {
                        onPlaybackAction(PlaybackActions.OnVideoRatioToggle)
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(MaterialTheme.dimens.size8),
                imageVector = Icons.Rounded.AspectRatio,
                contentDescription = "TOGGLE_RATIO_MODE",
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Icon(
                modifier = Modifier
                    .clickable {
                        onPlaybackAction(PlaybackActions.OnVideoResizeToggle)
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(MaterialTheme.dimens.size8),
                imageVector = Icons.Rounded.Crop,
                contentDescription = "TOGGLE_RESIZE_MODE",
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            Icon(
                modifier = Modifier
                    .clickable {
                        onPlaybackAction(PlaybackActions.OnFullScreenToggle)
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(MaterialTheme.dimens.size8),
                imageVector = if (isFullScreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                contentDescription = "TOGGLE_FULL_SCREEN",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}