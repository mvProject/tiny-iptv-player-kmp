package com.mvproject.tinyiptvkmp.features.channels.api.di

import com.mvproject.tinyiptvkmp.core.datastore.DataStorePathProvider
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStoreFactory
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.ChannelsLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.ChannelsLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.ChannelsPreferencesProto
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.ChannelsPreferencesSerializer
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.FavoriteChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.FavoriteChannelLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.PlaylistChannelLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.remote.PlaylistChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.remote.PlaylistChannelRemoteDataSourceImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.repository.ChannelFavoriteRepositoryImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.repository.ChannelsRepositoryImpl
import com.mvproject.tinyiptvkmp.features.channels.api.data.repository.PlaylistChannelRepositoryImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelsRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.DeletePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.DeletePlaylistContentUseCaseImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.MarkChannelsEpgInfoUpdateRequiredUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.MarkChannelsEpgInfoUpdateRequiredUseCaseImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ObserveChannelsEpgInfoUpdateRequiredUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ObserveChannelsEpgInfoUpdateRequiredUseCaseImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ObserveChannelsSettingsUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ObserveChannelsSettingsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ReplacePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ReplacePlaylistContentUseCaseImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.UpdateChannelsViewTypeUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.UpdateChannelsViewTypeUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val CHANNELS_PREFERENCES = "channelsPreferences"
private const val CHANNELS_PREFERENCES_FILE_NAME = "tiny_iptv_channels_settings.pb"

val channelsApiModule =
    module {
        single<ProtoStore<ChannelsPreferencesProto>>(named(CHANNELS_PREFERENCES)) {
            ProtoStoreFactory.create(
                serializer = ChannelsPreferencesSerializer,
                producePath = {
                    get<DataStorePathProvider>().providePath(CHANNELS_PREFERENCES_FILE_NAME)
                },
            )
        }
        single<ChannelsLocalDataSource> {
            ChannelsLocalDataSourceImpl(get(named(CHANNELS_PREFERENCES)))
        }
        single<FavoriteChannelLocalDataSource> { FavoriteChannelLocalDataSourceImpl(get()) }
        single<PlaylistChannelLocalDataSource> { PlaylistChannelLocalDataSourceImpl(get()) }
        single<PlaylistChannelRemoteDataSource> { PlaylistChannelRemoteDataSourceImpl(get()) }
        single<ChannelsRepository> { ChannelsRepositoryImpl(get()) }
        single<ChannelFavoriteRepository> { ChannelFavoriteRepositoryImpl(get()) }
        single<PlaylistChannelRepository> { PlaylistChannelRepositoryImpl(get(), get()) }

        single<ObserveChannelsSettingsUseCase> { ObserveChannelsSettingsUseCaseImpl(get()) }
        single<ObserveChannelsEpgInfoUpdateRequiredUseCase> {
            ObserveChannelsEpgInfoUpdateRequiredUseCaseImpl(get())
        }
        single<MarkChannelsEpgInfoUpdateRequiredUseCase> {
            MarkChannelsEpgInfoUpdateRequiredUseCaseImpl(get())
        }
        single<UpdateChannelsViewTypeUseCase> { UpdateChannelsViewTypeUseCaseImpl(get()) }
        single<ToggleFavoriteChannelUseCase> { ToggleFavoriteChannelUseCase(get()) }
        single<ReplacePlaylistContentUseCase> { ReplacePlaylistContentUseCaseImpl(get(), get()) }
        single<DeletePlaylistContentUseCase> { DeletePlaylistContentUseCaseImpl(get(), get()) }
    }
