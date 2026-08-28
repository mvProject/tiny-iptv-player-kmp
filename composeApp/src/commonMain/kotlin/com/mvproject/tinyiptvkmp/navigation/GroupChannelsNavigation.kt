/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.channels.navigation.GroupChannelsRoute

fun EntryProviderScope<AppRoutes>.groupChannels(
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: (String, String, String) -> Unit,
) {
    entry<AppRoutes.TvPlaylistChannels> { key ->
        GroupChannelsRoute(
            group = key.group,
            groupType = key.groupType,
            onNavigateBack = onNavigateBack,
            onNavigateToPlayer = onNavigateToPlayer,
        )
    }
}
