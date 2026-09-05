package com.mvproject.tinyiptvkmp.features.channels.api.domain.model

data class PlaylistChannelWindow(
    val channels: List<TvChannel>,
    val currentIndex: Int,
    val totalCount: Int,
)
