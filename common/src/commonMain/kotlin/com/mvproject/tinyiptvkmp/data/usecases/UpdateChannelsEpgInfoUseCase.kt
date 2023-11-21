/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.repository.EpgInfoRepository
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.KLog

class UpdateChannelsEpgInfoUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val epgInfoRepository: EpgInfoRepository,
) {
    suspend operator fun invoke() {
        if (preferenceRepository.isEpgInfoDataExist()) {
            val epgInfos = epgInfoRepository.loadEpgInfoData().asSequence()
            val channels = playlistChannelsRepository.loadChannels().asSequence()
            val favorites = favoriteChannelsRepository.loadFavoriteChannelUrls()


                val mappedChannels = channels.map { channel ->
                    val epgInfo = epgInfos.find { epg ->
                        epg.channelName.equals(channel.channelName, ignoreCase = true) ||
                                channel.channelName.equals(
                                    epg.channelName + " HD",
                                    ignoreCase = true
                                )

                        // || channel.channelName.startsWith(it.channelName, ignoreCase = true)
                    }

                    if (epgInfo != null) {
                            channel.copy(
                                channelLogo = epgInfo.channelLogo,
                                epgId = epgInfo.channelId
                            )
                    } else {
                        channel // If no match is found, keep the original Class1 object
                    }
                }

                KLog.w("mappedChannels count:${mappedChannels.count()}")

                playlistChannelsRepository.updatePlaylistChannels(mappedChannels.toList())

                mappedChannels.forEach { channel ->
                    if (channel.channelUrl in favorites) {
                        KLog.w("update in favorite ${channel.channelName}")
                        favoriteChannelsRepository.updatePlaylistFavoriteChannels(channel = channel)
                    }
                }

                preferenceRepository.setChannelsEpgInfoUpdateRequired(state = false)
        } else {
            KLog.e("UpdateChannelsEpgInfoUseCase EpgInfo not exist")
        }
    }
}