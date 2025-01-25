package com.mvproject.tinyiptvkmp.features.player.components

interface PlayerState {
    fun play()

    fun pause()

    fun setVolume(value: Float)

    fun setPlayingState(value: Boolean)

    fun restartPlayer()

    fun setPlayerChannel(channelUrl: String)
}
