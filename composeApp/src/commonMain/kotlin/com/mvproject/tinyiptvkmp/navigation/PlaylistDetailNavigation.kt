/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.playlist.navigation.PlaylistDetailRoute

fun EntryProviderScope<AppRoutes>.playlistDetail(onNavigateBack: () -> Unit) {
    entry<AppRoutes.PlaylistDetail> { key ->
        PlaylistDetailRoute(
            playlistId = key.id,
            onNavigateBack = onNavigateBack,
        )
    }
}
