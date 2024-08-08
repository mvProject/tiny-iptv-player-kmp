/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:24
 *
 */

package com.mvproject.tinyiptvkmp.data.mappers

import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgInfo
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.database.entity.EpgInfoEntity
import com.mvproject.tinyiptvkmp.database.entity.EpgProgramEntity
import com.mvproject.tinyiptvkmp.database.entity.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.database.entity.PlaylistEntity
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelEpg
import com.mvproject.tinyiptvkmp.utils.CommonUtils.toBoolean
import com.mvproject.tinyiptvkmp.utils.CommonUtils.toLong
import com.mvproject.tinyiptvkmp.utils.TimeUtils.correctTimeZone

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
        epgContent: TvPlaylistChannelEpg = TvPlaylistChannelEpg(),
    ) = with(this) {
        TvPlaylistChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            epgId = epgId,
            favoriteType = favoriteType,
            channelEpg = epgContent,
            isEpgUsing = isEpgUsing,
        )
    }

    fun PlaylistEntity.toPlaylist() =
        with(this) {
            Playlist(
                id = id,
                playlistTitle = title,
                playlistUrl = url,
                lastUpdateDate = lastUpdateDate,
                updatePeriod = updatePeriod,
                playlistLocalName = localFilename,
                isLocalSource = isLocal.toBoolean(),
            )
        }

    fun Playlist.toPlaylistRoom() =
        with(this) {
            PlaylistEntity(
                id = id,
                title = playlistTitle,
                url = playlistUrl,
                lastUpdateDate = lastUpdateDate,
                updatePeriod = updatePeriod,
                localFilename = playlistLocalName,
                isLocal = isLocalSource.toLong(),
            )
        }

    fun EpgProgramEntity.toEpgProgram() =
        with(this) {
            EpgProgram(
                channelId = channelId,
                start = programStart,
                stop = programEnd,
                title = title,
                description = description,
            )
        }

    fun EpgProgram.toEpgProgramEntity() =
        with(this) {
            EpgProgramEntity(
                channelId = channelId,
                programStart = start.correctTimeZone(),
                programEnd = stop.correctTimeZone(),
                title = title,
                description = description,
            )
        }

    fun EpgInfoEntity.toEpgInfo() =
        with(this) {
            EpgInfo(
                channelId = channelId,
                channelName = channelName,
                channelLogo = channelLogo,
                lastUpdate = lastUpdated,
            )
        }
}
