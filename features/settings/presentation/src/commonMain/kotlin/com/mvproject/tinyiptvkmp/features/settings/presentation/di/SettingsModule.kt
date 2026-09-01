package com.mvproject.tinyiptvkmp.features.settings.presentation.di

import com.mvproject.tinyiptvkmp.features.settings.presentation.general.SettingsGeneralViewModel
import com.mvproject.tinyiptvkmp.features.settings.presentation.player.SettingsPlayerViewModel
import com.mvproject.tinyiptvkmp.features.settings.presentation.playlist.SettingsPlaylistViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val settingsModule = module {
    viewModel<SettingsPlayerViewModel>()
    viewModel<SettingsGeneralViewModel>()
    viewModel<SettingsPlaylistViewModel>()
}
