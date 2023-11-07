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
import androidx.compose.ui.unit.Constraints
import com.eygraber.uri.Uri
import com.mvproject.tinyiptv.data.enums.ResizeMode

private const val MAX_ASPECT_RATIO_DIFFERENCE_FRACTION = 0.01f

object PlayerUtils {
    internal fun Constraints.resizeForVideo(
        mode: ResizeMode,
        aspectRatio: Float
    ): Constraints {
        if (aspectRatio <= 0f) return this

        var width = maxWidth
        var height = maxHeight

        val constraintAspectRatio: Float = (width / height).toFloat()
        val difference = aspectRatio / constraintAspectRatio - 1

        if (kotlin.math.abs(difference) <= MAX_ASPECT_RATIO_DIFFERENCE_FRACTION) {
            return this
        }

        when (mode) {
            ResizeMode.Fit -> {
                if (difference > 0) {
                    height = (width / aspectRatio).toInt()
                } else {
                    width = (height * aspectRatio).toInt()
                }
            }

            ResizeMode.Zoom -> {
                if (difference > 0) {
                    width = (height * aspectRatio).toInt()
                } else {
                    height = (width / aspectRatio).toInt()
                }
            }

            ResizeMode.FixedWidth -> {
                height = (width / aspectRatio).toInt()
            }

            ResizeMode.FixedHeight -> {
                width = (height * aspectRatio).toInt()
            }

            ResizeMode.Fill -> Unit
        }
        return this.copy(maxWidth = width, maxHeight = height)
    }

    internal fun getProperVolumeIcon(value: Int) = when {
        value < 35 -> Icons.Rounded.VolumeMute
        value > 65 -> Icons.Rounded.VolumeUp
        else -> Icons.Rounded.VolumeDown
    }

    // todo uri fix
    fun String.getNameFromStringUri() =
        Uri.parse(this).path?.split("/")?.last() ?: AppConstants.EMPTY_STRING
}