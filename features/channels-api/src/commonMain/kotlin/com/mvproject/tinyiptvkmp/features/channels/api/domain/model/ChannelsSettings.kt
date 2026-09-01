package com.mvproject.tinyiptvkmp.features.channels.api.domain.model

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType

data class ChannelsSettings(
    val channelsEpgInfoUpdateRequired: Boolean,
    val channelsViewType: ChannelsViewType,
)
