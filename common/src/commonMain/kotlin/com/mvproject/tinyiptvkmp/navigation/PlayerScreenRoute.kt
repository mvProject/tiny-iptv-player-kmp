/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewContainer
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewViewModel

data class PlayerScreenRoute(
    val mediaUrl: String,
    val mediaGroup: String
) : Screen {

    @Composable
    override fun Content() {
        val videoViewViewModel: VideoViewViewModel = getScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        VideoViewContainer(
            viewModel = videoViewViewModel,
            channelUrl = mediaUrl,
            channelGroup = mediaGroup,
            onNavigateBack = {
                navigator.pop()
            }
        )
    }
}

