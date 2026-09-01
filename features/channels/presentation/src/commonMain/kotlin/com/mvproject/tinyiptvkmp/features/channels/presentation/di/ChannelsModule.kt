package com.mvproject.tinyiptvkmp.features.channels.presentation.di

import com.mvproject.tinyiptvkmp.features.channels.presentation.GroupChannelsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val channelsModule = module {
    viewModel<GroupChannelsViewModel>()
}
