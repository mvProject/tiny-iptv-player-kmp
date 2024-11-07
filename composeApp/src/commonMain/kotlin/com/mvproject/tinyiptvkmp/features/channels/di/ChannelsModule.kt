package com.mvproject.tinyiptvkmp.features.channels.di

import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val channelsModule = module {
    factoryOf(::GroupChannelsViewModel)
}