package com.mvproject.tinyiptvkmp.features.channels.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelFavoriteSelector as DesignSystemChannelFavoriteSelector

@Composable
fun ChannelFavoriteSelector(
    modifier: Modifier = Modifier,
    favoriteType: String = FavoriteType.NONE.name,
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
