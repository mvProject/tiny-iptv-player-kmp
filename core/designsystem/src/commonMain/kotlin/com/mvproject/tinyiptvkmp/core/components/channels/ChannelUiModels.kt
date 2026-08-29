package com.mvproject.tinyiptvkmp.core.components.channels

import androidx.compose.runtime.Immutable

@Immutable
data class ChannelItemUiModel(
    val id: String,
    val name: String,
    val logoUrl: String,
    val isFavorite: Boolean,
    val currentProgram: ChannelProgramUiModel?,
)

@Immutable
data class ChannelProgramUiModel(
    val id: String,
    val title: String,
    val startMillis: Long,
    val endMillis: Long,
    val progress: Float,
)

@Immutable
data class FavoriteOptionUiModel(
    val id: String,
    val label: String,
    val isSelected: Boolean,
)
