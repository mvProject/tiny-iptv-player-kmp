package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository

interface UpdateEpgUpdatePeriodUseCase {
    suspend operator fun invoke(period: Int)
}

internal class UpdateEpgUpdatePeriodUseCaseImpl(
    private val repository: EpgRepository,
) : UpdateEpgUpdatePeriodUseCase {
    override suspend operator fun invoke(period: Int) {
        repository.updateEpgUpdatePeriod(period)
    }
}
