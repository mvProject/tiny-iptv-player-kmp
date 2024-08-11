/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 30.01.24, 16:36
 *
 */

package com.mvproject.tinyiptvkmp.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptvkmp.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptvkmp.utils.KLog
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.GZip
import kotlinx.serialization.json.Json

internal const val dataStoreFileName = "tiny_iptv.preferences_pb"

expect fun createPlatformHttpClient(): HttpClient

expect fun isMediaPlayable(errorCode: Int?): Boolean

@Composable
expect fun PlayerViewContainer(
    modifier: Modifier = Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit = {},
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {},
    controls: @Composable () -> Unit
)

@Composable
expect fun LocalFileSelectButton(onPlaylistAction: (PlaylistAction) -> Unit)

@Composable
expect fun AdditionalPlayerControls(
    modifier: Modifier,
    action: () -> Unit,
    onPlaybackAction: (PlaybackActions) -> Unit
)

@Composable
expect fun ExecuteOnResume(action: () -> Unit)

internal fun createHttpClient(): HttpClient {
    return createPlatformHttpClient().config {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    KLog.w("Ktor log:") { message }
                }
            }
            level = LogLevel.ALL
        }
        install(HttpHeaders.ContentEncoding) {
            GZip
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout)
    }
}

@Composable
expect fun TwoPaneContainer(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit
)

expect class LocalPlaylistDataSource {
    fun getLocalPlaylistData(
        playlistId: Long,
        uri: String
    ): List<PlaylistChannel>
}