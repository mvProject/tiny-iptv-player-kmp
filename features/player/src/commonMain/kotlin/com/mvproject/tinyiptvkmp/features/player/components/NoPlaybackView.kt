/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionFraction
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@Composable
fun NoPlaybackView(
    modifier: Modifier = Modifier.fillMaxSize(),
    isVisible: Boolean = false,
    text: String = "NoPlaybackView",
    logo: Painter,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(MaterialTheme.dimensionFraction.fraction70),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = logo,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colorSchemeExtended.emptyProgramTitle
                        )
                        .padding(MaterialTheme.dimensionSize.size12),
                    contentDescription = text
                )

                SpacerHeight(height = 16.dp)
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorSchemeExtended.emptyProgramTitle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
