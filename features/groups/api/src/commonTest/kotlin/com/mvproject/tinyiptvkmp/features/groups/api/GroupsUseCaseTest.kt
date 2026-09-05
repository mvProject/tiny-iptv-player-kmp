package com.mvproject.tinyiptvkmp.features.groups.api

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelWindow
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelGroupSelection
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelWindowUseCase
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelWindowUseCaseImpl
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCaseImpl
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetPlaylistGroupUseCaseImpl
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
            FavoriteChannel(channelUrl = "url-news-1", favoriteType = FavoriteType.MOVIE),
            FavoriteChannel(channelUrl = "url-sport-1", favoriteType = FavoriteType.SPORT),
        )

        val groups = GetPlaylistGroupUseCaseImpl(
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

        val groups = GetPlaylistGroupUseCaseImpl(
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
    fun playlistGroupsHideBlankSpecifiedGroupsAndKeepAllCount() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "Ungrouped 1", url = "url-ungrouped-1", group = ""),
            playlistChannel(name = "Ungrouped 2", url = "url-ungrouped-2", group = "   "),
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
        )

        val groups = GetPlaylistGroupUseCaseImpl(
            playlistChannelRepository = playlistRepository,
            favoriteChannelsRepository = favoriteRepository,
        )(playlistId = "playlist")

        val allGroup = groups.first { group -> group.groupType == GroupType.ALL }

        assertEquals(3, allGroup.groupContentCount)
        assertEquals(
            listOf("NEWS"),
            groups
                .filter { group -> group.groupType == GroupType.SPECIFIED }
                .map { group -> group.groupName },
        )
    }

    @Test
    fun playlistGroupsKeepAllWhenEveryChannelHasBlankGroup() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "Ungrouped 1", url = "url-ungrouped-1", group = ""),
            playlistChannel(name = "Ungrouped 2", url = "url-ungrouped-2", group = "   "),
        )

        val groups = GetPlaylistGroupUseCaseImpl(
            playlistChannelRepository = playlistRepository,
            favoriteChannelsRepository = favoriteRepository,
        )(playlistId = "playlist")

        val allGroup = groups.first { group -> group.groupType == GroupType.ALL }

        assertEquals(2, allGroup.groupContentCount)
        assertEquals(
            emptyList<String>(),
            groups
                .filter { group -> group.groupType == GroupType.SPECIFIED }
                .map { group -> group.groupName },
        )
    }

    @Test
    fun groupChannelsLoadsSpecifiedGroupAndAppliesFavoriteNames() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "Sport 1", url = "url-sport-1", group = "SPORT"),
        )
        playlistRepository.favorites = listOf(
            FavoriteChannel(channelUrl = "url-news-1", favoriteType = FavoriteType.CARTOON),
        )

        val channels = GetGroupChannelsUseCaseImpl(
            playlistChannelRepository = playlistRepository,
        )(
            playlistId = "playlist",
            selection = ChannelGroupSelection.Specified(groupName = "NEWS"),
        )

        assertEquals(listOf("News 1"), channels.channels.map { channel -> channel.channelName })
        assertEquals(FavoriteType.CARTOON, channels.channels.single().favoriteType)
    }

    @Test
    fun groupChannelsLoadsFavoritesByFavoriteNameAndAllFallback() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "Movie 1", url = "url-movie-1", group = "MOVIES"),
            playlistChannel(name = "Sport 1", url = "url-sport-1", group = "SPORT"),
        )
        playlistRepository.favorites = listOf(
            FavoriteChannel(channelUrl = "url-movie-1", favoriteType = FavoriteType.MOVIE),
        )

        val useCase = GetGroupChannelsUseCaseImpl(
            playlistChannelRepository = playlistRepository,
        )

        val favoriteChannels =
            useCase(
                playlistId = "playlist",
                selection = ChannelGroupSelection.Favorite(type = FavoriteType.MOVIE),
            )
        val allChannels = useCase(
            playlistId = "playlist",
            selection = ChannelGroupSelection.All,
        )

        assertEquals(
            listOf("Movie 1"),
            favoriteChannels.channels.map { channel -> channel.channelName })
        assertEquals(
            listOf("Movie 1", "Sport 1"),
            allChannels.channels.map { channel -> channel.channelName })
        assertEquals(FavoriteType.NONE, allChannels.channels[1].favoriteType)
    }

    @Test
    fun groupChannelsReturnsBoundedPageAndHasMore() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "News 2", url = "url-news-2", group = "NEWS"),
            playlistChannel(name = "News 3", url = "url-news-3", group = "NEWS"),
        )

        val channels = GetGroupChannelsUseCaseImpl(
            playlistChannelRepository = playlistRepository,
        )(
            playlistId = "playlist",
            selection = ChannelGroupSelection.Specified(groupName = "NEWS"),
            offset = 1,
            limit = 1,
        )

        assertEquals(listOf("News 2"), channels.channels.map { channel -> channel.channelName })
        assertEquals(2, channels.nextOffset)
        assertEquals(true, channels.hasMore)
    }

    @Test
    fun groupChannelsFiltersSearchInRepository() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "Sport 1", url = "url-sport-1", group = "NEWS"),
        )

        val channels = GetGroupChannelsUseCaseImpl(
            playlistChannelRepository = playlistRepository,
        )(
            playlistId = "playlist",
            selection = ChannelGroupSelection.Specified(groupName = "NEWS"),
            searchQuery = "news",
        )

        assertEquals(listOf("News 1"), channels.channels.map { channel -> channel.channelName })
    }

    @Test
    fun groupChannelWindowLoadsPreviousCurrentAndNextForMiddleChannel() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "News 2", url = "url-news-2", group = "NEWS"),
            playlistChannel(name = "News 3", url = "url-news-3", group = "NEWS"),
        )

        val window =
            GetGroupChannelWindowUseCaseImpl(playlistChannelRepository = playlistRepository)(
                playlistId = "playlist",
                selection = ChannelGroupSelection.Specified(groupName = "NEWS"),
                channelUrl = "url-news-2",
            )

        assertEquals(listOf("News 1", "News 2", "News 3"), window.channels.map { it.channelName })
        assertEquals(1, window.currentIndex)
        assertEquals(3, window.totalCount)
    }

    @Test
    fun groupChannelWindowLoadsCurrentAndNextForFirstChannel() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "News 2", url = "url-news-2", group = "NEWS"),
            playlistChannel(name = "News 3", url = "url-news-3", group = "NEWS"),
        )

        val window =
            GetGroupChannelWindowUseCaseImpl(playlistChannelRepository = playlistRepository)(
                playlistId = "playlist",
                selection = ChannelGroupSelection.Specified(groupName = "NEWS"),
                channelUrl = "url-news-1",
            )

        assertEquals(listOf("News 1", "News 2"), window.channels.map { it.channelName })
        assertEquals(0, window.currentIndex)
        assertEquals(3, window.totalCount)
    }

    @Test
    fun groupChannelWindowLoadsPreviousAndCurrentForLastChannel() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "News 2", url = "url-news-2", group = "NEWS"),
            playlistChannel(name = "News 3", url = "url-news-3", group = "NEWS"),
        )

        val window =
            GetGroupChannelWindowUseCaseImpl(playlistChannelRepository = playlistRepository)(
                playlistId = "playlist",
                selection = ChannelGroupSelection.Specified(groupName = "NEWS"),
                channelUrl = "url-news-3",
            )

        assertEquals(listOf("News 2", "News 3"), window.channels.map { it.channelName })
        assertEquals(2, window.currentIndex)
        assertEquals(3, window.totalCount)
    }

    @Test
    fun groupChannelWindowReturnsEmptyForMissingChannelUrl() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
        )

        val window =
            GetGroupChannelWindowUseCaseImpl(playlistChannelRepository = playlistRepository)(
                playlistId = "playlist",
                selection = ChannelGroupSelection.Specified(groupName = "NEWS"),
                channelUrl = "missing",
            )

        assertEquals(emptyList(), window.channels)
        assertEquals(GetGroupChannelWindowUseCase.NO_CURRENT_INDEX, window.currentIndex)
        assertEquals(0, window.totalCount)
    }

    @Test
    fun groupChannelWindowUsesFavoriteOrdering() = runTest {
        playlistRepository.channels = listOf(
            playlistChannel(name = "News 1", url = "url-news-1", group = "NEWS"),
            playlistChannel(name = "News 2", url = "url-news-2", group = "NEWS"),
            playlistChannel(name = "News 3", url = "url-news-3", group = "NEWS"),
        )
        playlistRepository.favorites = listOf(
            FavoriteChannel(channelUrl = "url-news-3", favoriteType = FavoriteType.COMMON),
            FavoriteChannel(channelUrl = "url-news-1", favoriteType = FavoriteType.COMMON),
            FavoriteChannel(channelUrl = "url-news-2", favoriteType = FavoriteType.COMMON),
        )

        val window =
            GetGroupChannelWindowUseCaseImpl(playlistChannelRepository = playlistRepository)(
                playlistId = "playlist",
                selection = ChannelGroupSelection.Favorite(type = FavoriteType.COMMON),
                channelUrl = "url-news-1",
            )

        assertEquals(listOf("News 3", "News 1", "News 2"), window.channels.map { it.channelName })
        assertEquals(1, window.currentIndex)
        assertEquals(3, window.totalCount)
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
    var favorites: List<FavoriteChannel> = emptyList()

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

    override suspend fun loadPlaylistChannelsWithFavorites(
        playlistId: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        channels
            .filterBySearch(searchQuery)
            .drop(offset)
            .take(limit)
            .withFavoriteTypes()

    override suspend fun loadPlaylistGroupChannelsWithFavorites(
        playlistId: String,
        group: String,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> =
        channels
            .filter { channel -> channel.channelGroup == group }
            .filterBySearch(searchQuery)
            .drop(offset)
            .take(limit)
            .withFavoriteTypes()

    override suspend fun loadFavoritePlaylistChannels(
        playlistId: String,
        favoriteType: FavoriteType,
        offset: Int,
        limit: Int,
        searchQuery: String,
    ): List<TvChannel> {
        val urls = favorites
            .filter { favorite -> favorite.favoriteType == favoriteType }
            .map { favorite -> favorite.channelUrl }
            .toSet()

        return channels
            .filter { channel -> channel.channelUrl in urls }
            .filterBySearch(searchQuery)
            .drop(offset)
            .take(limit)
            .withFavoriteTypes()
    }

    override suspend fun loadPlaylistChannelWindowWithFavorites(
        playlistId: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? =
        channels
            .withFavoriteTypes()
            .windowAround(
                channelUrl = channelUrl,
                before = before,
                after = after,
            )

    override suspend fun loadPlaylistGroupChannelWindowWithFavorites(
        playlistId: String,
        group: String,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? =
        channels
            .filter { channel -> channel.channelGroup == group }
            .withFavoriteTypes()
            .windowAround(
                channelUrl = channelUrl,
                before = before,
                after = after,
            )

    override suspend fun loadFavoritePlaylistChannelWindow(
        playlistId: String,
        favoriteType: FavoriteType,
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? {
        val favoriteUrls =
            favorites
                .filter { favorite -> favorite.favoriteType == favoriteType }
                .map { favorite -> favorite.channelUrl }

        return favoriteUrls
            .mapNotNull { favoriteUrl -> channels.firstOrNull { channel -> channel.channelUrl == favoriteUrl } }
            .withFavoriteTypes()
            .windowAround(
                channelUrl = channelUrl,
                before = before,
                after = after,
            )
    }

    override suspend fun deletePlaylistChannels(listId: String) = Unit

    private fun List<PlaylistChannel>.filterBySearch(searchQuery: String): List<PlaylistChannel> =
        if (searchQuery.isBlank()) {
            this
        } else {
            filter { channel -> channel.channelName.contains(searchQuery, ignoreCase = true) }
        }

    private fun List<PlaylistChannel>.withFavoriteTypes(): List<TvChannel> {
        val favoritesByUrl = favorites.associateBy { favorite -> favorite.channelUrl }

        return map { channel ->
            TvChannel(
                channelName = channel.channelName,
                channelLogo = channel.channelLogo,
                channelUrl = channel.channelUrl,
                programId = channel.programId,
                favoriteType = favoritesByUrl[channel.channelUrl]?.favoriteType
                    ?: FavoriteType.NONE,
            )
        }
    }

    private fun List<TvChannel>.windowAround(
        channelUrl: String,
        before: Int,
        after: Int,
    ): PlaylistChannelWindow? {
        val currentIndex = indexOfFirst { channel -> channel.channelUrl == channelUrl }
        if (currentIndex < 0) return null

        val fromIndex = (currentIndex - before).coerceAtLeast(0)
        val toIndex = (currentIndex + after + 1).coerceAtMost(size)
        return PlaylistChannelWindow(
            channels = subList(fromIndex, toIndex),
            currentIndex = currentIndex,
            totalCount = size,
        )
    }
}

private class FakeChannelFavoriteRepository : ChannelFavoriteRepository {
    var favorites: List<FavoriteChannel> = emptyList()

    override suspend fun addChannelToFavorite(
        playlistId: String,
        channelName: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    ) = Unit

    override suspend fun deleteChannelFromFavorite(playlistId: String, channelUrl: String) = Unit

    override suspend fun updateFavoriteType(
        playlistId: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    ) = Unit

    override suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel> =
        favorites

    override suspend fun loadFavoriteChannelUrls(): List<String> =
        favorites.map { favorite -> favorite.channelUrl }

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> =
        loadFavoriteChannelUrls()

    override suspend fun loadFavoriteChannelNamesByUrl(playlistId: String): Map<String, String> =
        emptyMap()

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
