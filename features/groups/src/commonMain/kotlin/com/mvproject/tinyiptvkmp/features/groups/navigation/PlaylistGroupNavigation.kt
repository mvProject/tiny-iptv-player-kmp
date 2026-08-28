/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.features.groups.navigation

import androidx.compose.runtime.Composable
import com.mvproject.tinyiptvkmp.features.groups.GroupScreen
import com.mvproject.tinyiptvkmp.features.groups.GroupViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlaylistGroupsRoute(
    onNavigateToSettings: () -> Unit,
    onNavigateToGroup: (String, String) -> Unit,
) {
    val groupViewModel = koinViewModel<GroupViewModel>()

    GroupScreen(
        viewModel = groupViewModel,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToGroup = onNavigateToGroup,
    )
}
