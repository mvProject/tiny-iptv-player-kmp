/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.components

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
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType

@Composable
fun ChannelFavoriteSelector(
    modifier: Modifier = Modifier,
    favoriteType: String = FavoriteType.NONE.name,
    onSelectFavorite: (FavoriteType) -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .wrapContentHeight()
                .width(MaterialTheme.dimensionSize.size310)
                .padding(MaterialTheme.dimensionSize.size8),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = MaterialTheme.dimensionSize.size8,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(MaterialTheme.dimensionSize.size24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val favTypes = FavoriteType.entries.filter { it != FavoriteType.NONE }

            favTypes.forEach { fav ->

                val favoriteTextColor =
                    if (favoriteType == fav.name) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    border =
                        BorderStroke(
                            width = MaterialTheme.dimensionSize.size1,
                            color = MaterialTheme.colorSchemeExtended.activeInput,
                        ),
                    contentPadding = PaddingValues(),
                    onClick = { onSelectFavorite(fav) },
                ) {
                    Text(
                        text = fav.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = favoriteTextColor,
                    )
                }
            }
        }
    }
}
