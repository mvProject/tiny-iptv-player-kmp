package com.mvproject.tinyiptvkmp.features.playlist.api.data.local

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.database.PlaylistEntity
import kotlinx.coroutines.flow.Flow

internal interface PlaylistLocalDataSource {
    suspend fun getPlaylistById(id: String): PlaylistEntity

    fun observePlaylists(): Flow<List<PlaylistEntity>>

    suspend fun getAllPlaylists(): List<PlaylistEntity>

    suspend fun deletePlaylist(id: String)

    suspend fun savePlaylists(playlists: List<PlaylistEntity>)

    suspend fun savePlaylist(playlist: PlaylistEntity)
}
