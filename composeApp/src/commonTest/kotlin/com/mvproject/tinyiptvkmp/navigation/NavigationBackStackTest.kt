package com.mvproject.tinyiptvkmp.navigation

import com.mvproject.tinyiptvkmp.core.navigation.NavigationOptions
import kotlin.test.Test
import kotlin.test.assertEquals

class NavigationBackStackTest {
    @Test
    fun defaultNavigationPushesDestination() {
        val backStack = mutableListOf<AppRoutes>(AppRoutes.PlaylistGroup)

        backStack.navigateTo(AppRoutes.SettingsGeneral)

        assertEquals(
            routes(AppRoutes.PlaylistGroup, AppRoutes.SettingsGeneral),
            backStack,
        )
    }

    @Test
    fun launchSingleTopDoesNotDuplicateTopDestination() {
        val backStack = mutableListOf<AppRoutes>(
            AppRoutes.PlaylistGroup,
            AppRoutes.SettingsGeneral,
        )

        backStack.navigateTo(
            destination = AppRoutes.SettingsGeneral,
            options = NavigationOptions(launchSingleTop = true),
        )

        assertEquals(
            routes(AppRoutes.PlaylistGroup, AppRoutes.SettingsGeneral),
            backStack,
        )
    }

    @Test
    fun replaceCurrentReplacesTopDestination() {
        val backStack = mutableListOf<AppRoutes>(
            AppRoutes.PlaylistGroup,
            AppRoutes.SettingsGeneral,
        )

        backStack.navigateTo(
            destination = AppRoutes.SettingsPlayer,
            options = NavigationOptions(replaceCurrent = true),
        )

        assertEquals(
            routes(AppRoutes.PlaylistGroup, AppRoutes.SettingsPlayer),
            backStack,
        )
    }

    @Test
    fun popUpToStartKeepsRootAndPushesDestination() {
        val backStack = mutableListOf<AppRoutes>(
            AppRoutes.PlaylistGroup,
            AppRoutes.SettingsGeneral,
            AppRoutes.SettingsPlayer,
        )

        backStack.navigateTo(
            destination = AppRoutes.SettingsPlaylist,
            options = NavigationOptions(popUpToStart = true),
        )

        assertEquals(
            routes(AppRoutes.PlaylistGroup, AppRoutes.SettingsPlaylist),
            backStack,
        )
    }

    @Test
    fun inclusivePopUpToStartReplacesRootWithDestination() {
        val backStack = mutableListOf<AppRoutes>(
            AppRoutes.PlaylistGroup,
            AppRoutes.SettingsGeneral,
            AppRoutes.SettingsPlayer,
        )

        backStack.navigateTo(
            destination = AppRoutes.SettingsPlaylist,
            options = NavigationOptions(
                popUpToStart = true,
                inclusive = true,
            ),
        )

        assertEquals(
            routes(AppRoutes.SettingsPlaylist),
            backStack,
        )
    }

    @Test
    fun clearBackStackMakesDestinationRoot() {
        val backStack = mutableListOf<AppRoutes>(
            AppRoutes.PlaylistGroup,
            AppRoutes.SettingsGeneral,
        )

        backStack.navigateTo(
            destination = AppRoutes.SettingsPlaylist,
            options = NavigationOptions(clearBackStack = true),
        )

        assertEquals(
            routes(AppRoutes.SettingsPlaylist),
            backStack,
        )
    }

    @Test
    fun navigateUpDoesNotRemoveRootDestination() {
        val backStack = mutableListOf<AppRoutes>(AppRoutes.PlaylistGroup)

        backStack.navigateUp()

        assertEquals(
            routes(AppRoutes.PlaylistGroup),
            backStack,
        )
    }
}

private fun routes(vararg routes: AppRoutes): List<AppRoutes> = routes.toList()
