/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.11.23, 15:05
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import com.mvproject.tinyiptvkmp.data.enums.ResizeMode
import com.mvproject.tinyiptvkmp.utils.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_2
import com.mvproject.tinyiptvkmp.utils.PlayerUtils.resizeForVideo

fun Modifier.adaptiveLayout(
    aspectRatio: Float = FLOAT_VALUE_1,
    resizeMode: ResizeMode = ResizeMode.Fit
) = this then layout { measurable, constraints ->
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