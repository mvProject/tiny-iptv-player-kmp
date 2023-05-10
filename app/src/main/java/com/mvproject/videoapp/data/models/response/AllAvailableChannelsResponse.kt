/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.data.models.response

import com.mvproject.videoapp.data.models.parse.AvailableChannelParseModel
import kotlinx.serialization.Serializable

@Serializable
data class AllAvailableChannelsResponse(
    val channels: List<AvailableChannelParseModel>
)