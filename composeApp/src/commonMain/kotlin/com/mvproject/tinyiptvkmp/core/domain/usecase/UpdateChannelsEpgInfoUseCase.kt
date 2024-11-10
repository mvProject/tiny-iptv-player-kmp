package com.mvproject.tinyiptvkmp.core.domain.usecase

import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.data.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository

class UpdateChannelsEpgInfoUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val epgChannelRepository: EpgChannelRepository,
) {
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

        Logger.w("testing update mappedChannels count:${mappedChannels.count()}")

        playlistChannelsRepository.savePlaylistChannels(mappedChannels.toList())

        mappedChannels.forEach { channel ->
            if (channel.channelUrl in favorites) {
                Logger.w("update in favorite ${channel.channelName}")
                favoriteChannelsRepository.updatePlaylistFavoriteChannels(
                    channelName = channel.channelName,
                    channelUrl = channel.channelUrl
                )
            }
        }

        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = false)
    }
}