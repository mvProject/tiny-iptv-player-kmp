/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.11.23, 14:18
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.navigateToTvPlaylistChannels
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.tvPlaylistChannels
import com.mvproject.tinyiptvkmp.ui.screens.groups.navigation.playlistGroups
import com.mvproject.tinyiptvkmp.ui.screens.player.navigation.navigateToVideoView
import com.mvproject.tinyiptvkmp.ui.screens.player.navigation.videoView
import com.mvproject.tinyiptvkmp.ui.screens.playlist.navigation.navigateToPlaylistDetail
import com.mvproject.tinyiptvkmp.ui.screens.playlist.navigation.playlistDetail
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.navigation.navigateToSettingsGeneral
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.navigation.settingsGeneral
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.navigation.navigateToSettingsPlayer
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.navigation.settingsPlayer
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.navigation.navigateToSettingsPlaylist
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.navigation.settingsPlaylist
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    startDestination: String
) {
    val navigator = rememberNavigator()

    NavHost(
        modifier = modifier,
        navigator = navigator,
        navTransition = NavTransition(),
        initialRoute = startDestination,
    ) {

        playlistGroups(
            onNavigateToSettings = navigator::navigateToSettingsGeneral,
            onNavigateToGroup = navigator::navigateToTvPlaylistChannels
        )

        tvPlaylistChannels(
            onNavigateBack = navigator::goBack,
            onNavigateSelected = navigator::navigateToVideoView
        )

        playlistDetail(
            onNavigateBack = navigator::goBack
        )

        videoView(
            onNavigateBack = navigator::goBack
        )

        settingsGeneral(
            onNavigateBack = navigator::goBack,
            onNavigatePlaylistSettings = navigator::navigateToSettingsPlaylist,
            onNavigatePlayerSettings = navigator::navigateToSettingsPlayer
        )

        settingsPlaylist(
            onNavigateBack = navigator::goBack,
            onNavigatePlaylist = navigator::navigateToPlaylistDetail
        )

        settingsPlayer(
            onNavigateBack = navigator::goBack
        )

    }
}