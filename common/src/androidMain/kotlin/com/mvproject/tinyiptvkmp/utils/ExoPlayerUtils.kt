/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 17:18
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
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.ui.screens.player.state.VideoPlaybackState

object ExoPlayerUtils {
    @OptIn(UnstableApi::class)
    fun createVideoPlayer(context: Context): ExoPlayer {
        val renderersFactory = createRenderersFactory(context)

        val trackSelector = createTrackSelector(context)

        val defaultDataSourceFactory = createDataSourceFactory()

        val source = HlsMediaSource.Factory(defaultDataSourceFactory)
            .setAllowChunklessPreparation(false)

        val audioAttributes = createAudioAttributes()

        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setTrackSelector(trackSelector)
            .setRenderersFactory(renderersFactory)
            .setMediaSourceFactory(source)
            .build()
    }

    @OptIn(UnstableApi::class)
    private fun createDataSourceFactory(): DefaultHttpDataSource.Factory {
        return DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)

    }

    @OptIn(UnstableApi::class)
    private fun createTrackSelector(context: Context): DefaultTrackSelector {
        return DefaultTrackSelector(context).apply {
            parameters = buildUponParameters()
                //  .setAllowAudioMixedDecoderSupportAdaptiveness(true)
                //  .setAllowAudioMixedMimeTypeAdaptiveness(true)
                .build()
        }
    }

    @OptIn(UnstableApi::class)
    private fun createRenderersFactory(context: Context): DefaultRenderersFactory {
        return DefaultRenderersFactory(context).apply {
            setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
        }
    }

    @OptIn(UnstableApi::class)
    private fun createAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
    }

    fun mapToVideoPlaybackState(playbackState: Int, errorCode: Int? = null): VideoPlaybackState {
        return when (playbackState) {
            Player.STATE_IDLE -> {
                VideoPlaybackState.VideoPlaybackIdle(errorCode = errorCode)
            }

            Player.STATE_BUFFERING -> VideoPlaybackState.VideoPlaybackBuffering
            Player.STATE_ENDED -> VideoPlaybackState.VideoPlaybackEnded
            else -> VideoPlaybackState.VideoPlaybackReady
        }
    }

    fun List<TvPlaylistChannel>.createMediaItems(): List<MediaItem> {
        return buildList {
            this@createMediaItems.forEach { video ->
                add(
                    MediaItem.Builder()
                        .setUri(video.channelUrl)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setDisplayTitle(video.channelName)
                                .build()
                        ).build()
                )
            }
        }
    }

    fun createMediaItem(
        title: String,
        url: String
    ): MediaItem {
        return MediaItem.Builder()
            .setUri(url)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setDisplayTitle(title)
                    .build()
            ).build()
    }
}