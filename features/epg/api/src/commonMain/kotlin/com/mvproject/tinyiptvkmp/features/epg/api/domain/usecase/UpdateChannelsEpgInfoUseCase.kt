package com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import org.koin.core.component.KoinComponent

interface UpdateChannelsEpgInfoUseCase {
    suspend operator fun invoke()
}

internal class UpdateChannelsEpgInfoUseCaseImpl(
    private val channelsRepository: ChannelsRepository,
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val channelFavoriteRepository: ChannelFavoriteRepository,
    private val epgChannelRepository: EpgChannelRepository,
) : UpdateChannelsEpgInfoUseCase,
    KoinComponent {
    private val logger by injectLogger()

    override suspend operator fun invoke() {
        val epgInfos = epgChannelRepository.loadEpgInfoData().asSequence()
        val channels = playlistChannelRepository.loadAllChannels().asSequence()
        val favorites = channelFavoriteRepository.loadFavoriteChannelUrls()

        val mappedChannels =
            channels.map { channel ->
                val channelName = channel.channelName
                val epgInfo = epgInfos.firstOrNull { channelName == it.title }

                if (epgInfo != null) {
                    channel.copy(
                        channelLogo = epgInfo.logo,
                        programId = epgInfo.programId,
                    )
                } else {
                    channel // If no match is found, keep the original Class1 object
                }
            }
                .toList()

        logger.d { "Mapped channels count=${mappedChannels.count()}" }

        playlistChannelRepository.savePlaylistChannels(mappedChannels)

        mappedChannels.forEach { channel ->
            if (channel.channelUrl in favorites) {
                logger.d { "Updating favorite channel EPG info" }
                channelFavoriteRepository.updateFavoriteChannel(
                    playlistId = channel.parentListId,
                    channelName = channel.channelName,
                    channelUrl = channel.channelUrl,
                )
            }
        }

        channelsRepository.markChannelsEpgInfoUpdated()
    }
}
