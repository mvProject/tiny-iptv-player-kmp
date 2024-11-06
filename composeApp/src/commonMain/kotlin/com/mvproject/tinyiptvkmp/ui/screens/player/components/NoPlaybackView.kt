/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player.components

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
import com.mvproject.tinyiptvkmp.core.ui.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.ui.theme.dimens

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
                    .fillMaxSize(MaterialTheme.dimens.fraction70),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = logo,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size96)
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        .padding(MaterialTheme.dimens.size12),
                    contentDescription = text
                )

                SpacerHeight(height = MaterialTheme.dimens.size16)
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// todo replace preview
/*@Composable
@Preview(showBackground = true)
fun DarkPreviewNoPlaybackView() {
    VideoAppTheme(darkTheme = true) {
        NoPlaybackView(
            textRes = R.string.msg_no_internet_found,
            iconRes = R.drawable.ic_no_network
        )
    }
}*/


