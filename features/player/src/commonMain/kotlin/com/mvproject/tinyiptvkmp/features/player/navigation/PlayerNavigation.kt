/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.player.navigation

import androidx.compose.runtime.Composable
import com.mvproject.tinyiptvkmp.features.player.PlayerArgs
import com.mvproject.tinyiptvkmp.features.player.PlayerScreen
import com.mvproject.tinyiptvkmp.features.player.PlayerViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlayerRoute(
    channelName: String,
    group: String,
    groupType: String,
    onNavigateBack: () -> Unit,
) {
    val playerViewModel = koinViewModel<PlayerViewModel>(
        parameters = {
            parametersOf(
                PlayerArgs(
                    channelName = channelName,
                    group = group,
                    groupType = groupType,
                )
            )
        }
    )

    PlayerScreen(
        viewModel = playerViewModel,
        onNavigateBack = onNavigateBack,
    )
}
