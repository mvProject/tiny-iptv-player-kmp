package com.mvproject.tinyiptvkmp.core.common.utils

import androidx.compose.ui.unit.Constraints
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode

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
}