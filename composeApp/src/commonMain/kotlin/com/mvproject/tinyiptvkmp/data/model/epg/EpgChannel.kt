/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.data.model.epg

import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import kotlinx.serialization.Serializable

@Serializable
data class EpgChannel(
    val id: String,
    val programId: String,
    val title: String,
    val logo: String = String.empty,
)
