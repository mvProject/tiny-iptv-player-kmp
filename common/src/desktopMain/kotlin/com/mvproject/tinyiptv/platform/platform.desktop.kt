/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 11:54
 *
 */

package com.mvproject.tinyiptv.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import com.mvproject.tinyiptv.ui.PlayerView
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptv.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java

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
    // todo file open and read
    val fileSelectLauncher = rememberFilePickerLauncher(
        type = FilePickerFileType.Custom(
            listOf(AppConstants.PLAYLIST_MIME_TYPE)
        ),
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files ->
            try {
                val content = files.firstOrNull()?.readText()
                Napier.w("testing FileSelectButton readText content $content")
            } catch (ex: Exception) {
                Napier.e("testing FileSelectButton readText ${ex.message}")
            }
            try {
                val content = files.firstOrNull()?.readLines()
                Napier.w("testing FileSelectButton readLines content $content")
            } catch (ex: Exception) {
                Napier.e("testing FileSelectButton readText ${ex.message}")
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
            text = "Add local playlist",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
actual fun ClosePlayer(
    modifier: Modifier,
    action: () -> Unit
) {
    IconButton(
        onClick = action,
        modifier = modifier
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

/*
@Composable
actual fun ImageLogo(source: String) {
    // todo show image
}
*/

@Composable
actual fun ExecuteOnResume(action: () -> Unit) {
    // todo implement
    action()
}

actual fun isMediaPlayable(errorCode: Int?): Boolean {
    // todo check media playable
    return true
}