/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 09.05.23, 12:46
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.data.usecases.AddLocalPlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.AddRemotePlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.DeletePlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptv.data.usecases.GetDefaultPlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptv.data.usecases.GetPlaylistGroupUseCase
import com.mvproject.tinyiptv.data.usecases.GetPlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.GetRemotePlaylistsUseCase
import com.mvproject.tinyiptv.data.usecases.ToggleChannelEpgUseCase
import com.mvproject.tinyiptv.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptv.data.usecases.UpdatePlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateRemotePlaylistChannelsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
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
}