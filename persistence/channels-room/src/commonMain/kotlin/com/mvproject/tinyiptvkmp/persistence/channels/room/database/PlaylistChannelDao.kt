/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.persistence.channels.room.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlaylistChannelDao {
    @Upsert
    suspend fun savePlaylistChannels(data: List<PlaylistChannelEntity>)

    @Query(
        "SELECT * FROM playlistChannels WHERE parentListId == :playlistId AND channelUrl IN (:urls)",
    )
    suspend fun getChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannelEntity>

    @Query(
        "SELECT * FROM playlistChannels WHERE parentListId == :playlistId AND channelGroup = :group",
    )
    suspend fun getChannelsByPlaylistGroup(
        playlistId: String,
        group: String,
    ): List<PlaylistChannelEntity>

    @Query("SELECT COUNT(*) FROM playlistChannels WHERE parentListId == :playlistId")
    suspend fun getPlaylistChannelsCount(playlistId: String): Int

    @Query(
        "SELECT rowid FROM playlistChannels " +
                "WHERE parentListId = :playlistId AND channelUrl = :channelUrl " +
                "LIMIT 1",
    )
    suspend fun getPlaylistChannelRowId(
        playlistId: String,
        channelUrl: String,
    ): Long?

    @Query(
        "SELECT COUNT(*) FROM playlistChannels " +
                "WHERE parentListId = :playlistId AND rowid < :rowId",
    )
    suspend fun getPlaylistChannelsBeforeRowId(
        playlistId: String,
        rowId: Long,
    ): Int

    @Query(
        "SELECT COUNT(*) FROM playlistChannels WHERE parentListId == :playlistId AND channelGroup = :group",
    )
    suspend fun getPlaylistGroupChannelsCount(
        playlistId: String,
        group: String,
    ): Int

    @Query(
        "SELECT rowid FROM playlistChannels " +
                "WHERE parentListId = :playlistId AND channelGroup = :group " +
                "AND channelUrl = :channelUrl " +
                "LIMIT 1",
    )
    suspend fun getPlaylistGroupChannelRowId(
        playlistId: String,
        group: String,
        channelUrl: String,
    ): Long?

    @Query(
        "SELECT COUNT(*) FROM playlistChannels " +
                "WHERE parentListId = :playlistId AND channelGroup = :group " +
                "AND rowid < :rowId",
    )
    suspend fun getPlaylistGroupChannelsBeforeRowId(
        playlistId: String,
        group: String,
        rowId: Long,
    ): Int

    @Query("SELECT * FROM playlistChannels WHERE parentListId == :playlistId")
    suspend fun getPlaylistChannelsById(playlistId: String): List<PlaylistChannelEntity>

    @Query("SELECT * FROM playlistChannels")
    suspend fun getAllChannels(): List<PlaylistChannelEntity>

    @Query(
        "SELECT pc.channelName, pc.channelUrl, pc.channelLogo, pc.programId, fc.favoriteType " +
                "FROM playlistChannels pc " +
                "LEFT JOIN favoriteChannels fc " +
                "ON fc.parentListId = pc.parentListId AND fc.channelUrl = pc.channelUrl " +
                "WHERE pc.parentListId = :playlistId " +
                "AND (:searchQuery = '' OR LOWER(pc.channelName) LIKE '%' || LOWER(:searchQuery) || '%') " +
                "ORDER BY pc.rowid " +
                "LIMIT :limit OFFSET :offset",
    )
    suspend fun getPlaylistChannelsWithFavorites(
        playlistId: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<PlaylistChannelFavorite>

    @Query(
        "SELECT pc.channelName, pc.channelUrl, pc.channelLogo, pc.programId, fc.favoriteType " +
                "FROM playlistChannels pc " +
                "LEFT JOIN favoriteChannels fc " +
                "ON fc.parentListId = pc.parentListId AND fc.channelUrl = pc.channelUrl " +
                "WHERE pc.parentListId = :playlistId AND pc.channelGroup = :group " +
                "AND (:searchQuery = '' OR LOWER(pc.channelName) LIKE '%' || LOWER(:searchQuery) || '%') " +
                "ORDER BY pc.rowid " +
                "LIMIT :limit OFFSET :offset",
    )
    suspend fun getPlaylistGroupChannelsWithFavorites(
        playlistId: String,
        group: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<PlaylistChannelFavorite>

    @Query(
        "SELECT pc.channelName, pc.channelUrl, pc.channelLogo, pc.programId, fc.favoriteType " +
                "FROM favoriteChannels fc " +
                "INNER JOIN playlistChannels pc " +
                "ON pc.parentListId = fc.parentListId AND pc.channelUrl = fc.channelUrl " +
                "WHERE fc.parentListId = :playlistId AND fc.favoriteType = :favoriteType " +
                "AND (:searchQuery = '' OR LOWER(pc.channelName) LIKE '%' || LOWER(:searchQuery) || '%') " +
                "ORDER BY fc.channelOrder " +
                "LIMIT :limit OFFSET :offset",
    )
    suspend fun getFavoritePlaylistChannels(
        playlistId: String,
        favoriteType: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<PlaylistChannelFavorite>

    @Query(
        "SELECT COUNT(*) FROM favoriteChannels fc " +
                "INNER JOIN playlistChannels pc " +
                "ON pc.parentListId = fc.parentListId AND pc.channelUrl = fc.channelUrl " +
                "WHERE fc.parentListId = :playlistId AND fc.favoriteType = :favoriteType",
    )
    suspend fun getFavoritePlaylistChannelsCount(
        playlistId: String,
        favoriteType: String,
    ): Int

    @Query(
        "SELECT fc.channelOrder FROM favoriteChannels fc " +
                "INNER JOIN playlistChannels pc " +
                "ON pc.parentListId = fc.parentListId AND pc.channelUrl = fc.channelUrl " +
                "WHERE fc.parentListId = :playlistId AND fc.favoriteType = :favoriteType " +
                "AND fc.channelUrl = :channelUrl " +
                "LIMIT 1",
    )
    suspend fun getFavoritePlaylistChannelOrder(
        playlistId: String,
        favoriteType: String,
        channelUrl: String,
    ): Long?

    @Query(
        "SELECT COUNT(*) FROM favoriteChannels fc " +
                "INNER JOIN playlistChannels pc " +
                "ON pc.parentListId = fc.parentListId AND pc.channelUrl = fc.channelUrl " +
                "WHERE fc.parentListId = :playlistId AND fc.favoriteType = :favoriteType " +
                "AND fc.channelOrder < :channelOrder",
    )
    suspend fun getFavoritePlaylistChannelsBeforeOrder(
        playlistId: String,
        favoriteType: String,
        channelOrder: Long,
    ): Int

    @Query(
        "SELECT DISTINCT channelGroup FROM playlistChannels " +
                "WHERE parentListId == :playlistId AND TRIM(channelGroup) != '' " +
                "ORDER BY channelGroup",
    )
    suspend fun getPlaylistChannelsGroups(playlistId: String): List<String>

    @Query(
        "SELECT channelGroup AS groupName, COUNT(*) AS groupContentCount " +
                "FROM playlistChannels " +
                "WHERE parentListId == :playlistId AND TRIM(channelGroup) != '' " +
                "GROUP BY channelGroup " +
                "ORDER BY channelGroup",
    )
    suspend fun getPlaylistGroupCounts(playlistId: String): List<PlaylistGroupCount>

    @Query("DELETE FROM playlistChannels WHERE parentListId = :id")
    suspend fun deletePlaylistChannels(id: String)
}
