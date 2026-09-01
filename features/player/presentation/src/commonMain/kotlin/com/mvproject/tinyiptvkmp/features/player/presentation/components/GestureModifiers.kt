/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.11.23, 18:02
 *
 */

package com.mvproject.tinyiptvkmp.features.player.presentation.components

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import com.mvproject.tinyiptvkmp.core.foundation.common.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.common.SCREEN_PERCENTAGE_25
import com.mvproject.tinyiptvkmp.core.foundation.common.SCREEN_PERCENTAGE_30
import com.mvproject.tinyiptvkmp.core.foundation.common.SCREEN_PERCENTAGE_40
import com.mvproject.tinyiptvkmp.core.foundation.common.SCREEN_PERCENTAGE_75
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerAction
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerState
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs

fun Modifier.handleTapGestures(
    onAction: (PlayerAction) -> Unit
) = this then pointerInput(Unit) {
    detectTapGestures(
        onDoubleTap = { onAction(PlayerAction.ToggleFullScreen) },
        onTap = { onAction(PlayerAction.TogglePlayer) },
        onLongPress = { onAction(PlayerAction.OpenOsd(PlayerState.PlayerOSD.ChannelPrograms)) }
    )
}

fun Modifier.handleVerticalGestures(
    onAction: (PlayerAction) -> Unit
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
                            PlayerAction.OpenOsd(PlayerState.PlayerOSD.GroupChannels)
                        } else {
                            PlayerAction.OpenOsd(PlayerState.PlayerOSD.ProgramInfo)
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
                            PlayerAction.VolumeDown
                        } else {
                            PlayerAction.VolumeUp
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
    onAction: (PlayerAction) -> Unit
) = this then Modifier.pointerInput(Unit) {
    var totalDrag = FLOAT_VALUE_ZERO
    val dragThreshold = size.width * SCREEN_PERCENTAGE_30

    coroutineScope {
        detectHorizontalDragGestures(
            onDragEnd = {
                if (abs(totalDrag) > dragThreshold) {
                    val action = if (totalDrag > FLOAT_VALUE_ZERO) {
                        PlayerAction.SelectNext
                    } else {
                        PlayerAction.SelectPrevious
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