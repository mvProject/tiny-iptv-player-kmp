/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.repository.EpgInfoRepository
import com.mvproject.tinyiptv.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import io.github.aakira.napier.Napier
import kotlin.time.measureTime

class UpdateChannelsEpgInfoUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val epgInfoRepository: EpgInfoRepository,
) {
    suspend operator fun invoke() {
        if (preferenceRepository.isEpgInfoDataExist()) {
            Napier.i("testing start UpdateChannelsEpgInfoUseCase")

            val epgInfos = epgInfoRepository.loadEpgInfoData().asSequence()
            val channels = playlistChannelsRepository.loadChannels().asSequence()
            val favorites = favoriteChannelsRepository.loadFavoriteChannelUrls()

            val duration = measureTime {
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
                        Napier.i("testing epgInfo.channelLogo ${epgInfo.channelLogo}")
                        Napier.i("testing epgInfo.channelId ${epgInfo.channelId}")
                        channel.copy(
                            channelLogo = epgInfo.channelLogo,
                            epgId = epgInfo.channelId
                        )
                    } else {
                        channel // If no match is found, keep the original Class1 object
                    }
                }

                Napier.w("testing mappedChannels count:${mappedChannels.count()}")

                playlistChannelsRepository.updatePlaylistChannels(mappedChannels.toList())

                mappedChannels.forEach { channel ->
                    if (channel.channelUrl in favorites) {
                        Napier.w("testing update in favorite ${channel.channelName}")
                        favoriteChannelsRepository.updatePlaylistFavoriteChannels(channel = channel)
                    }
                }

                preferenceRepository.setChannelsEpgInfoUpdateRequired(state = false)
            }

            Napier.e("testing duration2 in seconds ${duration}}")
        } else {
            Napier.e("testing UpdateChannelsEpgInfoUseCase EpgInfo not exist")
        }
    }
}