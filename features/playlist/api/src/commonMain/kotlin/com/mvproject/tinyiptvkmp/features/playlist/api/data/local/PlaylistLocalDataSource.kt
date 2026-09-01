package com.mvproject.tinyiptvkmp.features.playlist.api.data.local

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistLocalDataSource {
    suspend fun getPlaylistById(id: String): Playlist

    fun observePlaylists(): Flow<List<Playlist>>

    suspend fun getAllPlaylists(): List<Playlist>

    suspend fun getSelectedPlaylistId(): String?

    suspend fun getRemotePlaylistsWithUpdatePeriod(
        playlistType: String,
        noUpdatePeriod: Long,
    ): List<Playlist>

    suspend fun deletePlaylist(id: String)

    suspend fun savePlaylists(playlists: List<Playlist>)

    suspend fun savePlaylist(playlist: Playlist)

    suspend fun selectPlaylist(id: String)
}
