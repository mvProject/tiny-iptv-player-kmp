package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@OptIn(ExperimentalSerializationApi::class)
@Serializable
internal data class ChannelsPreferencesProto(
    @ProtoNumber(1) val channelsViewType: String = "",
    @ProtoNumber(2) val channelsEpgInfoUpdateRequired: Boolean = false,
)
