package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository

interface UpdateInfoUpdatePeriodUseCase {
    suspend operator fun invoke(period: Int)
}

internal class UpdateInfoUpdatePeriodUseCaseImpl(
    private val repository: EpgRepository,
) : UpdateInfoUpdatePeriodUseCase {
    override suspend operator fun invoke(period: Int) {
        repository.updateInfoUpdatePeriod(period)
    }
}
