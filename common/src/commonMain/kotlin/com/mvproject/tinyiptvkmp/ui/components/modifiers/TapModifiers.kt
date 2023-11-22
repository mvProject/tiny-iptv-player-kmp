/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.modifiers

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.mvproject.tinyiptvkmp.ui.screens.player.action.PlaybackActions

fun Modifier.defaultPlayerTapGesturesState(
    onAction: (PlaybackActions) -> Unit
) = pointerInput(Unit) {
    detectTapGestures(
        onDoubleTap = {
            onAction(PlaybackActions.OnFullScreenToggle)
        },
        onTap = {
            onAction(PlaybackActions.OnPlayerUiToggle)
        },
        onLongPress = {
            onAction(PlaybackActions.OnEpgUiToggle)
        }
    )
}