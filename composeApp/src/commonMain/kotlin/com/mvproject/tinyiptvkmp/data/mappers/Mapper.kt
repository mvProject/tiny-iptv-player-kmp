package com.mvproject.tinyiptvkmp.data.mappers

import com.mvproject.tinyiptvkmp.data.model.epg.EpgChannel
import com.mvproject.tinyiptvkmp.data.model.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.data.model.response.EpgProgramResponse
import com.mvproject.tinyiptvkmp.database.entity.EpgChannelEntity
import com.mvproject.tinyiptvkmp.database.entity.EpgProgramEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object Mapper {
    fun EpgChannelEntity.toEpgChannelModel() =
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
    fun EpgProgramResponse.asProgramEntity(id: String) =
        with(this) {
            EpgProgramEntity(
                programId = Uuid.random().toString(),
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                channelId = id,
            )
        }
}
