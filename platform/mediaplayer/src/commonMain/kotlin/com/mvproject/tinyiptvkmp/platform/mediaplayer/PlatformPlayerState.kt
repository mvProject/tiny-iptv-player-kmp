package com.mvproject.tinyiptvkmp.platform.mediaplayer

internal interface PlatformPlayerState {
    fun play()

    fun pause()

    fun setVolume(value: Float)

    fun setPlayingState(value: Boolean)

    fun restartPlayer()

    fun setPlayerChannel(channelUrl: String)
}
