package com.mvproject.tinyiptvkmp.core.components.channels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.no_channel_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun ChannelLogo(
    channelLogo: String,
    channelName: String,
    modifier: Modifier = Modifier,
    imageSize: Dp = 48.dp,
) {
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.size(imageSize),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(imageSize)
                .clip(MaterialTheme.shapes.small),
            model = channelLogo.ifBlank { null },
            onLoading = { isLoading = true },
            onError = { isLoading = false },
            onSuccess = { isLoading = false },
            contentScale = ContentScale.FillBounds,
            contentDescription = channelName,
            placeholder = painterResource(Res.drawable.no_channel_logo),
            error = painterResource(Res.drawable.no_channel_logo),
        )

        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
