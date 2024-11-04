/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:06
 *
 */

package com.mvproject.tinyiptvkmp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mvproject.tinyiptvkmp.database.entity.PlaylistChannelEntity

@Dao
interface PlaylistChannelDao {
    @Upsert
    suspend fun savePlaylistChannels(data: List<PlaylistChannelEntity>)

    @Query(
        "SELECT * FROM playlistChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1) AND channelUrl IN (:urls)",
    )
    suspend fun getChannelsByUrls(urls: List<String>): List<PlaylistChannelEntity>

    @Query(
        "SELECT * FROM playlistChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1) AND channelGroup = :group",
    )
    suspend fun getChannelsByPlaylistGroup(group: String): List<PlaylistChannelEntity>

    @Query("SELECT COUNT(*) FROM playlistChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1)")
    suspend fun getPlaylistChannelsCount(): Int

    @Query(
        "SELECT COUNT(*) FROM playlistChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1) AND channelGroup = :group",
    )
    suspend fun getPlaylistGroupChannelsCount(group: String): Int

    @Query("SELECT * FROM playlistChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1)")
    suspend fun getPlaylistChannelsById(): List<PlaylistChannelEntity>

    @Query("SELECT * FROM playlistChannels")
    suspend fun getAllChannels(): List<PlaylistChannelEntity>

    @Query("SELECT channelGroup FROM playlistChannels WHERE parentListId == (SELECT id FROM playlists WHERE isSelected == 1)")
    suspend fun getPlaylistChannelsGroups(): List<String>

    @Query("DELETE FROM playlistChannels WHERE parentListId = :id")
    suspend fun deletePlaylistChannels(id: Long)
}
