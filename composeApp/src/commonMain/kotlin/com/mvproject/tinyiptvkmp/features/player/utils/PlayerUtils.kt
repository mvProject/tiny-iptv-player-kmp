package com.mvproject.tinyiptvkmp.features.player.utils

import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

object PlayerUtils {
    val TvChannel.programTitle
        get() = this.programs.firstOrNull()
            ?.title ?: String.empty

    val TvChannel.programDescription
        get() = this.programs.firstOrNull()
            ?.description ?: String.empty

}