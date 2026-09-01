package com.mvproject.tinyiptvkmp.core.datastore.preferences

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AppPreferencesProto(
    @ProtoNumber(2) val epgDataLastUpdate: Long = -1L,
    @ProtoNumber(3) val epgInfoLastUpdatePeriod: Int = 5,
    @ProtoNumber(4) val epgMainLastUpdatePeriod: Int = 5,
    @ProtoNumber(5) val defaultResizeMode: Int = 0,
    @ProtoNumber(6) val defaultVideoSizeMode: Int = 0,
    @ProtoNumber(7) val defaultRatioMode: Int = 1,
    @ProtoNumber(8) val defaultFullscreenMode: Boolean = false,
    @ProtoNumber(9) val epgInfoDataLastUpdate: Long = 0L,
    @ProtoNumber(11) val playlistContentLoadRequired: String = "",
    @ProtoNumber(12) val epgProgramClean: Long = -1L,
)
