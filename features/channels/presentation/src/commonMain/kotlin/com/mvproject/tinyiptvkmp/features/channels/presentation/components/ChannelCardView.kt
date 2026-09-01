package com.mvproject.tinyiptvkmp.features.channels.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelCardView as DesignSystemChannelCardView

@Composable
fun ChannelCardView(
    modifier: Modifier = Modifier,
    channel: TvChannelWithPrograms,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    DesignSystemChannelCardView(
        modifier = modifier,
        channel = channel.toChannelItemUiModel(),
        onChannelSelect = onChannelSelect,
        onFavoriteClick = onFavoriteClick,
        onShowEpgClick = onShowEpgClick,
    )
}

@Composable
@Preview
private fun PreviewChannelCardViewFavorite() {
    AppTheme {
        ChannelCardView(channel = ChannelPreviewData.channelWithPrograms)
    }
}
