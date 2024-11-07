package com.mvproject.tinyiptvkmp.features.settings.di

import com.mvproject.tinyiptvkmp.features.settings.general.SettingsViewModel
import com.mvproject.tinyiptvkmp.features.settings.player.SettingsPlayerViewModel
import com.mvproject.tinyiptvkmp.features.settings.playlist.SettingsPlaylistViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsModule = module {
    factoryOf(::SettingsPlayerViewModel)
    factoryOf(::SettingsViewModel)
    factoryOf(::SettingsPlaylistViewModel)
}