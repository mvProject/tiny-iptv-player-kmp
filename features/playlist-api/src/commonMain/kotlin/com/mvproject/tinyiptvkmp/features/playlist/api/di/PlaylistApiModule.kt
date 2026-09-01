package com.mvproject.tinyiptvkmp.features.playlist.api.di

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.data.repository.PlaylistRepositoryImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetPlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetRemotePlaylistsToRefreshUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetRemotePlaylistsToRefreshUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.ObservePlaylistsUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.ObservePlaylistsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistLastUpdateDateUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistLastUpdateDateUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCaseImpl
import org.koin.dsl.module

val playlistApiModule =
    module {
        single<PlaylistLocalDataSource> { PlaylistLocalDataSourceImpl(get()) }
        single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }

        single<GetPlaylistUseCase> { GetPlaylistUseCaseImpl(get()) }
        single<ObservePlaylistsUseCase> { ObservePlaylistsUseCaseImpl(get()) }
        single<GetRemotePlaylistsToRefreshUseCase> { GetRemotePlaylistsToRefreshUseCaseImpl(get()) }
        single<CreatePlaylistUseCase> { CreatePlaylistUseCaseImpl(get()) }
        single<UpdatePlaylistUseCase> { UpdatePlaylistUseCaseImpl(get()) }
        single<SelectPlaylistUseCase> { SelectPlaylistUseCaseImpl(get()) }
        single<DeletePlaylistUseCase> { DeletePlaylistUseCaseImpl(get()) }
        single<UpdatePlaylistLastUpdateDateUseCase> { UpdatePlaylistLastUpdateDateUseCaseImpl(get()) }
    }
