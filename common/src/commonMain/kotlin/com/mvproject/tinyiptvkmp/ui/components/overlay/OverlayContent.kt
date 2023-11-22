/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
fun OverlayContent(
    isVisible: Boolean = false,
    onViewTap: () -> Unit = {},
    contentAlpha: Float = MaterialTheme.dimens.alpha80,
    content: @Composable BoxScope.() -> Unit
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onViewTap() }
                    )
                },
            contentAlignment = Alignment.Center
        ) {

            content()
        }
    }
}