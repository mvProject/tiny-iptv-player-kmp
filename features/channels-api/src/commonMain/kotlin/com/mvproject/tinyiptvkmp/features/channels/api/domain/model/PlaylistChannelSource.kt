package com.mvproject.tinyiptvkmp.features.channels.api.domain.model

data class PlaylistChannelSource(
    val parentListId: String,
    val source: String,
    val sourceType: PlaylistChannelSourceType,
)

enum class PlaylistChannelSourceType {
    LOCAL,
    REMOTE,
}
