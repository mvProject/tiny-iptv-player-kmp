package com.mvproject.tinyiptvkmp.features.channels.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelGridView as DesignSystemChannelGridView

@Composable
fun ChannelGridView(
    modifier: Modifier = Modifier,
    channel: TvChannelWithPrograms,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    DesignSystemChannelGridView(
        modifier = modifier,
        channel = channel.toChannelItemUiModel(),
        onChannelSelect = onChannelSelect,
        onFavoriteClick = onFavoriteClick,
        onShowEpgClick = onShowEpgClick,
    )
}

@Preview
@Composable
private fun PreviewChannelGridViewFavorite() {
    AppTheme {
        ChannelGridView(channel = ChannelPreviewData.channelWithPrograms)
    }
}
