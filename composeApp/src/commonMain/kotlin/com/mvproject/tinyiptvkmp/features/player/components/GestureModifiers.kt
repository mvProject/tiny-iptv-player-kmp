/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.11.23, 18:02
 *
 */

package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import com.mvproject.tinyiptvkmp.core.common.AppConstants.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.SCREEN_PERCENTAGE_25
import com.mvproject.tinyiptvkmp.core.common.AppConstants.SCREEN_PERCENTAGE_30
import com.mvproject.tinyiptvkmp.core.common.AppConstants.SCREEN_PERCENTAGE_40
import com.mvproject.tinyiptvkmp.core.common.AppConstants.SCREEN_PERCENTAGE_75
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiAction
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs

fun Modifier.handleTapGestures(
    onAction: (UiAction) -> Unit
) = this then pointerInput(Unit) {
    detectTapGestures(
        onDoubleTap = { onAction(UiAction.ToggleFullScreen) },
        onTap = { onAction(UiAction.TogglePlayerUi) },
        onLongPress = { onAction(UiAction.ToggleProgramsUi) }
    )
}

fun Modifier.handleVerticalGestures(
    onAction: (UiAction) -> Unit
) = this then Modifier.pointerInput(Unit) {

    val screenMiddlePart =
        (size.width * SCREEN_PERCENTAGE_25).toInt()..(size.width * SCREEN_PERCENTAGE_75).toInt()

    var startX = FLOAT_VALUE_ZERO
    var totalDrag = FLOAT_VALUE_ZERO
    val dragThreshold = size.height * SCREEN_PERCENTAGE_40
    val volumeThreshold = 100f

    coroutineScope {
        detectVerticalDragGestures(
            onDragStart = {
                startX = it.x
                totalDrag = FLOAT_VALUE_ZERO
            },
            onDragEnd = {
                if (startX.toInt() in screenMiddlePart) {
                    if (abs(totalDrag) > dragThreshold) {
                        val action = if (totalDrag > 0) {
                            UiAction.ToggleChannelsUi
                        } else {
                            UiAction.ToggleProgramInfoUi
                        }
                        onAction(action)
                    }
                }
            },
            onVerticalDrag = { change, dragAmount ->
                totalDrag += dragAmount

                if (startX.toInt() !in screenMiddlePart) {
                    while (abs(totalDrag) >= volumeThreshold) {
                        val volumeAction = if (totalDrag > 0) {
                            UiAction.VolumeDown
                        } else {
                            UiAction.VolumeUp
                        }
                        onAction(volumeAction)
                        totalDrag -= if (totalDrag > 0) volumeThreshold else -volumeThreshold
                    }
                }

                if (change.positionChange() != Offset.Zero) change.consume()
            }
        )
    }
}

fun Modifier.handleHorizontalGestures(
    onAction: (UiAction) -> Unit
) = this then Modifier.pointerInput(Unit) {
    var totalDrag = FLOAT_VALUE_ZERO
    val dragThreshold = size.width * SCREEN_PERCENTAGE_30

    coroutineScope {
        detectHorizontalDragGestures(
            onDragEnd = {
                if (abs(totalDrag) > dragThreshold) {
                    val action = if (totalDrag > FLOAT_VALUE_ZERO) {
                        UiAction.SelectNext
                    } else {
                        UiAction.SelectPrevious
                    }
                    onAction(action)
                }
                totalDrag = 0f

            },
            onHorizontalDrag = { change, dragAmount ->
                totalDrag += dragAmount
                if (change.positionChange() != Offset.Zero) change.consume()
            }
        )
    }
}