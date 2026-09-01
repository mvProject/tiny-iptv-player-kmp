package com.mvproject.tinyiptvkmp.features.playlist.presentation.di

import com.mvproject.tinyiptvkmp.features.playlist.presentation.PlaylistViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val playlistModule = module {
    viewModel<PlaylistViewModel>()
}
