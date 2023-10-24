/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.mvproject.tinyiptv.ui.screens.player.VideoView
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import org.koin.androidx.compose.koinViewModel

data class PlayerScreenRoute(
    val mediaUrl: String,
    val mediaGroup: String
) : Screen {

    @Composable
    override fun Content() {
        val videoViewViewModel: VideoViewViewModel = koinViewModel()

        VideoView(
            viewModel = videoViewViewModel,
            channelUrl = mediaUrl,
            channelGroup = mediaGroup
        )
    }
}