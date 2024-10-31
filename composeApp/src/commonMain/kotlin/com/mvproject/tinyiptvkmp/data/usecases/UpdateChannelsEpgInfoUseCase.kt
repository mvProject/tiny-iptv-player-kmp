/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 13.06.24, 09:51
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.KLog

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
                        epgId = epgInfo.programId,
                    )
                } else {
                    channel // If no match is found, keep the original Class1 object
                }
            }

        KLog.w("testing update mappedChannels count:${mappedChannels.count()}")

        playlistChannelsRepository.savePlaylistChannels(mappedChannels.toList())

        mappedChannels.forEach { channel ->
            if (channel.channelUrl in favorites) {
                KLog.w("update in favorite ${channel.channelName}")
                favoriteChannelsRepository.updatePlaylistFavoriteChannels(channel = channel)
            }
        }

        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = false)
    }
}
