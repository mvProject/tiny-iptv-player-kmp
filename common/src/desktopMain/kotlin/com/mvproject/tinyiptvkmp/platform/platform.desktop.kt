/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FeaturedPlayList
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import com.mvproject.tinyiptvkmp.MainRes
import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.ui.PlayerViewSwing
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import okio.use
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
): Font = Font("font/$res.ttf", weight, style)


actual fun createPlatformHttpClient(): HttpClient {
    // return HttpClient(OkHttp)
    return HttpClient(Java)
}


@Composable
actual fun PlayerViewContainer(
    modifier: Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
    controls: @Composable () -> Unit
) {
    PlayerViewSwing(
        modifier = modifier,
        videoViewState = videoViewState,
        onPlaybackAction = onPlaybackAction,
        onPlaybackStateAction = onPlaybackStateAction,
        controls = controls
    )
}

@Composable
actual fun LocalFileSelectButton(onPlaylistAction: (PlaylistAction) -> Unit) {

    val fileSelectLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Custom(
            listOf(AppConstants.PLAYLIST_MIME_TYPE)
        ),
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files ->
            files.firstOrNull()?.let { file ->
                onPlaylistAction(
                    PlaylistAction.SetLocalUri(
                        name = file.name,
                        uri = file.absolutePath
                    )
                )
            }
        }
    )

    OutlinedButton(
        onClick = {
            fileSelectLauncher.launch()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = MainRes.string.btn_add_local,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
actual fun AdditionalPlayerControls(
    modifier: Modifier,
    action: () -> Unit,
    onPlaybackAction: (PlaybackActions) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier
                .clickable {
                    action()
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.Close,
            contentDescription = "PLAYBACK_CLOSE",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size32))

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnVolumeDown)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.VolumeDown,
            contentDescription = "VOLUME_DOWN",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnVolumeUp)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.VolumeUp,
            contentDescription = "VOLUME_UP",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size24))

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnPreviousSelected)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.SkipPrevious,
            contentDescription = "PreviousSelected",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnNextSelected)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = "NextSelected",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size24))

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnEpgUiToggle)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.ViewList,
            contentDescription = "Show epg",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnChannelsUiToggle)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.FeaturedPlayList,
            contentDescription = "Show channels",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        Icon(
            modifier = Modifier
                .clickable {
                    onPlaybackAction(PlaybackActions.OnChannelInfoUiToggle)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(MaterialTheme.dimens.size8),
            imageVector = Icons.Rounded.Info,
            contentDescription = "Show channel info",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size24))
    }

}

@Composable
actual fun ExecuteOnResume(action: () -> Unit) {
    action()
}

actual fun isMediaPlayable(errorCode: Int?): Boolean {
    // todo check media playable
    return true
}

@Composable
actual fun TwoPaneContainer(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight()
        ) {
            first()
        }
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {
            second()
        }
    }
}

actual class LocalPlaylistDataSource {
    actual fun getLocalPlaylistData(
        playlistId: Long,
        uri: String
    ): List<PlaylistChannel> {

        val file = File(uri)

        return buildList {
            InputStreamReader(FileInputStream(file), Charsets.UTF_8).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { bufferedReader ->
                    bufferedReader.readText().also { content ->

                        val channels = ParseMappers.parseStringToChannels(
                            playlistId = playlistId,
                            source = content
                        )

                        addAll(channels)
                    }
                }
            }
        }
    }
}