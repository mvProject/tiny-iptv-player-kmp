/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:25
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

class GetPlaylistGroupUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
) {
    suspend operator fun invoke(): List<ChannelsGroup> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val groups =
            playlistChannelsRepository.loadPlaylistGroups(
                listId = currentPlaylistId,
            )
        val allChannelsCount =
            playlistChannelsRepository.loadPlaylistChannelsCount(
                listId = currentPlaylistId,
            )

        return buildList {
            // add(
            //     ChannelsGroup(
            //         groupName = getString(Res.string.channel_folder_all),
            //         groupContentCount = allChannelsCount,
            //     ),
            // )

            // add(
            //     ChannelsGroup(
            //         groupName = getString(Res.string.channel_folder_favorite),
            //     ),
            // )

            groups.forEach { group ->
                val count =
                    playlistChannelsRepository.loadPlaylistGroupChannelsCount(
                        listId = currentPlaylistId,
                        group = group,
                    )
                add(
                    ChannelsGroup(
                        groupName = group,
                        groupType = GroupType.SPECIFIED,
                        groupContentCount = count,
                    ),
                )
            }
        }
    }
}
