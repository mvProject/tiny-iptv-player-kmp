package com.mvproject.tinyiptvkmp.features.playlist.api.data.repository

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.data.mapper.toPlaylist
import com.mvproject.tinyiptvkmp.features.playlist.api.data.mapper.toPlaylistEntity
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PlaylistRepositoryImpl(
    private val local: PlaylistLocalDataSource,
) : PlaylistRepository {
    override suspend fun getPlaylistById(id: String): Playlist =
        local.getPlaylistById(id = id).toPlaylist()

    override fun observePlaylists(): Flow<List<Playlist>> =
        local.observePlaylists().map { playlists -> playlists.map { it.toPlaylist() } }

    override suspend fun getAllPlaylists(): List<Playlist> =
        local.getAllPlaylists().map { it.toPlaylist() }

    override suspend fun deletePlaylist(playlist: Playlist) {
        local.deletePlaylist(id = playlist.id)
    }

    override suspend fun savePlaylists(playlists: List<Playlist>) {
        local.savePlaylists(playlists.map { it.toPlaylistEntity() })
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        local.savePlaylist(playlist.toPlaylistEntity())
    }
}
