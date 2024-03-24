/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 14:58
 *
 */

package com.mvproject.tinyiptvkmp.platform

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.media3.common.PlaybackException
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.mvproject.tinyiptvkmp.data.mappers.ParseMappers
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.ui.PlayerView
import com.mvproject.tinyiptvkmp.ui.findActivity
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants
import com.mvproject.tinyiptvkmp.utils.CommonUtils.getNameFromStringUri
import com.mvproject.tinyiptvkmp.utils.KLog
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.btn_add_local
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

actual fun createPlatformHttpClient(): HttpClient {
    return HttpClient(Android)
}

@Composable
actual fun PlayerViewContainer(
    modifier: Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit,
    controls: @Composable () -> Unit,
) {
    PlayerView(
        modifier = modifier,
        videoViewState = videoViewState,
        onPlaybackAction = onPlaybackAction,
        onPlaybackStateAction = onPlaybackStateAction,
        controls = controls,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun LocalFileSelectButton(onPlaylistAction: (PlaylistAction) -> Unit) {
    val fileSelectLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
            onResult = { uri ->
                onPlaylistAction(
                    PlaylistAction.SetLocalUri(
                        name = uri.toString().getNameFromStringUri(),
                        uri = uri.toString(),
                    ),
                )
            },
        )

    OutlinedButton(
        onClick = { fileSelectLauncher.launch(arrayOf(AppConstants.PLAYLIST_MIME_TYPE)) },
        modifier = Modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = stringResource(Res.string.btn_add_local),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
actual fun AdditionalPlayerControls(
    modifier: Modifier,
    action: () -> Unit,
    onPlaybackAction: (PlaybackActions) -> Unit,
) {
    // no need yet
}

@Composable
actual fun ExecuteOnResume(action: () -> Unit) {
    LifecycleResumeEffect(Unit) {
        action()

        onPauseOrDispose {
        }
    }
}

actual fun isMediaPlayable(errorCode: Int?): Boolean {
    val isMediaPlayable =
        when (errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> false
            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> false
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> false
            else -> true
        }
    KLog.e("testing errorCode:$errorCode, isMediaPlayable:$isMediaPlayable")
    return isMediaPlayable
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun TwoPaneContainer(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val windowSizeClass = calculateWindowSizeClass()
    val displayFeatures = calculateDisplayFeatures(activity)

    TwoPane(
        first = {
            first()
        },
        second = {
            second()
        },
        strategy =
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> VerticalTwoPaneStrategy(MaterialTheme.dimens.fraction50)
                WindowWidthSizeClass.Medium -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction60)
                else -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction70)
            },
        displayFeatures = displayFeatures,
    )
}

actual class LocalPlaylistDataSource(private val context: Context) {
    @SuppressLint("Recycle")
    actual fun getLocalPlaylistData(
        playlistId: Long,
        uri: String,
    ): List<PlaylistChannel> {
        val file =
            context.contentResolver
                .openFileDescriptor(Uri.parse(uri), MODE_READ_ONLY)
                ?.fileDescriptor

        return buildList {
            InputStreamReader(FileInputStream(file), Charsets.UTF_8).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { bufferedReader ->
                    bufferedReader.readText().also { content ->

                        val channels =
                            ParseMappers.parseStringToChannels(
                                playlistId = playlistId,
                                source = content,
                            )

                        addAll(channels)
                    }
                }
            }
        }
    }

    private companion object {
        const val MODE_READ_ONLY = "r"
    }
}
