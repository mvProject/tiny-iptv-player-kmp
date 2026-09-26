package com.mvproject.tinyiptvkmp.core.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_add
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_remove
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onClick: () -> Unit = {},
) {
    val contentDescription = stringResource(
        if (isFavorite) Res.string.favorite_remove else Res.string.favorite_add
    )
    val icon = if (isFavorite)
        Icons.Rounded.Favorite
    else
        Icons.Rounded.FavoriteBorder

    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
private fun FavoritePreview() {
    AppTheme {
        FavoriteButton()
    }
}
