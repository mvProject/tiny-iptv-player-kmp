/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:50
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.mvproject.tinyiptvkmp.ui.components.views.ThreeBounceAnimation
import com.mvproject.tinyiptvkmp.ui.theme.dimens
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
    var isLoading by remember {
        mutableStateOf(false)
    }

    val imageSize =
        if (isLarge) {
            MaterialTheme.dimens.size64
        } else {
            MaterialTheme.dimens.size42
        }

    AsyncImage(
        modifier =
            modifier
                .size(imageSize)
                .clip(MaterialTheme.shapes.small),
        model = channelLogo,
        onLoading = {
            isLoading = true
        },
        onError = {
            isLoading = false
        },
        onSuccess = {
            isLoading = false
        },
        contentScale = ContentScale.FillBounds,
        contentDescription = channelName,
        placeholder = painterResource(Res.drawable.no_channel_logo),
        error = painterResource(Res.drawable.no_channel_logo),
    )

    if (isLoading) {
        Box(modifier = modifier.size(imageSize)) {
            ThreeBounceAnimation()
        }
    }
}
