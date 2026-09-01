package com.mvproject.tinyiptvkmp.features.channels.api

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.DeletePlaylistContentUseCaseImpl
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ReplacePlaylistContentUseCaseImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlaylistContentUseCaseTest {
    @Test
    fun replaceContentDeletesBeforeLoadWhenRequested() = runTest {
        val channelRepository = FakePlaylistChannelRepository()
        val useCase =
            ReplacePlaylistContentUseCaseImpl(
                playlistChannelRepository = channelRepository,
                channelFavoriteRepository = FakeChannelFavoriteRepository(),
            )
        channelRepository.loadedChannels["playlist-1"] =
            listOf(channel("playlist-1", "News", "url-1"))

        val contentReplaced =
            useCase.replaceRemotePlaylistContent(
                playlistId = "playlist-1",
                source = "https://example.com/list.m3u",
                clearExistingContentBeforeLoading = true,
            )

        assertTrue(contentReplaced)
        assertEquals(
            listOf("delete:playlist-1", "load-remote:playlist-1", "save:playlist-1"),
            channelRepository.operations,
        )
    }

    @Test
    fun replaceLocalContentUsesLocalLoader() = runTest {
        val channelRepository = FakePlaylistChannelRepository()
        val useCase =
            ReplacePlaylistContentUseCaseImpl(
                playlistChannelRepository = channelRepository,
                channelFavoriteRepository = FakeChannelFavoriteRepository(),
            )
        channelRepository.loadedChannels["playlist-1"] =
            listOf(channel("playlist-1", "News", "url-1"))

        val contentReplaced =
            useCase.replaceLocalPlaylistContent(
                playlistId = "playlist-1",
                source = "/tmp/list.m3u",
                clearExistingContentBeforeLoading = true,
            )

        assertTrue(contentReplaced)
        assertEquals(
            listOf("delete:playlist-1", "load-local:playlist-1", "save:playlist-1"),
            channelRepository.operations,
        )
    }

    @Test
    fun replaceContentKeepsExistingContentWhenParsedChannelsAreEmpty() = runTest {
        val channelRepository = FakePlaylistChannelRepository()
        val useCase =
            ReplacePlaylistContentUseCaseImpl(
                playlistChannelRepository = channelRepository,
                channelFavoriteRepository = FakeChannelFavoriteRepository(),
            )

        val contentReplaced =
            useCase.replaceRemotePlaylistContent(
                playlistId = "playlist-1",
                source = "https://example.com/list.m3u",
                clearExistingContentBeforeLoading = false,
            )

        assertFalse(contentReplaced)
        assertEquals(listOf("load-remote:playlist-1"), channelRepository.operations)
    }

    @Test
    fun replaceContentDeletesAfterSuccessfulLoadWhenRequested() = runTest {
        val channelRepository = FakePlaylistChannelRepository()
        val useCase =
            ReplacePlaylistContentUseCaseImpl(
                playlistChannelRepository = channelRepository,
                channelFavoriteRepository = FakeChannelFavoriteRepository(),
            )
        channelRepository.loadedChannels["playlist-1"] =
            listOf(channel("playlist-1", "News", "url-1"))

        val contentReplaced =
            useCase.replaceRemotePlaylistContent(
                playlistId = "playlist-1",
                source = "https://example.com/list.m3u",
                clearExistingContentBeforeLoading = false,
            )

        assertTrue(contentReplaced)
        assertEquals(
            listOf("load-remote:playlist-1", "delete:playlist-1", "save:playlist-1"),
            channelRepository.operations,
        )
    }

    @Test
    fun replaceContentReconcilesFavoritesByPlaylistIdAndUrl() = runTest {
        val channelRepository = FakePlaylistChannelRepository()
        val favoriteRepository =
            FakeChannelFavoriteRepository(
                favoriteUrlsByPlaylistId = mutableMapOf("playlist-1" to listOf("url-1")),
            )
        val useCase =
            ReplacePlaylistContentUseCaseImpl(
                playlistChannelRepository = channelRepository,
                channelFavoriteRepository = favoriteRepository,
            )
        channelRepository.loadedChannels["playlist-1"] =
            listOf(channel("playlist-1", "Updated News", "url-1"))

        useCase.replaceRemotePlaylistContent(
            playlistId = "playlist-1",
            source = "https://example.com/list.m3u",
            clearExistingContentBeforeLoading = true,
        )

        assertEquals(listOf("playlist-1:Updated News:url-1"), favoriteRepository.updatedFavorites)
    }

    @Test
    fun deleteContentDeletesFavoritesAndChannelsForPlaylist() = runTest {
        val channelRepository = FakePlaylistChannelRepository()
        val favoriteRepository = FakeChannelFavoriteRepository()
        val useCase =
            DeletePlaylistContentUseCaseImpl(
                playlistChannelRepository = channelRepository,
                channelFavoriteRepository = favoriteRepository,
            )

        useCase(playlistId = "playlist-1")

        assertEquals(listOf("playlist-1"), favoriteRepository.deletedPlaylists)
        assertEquals(listOf("delete:playlist-1"), channelRepository.operations)
    }
}

