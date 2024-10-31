/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:12
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import com.mvproject.tinyiptvkmp.data.usecases.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.SavePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.data.usecases.SavePlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateRemotePlaylistChannelsUseCase
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
