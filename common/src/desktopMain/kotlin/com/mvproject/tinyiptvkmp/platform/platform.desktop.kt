/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 30.11.23, 18:46
 *
 */

package com.mvproject.tinyiptvkmp.platform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mvproject.tinyiptvkmp.ui.PlayerView
import com.mvproject.tinyiptvkmp.ui.components.views.PlaybackControl
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
    PlayerView(
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

        PlaybackControl(
            imageVector = Icons.Rounded.Close,
            action = action
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size32))

        PlaybackControl(
            imageVector = Icons.Rounded.VolumeDown,
            action = { onPlaybackAction(PlaybackActions.OnVolumeDown) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        PlaybackControl(
            imageVector = Icons.Rounded.VolumeUp,
            action = { onPlaybackAction(PlaybackActions.OnVolumeUp) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size24))

        PlaybackControl(
            imageVector = Icons.Rounded.SkipPrevious,
            action = { onPlaybackAction(PlaybackActions.OnPreviousSelected) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        PlaybackControl(
            imageVector = Icons.Rounded.SkipNext,
            action = { onPlaybackAction(PlaybackActions.OnNextSelected) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size24))

        PlaybackControl(
            imageVector = Icons.Rounded.ViewList,
            action = { onPlaybackAction(PlaybackActions.OnEpgUiToggle) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        PlaybackControl(
            imageVector = Icons.Rounded.FeaturedPlayList,
            action = { onPlaybackAction(PlaybackActions.OnChannelsUiToggle) }
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        PlaybackControl(
            imageVector = Icons.Rounded.Info,
            action = { onPlaybackAction(PlaybackActions.OnChannelInfoUiToggle) }
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
                .weight(MaterialTheme.dimens.weight6)
                .fillMaxHeight()
        ) {
            first()
        }
        Column(
            modifier = Modifier
                .weight(MaterialTheme.dimens.weight2)
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