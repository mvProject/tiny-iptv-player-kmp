/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.tinyiptv.ui.components.modifiers

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions

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