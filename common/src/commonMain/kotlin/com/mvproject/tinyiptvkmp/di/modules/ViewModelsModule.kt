/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import com.mvproject.tinyiptvkmp.ui.screens.channels.TvPlaylistChannelsViewModel
import com.mvproject.tinyiptvkmp.ui.screens.groups.GroupViewModel
import com.mvproject.tinyiptvkmp.ui.screens.player.VideoViewViewModel
import com.mvproject.tinyiptvkmp.ui.screens.playlist.PlaylistViewModel
import com.mvproject.tinyiptvkmp.ui.screens.settings.general.SettingsViewModel
import com.mvproject.tinyiptvkmp.ui.screens.settings.player.SettingsPlayerViewModel
import com.mvproject.tinyiptvkmp.ui.screens.settings.playlist.SettingsPlaylistViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelsModule = module {
    singleOf(::TvPlaylistChannelsViewModel)
    singleOf(::GroupViewModel)
    singleOf(::SettingsPlayerViewModel)
    singleOf(::SettingsViewModel)
    singleOf(::SettingsPlaylistViewModel)
    singleOf(::VideoViewViewModel)
    singleOf(::PlaylistViewModel)
}