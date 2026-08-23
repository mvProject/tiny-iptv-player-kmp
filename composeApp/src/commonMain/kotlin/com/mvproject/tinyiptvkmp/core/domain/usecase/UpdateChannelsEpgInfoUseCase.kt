package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import org.koin.core.component.KoinComponent

class UpdateChannelsEpgInfoUseCase(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val epgChannelRepository: EpgChannelRepository,
) : KoinComponent {
    private val logger by injectLogger()

    suspend operator fun invoke() {
        val epgInfos = epgChannelRepository.loadEpgInfoData().asSequence()
        val channels = playlistChannelsRepository.loadAllChannels().asSequence()
        val favorites = favoriteChannelsRepository.loadFavoriteChannelUrls()

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

        logger.w { "testing update mappedChannels count:${mappedChannels.count()}" }

        playlistChannelsRepository.savePlaylistChannels(mappedChannels)

        mappedChannels.forEach { channel ->
            if (channel.channelUrl in favorites) {
                logger.w { "update in favorite ${channel.channelName}" }
                favoriteChannelsRepository.updatePlaylistFavoriteChannels(
                    channelName = channel.channelName,
                    channelUrl = channel.channelUrl
                )
            }
        }

        preferencesStore.update { preferences ->
            preferences.copy(channelsEpgInfoUpdateRequired = false)
        }
    }
}
