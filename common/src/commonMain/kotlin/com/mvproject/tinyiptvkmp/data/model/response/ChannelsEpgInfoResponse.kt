/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 25.10.23, 16:34
 *
 */

package com.mvproject.tinyiptvkmp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChannelsEpgInfoResponse(
    val channels: List<EpgInfoResponse>
)