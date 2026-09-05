package com.mvproject.tinyiptvkmp.navigation

import com.mvproject.tinyiptvkmp.core.navigation.AppNavigator
import com.mvproject.tinyiptvkmp.core.navigation.NavigationOptions
import com.mvproject.tinyiptvkmp.features.channels.presentation.nav.GroupChannelsNavigator
import com.mvproject.tinyiptvkmp.features.groups.presentation.nav.GroupNavigator
import com.mvproject.tinyiptvkmp.features.player.presentation.nav.PlayerNavigator
import com.mvproject.tinyiptvkmp.features.playlist.presentation.nav.PlaylistNavigator
import com.mvproject.tinyiptvkmp.features.settings.presentation.nav.SettingsNavigator
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


interface Navigator {
    val navigationActions: Flow<NavigationAction>

    suspend fun navigate(
        destination: AppRoutes,
        options: NavigationOptions = NavigationOptions.Default,
    )

    suspend fun navigateUp()
}

sealed interface NavigationAction {

    data class Navigate(
        val destination: AppRoutes,
        val options: NavigationOptions = NavigationOptions.Default,
    ) : NavigationAction

    data object NavigateUp : NavigationAction
}

class DefaultNavigator :
    KoinComponent,
    Navigator,
    AppNavigator,
    SettingsNavigator,
    PlaylistNavigator,
    GroupNavigator,
    GroupChannelsNavigator,
    PlayerNavigator {
    private val logger by injectLogger()
    private val _navigationActions = Channel<NavigationAction>()
    override val navigationActions = _navigationActions.receiveAsFlow()

    override suspend fun navigate(
        destination: AppRoutes,
        options: NavigationOptions,
    ) {
        _navigationActions.send(
            NavigationAction.Navigate(
                destination = destination,
                options = options,
            )
        )
    }

    override suspend fun navigateUp() {
        logger.d { "navigateUp" }
        _navigationActions.send(NavigationAction.NavigateUp)
    }

    override suspend fun navigateToPlaylistSettings(options: NavigationOptions) {
        logger.d { "navigateToPlaylistSettings" }
        navigate(
            destination = AppRoutes.SettingsPlaylist,
            options = options,
        )
    }

    override suspend fun navigateToPlayerSettings(options: NavigationOptions) {
        logger.d { "navigateToPlayerSettings" }
        navigate(
            destination = AppRoutes.SettingsPlayer,
            options = options,
        )
    }

    override suspend fun navigateToPlaylist(
        id: String,
        options: NavigationOptions,
    ) {
        logger.d { "navigateToPlaylist" }
        navigate(
            destination = AppRoutes.PlaylistDetail(id = id),
            options = options,
        )
    }

    override suspend fun navigateToSettings(options: NavigationOptions) {
        logger.d { "navigateToSettings" }
        navigate(
            destination = AppRoutes.SettingsGeneral,
            options = options,
        )
    }

    override suspend fun navigateToPlaylist(
        playlistId: String,
        groupKey: String,
        groupType: String,
        options: NavigationOptions,
    ) {
        logger.d { "navigateToPlaylist" }
        navigate(
            destination = AppRoutes.TvPlaylistChannels(
                playlistId = playlistId,
                group = groupKey,
                groupType = groupType,
            ),
            options = options,
        )
    }

    override suspend fun navigateToPlayer(
        playlistId: String,
        name: String,
        url: String,
        group: String,
        groupType: String,
        options: NavigationOptions,
    ) {
        logger.d { "navigateToPlayer" }
        navigate(
            destination = AppRoutes.Player(
                playlistId = playlistId,
                channelName = name,
                channelUrl = url,
                group = group,
                groupType = groupType,
            ),
            options = options,
        )
    }
}
