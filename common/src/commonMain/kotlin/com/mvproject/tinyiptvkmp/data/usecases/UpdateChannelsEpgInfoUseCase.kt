/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:29
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.repository.EpgInfoRepository
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.utils.CommonUtils.space
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

            epgInfos.forEach { epg ->
                val epgName = epg.channelName.trim().replace(String.space, String.empty).lowercase()
                val epgNameHd = epgName + "hd"
                if (epgName.contains("кино", ignoreCase = true)) {
                    KLog.w("testing UpdateChannelsEpgInfoUseCase epg $epgName")
                }
            }

            val mappedChannels =
                channels.map { channel ->
                    val channelName =
                        channel.channelName.trim().replace(String.space, String.empty).lowercase()
                    val channelNameHD = channelName + "hd"
                    KLog.d("testing UpdateChannelsEpgInfoUseCase channelName $channelName")
                    val epgInfo =
                        epgInfos.find { epg ->
                            val epgName =
                                epg.channelName.trim().replace(String.space, String.empty)
                                    .lowercase()
                            val epgNameHd = epgName + "hd"

                            channelName == epgName || channelName == epgNameHd || channelNameHD == epgName

                            // epg.channelName.equals(channel.channelName, ignoreCase = true) ||
                            //    channel.channelName.equals(
                            //        epg.channelName + " HD",
                            //        ignoreCase = true,
                            //    ) // ||

                            // channel.channelName.startsWith(epg.channelName, ignoreCase = true)
                        }

                    if (epgInfo != null) {
                        channel.copy(
                            channelLogo = epgInfo.channelLogo,
                            epgId = epgInfo.channelId,
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
