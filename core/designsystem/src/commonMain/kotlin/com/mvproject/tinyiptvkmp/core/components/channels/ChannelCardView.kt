package com.mvproject.tinyiptvkmp.core.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.components.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.components.texts.ChannelTitleLarge

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelCardView(
    channel: ChannelItemUiModel,
    modifier: Modifier = Modifier,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier
            .height(200.dp)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onShowEpgClick,
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ChannelLogo(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.TopCenter),
                channelLogo = channel.logoUrl,
                channelName = channel.name,
                imageSize = 96.dp,
            )

            ChannelTitleLarge(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.BottomCenter),
                title = channel.name,
                isFavorite = channel.isFavorite,
            )

            FavoriteButton(
                modifier = Modifier.align(Alignment.TopEnd),
                isFavorite = channel.isFavorite,
                onClick = onFavoriteClick,
            )
        }
    }
}
