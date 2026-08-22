/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsScreen
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<AppRoutes>.groupChannels(
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: (String, String, String) -> Unit,
) {
    entry<AppRoutes.TvPlaylistChannels> { key ->
        val groupChannelsViewModel = koinViewModel<GroupChannelsViewModel>(
            parameters = { parametersOf(key) }
        )

        GroupChannelsScreen(
            viewModel = groupChannelsViewModel,
            onNavigateBack = onNavigateBack,
            onNavigateToPlayer = onNavigateToPlayer,
        )
    }
}
