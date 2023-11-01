/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 16:28
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptv.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.ui.components.epg.PlayerChannelEpgItem
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants

@Composable
fun PlayerChannelView(
    modifier: Modifier = Modifier,
    currentChannel: TvPlaylistChannel,
    programCount: Int = AppConstants.INT_VALUE_1,
    isVisible: Boolean = false,
    isPlaying: Boolean = false,
    isFullScreen: Boolean = false,
    onPlaybackClose: () -> Unit = {},
    onPlaybackAction: (PlaybackActions) -> Unit = {}
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .alpha(MaterialTheme.dimens.alpha80)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            topStart = MaterialTheme.dimens.size16,
                            topEnd = MaterialTheme.dimens.size16
                        )
                    )
                    .align(Alignment.BottomCenter)
                    .padding(MaterialTheme.dimens.size4),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = currentChannel.channelName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))

                if (currentChannel.channelEpg.isNotEmpty()) {
                    currentChannel.channelEpg.toActual().take(programCount).forEach { epg ->
                        PlayerChannelEpgItem(epgProgram = epg)
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))
                }

                PlayerControlView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(MaterialTheme.dimens.alpha70),
                    isFavorite = currentChannel.isInFavorites,
                    isPlaying = isPlaying,
                    isFullScreen = isFullScreen,
                    onPlaybackAction = onPlaybackAction
                )
            }

            IconButton(
                onClick = onPlaybackClose,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(MaterialTheme.dimens.size4)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "PLAYBACK_CLOSE",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerChannelView() {
    VideoAppTheme(darkTheme = true) {
        PlayerChannelView(
            currentChannel = TvPlaylistChannel(
                channelName = "Test",
                channelEpg = listOf(
                    EpgProgram(
                        title = "Epg Title",
                        channelId = "id",
                        start = System.currentTimeMillis() - 30.minutes.inWholeMilliseconds,
                        stop = System.currentTimeMillis() + 30.minutes.inWholeMilliseconds,
                        description = "Epg Description"
                    )
                )
            )
        )
    }
}*/
