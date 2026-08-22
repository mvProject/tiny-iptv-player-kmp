/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.player.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.player.PlayerScreen
import com.mvproject.tinyiptvkmp.features.player.PlayerViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<AppRoutes>.player(onNavigateBack: () -> Unit) {
    entry<AppRoutes.Player> { key ->
        val playerViewModel = koinViewModel<PlayerViewModel>(
            parameters = { parametersOf(key) }
        )

        PlayerScreen(
            viewModel = playerViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
