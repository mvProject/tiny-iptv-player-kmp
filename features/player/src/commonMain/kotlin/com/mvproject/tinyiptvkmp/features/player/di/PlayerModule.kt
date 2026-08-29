package com.mvproject.tinyiptvkmp.features.player.di

import com.mvproject.tinyiptvkmp.features.player.PlayerViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val playerModule = module {
    viewModel<PlayerViewModel>()
}
