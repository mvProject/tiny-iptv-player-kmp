package com.mvproject.tinyiptvkmp.navigation

import com.mvproject.tinyiptvkmp.core.navigation.NavigationOptions

internal fun MutableList<AppRoutes>.applyNavigationAction(action: NavigationAction) {
    when (action) {
        is NavigationAction.Navigate -> navigateTo(
            destination = action.destination,
            options = action.options,
        )

        NavigationAction.NavigateUp -> navigateUp()
    }
}

internal fun MutableList<AppRoutes>.navigateTo(
    destination: AppRoutes,
    options: NavigationOptions = NavigationOptions.Default,
) {
    if (options.clearBackStack) {
        replaceAllWith(destination)
        return
    }

    if (options.popUpToStart) {
        popUpToStart(inclusive = options.inclusive)
    }

    if (options.replaceCurrent && isNotEmpty()) {
        removeAt(lastIndex)
    }

    if (options.launchSingleTop && lastOrNull() == destination) {
        return
    }

    add(destination)
}

internal fun MutableList<AppRoutes>.navigateUp() {
    if (size > 1) {
        removeAt(lastIndex)
    }
}

private fun MutableList<AppRoutes>.replaceAllWith(destination: AppRoutes) {
    clear()
    add(destination)
}

private fun MutableList<AppRoutes>.popUpToStart(inclusive: Boolean) {
    if (inclusive) {
        clear()
    } else {
        while (size > 1) {
            removeAt(lastIndex)
        }
    }
}
