package com.mvproject.tinyiptvkmp.core.navigation

data class NavigationOptions(
    val launchSingleTop: Boolean = false,
    val restoreState: Boolean = false,
    val popUpToStart: Boolean = false,
    val inclusive: Boolean = false,
    val replaceCurrent: Boolean = false,
    val clearBackStack: Boolean = false,
) {
    companion object {
        val Default = NavigationOptions()
    }
}
