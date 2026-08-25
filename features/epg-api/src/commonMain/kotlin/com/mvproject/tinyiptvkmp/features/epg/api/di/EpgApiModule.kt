package com.mvproject.tinyiptvkmp.features.epg.api.di

import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgChannelLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgProgramLocalDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.local.EpgProgramLocalDataSourceImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.remote.EpgChannelRemoteDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.remote.EpgChannelRemoteDataSourceImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.remote.EpgProgramRemoteDataSource
import com.mvproject.tinyiptvkmp.features.epg.api.data.remote.EpgProgramRemoteDataSourceImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.repository.EpgChannelRepositoryImpl
import com.mvproject.tinyiptvkmp.features.epg.api.data.repository.EpgProgramRepositoryImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.CleanProgramsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgChannelsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgProgramsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateChannelsEpgInfoUseCaseImpl
import org.koin.dsl.module

val epgApiModule =
    module {
        single<EpgChannelLocalDataSource> { EpgChannelLocalDataSourceImpl(get()) }
        single<EpgProgramLocalDataSource> { EpgProgramLocalDataSourceImpl(get()) }
        single<EpgChannelRemoteDataSource> { EpgChannelRemoteDataSourceImpl(get()) }
        single<EpgProgramRemoteDataSource> { EpgProgramRemoteDataSourceImpl(get()) }

        single<EpgChannelRepository> { EpgChannelRepositoryImpl(get(), get()) }
        single<EpgProgramRepository> { EpgProgramRepositoryImpl(get(), get()) }

        single<GetChannelsEpgUseCase> { GetChannelsEpgUseCaseImpl(get()) }
        single<GetGroupChannelsEpgUseCase> { GetGroupChannelsEpgUseCaseImpl(get()) }
        single<RefreshEpgChannelsUseCase> { RefreshEpgChannelsUseCaseImpl(get(), get()) }
        single<RefreshEpgProgramsUseCase> { RefreshEpgProgramsUseCaseImpl(get(), get()) }
        single<CleanProgramsUseCase> { CleanProgramsUseCaseImpl(get(), get()) }
        single<UpdateChannelsEpgInfoUseCase> {
            UpdateChannelsEpgInfoUseCaseImpl(get(), get(), get(), get())
        }
    }
