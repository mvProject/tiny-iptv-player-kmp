/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

package com.mvproject.tinyiptvkmp.core.components.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.mvproject.tinyiptvkmp.core.components.buttons.MenuButton
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_back
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_change_view_type
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.action_search
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.menu_view_type_card
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.menu_view_type_grid
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.menu_view_type_list
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithActions(
    appBarTitle: String,
    onBackClick: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onViewTypeChange: (ChannelsViewType) -> Unit = {},
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = INT_VALUE_1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            MenuButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(Res.string.action_back),
                onClick = onBackClick
            )
        },
        actions = {
            MenuButton(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(Res.string.action_search),
                onClick = onSearchClicked
            )

            MenuButton(
                imageVector = Icons.AutoMirrored.Outlined.ViewList,
                contentDescription = stringResource(Res.string.action_change_view_type),
                onClick = { isMenuOpen = !isMenuOpen }
            )

            DropdownMenu(
                modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false },
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Res.string.menu_view_type_list),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.LIST)
                        isMenuOpen = false
                    },
                    colors =
                        MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                        ),
                )
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimensionSize.size8),
                    color = MaterialTheme.colorSchemeExtended.divider,
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Res.string.menu_view_type_grid),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.GRID)
                        isMenuOpen = false
                    },
                    colors =
                        MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                        ),
                )
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimensionSize.size8),
                    color = MaterialTheme.colorSchemeExtended.divider,
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Res.string.menu_view_type_card),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.CARD)
                        isMenuOpen = false
                    },
                    colors =
                        MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                        ),
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
    )
}
