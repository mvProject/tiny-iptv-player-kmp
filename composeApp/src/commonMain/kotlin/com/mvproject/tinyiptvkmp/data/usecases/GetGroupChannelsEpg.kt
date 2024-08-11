/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.ChannelEpg
import com.mvproject.tinyiptvkmp.utils.TimeUtils

class GetGroupChannelsEpg(
    private val epgProgramRepository: EpgProgramRepository,
) {
    suspend operator fun invoke(channels: List<ChannelEpg>): List<ChannelEpg> {
        val select = channels.map { it.channelEpgId }

        val programsByIds =
            epgProgramRepository.getEpgProgramsByIds(
                channelIds = select,
                time = TimeUtils.actualDate,
            ).asSequence()

        val groupedProgramsByIds = programsByIds.groupBy { it.channelId }

        val channelEpgData =
            buildList {
                channels.forEach {
                    val programs = groupedProgramsByIds[it.channelEpgId] ?: emptyList()
                    add(it.copy(programs = programs))
                }
            }

        return channelEpgData
    }
}
