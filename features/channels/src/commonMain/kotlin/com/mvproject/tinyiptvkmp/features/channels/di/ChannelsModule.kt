package com.mvproject.tinyiptvkmp.features.channels.di

import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val channelsModule = module {
    viewModel<GroupChannelsViewModel>()
}
