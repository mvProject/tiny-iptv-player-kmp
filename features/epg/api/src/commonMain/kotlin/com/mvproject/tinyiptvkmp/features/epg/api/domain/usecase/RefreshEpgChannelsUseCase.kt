package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.epgUpdatePeriodToDuration
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

private const val channelsSourceUrl =
    "https://epg.ott-play.com/php/show_prow.php?f=edem/edem.xml.gz"

interface RefreshEpgChannelsUseCase {
    suspend operator fun invoke()
}

internal class RefreshEpgChannelsUseCaseImpl(
    private val epgRepository: EpgRepository,
    private val channelsRepository: ChannelsRepository,
    private val channelRepository: EpgChannelRepository,
) : RefreshEpgChannelsUseCase,
    KoinComponent {
    private val logger by injectLogger()

    override suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val currentDate = actualEpgDate
            val settings = epgRepository.getEpgSettings()
            val lastUpdate = settings.epgInfoDataLastUpdate
            val updatePeriod = epgUpdatePeriodToDuration(settings.epgInfoUpdatePeriod)
            val isRequired = (currentDate - lastUpdate) > updatePeriod
            logger.d { "RefreshEpgChannelsUseCase isRequired=$isRequired" }
            if (isRequired) {
                channelRepository.updateChannelsFromSource(sourceUrl = channelsSourceUrl)
                logger.d { "RefreshEpgChannelsUseCase updateChannels complete" }
                epgRepository.markEpgChannelsRefreshed(lastUpdate = currentDate)
                channelsRepository.markChannelsEpgInfoUpdateRequired()
            }
        }
    }
}
