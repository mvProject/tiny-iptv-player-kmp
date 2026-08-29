/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.11.23, 14:18
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    startDestination: AppRoutes
) {
    val backStack = rememberSerializable(serializer = SnapshotStateListSerializer()) {
        mutableStateListOf(startDestination)
    }

    fun navigate(route: AppRoutes) {
        backStack.add(route)
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = ::navigateBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            playlistGroups(
                onNavigateToSettings = { navigate(AppRoutes.SettingsGeneral) },
                onNavigateToGroup = { group, groupType ->
                    navigate(AppRoutes.TvPlaylistChannels(group = group, groupType = groupType))
                },
            )

            groupChannels(
                onNavigateBack = ::navigateBack,
                onNavigateToPlayer = { channelName, group, groupType ->
                    navigate(
                        AppRoutes.Player(
                            channelName = channelName,
                            group = group,
                            groupType = groupType,
                        )
                    )
                },
            )

            playlistDetail(onNavigateBack = ::navigateBack)

            player(onNavigateBack = ::navigateBack)

            settingsGeneral(
                onNavigateBack = ::navigateBack,
                onNavigateToPlaylistSettings = { navigate(AppRoutes.SettingsPlaylist) },
                onNavigateToPlayerSettings = { navigate(AppRoutes.SettingsPlayer) },
            )

            settingsPlaylist(
                onNavigateBack = ::navigateBack,
                onNavigatePlaylist = { id -> navigate(AppRoutes.PlaylistDetail(id = id)) },
            )

            settingsPlayer(onNavigateBack = ::navigateBack)
        },
    )
}
