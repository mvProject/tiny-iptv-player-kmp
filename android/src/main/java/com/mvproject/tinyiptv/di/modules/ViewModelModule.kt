/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.ui.screens.channels.TvPlaylistChannelsViewModel
import com.mvproject.tinyiptv.ui.screens.groups.GroupViewModel
import com.mvproject.tinyiptv.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import com.mvproject.tinyiptv.ui.screens.playlist.PlaylistViewModel
import com.mvproject.tinyiptv.ui.screens.settings.general.SettingsViewModel
import com.mvproject.tinyiptv.ui.screens.settings.player.SettingsPlayerViewModel
import com.mvproject.tinyiptv.ui.screens.settings.playlist.SettingsPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::VideoViewViewModel)
    viewModelOf(::PlaylistViewModel)
    viewModelOf(::SettingsPlaylistViewModel)
    viewModelOf(::TvPlaylistChannelsViewModel)
    viewModelOf(::GroupViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsPlayerViewModel)
    viewModelOf(::SettingsViewModel)
}