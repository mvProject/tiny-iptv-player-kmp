package com.mvproject.tinyiptvkmp.features.player.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.player.api.domain.repository.PlayerRepository

interface UpdateVideoSizeUseCase {
    suspend operator fun invoke(mode: Int)
}

internal class UpdateVideoSizeUseCaseImpl(
    private val repository: PlayerRepository,
) : UpdateVideoSizeUseCase {
    override suspend operator fun invoke(mode: Int) {
        repository.updateVideoSize(mode)
    }
}
