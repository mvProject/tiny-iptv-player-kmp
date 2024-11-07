package com.mvproject.tinyiptvkmp.features.playlist.di

import com.mvproject.tinyiptvkmp.features.playlist.PlaylistViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val playlistModule = module {
    factoryOf(::PlaylistViewModel)
}