/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.groups.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_cartoon
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_common
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_documentary
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_movie
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_show
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_sport
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType
import com.mvproject.tinyiptvkmp.features.groups.presentation.GroupAction
import com.mvproject.tinyiptvkmp.features.groups.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.groups.presentation.generated.resources.channel_folder_all
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun PlaylistGroupItem(
    modifier: Modifier = Modifier,
    group: ChannelsGroup,
    onUiAction: (GroupAction) -> Unit = {},
) {
    val title = group.title()

    ListItem(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    onUiAction(
                        GroupAction.NavigateToGroup(
                            groupKey = group.navigationKey(),
                            groupType = group.groupType.name
                        )
                    )
                },
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
        leadingContent = {
            Icon(
                modifier = Modifier.size(MaterialTheme.dimensionSize.size24),
                imageVector = Icons.Outlined.Folder,
                contentDescription = null,
            )
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trailingContent = {
            if (group.groupContentCount > INT_VALUE_ZERO) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Text(
                        text = group.groupContentCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        },
    )
}

/**
 * Localized row title. Falls back to the raw enum name only for [FavoriteType.NONE],
 * which should never reach a favorite group.
 */
@Composable
private fun ChannelsGroup.title(): String =
    when (groupType) {
        GroupType.ALL -> stringResource(Res.string.channel_folder_all)
        GroupType.FAVORITE ->
            groupFavoriteType.labelRes()?.let { label -> stringResource(label) }
                ?: groupFavoriteType.name

        GroupType.SPECIFIED -> groupName
    }

/**
 * The value the group is addressed by downstream. [ChannelGroupSelection.fromRoute] parses a
 * favorite group's key with `FavoriteType.valueOf`, so it must stay the enum name even though the
 * displayed title is localized.
 */
private fun ChannelsGroup.navigationKey(): String =
    when (groupType) {
        GroupType.FAVORITE -> groupFavoriteType.name
        GroupType.ALL, GroupType.SPECIFIED -> groupName
    }

private fun FavoriteType.labelRes(): StringResource? =
    when (this) {
        FavoriteType.COMMON -> DesignSystemRes.string.favorite_common
        FavoriteType.MOVIE -> DesignSystemRes.string.favorite_movie
        FavoriteType.SHOW -> DesignSystemRes.string.favorite_show
        FavoriteType.SPORT -> DesignSystemRes.string.favorite_sport
        FavoriteType.CARTOON -> DesignSystemRes.string.favorite_cartoon
        FavoriteType.DOCUMENTAL -> DesignSystemRes.string.favorite_documentary
        FavoriteType.NONE -> null
    }
