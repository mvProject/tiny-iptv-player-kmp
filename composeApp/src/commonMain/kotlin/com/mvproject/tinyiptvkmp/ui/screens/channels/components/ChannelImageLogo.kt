/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:50
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import com.mvproject.tinyiptvkmp.ui.components.views.ThreeBounceAnimation
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.painterResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.no_channel_logo

@Composable
fun ChannelImageLogo(
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    channelLogo: String,
    channelName: String,
) {
    val imageSize =
        if (isLarge) {
            MaterialTheme.dimens.size64
        } else {
            MaterialTheme.dimens.size42
        }

    when (val painterResource = asyncPainterResource(data = channelLogo)) {
        is Resource.Loading -> {
            Box(modifier = modifier.size(imageSize)) {
                ThreeBounceAnimation()
            }
        }

        is Resource.Success -> {
            val painter: Painter = painterResource.value
            Image(
                painter,
                contentDescription = channelName,
                modifier =
                    modifier
                        .size(imageSize)
                        .clip(MaterialTheme.shapes.small),
            )
        }

        is Resource.Failure -> {
            Image(
                painter = painterResource(Res.drawable.no_channel_logo),
                contentDescription = channelName,
                modifier =
                    modifier
                        .size(imageSize)
                        .clip(MaterialTheme.shapes.small),
            )
        }
    }
}
