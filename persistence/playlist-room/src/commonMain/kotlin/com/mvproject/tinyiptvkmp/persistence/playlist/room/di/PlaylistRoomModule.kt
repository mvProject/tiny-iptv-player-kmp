package com.mvproject.tinyiptvkmp.persistence.playlist.room.di

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.persistence.playlist.room.RoomPlaylistLocalDataSource
import org.koin.dsl.module

val playlistRoomModule =
    module {
        single<PlaylistLocalDataSource> { RoomPlaylistLocalDataSource(get()) }
    }
