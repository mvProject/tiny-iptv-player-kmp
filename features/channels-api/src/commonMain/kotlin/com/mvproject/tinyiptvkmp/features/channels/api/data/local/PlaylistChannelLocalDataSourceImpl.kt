package com.mvproject.tinyiptvkmp.features.channels.api.data.local

import androidx.room.Transaction
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.PlaylistChannelDao
import com.mvproject.tinyiptvkmp.features.channels.api.data.local.database.PlaylistChannelEntity
import com.mvproject.tinyiptvkmp.features.channels.api.data.parser.M3UParser.parseStringToChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

internal class PlaylistChannelLocalDataSourceImpl(
    private val playlistChannelDao: PlaylistChannelDao,
) : PlaylistChannelLocalDataSource {
    @Transaction
    override suspend fun savePlaylistChannels(channels: List<PlaylistChannelEntity>) {
        playlistChannelDao.savePlaylistChannels(data = channels)
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

    override suspend fun loadChannelsById(playlistId: String): List<PlaylistChannelEntity> =
        playlistChannelDao.getPlaylistChannelsById(playlistId = playlistId)

    override suspend fun loadAllChannels(): List<PlaylistChannelEntity> =
        playlistChannelDao.getAllChannels()

    override suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannelEntity> =
        playlistChannelDao.getChannelsByUrls(
            playlistId = playlistId,
            urls = urls,
        )

    override suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannelEntity> =
        playlistChannelDao.getChannelsByPlaylistGroup(
            playlistId = playlistId,
            group = group,
        )

    override suspend fun deletePlaylistChannels(id: String) {
        playlistChannelDao.deletePlaylistChannels(id = id)
    }

    override suspend fun loadPlaylistContent(source: String): List<PlaylistChannelParseModel> =
        withContext(Dispatchers.IO) {
            val content = FileSystem.SYSTEM.read(source.toPath()) { readUtf8() }
            withContext(Dispatchers.Default) {
                parseStringToChannels(source = content)
            }
        }
}
