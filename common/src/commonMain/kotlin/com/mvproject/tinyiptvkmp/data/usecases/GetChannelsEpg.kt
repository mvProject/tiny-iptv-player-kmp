/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.05.24, 20:31
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.utils.TimeUtils

class GetChannelsEpg(
    private val epgProgramRepository: EpgProgramRepository,
) {
    suspend operator fun invoke(channelId: String): List<EpgProgram> {
        val programsById =
            epgProgramRepository.getEpgProgramsById(
                channelId = channelId,
                time = TimeUtils.actualDate,
            )

        return programsById
    }
}
