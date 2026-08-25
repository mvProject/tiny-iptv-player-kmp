package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_2
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import kotlin.math.abs

private const val MAX_ASPECT_RATIO_DIFFERENCE_FRACTION = 0.01f


fun Modifier.adaptiveLayout(
    videoSize: VideoSize
) = this then layout { measurable, constraints ->

    val resizedConstraint = constraints.resizeForVideo(videoSize)

    val placeable = measurable.measure(resizedConstraint)
    layout(constraints.maxWidth, constraints.maxHeight) {
        // Center x and y axis relative to the layout
        val placeableX = (constraints.maxWidth - resizedConstraint.maxWidth) / INT_VALUE_2
        val placeableY = (constraints.maxHeight - resizedConstraint.maxHeight) / INT_VALUE_2

        placeable.placeRelative(
            x = placeableX,
            y = placeableY
        )
    }
}


private fun Constraints.resizeForVideo(
    videoSize: VideoSize
): Constraints {

    var width = maxWidth
    var height = maxHeight

    // Calculate current screen aspect ratio
    val screenRatio: Float = (maxWidth / maxHeight).toFloat()

    // Get target aspect ratio (if specified in VideoSize, otherwise use screen ratio)
    val targetRatio = when (videoSize) {
        VideoSize.FullScreen -> 1.333f
        VideoSize.WideScreen -> 1.777f
        VideoSize.Cinematic -> 2.333f
        else -> screenRatio
    }

    if (targetRatio <= 0f) return this

    val difference = targetRatio / screenRatio - 1

    if (abs(difference) <= MAX_ASPECT_RATIO_DIFFERENCE_FRACTION) {
        return this
    }

    when (videoSize) {
        VideoSize.FitScreen -> {
            if (difference > 0) {
                height = (maxWidth / targetRatio).toInt()
                width = (height * targetRatio).toInt()
            } else {
                width = (maxHeight * targetRatio).toInt()
                height = (width / targetRatio).toInt()
            }
        }

        VideoSize.FillScreen -> {
            height = maxHeight
            width = maxWidth
        }

        VideoSize.FullScreen -> {
            if (difference > 0) {
                height = (maxWidth / targetRatio).toInt()
            } else {
                width = (maxHeight * targetRatio).toInt()
            }
        }

        VideoSize.WideScreen,
        VideoSize.Cinematic -> {
            // These modes always maintain their aspect ratio while fitting the screen
            if (difference > 0) {
                height = (maxWidth / targetRatio).toInt()
            } else {
                width = maxWidth
            }
        }
    }
    return this.copy(maxWidth = width, maxHeight = height)
}
