package com.mvproject.tinyiptvkmp.persistence.channels.room

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.local.PlaylistChannelLocalDataSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelWindow
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.persistence.channels.room.database.PlaylistChannelDao
import com.mvproject.tinyiptvkmp.persistence.channels.room.mapper.toChannelEntity
import com.mvproject.tinyiptvkmp.persistence.channels.room.mapper.toPlaylistChannel
import com.mvproject.tinyiptvkmp.persistence.channels.room.mapper.toTvChannel

internal class RoomPlaylistChannelLocalDataSource(
    private val playlistChannelDao: PlaylistChannelDao,
) : PlaylistChannelLocalDataSource {
    @Transaction
    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        playlistChannelDao.savePlaylistChannels(data = channels.map { it.toChannelEntity() })
    }

    override suspend fun loadPlaylistGroups(playlistId: String): List<String> =
        playlistChannelDao.getPlaylistChannelsGroups(playlistId = playlistId)

    override suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int> =
        playlistChannelDao.getPlaylistGroupCounts(playlistId = playlistId)
            .associate { groupCount -> groupCount.groupName to groupCount.groupContentCount }

    override suspend fun loadPlaylistChannelsCount(playlistId: String): Int =
        playlistChannelDao.getPlaylistChannelsCount(playlistId = playlistId)

    override suspend fun loadPlaylistGroupChannelsCount(
        playlistId: String,
        group: String,
    ): Int =
        playlistChannelDao.getPlaylistGroupChannelsCount(
            playlistId = playlistId,
            group = group,
        )

    override suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel> =
        playlistChannelDao.getPlaylistChannelsById(playlistId = playlistId)
            .map { it.toPlaylistChannel() }

    override suspend fun loadAllChannels(): List<PlaylistChannel> =
        playlistChannelDao.getAllChannels().map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannel> =
        playlistChannelDao.getChannelsByUrls(
            playlistId = playlistId,
            urls = urls,
        ).map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannel> =
        playlistChannelDao.getChannelsByPlaylistGroup(
            playlistId = playlistId,
            group = group,
        ).map { it.toPlaylistChannel() }

    override suspend fun loadPlaylistChannelsWithFavorites(
        playlistId: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        playlistChannelDao.getPlaylistChannelsWithFavorites(
            playlistId = playlistId,
            offset = offset,
            limit = limit,
            searchQuery = searchQuery,
        ).map { it.toTvChannel() }

    override suspend fun loadPlaylistGroupChannelsWithFavorites(
        playlistId: String,
        group: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        playlistChannelDao.getPlaylistGroupChannelsWithFavorites(
            playlistId = playlistId,
            group = group,
            offset = offset,
            limit = limit,
            searchQuery = searchQuery,
        ).map { it.toTvChannel() }

    override suspend fun loadFavoritePlaylistChannels(
        playlistId: String,
        favoriteType: FavoriteType,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        playlistChannelDao.getFavoritePlaylistChannels(
            playlistId = playlistId,
            favoriteType = favoriteType.name,
            offset = offset,
            limit = limit,
            searchQuery = searchQuery,
        ).map { it.toTvChannel() }

    override suspend fun loadPlaylistChannelWindowWithFavorites(
        playlistId: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? {
        val rowId =
            playlistChannelDao.getPlaylistChannelRowId(
                playlistId = playlistId,
                channelUrl = channelUrl,
            ) ?: return null
        val currentIndex =
            playlistChannelDao.getPlaylistChannelsBeforeRowId(
                playlistId = playlistId,
                rowId = rowId,
            )
        val totalCount = playlistChannelDao.getPlaylistChannelsCount(playlistId = playlistId)

        return buildWindow(
            currentIndex = currentIndex,
            totalCount = totalCount,
            before = before,
            after = after,
        ) { offset, limit ->
            loadPlaylistChannelsWithFavorites(
                playlistId = playlistId,
                offset = offset,
                limit = limit,
                searchQuery = "",
            )
        }
    }

    override suspend fun loadPlaylistGroupChannelWindowWithFavorites(
        playlistId: String,
        group: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? {
        val rowId =
            playlistChannelDao.getPlaylistGroupChannelRowId(
                playlistId = playlistId,
                group = group,
                channelUrl = channelUrl,
            ) ?: return null
        val currentIndex =
            playlistChannelDao.getPlaylistGroupChannelsBeforeRowId(
                playlistId = playlistId,
                group = group,
                rowId = rowId,
            )
        val totalCount =
            playlistChannelDao.getPlaylistGroupChannelsCount(
                playlistId = playlistId,
                group = group,
            )

        return buildWindow(
            currentIndex = currentIndex,
            totalCount = totalCount,
            before = before,
            after = after,
        ) { offset, limit ->
            loadPlaylistGroupChannelsWithFavorites(
                playlistId = playlistId,
                group = group,
                offset = offset,
                limit = limit,
                searchQuery = "",
            )
        }
    }

    override suspend fun loadFavoritePlaylistChannelWindow(
        playlistId: String,
        favoriteType: FavoriteType,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? {
        val favoriteTypeName = favoriteType.name
        val channelOrder =
            playlistChannelDao.getFavoritePlaylistChannelOrder(
                playlistId = playlistId,
                favoriteType = favoriteTypeName,
                channelUrl = channelUrl,
            ) ?: return null
        val currentIndex =
            playlistChannelDao.getFavoritePlaylistChannelsBeforeOrder(
                playlistId = playlistId,
                favoriteType = favoriteTypeName,
                channelOrder = channelOrder,
            )
        val totalCount =
            playlistChannelDao.getFavoritePlaylistChannelsCount(
                playlistId = playlistId,
                favoriteType = favoriteTypeName,
            )

        return buildWindow(
            currentIndex = currentIndex,
            totalCount = totalCount,
            before = before,
            after = after,
        ) { offset, limit ->
            loadFavoritePlaylistChannels(
                playlistId = playlistId,
                favoriteType = favoriteType,
                offset = offset,
                limit = limit,
                searchQuery = "",
            )
        }
    }

    override suspend fun deletePlaylistChannels(id: String) {
        playlistChannelDao.deletePlaylistChannels(id = id)
    }

    private suspend fun buildWindow(
        currentIndex: Int,
        totalCount: Int,
        before: Int,
        after: Int,
        loadChannels: suspend (offset: Int, limit: Int) -> List<TvChannel>,
    ): PlaylistChannelWindow {
        val offset = (currentIndex - before).coerceAtLeast(0)
        val endExclusive = (currentIndex + after + 1).coerceAtMost(totalCount)
        val limit = (endExclusive - offset).coerceAtLeast(0)

        return PlaylistChannelWindow(
            channels = loadChannels(offset, limit),
            currentIndex = currentIndex,
            totalCount = totalCount,
        )
    }
}
