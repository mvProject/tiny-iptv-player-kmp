/*
 *  Created by Medvediev Viktor [mvproject] 
 *  Copyright © 2023
 *  last modified : 20.10.23, 16:29
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun PlayerControlView(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isFullScreen: Boolean,
    onFavoriteToggle: () -> Unit,
    onPlaybackToggle: () -> Unit,
    onVideoResizeToggle: () -> Unit,
    onFullScreenToggle: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = onPlaybackToggle,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "PLAYBACK_TOGGLE",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onFavoriteToggle,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = "FAVORITE_TOGGLE",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            IconButton(
                onClick = onVideoResizeToggle,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Crop,
                    contentDescription = "TOGGLE_RESIZE_MODE",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            IconButton(
                onClick = onFullScreenToggle,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
            ) {
                Icon(
                    imageVector = if (isFullScreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                    contentDescription = "TOGGLE_FULL_SCREEN",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}