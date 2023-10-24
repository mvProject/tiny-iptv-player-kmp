/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mvproject.tinyiptv.VideoAppDatabase
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toPlaylist
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toPlaylistEntity
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistsRepository(private val db: VideoAppDatabase) {
    private val queries = db.playlistQueries

    suspend fun getPlaylistById(id: Long): Playlist? {
        return withContext(Dispatchers.IO) {
            queries.getPlaylistEntityById(id = id)
                .executeAsOneOrNull()
        }?.toPlaylist()
    }

    fun allPlaylistsFlow(): Flow<List<Playlist>> {
        return queries.getAllPlaylistEntities()
            .asFlow()
            .mapToList(Dispatchers.IO)
            //  .distinctUntilChanged()
            .map { list ->
                list.map { item ->
                    item.toPlaylist()
                }
            }
    }

    fun getAllPlaylists(): List<Playlist> {
        return queries.getAllPlaylistEntities()
            .executeAsList()
            .map { entity ->
                entity.toPlaylist()
            }
    }

    val playlistCount
        get() = queries.getAllPlaylistEntities()
            .executeAsList()
            .count()


    suspend fun deletePlaylistById(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deletePlaylistEntityById(id = id)
        }
    }

    suspend fun insertPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            queries.insertPlaylistEntity(
                playlist.toPlaylistEntity()
            )
        }
    }

    suspend fun addPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            queries.addPlaylist(
                playlist.toPlaylistEntity()
            )
        }
    }

    suspend fun updatePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            queries.updatePlaylist(
                id = playlist.id,
                title = playlist.playlistTitle,
                url = playlist.playlistUrl,
                lastUpdateDate = playlist.lastUpdateDate,
                updatePeriod = playlist.updatePeriod,
            )
        }
    }
}
