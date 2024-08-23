/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:01
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toPlaylist
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toPlaylistEntity
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepository(
    private val appDatabase: AppDatabase,
) {
    private val playlistDao = appDatabase.playlistDao()

    suspend fun getPlaylistById(id: Long): Playlist =
        playlistDao
            .getPlaylistById(id = id)
            .toPlaylist()

    fun allPlaylistsAsFlow(): Flow<List<Playlist>> =
        playlistDao
            .getAllPlaylistsAsFlow()
            .map { list ->
                list.map {
                    it.toPlaylist()
                }
            }

    suspend fun getAllPlaylists(): List<Playlist> =
        playlistDao
            .getAllPlaylists()
            .map {
                it.toPlaylist()
            }

    suspend fun playlistCount() = playlistDao.getAllPlaylistsCount()

    suspend fun deletePlaylistById(id: Long) {
        playlistDao.deletePlaylist(id = id)
    }

    suspend fun savePlaylist(playlist: Playlist) {
        val playlistEntity = playlist.toPlaylistEntity()
        playlistDao.savePlaylist(data = playlistEntity)
    }
}
