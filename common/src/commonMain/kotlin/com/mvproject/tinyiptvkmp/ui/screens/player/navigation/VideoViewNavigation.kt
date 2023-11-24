/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.11.23, 11:43
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.player.navigation

import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_VIDEO_VIEW_GROUP
import com.mvproject.tinyiptvkmp.navigation.NavConstants.ARG_VIDEO_VIEW_NAME
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewContainer
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewViewModel
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

fun Navigator.navigateToVideoView(mediaName: String, mediaGroup: String) {
    val route = AppRoutes.VideoView.route + "/$mediaName/$mediaGroup"
    this.navigate(route)
}

fun RouteBuilder.videoView(
    onNavigateBack: () -> Unit
) {
    scene(
        route = AppRoutes.VideoView.route + "/{$ARG_VIDEO_VIEW_NAME}/{$ARG_VIDEO_VIEW_GROUP}",
        navTransition = NavTransition()
    ) { backStackEntry ->
        val mediaName = backStackEntry.path<String>(ARG_VIDEO_VIEW_NAME)
        val mediaGroup = backStackEntry.path<String>(ARG_VIDEO_VIEW_GROUP)
        if (mediaName != null && mediaGroup != null) {
            val videoViewViewModel = koinViewModel(VideoViewViewModel::class)

            VideoViewContainer(
                viewModel = videoViewViewModel,
                channelName = mediaName,
                channelGroup = mediaGroup,
                onNavigateBack = onNavigateBack
            )
        }
    }
}