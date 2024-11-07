/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.network.data.response

import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty

data class EpgProgramResponse(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = String.empty,
    val description: String = String.empty,
    val category: String = String.empty,
)
