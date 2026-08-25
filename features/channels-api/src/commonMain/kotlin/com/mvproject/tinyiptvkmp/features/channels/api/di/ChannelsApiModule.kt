package com.mvproject.tinyiptvkmp.features.channels.api.di

import com.mvproject.tinyiptvkmp.features.channels.api.data.local.FavoriteChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.FavoriteChannelLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.remote.PlaylistChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.remote.PlaylistChannelRemoteDataSourceImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.repository.ChannelFavoriteRepositoryImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.repository.PlaylistChannelRepositoryImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCase
import org.koin.dsl.module

val channelsApiModule =
    module {
        single<FavoriteChannelLocalDataSource> { FavoriteChannelLocalDataSourceImpl(get()) }
        single<PlaylistChannelLocalDataSource> { PlaylistChannelLocalDataSourceImpl(get()) }
        single<PlaylistChannelRemoteDataSource> { PlaylistChannelRemoteDataSourceImpl(get()) }
        single<ChannelFavoriteRepository> { ChannelFavoriteRepositoryImpl(get(), get()) }
        single<PlaylistChannelRepository> { PlaylistChannelRepositoryImpl(get(), get(), get()) }

        single<ToggleFavoriteChannelUseCase> { ToggleFavoriteChannelUseCase(get()) }
    }
