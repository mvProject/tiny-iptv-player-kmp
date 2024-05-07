/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 15:20
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import com.mvproject.tinyiptvkmp.data.usecases.AddLocalPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.AddRemotePlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetDefaultPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetRemotePlaylistsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.SaveLocalPlaylistChannels
import com.mvproject.tinyiptvkmp.data.usecases.SaveRemotePlaylistChannels
import com.mvproject.tinyiptvkmp.data.usecases.ToggleChannelEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdatePlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdateRemotePlaylistChannelsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule =
    module {
        singleOf(::AddLocalPlaylistUseCase)
        singleOf(::AddRemotePlaylistUseCase)
        singleOf(::UpdatePlaylistUseCase)
        singleOf(::DeletePlaylistUseCase)

        singleOf(::GetPlaylistUseCase)
        singleOf(::GetDefaultPlaylistUseCase)
        singleOf(::GetRemotePlaylistsUseCase)

        singleOf(::UpdateRemotePlaylistChannelsUseCase)
        singleOf(::UpdateChannelsEpgInfoUseCase)

        singleOf(::GetPlaylistGroupUseCase)
        singleOf(::GetGroupChannelsUseCase)

        singleOf(::ToggleFavoriteChannelUseCase)
        singleOf(::ToggleChannelEpgUseCase)
        singleOf(::EpgInfoUpdateUseCase)
        singleOf(::UpdateEpgUseCase)

        singleOf(::SaveLocalPlaylistChannels)
        singleOf(::SaveRemotePlaylistChannels)
    }
