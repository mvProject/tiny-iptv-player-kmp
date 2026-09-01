package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository

interface MarkChannelsEpgInfoUpdateRequiredUseCase {
    suspend operator fun invoke()
}

internal class MarkChannelsEpgInfoUpdateRequiredUseCaseImpl(
    private val repository: ChannelsRepository,
) : MarkChannelsEpgInfoUpdateRequiredUseCase {
    override suspend operator fun invoke() {
        repository.markChannelsEpgInfoUpdateRequired()
    }
}
