/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:55
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsArgs
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsScreen
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<AppRoutes>.groupChannels() {
    entry<AppRoutes.TvPlaylistChannels> { key ->

        val groupChannelsViewModel = koinViewModel<GroupChannelsViewModel>(
            parameters = {
                parametersOf(
                    GroupChannelsArgs(
                        group = key.group,
                        groupType = key.groupType
                    )
                )
            }
        )

        GroupChannelsScreen(viewModel = groupChannelsViewModel)
    }
}
