/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.model.response

import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING
import kotlinx.serialization.Serializable

@Serializable
data class EpgProgramResponse(
    val start: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val category: String = EMPTY_STRING
)