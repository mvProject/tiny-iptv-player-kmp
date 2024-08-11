/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.06.24, 11:51
 *
 */

package com.mvproject.tinyiptvkmp.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoPlaybackState

object ExoPlayerUtils {
    @OptIn(UnstableApi::class)
    fun createVideoPlayer(context: Context): ExoPlayer {
        val renderersFactory = createRenderersFactory(context)

        val trackSelector = createTrackSelector(context)

        val defaultDataSourceFactory = createDataSourceFactory()

        val source =
            HlsMediaSource
                .Factory(defaultDataSourceFactory)
                .setAllowChunklessPreparation(false)

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
    private fun createDataSourceFactory(): DefaultHttpDataSource.Factory =
        DefaultHttpDataSource
            .Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)

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

    fun mapToVideoPlaybackState(
        playbackState: Int,
        errorCode: Int? = null,
    ): VideoPlaybackState =
        when (playbackState) {
            Player.STATE_IDLE -> {
                VideoPlaybackState.VideoPlaybackIdle(errorCode = errorCode)
            }

            Player.STATE_BUFFERING -> VideoPlaybackState.VideoPlaybackBuffering
            Player.STATE_ENDED -> VideoPlaybackState.VideoPlaybackEnded
            else -> VideoPlaybackState.VideoPlaybackReady
        }

    fun createMediaItem(
        title: String,
        url: String,
    ): MediaItem =
        MediaItem
            .Builder()
            .setUri(url)
            .setMediaMetadata(
                MediaMetadata
                    .Builder()
                    .setDisplayTitle(title)
                    .build(),
            ).build()
}
