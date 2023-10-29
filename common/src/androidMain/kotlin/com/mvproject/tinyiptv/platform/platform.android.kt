/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 11:06
 *
 */

package com.mvproject.tinyiptv.platform

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.media3.common.PlaybackException
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.tinyiptv.common.R
import com.mvproject.tinyiptv.ui.screens.player.VideoView
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import com.mvproject.tinyiptv.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

@SuppressLint("DiscouragedApi")
@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(res, "font", context.packageName)
    return Font(id, weight, style)
}

actual fun createPlatformHttpClient(): HttpClient {
    return HttpClient(Android)
}

@Composable
actual fun LocalFileSelectButton(onPlaylistAction: (PlaylistAction) -> Unit) {
    val fileSelectLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            onPlaylistAction(PlaylistAction.SetLocalUri(uri.toString()))
        }
    )

    OutlinedButton(
        onClick = { fileSelectLauncher.launch(arrayOf(AppConstants.PLAYLIST_MIME_TYPE)) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = stringResource(id = R.string.pl_btn_add_local),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
actual fun ImageLogo(source: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(source)
            .crossfade(true)
            .placeholder(R.drawable.no_channel_logo)
            .error(R.drawable.no_channel_logo)
            .build(),
        contentDescription = "channel.channelName",
        modifier = Modifier
            .size(MaterialTheme.dimens.size42)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
actual fun ExecuteOnResume(action: () -> Unit) {
    LifecycleResumeEffect(Unit) {
        Napier.w("testing LifecycleResumeEffect launched")
        action()
        onPauseOrDispose {
            Napier.w("testing LifecycleResumeEffect onPauseOrDispose")
        }
    }
}

actual fun isMediaPlayable(errorCode: Int?): Boolean {
    val isMediaPlayable = when (errorCode) {
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> false
        PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> false
        PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> false
        else -> true
    }
    Napier.e("testing errorCode:$errorCode, isMediaPlayable:$isMediaPlayable")
    return isMediaPlayable
}

@Composable
actual fun PlayerScreenRouteContent(
    viewModel: VideoViewViewModel,
    channelUrl: String,
    channelGroup: String
) {
    VideoView(
        viewModel = viewModel,
        channelUrl = channelUrl,
        channelGroup = channelGroup
    )
}