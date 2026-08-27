package com.mvproject.tinyiptvkmp.features.settings.di

import com.mvproject.tinyiptvkmp.features.settings.general.SettingsGeneralViewModel
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerViewModel
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val settingsModule = module {
    viewModel<SettingsPlayerViewModel>()
    viewModel<SettingsGeneralViewModel>()
    viewModel<SettingsPlaylistViewModel>()
}
