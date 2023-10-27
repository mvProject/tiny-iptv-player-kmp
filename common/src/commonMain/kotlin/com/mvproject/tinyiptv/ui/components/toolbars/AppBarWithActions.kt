/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptv.ui.components.toolbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithActions(
    appBarTitle: String,
    onBackClick: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onViewTypeChange: (ChannelsViewType) -> Unit = {}
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            FilledIconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.NavigateBefore,
                    contentDescription = "Back",
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            IconButton(
                onClick = { isMenuOpen = !isMenuOpen }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ViewList,
                    contentDescription = "Change Grid",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            DropdownMenu(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    ),
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false }
            ) {
                DropdownMenuItem(
                    text = {
                        // todo fix hardcoded string resources
                        Text(
                            //  text = stringResource(id = R.string.menu_view_type_list),
                            text = "List",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.LIST)
                        isMenuOpen = !isMenuOpen
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.size8),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DropdownMenuItem(
                    text = {
                        // todo fix hardcoded string resources
                        Text(
                            //  text = stringResource(id = R.string.menu_view_type_grid),
                            text = "Grid",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.GRID)
                        isMenuOpen = !isMenuOpen
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.size8),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DropdownMenuItem(
                    text = {
                        // todo fix hardcoded string resources
                        Text(
                            //text = stringResource(id = R.string.menu_view_type_card),
                            text = "Card",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    onClick = {
                        onViewTypeChange(ChannelsViewType.CARD)
                        isMenuOpen = !isMenuOpen
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

// todo replace preview
/*
@Preview(showBackground = true)
@Composable
fun PreviewDarkAppBarWithActions() {
    VideoAppTheme(darkTheme = true) {
        AppBarWithActions(
            appBarTitle = stringResource(id = R.string.app_name)
        )
    }
}*/
