/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:24
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.mappers

import com.mvproject.tinyiptvkmp.core.data.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.core.database.entity.EpgProgramEntity
import com.mvproject.tinyiptvkmp.core.database.entity.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.PlaylistEntity
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.FavType
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.model.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType

object EntityMapper {
    fun PlaylistChannelEntity.toPlaylistChannel() =
        with(this) {
            PlaylistChannel(
                channelName = channelName,
                channelLogo = channelLogo,
                channelUrl = channelUrl,
                channelGroup = channelGroup,
                epgId = epgId,
                parentListId = parentListId,
            )
        }

    fun PlaylistChannel.toChannelEntity() =
        with(this) {
            PlaylistChannelEntity(
                channelName = channelName,
                channelLogo = channelLogo,
                channelUrl = channelUrl,
                channelGroup = channelGroup,
                epgId = epgId,
                parentListId = parentListId,
            )
        }

    fun PlaylistChannel.toTvPlaylistChannel(
        isEpgUsing: Boolean = false,
        favoriteType: FavoriteType,
        epgContent: List<EpgProgram> = emptyList(),
    ) = with(this) {
        TvPlaylistChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            epgId = epgId,
            favoriteType = favoriteType,
            programs = epgContent,
            isEpgUsing = isEpgUsing,
        )
    }

    fun PlaylistEntity.toPlaylist() =
        with(this) {
            Playlist(
                id = id,
                playlistName = playlistName,
                playlistSource = playlistSource,
                playlistType = playlistType,
                lastUpdateDate = lastUpdateDate,
                updatePeriod = updatePeriod,
                isSelected = isSelected,
            )
        }

    fun Playlist.toPlaylistEntity() =
        with(this) {
            PlaylistEntity(
                id = id,
                playlistName = playlistName,
                playlistSource = playlistSource,
                playlistType = playlistType,
                lastUpdateDate = lastUpdateDate,
                updatePeriod = updatePeriod,
                isSelected = isSelected,
            )
        }

    fun EpgProgramEntity.toEpgProgram() =
        with(this) {
            EpgProgram(
                programId = programId,
                channelId = channelId,
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
            )
        }

    fun FavoriteChannelEntity.toFavType() =
        with(this) {
            FavType(
                url = channelUrl,
                type = favoriteType,
            )
        }
}
