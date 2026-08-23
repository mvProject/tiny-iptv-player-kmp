package com.mvproject.tinyiptvkmp.features.playlist.di

import com.mvproject.tinyiptvkmp.features.playlist.PlaylistViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val playlistModule = module {
    viewModel<PlaylistViewModel>()
}
