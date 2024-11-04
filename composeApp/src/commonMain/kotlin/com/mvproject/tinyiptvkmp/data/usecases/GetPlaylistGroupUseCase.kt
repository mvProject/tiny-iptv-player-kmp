/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:25
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.data.model.channels.ChannelsGroup
import com.mvproject.tinyiptvkmp.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO

class GetPlaylistGroupUseCase(
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(): List<ChannelsGroup> {
        val allChannelsCount =
            playlistChannelsRepository.loadPlaylistChannelsCount()

        val favorites = favoriteChannelsRepository.loadSelectedFavoriteChannels()

        val groups = playlistChannelsRepository.loadPlaylistGroups()

        val allGroup =
            ChannelsGroup(
                groupType = GroupType.ALL,
                groupContentCount = allChannelsCount,
            )

        val favouriteGroups =
            buildList {
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

        val playlistGroups =
            buildList {
                groups.forEach { group ->
                    val count =
                        playlistChannelsRepository
                            .loadPlaylistGroupChannelsCount(group = group)
                    add(
                        ChannelsGroup(
                            groupName = group,
                            groupType = GroupType.SPECIFIED,
                            groupContentCount = count,
                        ),
                    )
                }
            }

        return buildList {
            add(allGroup)
            addAll(favouriteGroups)
            addAll(playlistGroups)
        }
    }
}
