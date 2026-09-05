package com.mvproject.tinyiptvkmp.features.player.api.domain.model

import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize

data class PlayerSettings(
    val isFullscreenEnabled: Boolean,
    val videoSize: VideoSize,
)
