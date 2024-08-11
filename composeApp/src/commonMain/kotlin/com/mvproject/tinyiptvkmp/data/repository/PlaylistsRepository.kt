/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:01
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toPlaylist
import com.mvproject.tinyiptvkmp.data.mappers.EntityMapper.toPlaylistRoom
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepository(
    private val appDatabase: AppDatabase,
) {
    private val playlistDao = appDatabase.playlistDao()

    suspend fun getPlaylistByIdRoom(id: Long): Playlist {
        return playlistDao.getPlaylistInfoById(id = id).toPlaylist()
    }

    fun allPlaylistsFlowRoom(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylistInfoFlow().map { list -> list.map { it.toPlaylist() } }
    }

    suspend fun getAllPlaylistsRoom(): List<Playlist> {
        return playlistDao.getAllPlaylistInfo().map { it.toPlaylist() }
    }

    suspend fun playlistCount() = playlistDao.getAllPlaylistCount()

    suspend fun deletePlaylistByIdRoom(id: Long) {
        playlistDao.deleteSinglePlaylistInfo(id = id)
    }

    suspend fun addPlaylistRoom(playlist: Playlist) {
        val playlistEntity = playlist.toPlaylistRoom()
        playlistDao.insertPlaylistInfo(data = playlistEntity)
    }

    suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = playlist.toPlaylistRoom()
        playlistDao.insertPlaylistInfo(data = playlistEntity)
    }
}
