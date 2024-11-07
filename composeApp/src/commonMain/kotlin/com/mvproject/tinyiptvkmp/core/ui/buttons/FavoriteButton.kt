package com.mvproject.tinyiptvkmp.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.theme.VideoAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onClick: () -> Unit = {},
) {
    val icon = if (isFavorite) Icons.Rounded.Favorite
    else Icons.Rounded.FavoriteBorder

    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "FavoriteButton",
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
private fun FavoritePreview() {
    VideoAppTheme {
        FavoriteButton()
    }
}
