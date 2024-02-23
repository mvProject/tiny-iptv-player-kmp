/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 30.01.24, 16:37
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.overlay

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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OverlayChannelOptions(
    modifier: Modifier = Modifier,
    isEpgEnabled: Boolean = false,
    isEpgUsing: Boolean = false,
    isInFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onShowEpg: () -> Unit = {},
    onToggleEpgState: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .width(MaterialTheme.dimens.size310)
            .padding(MaterialTheme.dimens.size8),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = MaterialTheme.dimens.size8
    ) {
        val epgUsingTextColor = if (isEpgUsing)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.outline

        val epgUsingText = if (isEpgEnabled)
            Res.string.menu_channel_option_epg_disable
        else
            Res.string.menu_channel_option_epg_enable

        Column(
            modifier = Modifier
                .padding(MaterialTheme.dimens.size24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                enabled = isEpgUsing,
                border = BorderStroke(
                    width = MaterialTheme.dimens.size1,
                    color = MaterialTheme.colorScheme.outline
                ),
                contentPadding = PaddingValues(),
                onClick = onToggleEpgState
            ) {
                Text(
                    text = stringResource(epgUsingText),
                    style = MaterialTheme.typography.titleSmall,
                    color = epgUsingTextColor,
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

            val epgVisibleTextColor = if (isEpgEnabled)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.outline

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = isEpgEnabled,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = MaterialTheme.dimens.size1,
                    color = MaterialTheme.colorScheme.outline
                ),
                contentPadding = PaddingValues(),
                onClick = onShowEpg
            ) {
                Text(
                    text = stringResource(Res.string.menu_channel_option_epg_show),
                    style = MaterialTheme.typography.titleSmall,
                    color = epgVisibleTextColor,
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

            val favoriteTextColor = if (isInFavorite)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onPrimary

            val favoriteText = if (isInFavorite)
                Res.string.menu_channel_option_remove_favorite
            else
                Res.string.menu_channel_option_add_favorite

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = MaterialTheme.dimens.size1,
                    color = MaterialTheme.colorScheme.outline
                ),
                contentPadding = PaddingValues(),
                onClick = onToggleFavorite
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