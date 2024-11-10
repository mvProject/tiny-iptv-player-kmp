/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.features.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.features.player.PlayerScreen
import com.mvproject.tinyiptvkmp.features.player.PlayerViewModel
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToPlayer(
    channelName: String,
    group: String,
    groupType: String,
) {
    this.navigate(AppRoutes.Player(channelName = channelName, group = group, groupType = groupType))
}

fun NavGraphBuilder.player(onNavigateBack: () -> Unit) {
    composable<AppRoutes.Player> {
        val playerViewModel = koinViewModel<PlayerViewModel>()

        PlayerScreen(
            viewModel = playerViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
