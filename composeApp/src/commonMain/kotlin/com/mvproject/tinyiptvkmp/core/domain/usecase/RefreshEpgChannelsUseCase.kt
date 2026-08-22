package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.CHANNELS_SOURCE_URL
import com.mvproject.tinyiptvkmp.core.common.utils.actualDate
import com.mvproject.tinyiptvkmp.core.common.utils.typeToDuration
import com.mvproject.tinyiptvkmp.core.data.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.network.datasource.EpgChannelDatasource
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class RefreshEpgChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val epgChannelDatasource: EpgChannelDatasource,
    private val channelRepository: EpgChannelRepository,
) : KoinComponent {
    private val logger by injectLogger()

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val currentDate = actualDate
            val lastUpdate = preferenceRepository.getEpgInfoDataLastUpdate()
            val updatePeriod =
                typeToDuration(preferenceRepository.getEpgInfoUpdatePeriod())
            val isRequired = (currentDate - lastUpdate) > updatePeriod
            logger.d { "testing RefreshEpgChannelsUseCase isRequired $isRequired" }
            if (isRequired) {
                val sourceChannels =
                    epgChannelDatasource.getChannelsFromSource(
                        sourceUrl = CHANNELS_SOURCE_URL,
                    )

                channelRepository.updateChannels(channels = sourceChannels)
                logger.w { "testing RefreshEpgChannelsUseCase updateChannels complete" }
                preferenceRepository.apply {
                    setEpgInfoDataLastUpdate(timestamp = currentDate)
                    setChannelsEpgInfoUpdateRequired(state = true)
                }
            }
        }
    }
}
