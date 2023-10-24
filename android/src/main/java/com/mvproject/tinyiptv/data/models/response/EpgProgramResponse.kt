/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.models.response

import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING
import kotlinx.serialization.Serializable

@Serializable
data class EpgProgramResponse(
    val start: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val category: String = EMPTY_STRING
)