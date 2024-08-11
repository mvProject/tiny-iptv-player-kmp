/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 28.05.24, 15:40
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
fun OverlayContent(
    isVisible: Boolean = false,
    onViewTap: () -> Unit = {},
    contentAlpha: Float = MaterialTheme.dimens.alpha80,
    content: @Composable BoxScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .alpha(contentAlpha)
                    .clickable(
                        indication = null,
                        interactionSource =
                            remember {
                                MutableInteractionSource()
                            },
                        onClick = onViewTap,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
