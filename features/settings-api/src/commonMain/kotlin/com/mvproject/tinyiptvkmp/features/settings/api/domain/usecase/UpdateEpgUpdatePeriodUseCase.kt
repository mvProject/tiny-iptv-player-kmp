package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository

interface UpdateEpgUpdatePeriodUseCase {
    suspend operator fun invoke(period: Int)
}

internal class UpdateEpgUpdatePeriodUseCaseImpl(
    private val repository: SettingsRepository,
) : UpdateEpgUpdatePeriodUseCase {
    override suspend operator fun invoke(period: Int) {
        repository.updateEpgUpdatePeriod(period)
    }
}