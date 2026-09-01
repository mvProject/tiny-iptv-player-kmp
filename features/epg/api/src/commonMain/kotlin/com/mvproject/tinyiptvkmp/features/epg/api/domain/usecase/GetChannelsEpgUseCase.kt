package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate

interface GetChannelsEpgUseCase {
    suspend operator fun invoke(channelId: String): List<EpgProgram>
}

internal class GetChannelsEpgUseCaseImpl(
    private val epgProgramRepository: EpgProgramRepository,
) : GetChannelsEpgUseCase {
    override suspend operator fun invoke(channelId: String): List<EpgProgram> =
        epgProgramRepository.getEpgProgramsById(
            channelId = channelId,
            time = actualEpgDate,
        )
}
