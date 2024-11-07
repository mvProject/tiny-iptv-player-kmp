/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:12
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.di

import com.mvproject.tinyiptvkmp.core.domain.usecase.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SavePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SavePlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.UpdateRemotePlaylistChannelsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule =
    module {
        singleOf(::SavePlaylistUseCase)
        singleOf(::SavePlaylistContentUseCase)
        singleOf(::DeletePlaylistUseCase)

        singleOf(::GetPlaylistUseCase)

        singleOf(::UpdateRemotePlaylistChannelsUseCase)
        singleOf(::UpdateChannelsEpgInfoUseCase)

        singleOf(::GetPlaylistGroupUseCase)
        singleOf(::GetGroupChannelsUseCase)

        singleOf(::ToggleFavoriteChannelUseCase)

        singleOf(::GetGroupChannelsEpgUseCase)
        singleOf(::GetChannelsEpgUseCase)
        singleOf(::RefreshEpgChannelsUseCase)
        singleOf(::RefreshEpgProgramsUseCase)
        singleOf(::SelectPlaylistUseCase)
        singleOf(::CleanProgramsUseCase)
    }
