package com.mvproject.tinyiptvkmp.features.player.api.data.storage

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@OptIn(ExperimentalSerializationApi::class)
@Serializable
internal data class PlayerPreferencesProto(
    @ProtoNumber(1) val isFullscreenEnabled: Boolean = false,
    @ProtoNumber(2) val videoSize: Int = 0,
)
