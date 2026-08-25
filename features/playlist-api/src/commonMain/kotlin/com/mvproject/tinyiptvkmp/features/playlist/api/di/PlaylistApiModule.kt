package com.mvproject.tinyiptvkmp.features.playlist.api.di

import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.SelectedPlaylistProvider
import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.data.repository.PlaylistRepositoryImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.data.repository.SelectedPlaylistProviderImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetPlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.ObservePlaylistsUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.ObservePlaylistsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SavePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SavePlaylistContentUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SavePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SavePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdateRemotePlaylistChannelsUseCaseImpl
import org.koin.dsl.module

val playlistApiModule =
    module {
        single<PlaylistLocalDataSource> { PlaylistLocalDataSourceImpl(get()) }
        single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }
        single<SelectedPlaylistProvider> { SelectedPlaylistProviderImpl(get()) }

        single<GetPlaylistUseCase> { GetPlaylistUseCaseImpl(get()) }
        single<ObservePlaylistsUseCase> { ObservePlaylistsUseCaseImpl(get()) }
        single<SavePlaylistUseCase> { SavePlaylistUseCaseImpl(get(), get()) }
        single<SelectPlaylistUseCase> { SelectPlaylistUseCaseImpl(get()) }
        single<DeletePlaylistUseCase> { DeletePlaylistUseCaseImpl(get(), get(), get()) }
        single<SavePlaylistContentUseCase> {
            SavePlaylistContentUseCaseImpl(get(), get(), get())
        }
        single<UpdateRemotePlaylistChannelsUseCase> {
            UpdateRemotePlaylistChannelsUseCaseImpl(get(), get(), get(), get())
        }
    }
