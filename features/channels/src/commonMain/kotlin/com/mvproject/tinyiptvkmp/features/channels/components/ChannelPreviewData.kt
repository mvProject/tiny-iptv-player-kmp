package com.mvproject.tinyiptvkmp.features.channels.components

import com.mvproject.tinyiptvkmp.core.foundation.utils.actualDate
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.withPrograms
import kotlin.time.Duration.Companion.minutes

internal object ChannelPreviewData {
    val channelWithPrograms =
        TvChannel(
            channelName = "channelName",
            channelLogo = "",
            channelUrl = "",
        ).withPrograms(
            listOf(
                EpgProgram(
                    programId = "1",
                    channelId = "1",
                    dateTimeStart = actualDate - 30.minutes.inWholeMilliseconds,
                    dateTimeEnd = actualDate + 90.minutes.inWholeMilliseconds,
                    title = "test title",
                    description = "test description",
                )
            )
        )
}
