package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.ChannelEpgMap
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate

interface GetGroupChannelsEpgUseCase {
    suspend operator fun invoke(
        channelsIds: List<String>,
        programCount: Int = 1,
    ): ChannelEpgMap
}

internal class GetGroupChannelsEpgUseCaseImpl(
    private val epgProgramRepository: EpgProgramRepository,
) : GetGroupChannelsEpgUseCase {
    override suspend operator fun invoke(
        channelsIds: List<String>,
        programCount: Int,
    ): ChannelEpgMap {
        val programsByIds =
            epgProgramRepository
                .getEpgProgramsByIds(
                    channelIds = channelsIds,
                    time = actualEpgDate,
                ).asSequence()

        return programsByIds
            .groupBy { it.channelId }
            .mapValues { (_, programs) -> programs.take(programCount) }
    }
}
