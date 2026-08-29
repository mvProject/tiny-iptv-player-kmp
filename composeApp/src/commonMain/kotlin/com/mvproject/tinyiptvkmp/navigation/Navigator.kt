package com.mvproject.tinyiptvkmp.navigation

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator
import com.mvproject.tinyiptvkmp.features.settings.nav.SettingsNavigator
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


interface Navigator {
    val startDestination: AppRoutes
    val navigationActions: Flow<NavigationAction>

    suspend fun navigate(destination: AppRoutes)

    suspend fun navigateUp()
}

sealed interface NavigationAction {

    data class Navigate(val destination: AppRoutes) : NavigationAction

    data object NavigateUp : NavigationAction
}

class DefaultNavigator(
    override val startDestination: AppRoutes,
) : KoinComponent, Navigator, AppNavigator, SettingsNavigator {
    private val logger by injectLogger()
    private val _navigationActions = Channel<NavigationAction>()
    override val navigationActions = _navigationActions.receiveAsFlow()

    override suspend fun navigate(
        destination: AppRoutes,
    ) {
        _navigationActions.send(NavigationAction.Navigate(destination = destination))
    }

    override suspend fun navigateUp() {
        logger.d { "navigateUp" }
        _navigationActions.send(NavigationAction.NavigateUp)
    }

    override suspend fun navigateToPlaylistSettings() {
        logger.d { "navigateToPlaylistSettings" }
        _navigationActions.send(NavigationAction.Navigate(destination = AppRoutes.SettingsPlaylist))
    }

    override suspend fun navigateToPlayerSettings() {
        logger.d { "navigateToPlayerSettings" }
        _navigationActions.send(NavigationAction.Navigate(destination = AppRoutes.SettingsPlayer))
    }

    override suspend fun navigateToPlaylist(id: String) {
        logger.d { "navigateToPlaylist" }
        _navigationActions.send(NavigationAction.Navigate(destination = AppRoutes.PlaylistDetail(id = id)))
    }
}