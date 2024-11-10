/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:01
 *
 */

package com.mvproject.tinyiptvkmp.core.data.repository

import com.mvproject.tinyiptvkmp.core.database.db.AppDatabase
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toPlaylist
import com.mvproject.tinyiptvkmp.core.domain.mappers.Mapper.toPlaylistEntity
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepository(
    private val appDatabase: AppDatabase,
) {
    private val playlistDao = appDatabase.playlistDao()

    suspend fun getPlaylistById(id: String): Playlist =
        playlistDao
            .getPlaylistById(id = id)
            .toPlaylist()

    fun allPlaylistsAsFlow(): Flow<List<Playlist>> =
        playlistDao
            .getAllPlaylistsAsFlow()
            .map { list ->
                list.map { it.toPlaylist() }
            }

    suspend fun getAllPlaylists(): List<Playlist> =
        playlistDao
            .getAllPlaylists()
            .map { it.toPlaylist() }

    suspend fun deleteSinglePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(id = playlist.id)
    }

    suspend fun savePlaylists(playlists: List<Playlist>) {
        val lists = playlists.map { it.toPlaylistEntity() }
        playlistDao.savePlaylists(data = lists)
    }

    suspend fun savePlaylist(playlist: Playlist) {
        val playlistEntity = playlist.toPlaylistEntity()
        playlistDao.savePlaylist(data = playlistEntity)
    }
}
