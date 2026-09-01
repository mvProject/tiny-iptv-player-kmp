package com.mvproject.tinyiptvkmp.features.groups.api

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.infrastructure.logging.di.loggingModule
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GroupsUseCaseTest {
    private lateinit var playlistRepository: FakePlaylistChannelRepository
    private lateinit var favoriteRepository: FakeChannelFavoriteRepository

    @BeforeTest
    fun setUp() {
        runCatching { stopKoin() }
        startKoin { modules(loggingModule) }
        playlistRepository = FakePlaylistChannelRepository()
        favoriteRepository = FakeChannelFavoriteRepository()
    }

    @AfterTest
    fun tearDown() {
        runCatching { stopKoin() }
    }

    @Test
    fun playlistGroupsAreOrderedWithAllFavoritesAndSpecifiedGroups() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "News 2", url = "url-news-2", group = "NEWS"),
            playlistChannel(name = "Sport 1", url = "url-sport-1", group = "SPORT"),
        )
        favoriteRepository.favorites = listOf(
            FavoriteChannel(channelUrl = "url-news-1", favoriteType = FavoriteType.MOVIE.name),
            FavoriteChannel(channelUrl = "url-sport-1", favoriteType = FavoriteType.SPORT.name),
        )

        val groups = GetPlaylistGroupUseCase(
            playlistChannelRepository = playlistRepository,
            favoriteChannelsRepository = favoriteRepository,
        )(playlistId = "playlist")

        assertEquals(GroupType.ALL, groups[0].groupType)
        assertEquals(3, groups[0].groupContentCount)
        assertEquals(
            listOf(
                FavoriteType.COMMON,
                FavoriteType.MOVIE,
                FavoriteType.SPORT,
            ),
            groups
                .filter { group -> group.groupType == GroupType.FAVORITE }
                .map { group -> group.groupFavoriteType },
        )
        assertEquals(
            listOf("NEWS", "SPORT"),
            groups
                .filter { group -> group.groupType == GroupType.SPECIFIED }
                .map { group -> group.groupName },
        )
    }

    @Test
    fun commonFavoriteGroupAppearsWhenEmptyAndOtherEmptyFavoriteGroupsAreHidden() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
        )

        val groups = GetPlaylistGroupUseCase(
            playlistChannelRepository = playlistRepository,
            favoriteChannelsRepository = favoriteRepository,
        )(playlistId = "playlist")

        val favoriteGroups = groups.filter { group -> group.groupType == GroupType.FAVORITE }
        assertEquals(
            listOf(FavoriteType.COMMON),
            favoriteGroups.map { group -> group.groupFavoriteType })
        assertEquals(0, favoriteGroups.single().groupContentCount)
    }

    @Test
    fun groupChannelsLoadsSpecifiedGroupAndAppliesFavoriteNames() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "Sport 1", url = "url-sport-1", group = "SPORT"),
        )
        favoriteRepository.favorites = listOf(
            FavoriteChannel(channelUrl = "url-news-1", favoriteType = FavoriteType.CARTOON.name),
        )

        val channels = GetGroupChannelsUseCase(
            playlistChannelRepository = playlistRepository,
            favoriteChannelsRepository = favoriteRepository,
        )(playlistId = "playlist", group = "NEWS", groupType = GroupType.SPECIFIED.name)

        assertEquals(listOf("News 1"), channels.map { channel -> channel.channelName })
        assertEquals(FavoriteType.CARTOON.name, channels.single().favoriteType)
    }

    @Test
    fun groupChannelsLoadsFavoritesByFavoriteNameAndAllFallback() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "Movie 1", url = "url-movie-1", group = "MOVIES"),
            playlistChannel(name = "Sport 1", url = "url-sport-1", group = "SPORT"),
        )
        favoriteRepository.favorites = listOf(
            FavoriteChannel(channelUrl = "url-movie-1", favoriteType = FavoriteType.MOVIE.name),
        )

        val useCase = GetGroupChannelsUseCase(
            playlistChannelRepository = playlistRepository,
            favoriteChannelsRepository = favoriteRepository,
        )

        val favoriteChannels =
            useCase(
                playlistId = "playlist",
                group = FavoriteType.MOVIE.name,
                groupType = GroupType.FAVORITE.name,
            )
        val allChannels = useCase(
            playlistId = "playlist",
            group = "",
            groupType = GroupType.ALL.name,
        )

        assertEquals(listOf("Movie 1"), favoriteChannels.map { channel -> channel.channelName })
        assertEquals(
            listOf("Movie 1", "Sport 1"),
            allChannels.map { channel -> channel.channelName })
        assertEquals(FavoriteType.NONE.name, allChannels[1].favoriteType)
    }

    private fun playlistChannel(
        name: String,
        url: String,
        group: String,
    ) = PlaylistChannel(
        channelName = name,
        channelUrl = url,
        channelGroup = group,
        parentListId = "playlist",
    )
}

private class FakePlaylistChannelRepository : PlaylistChannelRepository {
    var channels: List<PlaylistChannel> = emptyList()

    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) = Unit

    override suspend fun loadLocalPlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        channels

    override suspend fun loadRemotePlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> =
        channels

    override suspend fun loadPlaylistGroups(playlistId: String): List<String> =
        channels
            .map { channel -> channel.channelGroup }
            .distinct()

    override suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int> =
        channels
            .map { channel -> channel.channelGroup }
            .distinct()
            .associateWith { group ->
                channels.count { channel -> channel.channelGroup == group }
            }

    override suspend fun loadPlaylistChannelsCount(playlistId: String): Int = channels.size

    override suspend fun loadPlaylistGroupChannelsCount(playlistId: String, group: String): Int =
        channels.count { channel -> channel.channelGroup == group }

    override suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel> = channels

    override suspend fun loadAllChannels(): List<PlaylistChannel> = channels

    override suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannel> =
        channels.filter { channel -> channel.channelUrl in urls }

    override suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannel> =
        channels.filter { channel -> channel.channelGroup == group }

    override suspend fun deletePlaylistChannels(listId: String) = Unit
}

private class FakeChannelFavoriteRepository : ChannelFavoriteRepository {
    var favorites: List<FavoriteChannel> = emptyList()

    override suspend fun addChannelToFavorite(
        playlistId: String,
        channelName: String,
        channelUrl: String,
        favoriteType: String,
    ) = Unit

    override suspend fun deleteChannelFromFavorite(playlistId: String, channelUrl: String) = Unit

    override suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel> =
        favorites

    override suspend fun loadFavoriteChannelUrls(): List<String> =
        favorites.map { favorite -> favorite.channelUrl }

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> =
        loadFavoriteChannelUrls()

    override suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    ) = Unit

    override suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    ) = Unit

    override suspend fun deletePlaylistFavoriteChannels(playlistId: String) = Unit
}
