package com.mvproject.tinyiptvkmp.features.epg.api.data.mapper

import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal fun EpgChannelResponse.toEpgChannel() =
    with(this) {
        EpgChannel(
            title = channelName,
            logo = channelIcon,
            programId = channelId,
            id = Uuid.random().toString(),
        )
    }

@OptIn(ExperimentalUuidApi::class)
internal fun EpgProgramResponse.toEpgProgram(channelId: String) =
    with(this) {
        EpgProgram(
            programId = Uuid.random().toString(),
            dateTimeStart = dateTimeStart,
            dateTimeEnd = dateTimeEnd,
            title = title,
            description = description,
            channelId = channelId,
        )
    }
