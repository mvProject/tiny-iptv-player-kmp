/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.mappers

import com.mvproject.tinyiptvkmp.data.model.channels.PlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgInfo
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.model.epg.SelectedEpg
import com.mvproject.tinyiptvkmp.data.model.playlist.Playlist
import com.mvproject.tinyiptvkmp.utils.CommonUtils.toBoolean
import com.mvproject.tinyiptvkmp.utils.CommonUtils.toLong
import com.mvproject.tinyiptvkmp.utils.TimeUtils.correctTimeZone
import tinyiptvkmpdb.EpgInfoEntity
import tinyiptvkmpdb.EpgProgramEntity
import tinyiptvkmpdb.PlaylistChannelEntity
import tinyiptvkmpdb.PlaylistEntity
import tinyiptvkmpdb.SelectedEpgEntity

object EntityMapper {
    fun PlaylistChannelEntity.toPlaylistChannel() = with(this) {
        PlaylistChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgId = epgId,
            parentListId = parentListId
        )
    }

    fun PlaylistChannel.toPlaylistChannelEntity() = with(this) {
        PlaylistChannelEntity(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgId = epgId,
            parentListId = parentListId
        )
    }

    fun PlaylistChannel.toTvPlaylistChannel(
        isFavorite: Boolean = false,
        isEpgUsing: Boolean = false,
        epgContent: List<EpgProgram> = emptyList()
    ) = with(this) {
        TvPlaylistChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            epgId = epgId,
            channelEpg = epgContent,
            isInFavorites = isFavorite,
            isEpgUsing = isEpgUsing
        )
    }

    fun PlaylistEntity.toPlaylist() = with(this) {
        Playlist(
            id = id,
            playlistTitle = title,
            playlistUrl = url,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod,
            playlistLocalName = localFilename,
            isLocalSource = isLocal.toBoolean()
        )
    }

    fun Playlist.toPlaylistEntity() = with(this) {
        PlaylistEntity(
            id = id,
            title = playlistTitle,
            url = playlistUrl,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod,
            localFilename = playlistLocalName,
            isLocal = isLocalSource.toLong()
        )
    }

    fun EpgProgramEntity.toEpgProgram() = with(this) {
        EpgProgram(
            channelId = channelId,
            start = programStart,
            stop = programEnd,
            title = title,
            description = description
        )
    }

    fun EpgProgram.toEpgProgramEntity() = with(this) {
        EpgProgramEntity(
            channelId = channelId,
            programStart = start.correctTimeZone(),
            programEnd = stop.correctTimeZone(),
            title = title,
            description = description
        )
    }

    fun EpgInfoEntity.toEpgInfo() = with(this) {
        EpgInfo(
            channelId = channelId,
            channelName = channelName,
            channelLogo = channelLogo
        )
    }

    fun SelectedEpgEntity.toSelectedEpg() = with(this) {
        SelectedEpg(
            channelName = channelName,
            channelEpgId = channelEpgId
        )
    }
}



