package com.mvproject.tinyiptvkmp.features.playlist.api.data.mapper

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.database.PlaylistEntity
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType

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
