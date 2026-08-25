package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.actualEpgDate
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.epgUpdatePeriodToDuration
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

private const val channelsSourceUrl =
    "https://epg.ott-play.com/php/show_prow.php?f=edem/edem.xml.gz"

interface RefreshEpgChannelsUseCase {
    suspend operator fun invoke()
}

internal class RefreshEpgChannelsUseCaseImpl(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val channelRepository: EpgChannelRepository,
) : RefreshEpgChannelsUseCase,
    KoinComponent {
    private val logger by injectLogger()

    override suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            val currentDate = actualEpgDate
            val preferences = preferencesStore.data.first()
            val lastUpdate = preferences.epgInfoDataLastUpdate
            val updatePeriod = epgUpdatePeriodToDuration(preferences.epgInfoLastUpdatePeriod)
            val isRequired = (currentDate - lastUpdate) > updatePeriod
            logger.d { "testing RefreshEpgChannelsUseCase isRequired $isRequired" }
            if (isRequired) {
                channelRepository.updateChannelsFromSource(sourceUrl = channelsSourceUrl)
                logger.w { "testing RefreshEpgChannelsUseCase updateChannels complete" }
                preferencesStore.update { current ->
                    current.copy(
                        epgInfoDataLastUpdate = currentDate,
                        channelsEpgInfoUpdateRequired = true,
                    )
                }
            }
        }
    }
}
