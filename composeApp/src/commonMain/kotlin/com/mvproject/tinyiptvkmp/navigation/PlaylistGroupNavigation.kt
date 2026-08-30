/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:22
 *
 */

package com.mvproject.tinyiptvkmp.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.mvproject.tinyiptvkmp.features.groups.GroupScreen
import com.mvproject.tinyiptvkmp.features.groups.GroupViewModel
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<AppRoutes>.playlistGroups() {
    entry<AppRoutes.PlaylistGroup> {
        val groupViewModel = koinViewModel<GroupViewModel>()

        GroupScreen(viewModel = groupViewModel)
    }
}
