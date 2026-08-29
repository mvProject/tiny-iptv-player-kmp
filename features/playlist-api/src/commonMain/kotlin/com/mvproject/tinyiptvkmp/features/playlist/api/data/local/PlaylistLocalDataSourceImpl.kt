package com.mvproject.tinyiptvkmp.features.playlist.api.data.local

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.database.PlaylistDao
import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.database.PlaylistEntity
import kotlinx.coroutines.flow.Flow

internal class PlaylistLocalDataSourceImpl(
    private val playlistDao: PlaylistDao,
) : PlaylistLocalDataSource {
    override suspend fun getPlaylistById(id: String): PlaylistEntity =
        playlistDao.getPlaylistById(id = id)

    override fun observePlaylists(): Flow<List<PlaylistEntity>> =
        playlistDao.getAllPlaylistsAsFlow()

    override suspend fun getAllPlaylists(): List<PlaylistEntity> =
        playlistDao.getAllPlaylists()

    override suspend fun getSelectedPlaylistId(): String? =
        playlistDao.getSelectedPlaylistId()

    override suspend fun getRemotePlaylistsWithUpdatePeriod(
        playlistType: String,
        noUpdatePeriod: Long,
    ): List<PlaylistEntity> =
        playlistDao.getRemotePlaylistsWithUpdatePeriod(
            playlistType = playlistType,
            noUpdatePeriod = noUpdatePeriod,
        )

    override suspend fun deletePlaylist(id: String) {
        playlistDao.deletePlaylist(id = id)
    }

    override suspend fun savePlaylists(playlists: List<PlaylistEntity>) {
        playlistDao.savePlaylists(data = playlists)
    }

    override suspend fun savePlaylist(playlist: PlaylistEntity) {
        playlistDao.savePlaylist(data = playlist)
    }

    override suspend fun selectPlaylist(id: String) {
        playlistDao.selectPlaylist(id = id)
    }
}
