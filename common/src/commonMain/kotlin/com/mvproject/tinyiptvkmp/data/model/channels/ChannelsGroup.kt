/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.model.channels

import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO

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