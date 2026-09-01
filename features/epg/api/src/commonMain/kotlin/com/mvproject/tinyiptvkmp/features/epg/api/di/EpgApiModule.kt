package com.mvproject.tinyiptvkmp.features.epg.api.di

import com.mvproject.tinyiptvkmp.core.datastore.DataStorePathProvider
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStoreFactory
import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.remote.EpgChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.remote.EpgChannelRemoteDataSourceImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.remote.EpgProgramRemoteDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.datasource.remote.EpgProgramRemoteDataSourceImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.repository.EpgChannelRepositoryImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.repository.EpgProgramRepositoryImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.repository.EpgRepositoryImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.storage.EpgPreferencesProto
import com.mvproject.tinyiptvkmp.features.epg.api.data.storage.EpgPreferencesSerializer
import com.mvproject.tinyiptvkmp.features.epg.api.data.storage.EpgSettingsLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.storage.EpgSettingsLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.CleanProgramsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.ObserveEpgSettingsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.ObserveEpgSettingsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgChannelsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgProgramsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateChannelsEpgInfoUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateEpgUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateEpgUpdatePeriodUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateInfoUpdatePeriodUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateInfoUpdatePeriodUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val EPG_PREFERENCES = "epgPreferences"
private const val EPG_PREFERENCES_FILE_NAME = "tiny_iptv_epg_settings.pb"

val epgApiModule =
    module {
        single<ProtoStore<EpgPreferencesProto>>(named(EPG_PREFERENCES)) {
            ProtoStoreFactory.create(
                serializer = EpgPreferencesSerializer,
                producePath = {
                    get<DataStorePathProvider>().providePath(EPG_PREFERENCES_FILE_NAME)
                },
            )
        }
        single<EpgSettingsLocalDataSource> {
            EpgSettingsLocalDataSourceImpl(get(named(EPG_PREFERENCES)))
        }
        single<EpgChannelRemoteDataSource> { EpgChannelRemoteDataSourceImpl(get()) }
        single<EpgProgramRemoteDataSource> { EpgProgramRemoteDataSourceImpl(get()) }

        single<EpgChannelRepository> { EpgChannelRepositoryImpl(get(), get()) }
        single<EpgProgramRepository> { EpgProgramRepositoryImpl(get(), get()) }
        single<EpgRepository> { EpgRepositoryImpl(get()) }

        single<GetChannelsEpgUseCase> { GetChannelsEpgUseCaseImpl(get()) }
        single<GetGroupChannelsEpgUseCase> { GetGroupChannelsEpgUseCaseImpl(get()) }
        single<ObserveEpgSettingsUseCase> { ObserveEpgSettingsUseCaseImpl(get()) }
        single<UpdateInfoUpdatePeriodUseCase> { UpdateInfoUpdatePeriodUseCaseImpl(get()) }
        single<UpdateEpgUpdatePeriodUseCase> { UpdateEpgUpdatePeriodUseCaseImpl(get()) }
        single<RefreshEpgChannelsUseCase> { RefreshEpgChannelsUseCaseImpl(get(), get(), get()) }
        single<RefreshEpgProgramsUseCase> { RefreshEpgProgramsUseCaseImpl(get(), get()) }
        single<CleanProgramsUseCase> { CleanProgramsUseCaseImpl(get(), get()) }
        single<UpdateChannelsEpgInfoUseCase> {
            UpdateChannelsEpgInfoUseCaseImpl(get(), get(), get(), get())
        }
    }
