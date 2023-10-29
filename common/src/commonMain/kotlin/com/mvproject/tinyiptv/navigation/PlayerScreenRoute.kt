/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 28.10.23, 15:05
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.mvproject.tinyiptv.platform.PlayerScreenRouteContent
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel

data class PlayerScreenRoute(
    val mediaUrl: String,
    val mediaGroup: String
) : Screen {

    @Composable
    override fun Content() {
        val videoViewViewModel: VideoViewViewModel = getScreenModel()

        PlayerScreenRouteContent(
            viewModel = videoViewViewModel,
            channelUrl = mediaUrl,
            channelGroup = mediaGroup
        )
    }
}

