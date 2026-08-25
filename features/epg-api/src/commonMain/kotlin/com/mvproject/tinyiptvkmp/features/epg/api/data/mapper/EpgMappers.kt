package com.mvproject.tinyiptvkmp.features.epg.api.data.mapper

import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgChannelEntity
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.database.EpgProgramEntity
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgChannel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal object EpgMappers {
    fun EpgChannelEntity.toEpgChannel() =
        with(this) {
            EpgChannel(
                title = title,
                logo = logo,
                programId = programId,
                id = id,
            )
        }

    @OptIn(ExperimentalUuidApi::class)
    fun EpgChannelResponse.toEpgChannelEntity() =
        with(this) {
            EpgChannelEntity(
                title = channelName,
                logo = channelIcon,
                programId = channelId,
                id = Uuid.random().toString(),
            )
        }

    @OptIn(ExperimentalUuidApi::class)
    fun EpgProgramResponse.toEpgProgramEntity(channelId: String) =
        with(this) {
            EpgProgramEntity(
                programId = Uuid.random().toString(),
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                channelId = channelId,
            )
        }

    fun EpgProgramEntity.toEpgProgram() =
        with(this) {
            EpgProgram(
                programId = programId,
                channelId = channelId,
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
            )
        }
}
