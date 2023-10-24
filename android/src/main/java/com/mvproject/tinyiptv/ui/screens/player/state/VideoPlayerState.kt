/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.10.23, 15:28
 *
 */

package com.mvproject.tinyiptv.ui.screens.player.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Build and remember default implementation of [VideoPlayerState]
 *
 * @param hideControllerAfterMs time after which [VideoPlayerState.isControlUiVisible] will be set to false.
 * interactions with [VideoPlayerState.control] will reset the internal counter.
 * if null is provided the controller wont be hidden until [VideoPlayerState.hideControlUi] is called again
 * @param videoPositionPollInterval interval on which the [VideoPlayerState.videoPositionMs] will be updated,
 * you can set a lower number to update the ui faster though it will consume more cpu resources.
 * Take in consideration that this value is updated only when [VideoPlayerState.isControlUiVisible] is set to true,
 * if you need to get the last value use [ExoPlayer.getCurrentPosition].
 * @param coroutineScope this scope will be used to poll for [VideoPlayerState.videoPositionMs] updates
 * @param context used to build an [ExoPlayer] instance
 * @param config you can use this to configure [ExoPlayer]
 * */
@Composable
fun rememberVideoPlayerState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    context: Context = LocalContext.current,
    hideControllerAfterMs: Long? = 3000,
    hideVolumeAfterMs: Long? = 500,
    videoPositionPollInterval: Long = 500,
    isPlayerFullscreen: Boolean = false,
    videoResizeMode: ResizeMode = ResizeMode.Fit,
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
): VideoPlayerState = remember {
    val renderersFactory = DefaultRenderersFactory(context).apply {
        setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
    }

    val trackSelector = DefaultTrackSelector(context).apply {
        parameters = buildUponParameters()
            .setAllowAudioMixedDecoderSupportAdaptiveness(true)
            .setAllowAudioMixedMimeTypeAdaptiveness(true)
            .build()
    }

    val defaultDataSourceFactory = DefaultHttpDataSource.Factory()
        .setAllowCrossProtocolRedirects(true)
        .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
        .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)

    val source = HlsMediaSource.Factory(defaultDataSourceFactory)
        .setAllowChunklessPreparation(true)

    val audioAttributes = AudioAttributes.Builder()
        .setUsage(C.USAGE_MEDIA)
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .build()

    val exoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setTrackSelector(trackSelector)
        .setRenderersFactory(renderersFactory)
        .setMediaSourceFactory(source)
        .build()


    VideoPlayerStateImpl(
        player = exoPlayer,
        coroutineScope = coroutineScope,
        hideControllerAfterMs = hideControllerAfterMs,
        hideVolumeAfterMs = hideVolumeAfterMs,
        videoPositionPollInterval = videoPositionPollInterval,
        isPlayerFullscreen = isPlayerFullscreen,
        resizeMode = videoResizeMode,
        onPlaybackStateAction = onPlaybackStateAction
    ).also {
        it.player.apply {
            addListener(it)
        }
    }
}

