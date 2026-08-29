package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository

interface UpdateInfoUpdatePeriodUseCase {
    suspend operator fun invoke(period: Int)
}

internal class UpdateInfoUpdatePeriodUseCaseImpl(
    private val repository: SettingsRepository,
) : UpdateInfoUpdatePeriodUseCase {
    override suspend operator fun invoke(period: Int) {
        repository.updateInfoUpdatePeriod(period)
    }
}