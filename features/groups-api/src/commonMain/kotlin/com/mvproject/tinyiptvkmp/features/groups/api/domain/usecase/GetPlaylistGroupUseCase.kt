package com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType

class GetPlaylistGroupUseCase(
    private val playlistChannelRepository: PlaylistChannelRepository,
    private val favoriteChannelsRepository: ChannelFavoriteRepository,
) {
    suspend operator fun invoke(): List<ChannelsGroup> {
        val allChannelsCount =
            playlistChannelRepository.loadPlaylistChannelsCount()

        val favorites = favoriteChannelsRepository
            .loadSelectedFavoriteChannels()

        val groups = playlistChannelRepository.loadPlaylistGroups()

        val allGroup =
            ChannelsGroup(
                groupType = GroupType.ALL,
                groupContentCount = allChannelsCount,
            )

        val favouriteGroups =
            buildList {
                FavoriteType.entries.forEach { fav ->
                    if (fav != FavoriteType.NONE) {
                        val favCount = favorites.count { it.favoriteType == fav.name }
                        if (fav == FavoriteType.COMMON) {
                            add(
                                ChannelsGroup(
                                    groupType = GroupType.FAVORITE,
                                    groupFavoriteType = fav,
                                    groupContentCount = favCount,
                                ),
                            )
                        } else if (favCount > 0) {
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
                        playlistChannelRepository
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
