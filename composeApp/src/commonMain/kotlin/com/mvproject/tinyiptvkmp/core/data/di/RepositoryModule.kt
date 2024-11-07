/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:15
 *
 */

package com.mvproject.tinyiptvkmp.core.data.di

import com.mvproject.tinyiptvkmp.core.data.repository.EpgChannelRepository
import com.mvproject.tinyiptvkmp.core.data.repository.EpgProgramRepository
import com.mvproject.tinyiptvkmp.core.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.LocalPlaylistRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository

import com.mvproject.tinyiptvkmp.core.data.repository.RemotePlaylistRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule =
    module {
        singleOf(::PlaylistsRepository)
        singleOf(::PlaylistChannelsRepository)
        singleOf(::EpgProgramRepository)
        singleOf(::EpgChannelRepository)
        singleOf(::FavoriteChannelsRepository)
        singleOf(::RemotePlaylistRepository)
        singleOf(::LocalPlaylistRepository)
    }
