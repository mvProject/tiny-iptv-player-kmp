/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.network.data.response

data class EpgProgramResponse(
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
    val title: String = "",
    val description: String = "",
    val category: String = "",
)
