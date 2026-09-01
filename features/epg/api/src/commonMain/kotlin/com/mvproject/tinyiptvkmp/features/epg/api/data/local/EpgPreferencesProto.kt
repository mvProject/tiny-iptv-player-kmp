package com.mvproject.tinyiptvkmp.features.epg.api.data.local

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@OptIn(ExperimentalSerializationApi::class)
@Serializable
internal data class EpgPreferencesProto(
    @ProtoNumber(1) val epgDataLastUpdate: Long = -1L,
    @ProtoNumber(2) val epgInfoLastUpdatePeriod: Int = 5,
    @ProtoNumber(3) val epgMainLastUpdatePeriod: Int = 5,
    @ProtoNumber(4) val epgInfoDataLastUpdate: Long = 0L,
    @ProtoNumber(5) val epgProgramClean: Long = -1L,
)
