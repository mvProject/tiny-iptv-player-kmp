package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.utils.actualDate
import com.mvproject.tinyiptvkmp.core.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram

class GetChannelsEpgUseCase(
    private val epgProgramRepository: EpgProgramRepository,
) {
    suspend operator fun invoke(channelId: String): List<EpgProgram> {
        val programsById =
            epgProgramRepository.getEpgProgramsById(
                channelId = channelId,
                time = actualDate,
            )

        return programsById
    }
}