/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.player.PlayerArgs
import com.mvproject.tinyiptvkmp.features.player.PlayerScreen
import com.mvproject.tinyiptvkmp.features.player.PlayerViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<AppRoutes>.playerScreen() {
    entry<AppRoutes.Player> { key ->
        val playerViewModel = koinViewModel<PlayerViewModel>(
            parameters = {
                parametersOf(
                    PlayerArgs(
                        channelName = key.channelName,
                        group = key.group,
                        groupType = key.groupType,
                    )
                )
            }
        )

        PlayerScreen(viewModel = playerViewModel)
    }
}
