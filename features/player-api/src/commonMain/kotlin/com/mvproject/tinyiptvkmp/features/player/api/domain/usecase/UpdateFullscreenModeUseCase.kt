package com.mvproject.tinyiptvkmp.features.player.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.player.api.domain.repository.PlayerRepository

interface UpdateFullscreenModeUseCase {
    suspend operator fun invoke(enabled: Boolean)
}

internal class UpdateFullscreenModeUseCaseImpl(
    private val repository: PlayerRepository,
) : UpdateFullscreenModeUseCase {
    override suspend operator fun invoke(enabled: Boolean) {
        repository.updateFullscreenMode(enabled)
    }
}
