package com.mvproject.tinyiptvkmp.core.foundation.model

enum class ChannelsViewType {
    LIST,
    GRID,
    CARD;

    companion object {
        fun String?.mapViewType() =
            entries.firstOrNull { it.name == this } ?: LIST
    }
}
