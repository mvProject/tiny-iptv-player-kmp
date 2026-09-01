package com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository
import kotlinx.coroutines.flow.Flow

interface ObserveChannelsEpgInfoUpdateRequiredUseCase {
    operator fun invoke(): Flow<Boolean>
}

internal class ObserveChannelsEpgInfoUpdateRequiredUseCaseImpl(
    private val repository: ChannelsRepository,
) : ObserveChannelsEpgInfoUpdateRequiredUseCase {
    override operator fun invoke(): Flow<Boolean> =
        repository.observeChannelsEpgInfoUpdateRequired()
}
