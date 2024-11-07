package com.mvproject.tinyiptvkmp.features.player.di

import com.mvproject.tinyiptvkmp.features.player.PlayerViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val playerModule = module {
    factoryOf(::PlayerViewModel)
}