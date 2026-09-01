package com.mvproject.tinyiptvkmp.persistence.epg.room.mapper

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgChannelEntity
import com.mvproject.tinyiptvkmp.persistence.epg.room.database.EpgProgramEntity

internal fun EpgProgramEntity.toEpgProgram() =
    EpgProgram(
        programId = programId,
        channelId = channelId,
        title = title,
        description = description,
        dateTimeStart = dateTimeStart,
        dateTimeEnd = dateTimeEnd,
    )

internal fun EpgProgram.toEpgProgramEntity() =
    EpgProgramEntity(
        programId = programId,
        channelId = channelId,
        title = title,
        description = description,
        dateTimeStart = dateTimeStart,
        dateTimeEnd = dateTimeEnd,
    )

internal fun EpgChannelEntity.toEpgChannel() =
    EpgChannel(
        id = id,
        programId = programId,
        title = title,
        logo = logo,
    )

internal fun EpgChannel.toEpgChannelEntity() =
    EpgChannelEntity(
        id = id,
        programId = programId,
        title = title,
        logo = logo,
    )
