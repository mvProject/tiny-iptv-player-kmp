/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ChannelsEpgInfoResponse(
    val channels: List<EpgInfoResponse>
)