/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:53
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.menu_channel_option_add_favorite
import tinyiptvkmp.common.generated.resources.menu_channel_option_epg_show
import tinyiptvkmp.common.generated.resources.menu_channel_option_remove_favorite

@Composable
fun OverlayChannelOptions(
    modifier: Modifier = Modifier,
    isEpgEnabled: Boolean = false,
    isInFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onShowEpg: () -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .wrapContentHeight()
                .width(MaterialTheme.dimens.size310)
                .padding(MaterialTheme.dimens.size8),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = MaterialTheme.dimens.size8,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(MaterialTheme.dimens.size24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

            val epgVisibleTextColor =
                if (isEpgEnabled) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.outline
                }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = isEpgEnabled,
                shape = MaterialTheme.shapes.small,
                border =
                    BorderStroke(
                        width = MaterialTheme.dimens.size1,
                        color = MaterialTheme.colorScheme.outline,
                    ),
                contentPadding = PaddingValues(),
                onClick = onShowEpg,
            ) {
                Text(
                    text = stringResource(Res.string.menu_channel_option_epg_show),
                    style = MaterialTheme.typography.titleSmall,
                    color = epgVisibleTextColor,
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

            val favoriteTextColor =
                if (isInFavorite) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }

            val favoriteText =
                if (isInFavorite) {
                    Res.string.menu_channel_option_remove_favorite
                } else {
                    Res.string.menu_channel_option_add_favorite
                }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                border =
                    BorderStroke(
                        width = MaterialTheme.dimens.size1,
                        color = MaterialTheme.colorScheme.outline,
                    ),
                contentPadding = PaddingValues(),
                onClick = onToggleFavorite,
            ) {
                Text(
                    text = stringResource(favoriteText),
                    style = MaterialTheme.typography.titleSmall,
                    color = favoriteTextColor,
                )
            }
        }
    }
}