class VideoPlayerStateImpl(
    override val player: ExoPlayer,
    private val coroutineScope: CoroutineScope,
    private val hideControllerAfterMs: Long?,
    private val hideVolumeAfterMs: Long?,
    private val videoPositionPollInterval: Long,
    private val isPlayerFullscreen: Boolean,
    private val resizeMode: ResizeMode,
    private val onPlaybackStateAction: (PlaybackStateActions) -> Unit = {}
) : VideoPlayerState, Player.Listener {
    override val videoSize = mutableStateOf(1.7777778f)
    override val videoResizeMode = mutableStateOf(resizeMode)

    override val isFullscreen = mutableStateOf(isPlayerFullscreen)
    override val isPlaying = mutableStateOf(player.isPlaying)
    override val playerState = mutableIntStateOf(player.playbackState)
    override val isControlUiVisible = mutableStateOf(false)
    override val isVolumeUiVisible = mutableStateOf(false)

    private var pollVideoPositionJob: Job? = null
    private var pollVolumeJob: Job? = null
    private var controlUiLastInteractionMs = 0L
    private var volumeUiLastInteractionMs = 0L

    override fun hideControlUi() {
        controlUiLastInteractionMs = 0
        isControlUiVisible.value = false
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob = null
    }

    override fun showControlUi() {
        isControlUiVisible.value = true
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob = coroutineScope.launch {
            if (hideControllerAfterMs != null) {
                while (true) {
                    delay(videoPositionPollInterval)
                    controlUiLastInteractionMs += videoPositionPollInterval
                    if (controlUiLastInteractionMs >= hideControllerAfterMs) {
                        hideControlUi()
                        break
                    }
                }
            } else {
                while (true) {
                    delay(videoPositionPollInterval)
                }
            }
        }
    }

    override fun showVolumeUi() {
        isVolumeUiVisible.value = true
        pollVolumeJob?.cancel()
        pollVolumeJob = coroutineScope.launch {
            if (hideVolumeAfterMs != null) {
                while (true) {
                    delay(videoPositionPollInterval)
                    volumeUiLastInteractionMs += videoPositionPollInterval
                    if (volumeUiLastInteractionMs >= hideVolumeAfterMs) {
                        hideVolumeUi()
                        break
                    }
                }
            } else {
                while (true) {
                    delay(videoPositionPollInterval)
                }
            }
        }
    }

    override fun hideVolumeUi() {
        volumeUiLastInteractionMs = 0
        isVolumeUiVisible.value = false
        pollVolumeJob?.cancel()
        pollVolumeJob = null
    }

    override fun volumeUp() {
        coroutineScope.launch {
            delay(50)
            val currentBefore = player.volume
            val currentUpdate = currentBefore + 0.05f
            player.volume = currentUpdate
            delay(50)
        }

        showVolumeUi()

    }

    override fun volumeDown() {
        coroutineScope.launch {
            val currentBefore = player.volume
            val currentUpdate = currentBefore - 0.05f
            player.volume = currentUpdate
            delay(50)
        }

        showVolumeUi()
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        val data = mediaItem?.mediaMetadata?.displayTitle.toString()
        onPlaybackStateAction(
            PlaybackStateActions.OnMediaItemTransition(
                mediaTitle = data,
                index = player.currentMediaItemIndex
            )
        )
        showControlUi()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        this.isPlaying.value = isPlaying
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        this.playerState.intValue = playbackState
        Napier.e("testing playbackState:$playbackState, isMediaPlayable:${player.playerError?.errorCode}")
        val state = when (playbackState) {
            Player.STATE_IDLE -> {
                VideoPlaybackState.VideoPlaybackIdle(errorCode = player.playerError?.errorCode)
            }

            Player.STATE_BUFFERING -> VideoPlaybackState.VideoPlaybackBuffering
            Player.STATE_ENDED -> VideoPlaybackState.VideoPlaybackEnded
            else -> VideoPlaybackState.VideoPlaybackReady
        }
        onPlaybackStateAction(PlaybackStateActions.OnPlaybackState(state))
    }

    /*    override fun onTracksChanged(tracks: Tracks) {

            tracks.groups.forEachIndexed { _, group ->
                if (group.type == C.TRACK_TYPE_AUDIO) {
                    for (i in 0..group.length - 1) {
                        val trackFormat = group.getTrackFormat(i)
                        val audioTrackLanguage = trackFormat.language.toString()
                        val audioTrackLabel = trackFormat.label.toString()
                        Napier.i("testing group trackFormat $trackFormat")
                        Napier.i("testing group audioTrackLanguage $audioTrackLanguage")
                        Napier.i("testing group audioTrackLabel $audioTrackLabel")
                    }
                }
            }
        }*/

    //override fun onVideoSizeChanged(videoSize: VideoSize) {
    //    Napier.e("testing onVideoSizeChanged videoSize:$videoSize")
    //    this.videoSize.value = videoSize
    //}

    override fun togglePlayingState() {
        if (this.isPlaying.value) {
            player.pause()
        } else {
            player.play()
        }
    }

    override fun toggleControlUiState() {
        if (this.isControlUiVisible.value) {
            hideControlUi()
        } else {
            showControlUi()
        }
    }

    override fun toggleFullscreen() {
        this.isFullscreen.value = !this.isFullscreen.value
    }

    override fun play() {
        controlUiLastInteractionMs = 0
        player.play()
    }

    override fun pause() {
        controlUiLastInteractionMs = 0
        player.pause()
    }

    override fun next() {
        controlUiLastInteractionMs = 0
        player.seekToNextMediaItem()
    }

    override fun previous() {
        controlUiLastInteractionMs = 0
        player.seekToPreviousMediaItem()
    }

    override fun toggleVideoResizeMode() {
        val currentMode = videoResizeMode.value
        val nextMode = ResizeMode.toggleResizeMode(current = currentMode)
        controlUiLastInteractionMs = 0
        this.videoResizeMode.value = nextMode
    }

    override fun setFullscreen(value: Boolean) {
        controlUiLastInteractionMs = 0
        this.isFullscreen.value = value
    }

}

interface VideoPlayerState {
    val player: ExoPlayer

    val videoSize: State<Float>
    val videoResizeMode: State<ResizeMode>

    val isFullscreen: State<Boolean>
    val isPlaying: State<Boolean>
    val playerState: State<Int>

    val isControlUiVisible: State<Boolean>
    val isVolumeUiVisible: State<Boolean>

    fun hideControlUi()
    fun showControlUi()
    fun togglePlayingState()
    fun toggleControlUiState()
    fun showVolumeUi()
    fun hideVolumeUi()
    fun toggleFullscreen()
    fun play()
    fun pause()

    fun next()
    fun previous()
    fun setFullscreen(value: Boolean)
    fun toggleVideoResizeMode()

    fun volumeUp()
    fun volumeDown()
}