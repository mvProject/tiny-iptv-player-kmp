/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist.api.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Upsert
    suspend fun savePlaylist(data: PlaylistEntity)

    @Upsert
    suspend fun savePlaylists(data: List<PlaylistEntity>)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsAsFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query(
        "SELECT * FROM playlists " +
                "WHERE playlistType = :playlistType AND updatePeriod != :noUpdatePeriod",
    )
    suspend fun getRemotePlaylistsWithUpdatePeriod(
        playlistType: String,
        noUpdatePeriod: Long,
    ): List<PlaylistEntity>

    @Query("SELECT id FROM playlists WHERE isSelected = 1 LIMIT 1")
    suspend fun getSelectedPlaylistId(): String?

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: String): PlaylistEntity

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylist(id: String)

    @Query("UPDATE playlists SET isSelected = 0 WHERE isSelected = 1")
    suspend fun clearSelectedPlaylists()

    @Query("UPDATE playlists SET isSelected = 1 WHERE id = :id")
    suspend fun setSelectedPlaylist(id: String)

    @Transaction
    suspend fun selectPlaylist(id: String) {
        clearSelectedPlaylists()
        setSelectedPlaylist(id = id)
    }
}
