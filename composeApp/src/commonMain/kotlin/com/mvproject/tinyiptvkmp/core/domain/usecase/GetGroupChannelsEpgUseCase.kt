package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils
import com.mvproject.tinyiptvkmp.core.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelEpgMap

class GetGroupChannelsEpgUseCase(
    private val epgProgramRepository: EpgProgramRepository,
) {
    suspend operator fun invoke(channelsIds: List<String>): ChannelEpgMap {

        val programsByIds =
            epgProgramRepository
                .getEpgProgramsByIds(
                    channelIds = channelsIds,
                    time = TimeUtils.actualDate,
                ).asSequence()

        val groupedProgramsByIds = programsByIds
            .groupBy { it.channelId }
            .mapValues { (_, programs) -> programs.take(1) }

        return groupedProgramsByIds
    }
}