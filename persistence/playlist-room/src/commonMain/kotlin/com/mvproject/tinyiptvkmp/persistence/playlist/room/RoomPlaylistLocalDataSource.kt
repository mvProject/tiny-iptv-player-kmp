package com.mvproject.tinyiptvkmp.persistence.playlist.room

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.persistence.playlist.room.database.PlaylistDao
import com.mvproject.tinyiptvkmp.persistence.playlist.room.mapper.toPlaylist
import com.mvproject.tinyiptvkmp.persistence.playlist.room.mapper.toPlaylistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomPlaylistLocalDataSource(
    private val playlistDao: PlaylistDao,
) : PlaylistLocalDataSource {
    override suspend fun getPlaylistById(id: String): Playlist =
        playlistDao.getPlaylistById(id = id).toPlaylist()

    override fun observePlaylists(): Flow<List<Playlist>> =
        playlistDao.getAllPlaylistsAsFlow().map { playlists -> playlists.map { it.toPlaylist() } }

    override suspend fun getAllPlaylists(): List<Playlist> =
        playlistDao.getAllPlaylists().map { it.toPlaylist() }

    override suspend fun getSelectedPlaylistId(): String? =
        playlistDao.getSelectedPlaylistId()

    override suspend fun getRemotePlaylistsWithUpdatePeriod(
        playlistType: String,
        noUpdatePeriod: Long,
    ): List<Playlist> =
        playlistDao.getRemotePlaylistsWithUpdatePeriod(
            playlistType = playlistType,
            noUpdatePeriod = noUpdatePeriod,
        ).map { it.toPlaylist() }

    override suspend fun deletePlaylist(id: String) {
        playlistDao.deletePlaylist(id = id)
    }

    override suspend fun savePlaylists(playlists: List<Playlist>) {
        playlistDao.savePlaylists(data = playlists.map { it.toPlaylistEntity() })
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        playlistDao.savePlaylist(data = playlist.toPlaylistEntity())
    }

    override suspend fun selectPlaylist(id: String) {
        playlistDao.selectPlaylist(id = id)
    }
}

