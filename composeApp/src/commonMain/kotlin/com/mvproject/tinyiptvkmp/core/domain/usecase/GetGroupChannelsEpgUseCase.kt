package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.utils.actualDate
import com.mvproject.tinyiptvkmp.core.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelEpgMap

class GetGroupChannelsEpgUseCase(
    private val epgProgramRepository: EpgProgramRepository,
) {
    suspend operator fun invoke(
        channelsIds: List<String>,
        programCount: Int = 1
    ): ChannelEpgMap {

        val programsByIds =
            epgProgramRepository
                .getEpgProgramsByIds(
                    channelIds = channelsIds,
                    time = actualDate,
                ).asSequence()

        val groupedProgramsByIds = programsByIds
            .groupBy { it.channelId }
            .mapValues { (_, programs) -> programs.take(programCount) }

        return groupedProgramsByIds
    }
}