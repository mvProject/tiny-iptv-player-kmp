package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.enums.GroupType
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toFavType
import com.mvproject.tinyiptvkmp.core.domain.model.ChannelsGroup

class GetPlaylistGroupUseCase(
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
) {
    suspend operator fun invoke(): List<ChannelsGroup> {
        val allChannelsCount =
            playlistChannelsRepository.loadPlaylistChannelsCount()

        val favorites = favoriteChannelsRepository
            .loadSelectedFavoriteChannels()
            .map { item -> item.toFavType() }

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