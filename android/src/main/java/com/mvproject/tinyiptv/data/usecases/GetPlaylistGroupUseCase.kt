/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.AppConstants

class GetPlaylistGroupUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository
) {
    suspend operator fun invoke(): List<ChannelsGroup> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val groups = playlistChannelsRepository.loadPlaylistGroups(
            listId = currentPlaylistId
        )
        val allChannelsCount = playlistChannelsRepository.loadPlaylistChannelsCount(
            listId = currentPlaylistId
        )

        // todo without android context
        // AppConstants.FOLDER_CHANNELS_ALL
        // AppConstants.FOLDER_CHANNELS_FAVORITE

        return buildList {
            add(
                ChannelsGroup(
                    groupName = AppConstants.FOLDER_CHANNELS_ALL,
                    groupContentCount = allChannelsCount
                )
            )

            add(
                ChannelsGroup(
                    groupName = AppConstants.FOLDER_CHANNELS_FAVORITE
                )
            )

            groups.forEach { group ->
                val count = playlistChannelsRepository.loadPlaylistGroupChannelsCount(
                    listId = currentPlaylistId,
                    group = group
                )
                add(
                    ChannelsGroup(
                        groupName = group,
                        groupContentCount = count
                    )
                )
            }
        }
    }
}