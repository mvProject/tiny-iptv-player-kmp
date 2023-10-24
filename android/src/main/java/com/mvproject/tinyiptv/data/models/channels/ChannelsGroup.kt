/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.models.channels

import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_ZERO

data class ChannelsGroup(
    val groupName: String,
    val groupContentCount: Int = INT_VALUE_ZERO
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("groupName: $groupName")
            .append("\n")
            .append("groupContentCount: $groupContentCount")
            .toString()
}