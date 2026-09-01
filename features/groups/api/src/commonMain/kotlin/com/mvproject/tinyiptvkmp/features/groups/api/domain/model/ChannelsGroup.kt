/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:09
 *
 */

package com.mvproject.tinyiptvkmp.features.groups.api.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ChannelsGroup(
    val groupId: String = Uuid.random().toString(),
    val groupName: String = "",
    val groupType: GroupType = GroupType.ALL,
    val groupFavoriteType: FavoriteType = FavoriteType.NONE,
    val groupContentCount: Int = 0,
) {
    override fun toString() = buildString {
        append("\n")
        append("groupName: $groupName")
        append("\n")
        append("groupContentCount: $groupContentCount")
    }
}
