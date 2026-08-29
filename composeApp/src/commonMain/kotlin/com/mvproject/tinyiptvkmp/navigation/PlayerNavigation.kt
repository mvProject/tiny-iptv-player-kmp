/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.player.navigation.PlayerRoute

fun EntryProviderScope<AppRoutes>.player(onNavigateBack: () -> Unit) {
    entry<AppRoutes.Player> { key ->
        PlayerRoute(
            channelName = key.channelName,
            group = key.group,
            groupType = key.groupType,
            onNavigateBack = onNavigateBack,
        )
    }
}
