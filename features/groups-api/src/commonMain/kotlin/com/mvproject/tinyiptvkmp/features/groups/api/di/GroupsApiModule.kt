package com.mvproject.tinyiptvkmp.features.groups.api.di

import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetPlaylistGroupUseCase
import org.koin.dsl.module

val groupsApiModule =
    module {
        single<GetPlaylistGroupUseCase> { GetPlaylistGroupUseCase(get(), get()) }
        single<GetGroupChannelsUseCase> { GetGroupChannelsUseCase(get(), get()) }
    }
