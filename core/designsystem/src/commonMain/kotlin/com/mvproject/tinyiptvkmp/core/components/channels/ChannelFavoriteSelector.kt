package com.mvproject.tinyiptvkmp.core.components.channels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@Composable
fun ChannelFavoriteSelector(
    options: List<FavoriteOptionUiModel>,
    modifier: Modifier = Modifier,
    onSelectFavorite: (FavoriteOptionUiModel) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .width(MaterialTheme.dimensionSize.size310)
            .padding(MaterialTheme.dimensionSize.size8),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = MaterialTheme.dimensionSize.size8,
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimensionSize.size24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            options.forEach { option ->
                val favoriteTextColor =
                    if (option.isSelected) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(
                        width = MaterialTheme.dimensionSize.size1,
                        color = MaterialTheme.colorSchemeExtended.activeInput,
                    ),
                    contentPadding = PaddingValues(),
                    onClick = { onSelectFavorite(option) },
                ) {
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.titleSmall,
                        color = favoriteTextColor,
                    )
                }
            }
        }
    }
}
