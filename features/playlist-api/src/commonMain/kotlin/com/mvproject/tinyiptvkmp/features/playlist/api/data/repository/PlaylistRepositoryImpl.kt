package com.mvproject.tinyiptvkmp.features.playlist.api.data.repository

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.data.mapper.toPlaylist
import com.mvproject.tinyiptvkmp.features.playlist.api.data.mapper.toPlaylistEntity
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.util.playlistUpdatePeriodToDuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PlaylistRepositoryImpl(
    private val local: PlaylistLocalDataSource,
) : PlaylistRepository {
    override suspend fun getSelectedPlaylistId(): String? =
        local.getSelectedPlaylistId()

    override suspend fun getPlaylistById(id: String): Playlist =
        local.getPlaylistById(id = id).toPlaylist()

    override fun observePlaylists(): Flow<List<Playlist>> =
        local.observePlaylists().map { playlists -> playlists.map { it.toPlaylist() } }

    override suspend fun getAllPlaylists(): List<Playlist> =
        local.getAllPlaylists().map { it.toPlaylist() }

    override suspend fun getRemotePlaylistsToRefresh(nowMillis: Long): List<Playlist> =
        local.getRemotePlaylistsWithUpdatePeriod(
            playlistType = PlaylistType.REMOTE.name,
            noUpdatePeriod = 0L,
        ).map { it.toPlaylist() }
            .filter { playlist ->
                val updateDuration = playlistUpdatePeriodToDuration(playlist.updatePeriod.toInt())
                updateDuration > 0L && nowMillis - playlist.lastUpdateDate > updateDuration
            }

    override suspend fun deletePlaylist(playlist: Playlist) {
        local.deletePlaylist(id = playlist.id)
    }

    override suspend fun savePlaylists(playlists: List<Playlist>) {
        local.savePlaylists(playlists.map { it.toPlaylistEntity() })
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        local.savePlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun selectPlaylist(id: String) {
        local.selectPlaylist(id = id)
    }
}
