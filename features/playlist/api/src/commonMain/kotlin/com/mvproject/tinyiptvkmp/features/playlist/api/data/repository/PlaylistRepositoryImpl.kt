package com.mvproject.tinyiptvkmp.features.playlist.api.data.repository

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.util.playlistUpdatePeriodToDuration
import kotlinx.coroutines.flow.Flow

internal class PlaylistRepositoryImpl(
    private val localDataSource: PlaylistLocalDataSource,
) : PlaylistRepository {
    override suspend fun getSelectedPlaylistId(): String? =
        localDataSource.getSelectedPlaylistId()

    override suspend fun getPlaylistById(id: String): Playlist =
        localDataSource.getPlaylistById(id = id)

    override fun observePlaylists(): Flow<List<Playlist>> =
        localDataSource.observePlaylists()

    override suspend fun getAllPlaylists(): List<Playlist> =
        localDataSource.getAllPlaylists()

    override suspend fun getRemotePlaylistsToRefresh(nowMillis: Long): List<Playlist> =
        localDataSource.getRemotePlaylistsWithUpdatePeriod(
            playlistType = PlaylistType.REMOTE.name,
            noUpdatePeriod = 0L,
        ).filter { playlist ->
            val updateDuration = playlistUpdatePeriodToDuration(playlist.updatePeriod.toInt())
            updateDuration > 0L && nowMillis - playlist.lastUpdateDate > updateDuration
        }


    override suspend fun deletePlaylist(playlist: Playlist) {
        localDataSource.deletePlaylist(id = playlist.id)
    }

    override suspend fun savePlaylists(playlists: List<Playlist>) {
        localDataSource.savePlaylists(playlists)
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        localDataSource.savePlaylist(playlist)
    }

    override suspend fun selectPlaylist(id: String) {
        localDataSource.selectPlaylist(id = id)
    }
}
