/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.groups.navigation.PlaylistGroupsRoute

fun EntryProviderScope<AppRoutes>.playlistGroups(
    onNavigateToSettings: () -> Unit,
    onNavigateToGroup: (String, String) -> Unit,
) {
    entry<AppRoutes.PlaylistGroup> {
        PlaylistGroupsRoute(
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToGroup = onNavigateToGroup,
        )
    }
}
