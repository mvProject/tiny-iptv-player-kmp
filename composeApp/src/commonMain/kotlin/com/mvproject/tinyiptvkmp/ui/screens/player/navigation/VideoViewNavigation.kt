/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_VIDEO_VIEW_GROUP
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_VIDEO_VIEW_NAME
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewContainer
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavHostController.navigateToVideoView(mediaName: String, mediaGroup: String) {
    val route = AppRoutes.VideoView.route + "/$mediaName/$mediaGroup"
    this.navigate(route)
}

internal class VideoViewArgs(val media: String, val group: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                media = checkNotNull(savedStateHandle[ARG_VIDEO_VIEW_NAME]) as String,
                group = checkNotNull(savedStateHandle[ARG_VIDEO_VIEW_GROUP]) as String
            )
}

fun NavGraphBuilder.videoView(
    onNavigateBack: () -> Unit
) {
    composable(
        route = AppRoutes.VideoView.route + "/{$ARG_VIDEO_VIEW_NAME}/{$ARG_VIDEO_VIEW_GROUP}",
    ) { backStackEntry ->
        val mediaName =  backStackEntry.arguments?.getString(ARG_VIDEO_VIEW_NAME)
        val mediaGroup = backStackEntry.arguments?.getString(ARG_VIDEO_VIEW_GROUP)
        if (mediaName != null && mediaGroup != null) {
            val videoViewViewModel = koinViewModel<VideoViewViewModel>()

            VideoViewContainer(
                viewModel = videoViewViewModel,
                channelName = mediaName,
                channelGroup = mediaGroup,
                onNavigateBack = onNavigateBack
            )
        }
    }
}