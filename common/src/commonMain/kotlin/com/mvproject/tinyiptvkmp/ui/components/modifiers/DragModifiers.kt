/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
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
import kotlin.math.abs

fun Modifier.defaultPlayerVerticalGestures(
    onAction: (PlaybackActions) -> Unit
) =
    pointerInput(Unit) {
        var startY = 0f
        var startX = 0f
        var enxY = 0f

        val screenMiddle = (size.width * 0.3).toInt()..(size.width * 0.7).toInt()
        val screenLeftPart = 0..(size.width * 0.3).toInt()
        val screenRightPart = (size.width * 0.7).toInt()..size.width

        detectVerticalDragGestures(
            onDragStart = {
                startY = it.y
                startX = it.x
            },
            onDragEnd = {

                if (startX.toInt() in screenMiddle) {
                    val moveOffset = (enxY - startY).toInt()

                    if (abs(moveOffset) > (size.height * 0.4)) {
                        val directionRequestType = if (moveOffset > 0) {
                            PlaybackActions.OnChannelsUiToggle
                        } else
                            PlaybackActions.OnChannelInfoUiToggle
                        onAction(directionRequestType)
                    }
                }

                startY = 0f
                startX = 0f
                enxY = 0f
            },
            onVerticalDrag = { change, dragAmount ->
                enxY = change.position.y
                val moveOffset = (enxY - startY).toInt()
                val movement = moveOffset.div(10)
                if (movement.mod(5) > 1) {

                    if (startX.toInt() in screenLeftPart || startX.toInt() in screenRightPart) {
                        if (dragAmount > 5) {
                            onAction(PlaybackActions.OnVolumeDown)
                        }

                        if (dragAmount < -5) {
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
) = pointerInput(Unit) {
    var startX = -1f
    var endX = -1f

    detectHorizontalDragGestures(
        onDragStart = {
            startX = it.x
        },
        onDragEnd = {
            val moveOffset = endX - startX
            if (abs(moveOffset) > (size.width * 0.25)) {
                val directionRequestType = if (moveOffset > 0) {
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