package com.mvproject.tinyiptvkmp.features.groups.api.domain.model

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType

sealed interface ChannelGroupSelection {
    data object All : ChannelGroupSelection

    data class Favorite(val type: FavoriteType) : ChannelGroupSelection

    data class Specified(val groupName: String) : ChannelGroupSelection

    companion object {
        fun fromRoute(
            group: String,
            groupType: String,
        ): ChannelGroupSelection =
            when (GroupType.valueOf(groupType)) {
                GroupType.ALL -> All
                GroupType.FAVORITE -> Favorite(type = FavoriteType.valueOf(group))
                GroupType.SPECIFIED -> Specified(groupName = group)
            }
    }
}
