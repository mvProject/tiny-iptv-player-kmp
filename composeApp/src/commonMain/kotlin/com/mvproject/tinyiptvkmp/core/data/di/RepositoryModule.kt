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
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val repositoryModule =
    module {
        single<PlaylistsRepository>()
        single<PlaylistChannelsRepository>()
        single<EpgProgramRepository>()
        single<EpgChannelRepository>()
        single<FavoriteChannelsRepository>()
        single<RemotePlaylistRepository>()
        single<LocalPlaylistRepository>()
    }
