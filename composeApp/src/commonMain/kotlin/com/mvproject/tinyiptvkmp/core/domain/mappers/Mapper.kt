package com.mvproject.tinyiptvkmp.core.domain.mappers

import com.mvproject.tinyiptvkmp.core.data.model.EpgChannel
import com.mvproject.tinyiptvkmp.core.data.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.core.data.model.PlaylistChannelParseModel
import com.mvproject.tinyiptvkmp.core.database.entity.EpgChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.EpgProgramEntity
import com.mvproject.tinyiptvkmp.core.database.entity.FavoriteChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.core.database.entity.PlaylistEntity
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.FavType
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgChannelResponse
import com.mvproject.tinyiptvkmp.core.network.data.response.EpgProgramResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object Mapper {
    fun EpgChannelEntity.toEpgChannelModel() =
        with(this) {
            EpgChannel(
                title = title,
                logo = logo,
                programId = programId,
                id = id,
            )
        }

    @OptIn(ExperimentalUuidApi::class)
    fun EpgChannelResponse.toEpgChannelEntity() =
        with(this) {
            EpgChannelEntity(
                title = channelName,
                logo = channelIcon,
                programId = channelId,
                id = Uuid.random().toString(),
            )
        }

    @OptIn(ExperimentalUuidApi::class)
    fun EpgProgramResponse.asProgramEntity(id: String) =
        with(this) {
            EpgProgramEntity(
                programId = Uuid.random().toString(),
                dateTimeStart = dateTimeStart,
                dateTimeEnd = dateTimeEnd,
                title = title,
                description = description,
                channelId = id,
            )
        }

    fun PlaylistChannelEntity.toPlaylistChannel() =
        with(this) {
            PlaylistChannel(
                channelName = channelName,
                channelLogo = channelLogo,
                channelUrl = channelUrl,
                channelGroup = channelGroup,
                programId = programId,
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
                programId = programId,
                parentListId = parentListId,
            )
        }

    fun PlaylistChannel.toTvChannel(
        favoriteType: FavoriteType
    ) = with(this) {
        TvChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            programId = programId,
            favoriteType = favoriteType
        )
    }

    fun PlaylistEntity.toPlaylist() =
        with(this) {
            Playlist(
                id = id,
                playlistName = playlistName,
                playlistSource = playlistSource,
                playlistType = PlaylistType.valueOf(playlistType),
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
                playlistType = playlistType.name,
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
                type = FavoriteType.valueOf(favoriteType),
            )
        }

    fun PlaylistChannelParseModel.toPlaylistChannel(id: String) =
        with(this) {
            PlaylistChannel(
                channelName = channel,
                channelLogo = logoURL,
                channelUrl = streamURL,
                channelGroup = groupTitle,
                parentListId = id,
            )
        }
}
