/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 21:02
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.ui.screens.channels.TvPlaylistChannelsViewModel
import com.mvproject.tinyiptv.ui.screens.groups.GroupViewModel
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import com.mvproject.tinyiptv.ui.screens.playlist.PlaylistViewModel
import com.mvproject.tinyiptv.ui.screens.settings.general.SettingsViewModel
import com.mvproject.tinyiptv.ui.screens.settings.player.SettingsPlayerViewModel
import com.mvproject.tinyiptv.ui.screens.settings.playlist.SettingsPlaylistViewModel
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