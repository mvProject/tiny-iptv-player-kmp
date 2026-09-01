package com.mvproject.tinyiptvkmp.features.player.presentation.utils

import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms

val TvChannelWithPrograms.programTitle
    get() = this.programs.firstOrNull()
        ?.title ?: String.empty

val TvChannelWithPrograms.programDescription
    get() = this.programs.firstOrNull()
        ?.description ?: String.empty
