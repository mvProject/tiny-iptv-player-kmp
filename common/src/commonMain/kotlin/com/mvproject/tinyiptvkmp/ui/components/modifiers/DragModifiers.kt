/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.11.23, 18:02
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.modifiers

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptvkmp.utils.AppConstants.DRAG_THRESHOLD
import com.mvproject.tinyiptvkmp.utils.AppConstants.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.AppConstants.MOVEMENT_THRESHOLD
import com.mvproject.tinyiptvkmp.utils.AppConstants.SCREEN_PERCENTAGE_25
import com.mvproject.tinyiptvkmp.utils.AppConstants.SCREEN_PERCENTAGE_40
import com.mvproject.tinyiptvkmp.utils.SizeUtils.screenLeftPart
import com.mvproject.tinyiptvkmp.utils.SizeUtils.screenMiddlePart
import com.mvproject.tinyiptvkmp.utils.SizeUtils.screenRightPart
import kotlin.math.abs

fun Modifier.defaultPlayerVerticalGestures(
    onAction: (PlaybackActions) -> Unit
) = this then Modifier.pointerInput(Unit) {
    var startY = FLOAT_VALUE_ZERO
    var startX = FLOAT_VALUE_ZERO
    var enxY = FLOAT_VALUE_ZERO

    detectVerticalDragGestures(
        onDragStart = {
            startY = it.y
            startX = it.x
        },
        onDragEnd = {

            if (startX.toInt() in size.screenMiddlePart) {
                val moveOffset = (enxY - startY).toInt()

                if (abs(moveOffset) > (size.height * SCREEN_PERCENTAGE_40)) {
                    val directionRequestType = if (moveOffset > INT_VALUE_ZERO) {
                        PlaybackActions.OnChannelsUiToggle
                    } else
                        PlaybackActions.OnChannelInfoUiToggle
                    onAction(directionRequestType)
                }
            }

            startY = FLOAT_VALUE_ZERO
            startX = FLOAT_VALUE_ZERO
            enxY = FLOAT_VALUE_ZERO
            },
            onVerticalDrag = { change, dragAmount ->
                enxY = change.position.y
                val moveOffset = (enxY - startY).toInt()
                val movement = moveOffset.div(MOVEMENT_THRESHOLD)
                if (movement.mod(DRAG_THRESHOLD) > INT_VALUE_1) {

                    if (startX.toInt() in size.screenLeftPart || startX.toInt() in size.screenRightPart) {
                        if (dragAmount > DRAG_THRESHOLD) {
                            onAction(PlaybackActions.OnVolumeDown)
                        }

                        if (dragAmount < -DRAG_THRESHOLD) {
                            onAction(PlaybackActions.OnVolumeUp)
                        }
                    }
                }

                if (change.positionChange() != Offset.Zero) change.consume()
            }
        )
    }

fun Modifier.defaultPlayerHorizontalGestures(
    onAction: (PlaybackActions) -> Unit
) = this then Modifier.pointerInput(Unit) {
    var startX = FLOAT_VALUE_ZERO
    var endX = FLOAT_VALUE_ZERO

    detectHorizontalDragGestures(
        onDragStart = {
            startX = it.x
        },
        onDragEnd = {
            val moveOffset = endX - startX
            if (abs(moveOffset) > (size.width * SCREEN_PERCENTAGE_25)) {
                val directionRequestType = if (moveOffset > INT_VALUE_ZERO) {
                    PlaybackActions.OnNextSelected
                } else
                    PlaybackActions.OnPreviousSelected
                onAction(directionRequestType)
            }
        },
        onHorizontalDrag = { change, _ ->
            endX = change.position.x
        }
    )
}