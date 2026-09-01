/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.11.23, 14:18
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import com.mvproject.tinyiptvkmp.RootViewModel
import com.mvproject.tinyiptvkmp.core.base.mvi.CollectUiEffect
import org.koin.compose.koinInject

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    rootViewModel: RootViewModel,
    startDestination: AppRoutes
) {

    val navigator = koinInject<Navigator>()

    val backStack = rememberSerializable(serializer = SnapshotStateListSerializer()) {
        mutableStateListOf(navigator.startDestination)
    }

    CollectUiEffect(navigator.navigationActions) { action ->
        when (action) {
            is NavigationAction.Navigate -> backStack.add(action.destination)

            NavigationAction.NavigateUp -> backStack.removeLastOrNull()
        }
    }

    NavDisplay(
        modifier = modifier.fillMaxSize().imePadding(),
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = { appTransitionSpec() },
        popTransitionSpec = { appPopTransitionSpec() },
        predictivePopTransitionSpec = { _ -> appPopTransitionSpec() },
        entryProvider = entryProvider {
            playlistGroups()

            groupChannels()

            playlistDetail(rootViewModel = rootViewModel)

            playerScreen()

            settingsGeneral()

            settingsPlaylist(rootViewModel = rootViewModel)

            settingsPlayer()
        },
    )
}
