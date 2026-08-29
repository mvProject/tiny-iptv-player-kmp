package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository

interface UpdateVideoSizeUseCase {
    suspend operator fun invoke(mode: Int)
}

internal class UpdateVideoSizeUseCaseImpl(
    private val repository: SettingsRepository,
) : UpdateVideoSizeUseCase {
    override suspend operator fun invoke(mode: Int) {
        repository.updateVideoSize(mode)
    }
}
