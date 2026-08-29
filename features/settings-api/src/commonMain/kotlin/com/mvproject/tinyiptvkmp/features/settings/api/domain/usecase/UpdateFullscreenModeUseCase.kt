package com.mvproject.tinyiptvkmp.features.settings.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.settings.api.domain.repository.SettingsRepository


interface UpdateFullscreenModeUseCase {
    suspend operator fun invoke(enabled: Boolean)
}

internal class UpdateFullscreenModeUseCaseImpl(
    private val repository: SettingsRepository,
) : UpdateFullscreenModeUseCase {
    override suspend operator fun invoke(enabled: Boolean) {
        repository.updateFullscreenMode(enabled)
    }
}