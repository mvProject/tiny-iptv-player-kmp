/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.utils.TimeUtils

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

typealias ChannelEpgMap = Map<String, List<EpgProgram>>