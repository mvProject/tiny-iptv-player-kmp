/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:09
 *
 */

package com.mvproject.tinyiptvkmp.data.model.channels

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.enums.GroupType
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class ChannelsGroup(
    val groupName: String = String.empty,
    val groupType: GroupType = GroupType.ALL,
    val groupFavoriteType: FavoriteType = FavoriteType.NONE,
    val groupContentCount: Int = INT_VALUE_ZERO,
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("groupName: $groupName")
            .append("\n")
            .append("groupContentCount: $groupContentCount")
            .toString()
}
