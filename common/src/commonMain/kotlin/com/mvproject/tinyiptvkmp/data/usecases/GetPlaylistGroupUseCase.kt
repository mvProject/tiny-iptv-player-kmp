/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.MainRes
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

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

        return buildList {
            add(
                ChannelsGroup(
                    groupName = MainRes.string.channel_folder_all,
                    groupContentCount = allChannelsCount
                )
            )

            add(
                ChannelsGroup(
                    groupName = MainRes.string.channel_folder_favorite
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