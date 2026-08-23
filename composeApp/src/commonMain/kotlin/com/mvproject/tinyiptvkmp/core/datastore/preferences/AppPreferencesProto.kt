package com.mvproject.tinyiptvkmp.core.datastore.preferences

import com.mvproject.tinyiptvkmp.core.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.INT_VALUE_5
import com.mvproject.tinyiptvkmp.core.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.core.common.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AppPreferencesProto(
    @ProtoNumber(1) val channelsViewType: String = String.empty,
    @ProtoNumber(2) val epgDataLastUpdate: Long = LONG_NO_VALUE,
    @ProtoNumber(3) val epgInfoLastUpdatePeriod: Int = INT_VALUE_5,
    @ProtoNumber(4) val epgMainLastUpdatePeriod: Int = INT_VALUE_5,
    @ProtoNumber(5) val defaultResizeMode: Int = INT_VALUE_ZERO,
    @ProtoNumber(6) val defaultVideoSizeMode: Int = INT_VALUE_ZERO,
    @ProtoNumber(7) val defaultRatioMode: Int = INT_VALUE_1,
    @ProtoNumber(8) val defaultFullscreenMode: Boolean = false,
    @ProtoNumber(9) val epgInfoDataLastUpdate: Long = LONG_VALUE_ZERO,
    @ProtoNumber(10) val channelsEpgInfoUpdateRequired: Boolean = false,
    @ProtoNumber(11) val playlistContentLoadRequired: String = String.empty,
    @ProtoNumber(12) val epgProgramClean: Long = LONG_NO_VALUE,
)
