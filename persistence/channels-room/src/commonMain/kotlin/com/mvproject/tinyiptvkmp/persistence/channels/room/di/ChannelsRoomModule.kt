package com.mvproject.tinyiptvkmp.persistence.channels.room.di

import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.FavoriteChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.persistence.channels.room.RoomFavoriteChannelLocalDataSource
import com.mvproject.tinyiptvkmp.persistence.channels.room.RoomPlaylistChannelLocalDataSource
import org.koin.dsl.module

val channelsRoomModule =
    module {
        single<FavoriteChannelLocalDataSource> { RoomFavoriteChannelLocalDataSource(get()) }
        single<PlaylistChannelLocalDataSource> { RoomPlaylistChannelLocalDataSource(get()) }
    }
