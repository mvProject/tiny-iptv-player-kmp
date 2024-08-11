/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:25
 *
 */

package com.mvproject.tinyiptvkmp.data.helpers

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO

class GroupHelper(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend fun getAllGroup(): ChannelsGroup {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val allChannelsCount =
            playlistChannelsRepository.loadPlaylistChannelsCount(
                listId = currentPlaylistId,
            )

        return ChannelsGroup(
            groupType = GroupType.ALL,
            groupContentCount = allChannelsCount,
        )
    }

    suspend fun getFavoriteGroups(): List<ChannelsGroup> =
        buildList {
            val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()
            val favorites =
                favoriteChannelsRepository
                    .loadPlaylistFavoriteChannelUrls(listId = currentPlaylistId)

            FavoriteType.entries.forEach { fav ->
                if (fav != FavoriteType.NONE) {
                    val favCount = favorites.count { it.type.name == fav.name }
                    if (fav == FavoriteType.COMMON) {
                        add(
                            ChannelsGroup(
                                groupType = GroupType.FAVORITE,
                                groupFavoriteType = fav,
                                groupContentCount = favCount,
                            ),
                        )
                    } else if (favCount > INT_VALUE_ZERO) {
                        add(
                            ChannelsGroup(
                                groupType = GroupType.FAVORITE,
                                groupFavoriteType = fav,
                                groupContentCount = favCount,
                            ),
                        )
                    }
                }
            }
        }

    suspend fun getPlaylistGroups(): List<ChannelsGroup> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        val groups =
            playlistChannelsRepository.loadPlaylistGroups(
                listId = currentPlaylistId,
            )

        return buildList {
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
