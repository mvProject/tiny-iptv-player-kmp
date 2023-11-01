/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.10.23, 17:51
 *
 */

package com.mvproject.tinyiptv.ui.components.channels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import com.mvproject.tinyiptv.ui.theme.dimens
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChannelImageLogo(
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    channelLogo: String,
    channelName: String
) {
    val imageSize = if (isLarge) MaterialTheme.dimens.size64 else MaterialTheme.dimens.size42
    when (val painterResource = asyncPainterResource(data = channelLogo)) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }

        is Resource.Success -> {
            val painter: Painter = painterResource.value
            Image(
                painter,
                contentDescription = channelName,
                modifier = modifier
                    .size(imageSize)
                    .clip(MaterialTheme.shapes.small)
            )
        }

        is Resource.Failure -> {
            Image(
                painterResource("drawable/no_channel_logo.png"),
                contentDescription = channelName,
                modifier = modifier
                    .size(imageSize)
                    .clip(MaterialTheme.shapes.small)
            )
        }
    }
}