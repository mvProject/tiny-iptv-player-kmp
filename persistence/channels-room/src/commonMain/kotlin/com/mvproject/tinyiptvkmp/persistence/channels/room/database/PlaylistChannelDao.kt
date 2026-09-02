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
        "SELECT COUNT(*) FROM playlistChannels WHERE parentListId == :playlistId AND channelGroup = :group",
    )
    suspend fun getPlaylistGroupChannelsCount(
        playlistId: String,
        group: String,
    ): Int

    @Query("SELECT * FROM playlistChannels WHERE parentListId == :playlistId")
    suspend fun getPlaylistChannelsById(playlistId: String): List<PlaylistChannelEntity>

    @Query("SELECT * FROM playlistChannels")
    suspend fun getAllChannels(): List<PlaylistChannelEntity>

    @Query(
        "SELECT channelGroup FROM playlistChannels " +
                "WHERE parentListId == :playlistId AND TRIM(channelGroup) != ''",
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
