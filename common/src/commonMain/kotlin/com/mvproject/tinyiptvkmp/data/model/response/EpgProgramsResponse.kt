/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 25.10.23, 16:34
 *
 */

package com.mvproject.tinyiptvkmp.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpgProgramsResponse(
    @SerialName("ch_programme")
    val chPrograms: List<EpgProgramResponse>
)