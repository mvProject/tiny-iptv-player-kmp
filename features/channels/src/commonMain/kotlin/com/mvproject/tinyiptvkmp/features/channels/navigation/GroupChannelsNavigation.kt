/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.navigation

import androidx.compose.runtime.Composable
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsArgs
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsScreen
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GroupChannelsRoute(
    group: String,
    groupType: String,
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: (String, String, String) -> Unit,
) {
    val groupChannelsViewModel = koinViewModel<GroupChannelsViewModel>(
        parameters = { parametersOf(GroupChannelsArgs(group = group, groupType = groupType)) }
    )

    GroupChannelsScreen(
        viewModel = groupChannelsViewModel,
        onNavigateBack = onNavigateBack,
        onNavigateToPlayer = onNavigateToPlayer,
    )
}
