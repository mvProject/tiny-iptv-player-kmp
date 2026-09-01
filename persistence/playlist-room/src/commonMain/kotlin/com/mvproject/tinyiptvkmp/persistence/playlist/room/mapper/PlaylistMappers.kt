package com.mvproject.tinyiptvkmp.persistence.playlist.room.mapper

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.persistence.playlist.room.database.PlaylistEntity

internal fun PlaylistEntity.toPlaylist() =
    Playlist(
        id = id,
        playlistName = playlistName,
        playlistSource = playlistSource,
        playlistType = PlaylistType.valueOf(playlistType),
        lastUpdateDate = lastUpdateDate,
        updatePeriod = updatePeriod,
        isSelected = isSelected,
    )

internal fun Playlist.toPlaylistEntity() =
    PlaylistEntity(
        id = id,
        playlistName = playlistName,
        playlistSource = playlistSource,
        playlistType = playlistType.name,
        lastUpdateDate = lastUpdateDate,
        updatePeriod = updatePeriod,
        isSelected = isSelected,
    )
