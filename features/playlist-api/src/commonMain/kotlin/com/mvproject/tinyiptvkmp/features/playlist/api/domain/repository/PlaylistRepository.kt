package com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getPlaylistById(id: String): Playlist

    fun observePlaylists(): Flow<List<Playlist>>

    suspend fun getAllPlaylists(): List<Playlist>

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun savePlaylists(playlists: List<Playlist>)

    suspend fun savePlaylist(playlist: Playlist)
}