private class FakePlaylistChannelRepository : PlaylistChannelRepository {
    val operations = mutableListOf<String>()
    val loadedChannels = mutableMapOf<String, List<PlaylistChannel>>()

    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        operations += "save:${channels.firstOrNull()?.parentListId.orEmpty()}"
    }

    override suspend fun loadLocalPlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> {
        operations += "load-local:$playlistId"
        return loadedChannels[playlistId].orEmpty()
    }

    override suspend fun loadRemotePlaylistChannels(
        playlistId: String,
        source: String,
    ): List<PlaylistChannel> {
        operations += "load-remote:$playlistId"
        return loadedChannels[playlistId].orEmpty()
    }

    override suspend fun loadPlaylistGroups(playlistId: String): List<String> = emptyList()

    override suspend fun loadPlaylistGroupCounts(playlistId: String): Map<String, Int> = emptyMap()

    override suspend fun loadPlaylistChannelsCount(playlistId: String): Int = 0

    override suspend fun loadPlaylistGroupChannelsCount(playlistId: String, group: String): Int = 0

    override suspend fun loadChannelsById(playlistId: String): List<PlaylistChannel> = emptyList()

    override suspend fun loadAllChannels(): List<PlaylistChannel> = emptyList()

    override suspend fun loadPlaylistChannelsByUrls(
        playlistId: String,
        urls: List<String>,
    ): List<PlaylistChannel> =
        emptyList()

    override suspend fun loadPlaylistGroupChannels(
        playlistId: String,
        group: String,
    ): List<PlaylistChannel> =
        emptyList()

    override suspend fun deletePlaylistChannels(listId: String) {
        operations += "delete:$listId"
    }
}

private class FakeChannelFavoriteRepository(
    private val favoriteUrlsByPlaylistId: MutableMap<String, List<String>> = mutableMapOf(),
) : ChannelFavoriteRepository {
    val updatedFavorites = mutableListOf<String>()
    val deletedPlaylists = mutableListOf<String>()

    override suspend fun addChannelToFavorite(
        playlistId: String,
        channelName: String,
        channelUrl: String,
        favoriteType: String,
    ) = Unit

    override suspend fun deleteChannelFromFavorite(playlistId: String, channelUrl: String) = Unit

    override suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel> =
        emptyList()

    override suspend fun loadFavoriteChannelUrls(): List<String> =
        favoriteUrlsByPlaylistId.values.flatten()

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> =
        favoriteUrlsByPlaylistId[playlistId].orEmpty()

    override suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    ) {
        updatedFavorites += "$playlistId:$channelName:$channelUrl"
    }

    override suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    ) {
        channelNamesByUrl.forEach { (channelUrl, channelName) ->
            updatedFavorites += "$playlistId:$channelName:$channelUrl"
        }
    }

    override suspend fun deletePlaylistFavoriteChannels(playlistId: String) {
        deletedPlaylists += playlistId
    }
}

private fun channel(
    playlistId: String,
    name: String,
    url: String,
) = PlaylistChannel(
    channelName = name,
    channelUrl = url,
    channelGroup = "General",
    parentListId = playlistId,
)
