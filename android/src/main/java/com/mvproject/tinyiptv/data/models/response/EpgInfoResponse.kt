/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 31.05.23, 17:08
 *
 */

package com.mvproject.tinyiptv.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpgInfoResponse(
    @SerialName("chan_id")
    val channelId: String,
    @SerialName("chan_names")
    val channelNames: String,
    @SerialName("chan_icon")
    val channelIcon: String
)