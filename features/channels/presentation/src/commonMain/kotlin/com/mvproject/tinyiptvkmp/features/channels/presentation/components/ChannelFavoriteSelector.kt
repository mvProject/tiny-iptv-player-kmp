package com.mvproject.tinyiptvkmp.features.channels.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelFavoriteSelector as DesignSystemChannelFavoriteSelector

@Composable
fun ChannelFavoriteSelector(
    modifier: Modifier = Modifier,
    favoriteType: FavoriteType = FavoriteType.NONE,
    onSelectFavorite: (FavoriteType) -> Unit = {},
) {
    DesignSystemChannelFavoriteSelector(
        modifier = modifier,
        options = favoriteOptionsUiModels(favoriteType = favoriteType),
        onSelectFavorite = { option ->
            onSelectFavorite(FavoriteType.valueOf(option.id))
        },
    )
}
