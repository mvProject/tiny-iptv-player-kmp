package com.mvproject.tinyiptvkmp.features.settings.api.domain.model

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType

data class GeneralSettings(
    val infoUpdatePeriod: Int,
    val epgUpdatePeriod: Int,
    val channelsViewType: ChannelsViewType,
)
