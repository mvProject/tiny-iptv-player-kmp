/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Entity
data class EpgInfoEntity(
    @PrimaryKey
    val channelId: String = String.empty,
    val channelName: String = String.empty,
    val channelLogo: String = String.empty,
)
