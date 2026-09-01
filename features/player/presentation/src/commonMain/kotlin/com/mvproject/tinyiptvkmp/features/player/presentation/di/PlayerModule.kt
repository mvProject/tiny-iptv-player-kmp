package com.mvproject.tinyiptvkmp.features.player.presentation.di

import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val playerModule = module {
    viewModel<PlayerViewModel>()
}
