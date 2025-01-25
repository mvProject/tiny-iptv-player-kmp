/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:09
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.model

import com.mvproject.tinyiptvkmp.core.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.enums.GroupType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ChannelsGroup(
    val groupId: String = Uuid.random().toString(),
    val groupName: String = String.empty,
    val groupType: GroupType = GroupType.ALL,
    val groupFavoriteType: FavoriteType = FavoriteType.NONE,
    val groupContentCount: Int = INT_VALUE_ZERO,
) {
    override fun toString() = buildString {
        append("\n")
        append("groupName: $groupName")
        append("\n")
        append("groupContentCount: $groupContentCount")
    }
}
