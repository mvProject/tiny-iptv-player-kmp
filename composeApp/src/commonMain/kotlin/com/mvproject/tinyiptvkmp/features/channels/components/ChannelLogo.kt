/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:50
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import org.jetbrains.compose.resources.painterResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.no_channel_logo

@Composable
fun ChannelLogo(
    modifier: Modifier = Modifier,
    channelLogo: String,
    channelName: String,
    imageSize: Dp = MaterialTheme.dimensionSize.size48
) {
    var isLoading by remember {
        mutableStateOf(false)
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
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
