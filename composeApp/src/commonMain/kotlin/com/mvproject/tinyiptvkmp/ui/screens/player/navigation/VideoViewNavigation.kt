/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.ui.screens.player.PlayerScreen
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToVideoView(
    mediaName: String,
    mediaGroup: String,
) {
    this.navigate(AppRoutes.VideoView(mediaName = mediaName, mediaGroup = mediaGroup))
}

fun NavGraphBuilder.videoView(onNavigateBack: () -> Unit) {
    composable<AppRoutes.VideoView>{
        val videoViewViewModel = koinViewModel<VideoViewViewModel>()

        PlayerScreen(
            viewModel = videoViewViewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}
