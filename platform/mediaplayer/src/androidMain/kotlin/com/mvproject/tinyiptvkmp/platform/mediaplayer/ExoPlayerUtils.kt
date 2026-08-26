/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:51
 *
 */

package com.mvproject.tinyiptvkmp.platform.mediaplayer

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.ktor.KtorDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import io.github.anilbeesetti.nextlib.media3ext.ffdecoder.NextRenderersFactory
import io.ktor.client.HttpClient

internal object ExoPlayerUtils {
    @OptIn(UnstableApi::class)
    fun createVideoPlayer(
        context: Context,
        httpClient: HttpClient,
    ): ExoPlayer {
        //  val renderersFactory = createRenderersFactory(context)
        val renderersFactory =
            NextRenderersFactory(context).apply {
                setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            }

        val trackSelector = createTrackSelector(context)

        val defaultDataSourceFactory = createDataSourceFactory(
            context = context,
            httpClient = httpClient,
        )

        val source = DefaultMediaSourceFactory(defaultDataSourceFactory)

        val audioAttributes = createAudioAttributes()

        val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()

        return ExoPlayer
            .Builder(context)
            .setBandwidthMeter(bandwidthMeter)
            .setAudioAttributes(audioAttributes, true)
            .setTrackSelector(trackSelector)
            .setRenderersFactory(renderersFactory)
            .setMediaSourceFactory(source)
            .build()
    }

    @OptIn(UnstableApi::class)
    private fun createDataSourceFactory(
        context: Context,
        httpClient: HttpClient,
    ): DataSource.Factory {
        val ktorDataSourceFactory = KtorDataSource.Factory(httpClient = httpClient)

        return DefaultDataSource.Factory(context, ktorDataSourceFactory)
    }

    @OptIn(UnstableApi::class)
    private fun createTrackSelector(context: Context): DefaultTrackSelector =
        DefaultTrackSelector(
            context,
            AdaptiveTrackSelection.Factory(),
        ).apply {
            parameters =
                buildUponParameters()
                    .setMaxVideoSizeSd()
                    .setAllowAudioMixedChannelCountAdaptiveness(true)
                    .setAllowAudioMixedMimeTypeAdaptiveness(true)
                    .setAllowAudioMixedDecoderSupportAdaptiveness(true)
                    .build()
        }

    @OptIn(UnstableApi::class)
    private fun createRenderersFactory(context: Context): DefaultRenderersFactory =
        DefaultRenderersFactory(context).apply {
            setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
        }

    @OptIn(UnstableApi::class)
    private fun createAudioAttributes(): AudioAttributes =
        AudioAttributes
            .Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

    fun mapToMediaPlaybackState(
        playbackState: Int,
        errorCode: Int? = null,
    ): MediaPlaybackState =
        when (playbackState) {
            Player.STATE_IDLE -> {
                MediaPlaybackState.Idle(errorCode = errorCode)
            }

            Player.STATE_BUFFERING -> MediaPlaybackState.Buffering
            Player.STATE_ENDED -> MediaPlaybackState.Ended
            else -> MediaPlaybackState.Ready
        }

    fun createMediaItem(url: String): MediaItem {
        val builder = MediaItem
            .Builder()
            .setUri(url)

        if (url.contains(M3U8_EXTENSION, ignoreCase = true)) {
            builder.setMimeType(MimeTypes.APPLICATION_M3U8)
        }

        return builder.build()
    }

    private const val M3U8_EXTENSION = ".m3u8"
}
