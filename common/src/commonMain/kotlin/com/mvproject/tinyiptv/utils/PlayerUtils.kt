/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 16:18
 *
 */

package com.mvproject.tinyiptv.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeMute
import androidx.compose.material.icons.rounded.VolumeUp

object PlayerUtils {
    fun getProperVolumeIcon(value: Int) = when {
        value < 35 -> Icons.Rounded.VolumeMute
        value > 65 -> Icons.Rounded.VolumeUp
        else -> Icons.Rounded.VolumeDown
    }
}