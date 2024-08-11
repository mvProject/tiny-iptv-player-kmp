/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.database.entity

import androidx.room.Entity
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Entity(primaryKeys = ["channelId", "channelName"])
data class EpgInfoEntity(
    val channelId: String = String.empty,
    val channelName: String = String.empty,
    val channelLogo: String = String.empty,
    val lastUpdated: Long = LONG_VALUE_ZERO,
)
