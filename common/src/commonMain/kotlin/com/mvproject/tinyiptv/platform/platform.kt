/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 10:58
 *
 */

package com.mvproject.tinyiptv.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptv.ui.screens.playlist.action.PlaylistAction
import io.github.aakira.napier.Napier
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

@Composable
expect fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font

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
                    Napier.d(message, tag = "Ktor napier log:")
                    co.touchlab.kermit.Logger.w("Ktor kermit log:") { message }
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