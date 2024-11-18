package com.mvproject.tinyiptvkmp.core.common.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_2
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode

private const val MAX_ASPECT_RATIO_DIFFERENCE_FRACTION = 0.01f


object PlayerUtils {
    fun Modifier.adaptiveLayout(
        aspectRatio: Float = FLOAT_VALUE_1,
        resizeMode: ResizeMode = ResizeMode.Fit
    ) = this then layout { measurable, constraints ->

        Logger.d("testing adaptiveLayout aspectRatio $aspectRatio")
        Logger.w("testing adaptiveLayout resizeMode $resizeMode")

        val resizedConstraint = constraints.resizeForVideo(resizeMode, aspectRatio)
        val placeable = measurable.measure(resizedConstraint)
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Center x and y axis relative to the layout
            placeable.placeRelative(
                x = (constraints.maxWidth - resizedConstraint.maxWidth) / INT_VALUE_2,
                y = (constraints.maxHeight - resizedConstraint.maxHeight) / INT_VALUE_2
            )
        }
    }


    private fun Constraints.resizeForVideo(
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