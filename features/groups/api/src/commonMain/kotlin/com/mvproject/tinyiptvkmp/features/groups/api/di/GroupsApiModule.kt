package com.mvproject.tinyiptvkmp.features.groups.api.di

import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetPlaylistGroupUseCaseImpl
import org.koin.dsl.module

val groupsApiModule =
    module {
        single<GetPlaylistGroupUseCase> { GetPlaylistGroupUseCaseImpl(get(), get()) }
        single<GetGroupChannelsUseCase> { GetGroupChannelsUseCaseImpl(get()) }
    }
