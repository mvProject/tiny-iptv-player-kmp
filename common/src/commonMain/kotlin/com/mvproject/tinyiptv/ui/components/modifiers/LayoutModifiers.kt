/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 25.10.23, 16:34
 *
 */

package com.mvproject.tinyiptv.ui.components.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.utils.PlayerUtils.resizeForVideo

fun Modifier.adaptiveLayout(
    aspectRatio: Float,
    resizeMode: ResizeMode = ResizeMode.Fit
) = layout { measurable, constraints ->
    val resizedConstraint = constraints.resizeForVideo(resizeMode, aspectRatio)
    val placeable = measurable.measure(resizedConstraint)
    layout(constraints.maxWidth, constraints.maxHeight) {
        // Center x and y axis relative to the layout
        placeable.placeRelative(
            x = (constraints.maxWidth - resizedConstraint.maxWidth) / 2,
            y = (constraints.maxHeight - resizedConstraint.maxHeight) / 2
        )
    }
}