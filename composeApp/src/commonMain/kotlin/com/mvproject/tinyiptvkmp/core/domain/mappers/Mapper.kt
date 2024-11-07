package com.mvproject.tinyiptvkmp.core.domain.mappers

import com.mvproject.tinyiptvkmp.core.data.model.EpgChannel
import com.mvproject.tinyiptvkmp.core.database.entity.EpgChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.EpgProgramEntity
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgProgramResponse
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
