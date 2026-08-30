package com.mvproject.tinyiptvkmp.features.playlist.api

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.PlaylistChannelSource
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.PlaylistChannelRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.database.PlaylistEntity
import com.mvproject.tinyiptvkmp.features.playlist.api.data.repository.PlaylistRepositoryImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.data.repository.SelectedPlaylistProviderImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistSyncStateRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.PlaylistContentUpdater
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SavePlaylistContentUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdateRemotePlaylistChannelsUseCaseImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class PlaylistUseCaseTest {
    @Test
    fun createFirstPlaylistSelectsItAndReplacesContent() = runTest {
        val playlistRepository = FakePlaylistRepository()
        val channelRepository = FakePlaylistChannelRepository()
        val syncStateRepository = FakePlaylistSyncStateRepository()
        val useCase =
            CreatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(
                    channelRepository,
                    FakeChannelFavoriteRepository()
                ),
                syncStateRepository = syncStateRepository,
            )
        channelRepository.loadedChannels["playlist-1"] =
            listOf(channel("playlist-1", "News", "url-1"))

        useCase(remotePlaylist(id = "playlist-1"))

        val savedPlaylist = playlistRepository.getPlaylistById("playlist-1")
        assertTrue(savedPlaylist.isSelected)
        assertTrue(savedPlaylist.lastUpdateDate > 0L)
        assertEquals(
            listOf("delete:playlist-1", "load:playlist-1", "save:playlist-1"),
            channelRepository.operations,
        )
        assertEquals(1, syncStateRepository.epgMarks)
    }

    @Test
    fun localUpdateOnlyChangesNameAndPreservesSelection() = runTest {
        val existing =
            localPlaylist(
                id = "playlist-1",
                name = "Old",
                source = "old-file",
                isSelected = true,
            )
        val playlistRepository = FakePlaylistRepository(existing)
        val channelRepository = FakePlaylistChannelRepository()
        val useCase =
            UpdatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(
                    channelRepository,
                    FakeChannelFavoriteRepository()
                ),
                syncStateRepository = FakePlaylistSyncStateRepository(),
            )

        useCase(
            remotePlaylist(
                id = "playlist-1",
                name = "New",
                source = "https://new.example/list.m3u",
                updatePeriod = 5L,
            ),
        )

        val updatedPlaylist = playlistRepository.getPlaylistById("playlist-1")
        assertEquals("New", updatedPlaylist.playlistName)
        assertEquals("old-file", updatedPlaylist.playlistSource)
        assertEquals(PlaylistType.LOCAL, updatedPlaylist.playlistType)
        assertEquals(0L, updatedPlaylist.updatePeriod)
        assertTrue(updatedPlaylist.isSelected)
        assertEquals(emptyList(), channelRepository.operations)
    }

    @Test
    fun remoteNameAndPeriodUpdateDoesNotReloadContent() = runTest {
        val existing =
            remotePlaylist(
                id = "playlist-1",
                name = "Old",
                source = "https://old.example/list.m3u",
                updatePeriod = 1L,
                isSelected = true,
            )
        val playlistRepository = FakePlaylistRepository(existing)
        val channelRepository = FakePlaylistChannelRepository()
        val syncStateRepository = FakePlaylistSyncStateRepository()
        val useCase =
            UpdatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(
                    channelRepository,
                    FakeChannelFavoriteRepository()
                ),
                syncStateRepository = syncStateRepository,
            )

        useCase(
            remotePlaylist(
                id = "playlist-1",
                name = "New",
                source = "https://old.example/list.m3u",
                updatePeriod = 5L,
            ),
        )

        val updatedPlaylist = playlistRepository.getPlaylistById("playlist-1")
        assertEquals("New", updatedPlaylist.playlistName)
        assertEquals(5L, updatedPlaylist.updatePeriod)
        assertTrue(updatedPlaylist.isSelected)
        assertEquals(emptyList(), channelRepository.operations)
        assertEquals(0, syncStateRepository.epgMarks)
    }

    @Test
    fun remoteUrlUpdateDeletesOldContentBeforeLoadingNewContent() = runTest {
        val playlistRepository =
            FakePlaylistRepository(
                remotePlaylist(
                    id = "playlist-1",
                    source = "https://old.example/list.m3u",
                    isSelected = true,
                ),
            )
        val channelRepository = FakePlaylistChannelRepository()
        val favoriteRepository =
            FakeChannelFavoriteRepository(
                favoriteUrlsByPlaylistId = mutableMapOf("playlist-1" to listOf("url-1")),
            )
        val syncStateRepository = FakePlaylistSyncStateRepository()
        val useCase =
            UpdatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(channelRepository, favoriteRepository),
                syncStateRepository = syncStateRepository,
            )
        channelRepository.loadedChannels["playlist-1"] =
            listOf(channel("playlist-1", "Updated News", "url-1"))

        useCase(
            remotePlaylist(
                id = "playlist-1",
                source = "https://new.example/list.m3u",
            ),
        )

        val updatedPlaylist = playlistRepository.getPlaylistById("playlist-1")
        assertEquals("https://new.example/list.m3u", updatedPlaylist.playlistSource)
        assertTrue(updatedPlaylist.isSelected)
        assertTrue(updatedPlaylist.lastUpdateDate > 0L)
        assertEquals(
            listOf("delete:playlist-1", "load:playlist-1", "save:playlist-1"),
            channelRepository.operations,
        )
        assertEquals(listOf("playlist-1:Updated News:url-1"), favoriteRepository.updatedFavorites)
        assertEquals(1, syncStateRepository.epgMarks)
    }

    @Test
    fun scheduledRefreshProcessesOnlyDuePlaylistsAndMarksEpgWhenUpdated() = runTest {
        val duePlaylist = remotePlaylist(id = "due")
        val skippedPlaylist = remotePlaylist(id = "skipped")
        val playlistRepository =
            FakePlaylistRepository(
                duePlaylist,
                skippedPlaylist,
                duePlaylists = listOf(duePlaylist),
            )
        val channelRepository = FakePlaylistChannelRepository()
        val syncStateRepository = FakePlaylistSyncStateRepository()
        val useCase =
            UpdateRemotePlaylistChannelsUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(
                    channelRepository,
                    FakeChannelFavoriteRepository()
                ),
                syncStateRepository = syncStateRepository,
            )
        channelRepository.loadedChannels["due"] = listOf(channel("due", "News", "url-1"))

        useCase()

        assertEquals(
            listOf("load:due", "delete:due", "save:due"),
            channelRepository.operations,
        )
        assertEquals(1, syncStateRepository.epgMarks)
        assertTrue(playlistRepository.getPlaylistById("due").lastUpdateDate > 0L)
        assertEquals(0L, playlistRepository.getPlaylistById("skipped").lastUpdateDate)
    }

    @Test
    fun scheduledRefreshDoesNotClearOrMarkEpgWhenNoPlaylistIsDue() = runTest {
        val playlistRepository = FakePlaylistRepository(duePlaylists = emptyList())
        val syncStateRepository = FakePlaylistSyncStateRepository()
        val useCase =
            UpdateRemotePlaylistChannelsUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(
                    FakePlaylistChannelRepository(),
                    FakeChannelFavoriteRepository(),
                ),
                syncStateRepository = syncStateRepository,
            )

        useCase()

        assertEquals(0, syncStateRepository.epgMarks)
    }

    @Test
    fun manualRefreshKeepsExistingContentWhenParsedChannelsAreEmpty() = runTest {
        val playlistRepository = FakePlaylistRepository(remotePlaylist(id = "due"))
        val channelRepository = FakePlaylistChannelRepository()
        val syncStateRepository = FakePlaylistSyncStateRepository()
        val useCase =
            SavePlaylistContentUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(
                    channelRepository,
                    FakeChannelFavoriteRepository()
                ),
                syncStateRepository = syncStateRepository,
            )

        useCase("due")

        assertEquals(listOf("load:due"), channelRepository.operations)
        assertEquals(0, syncStateRepository.epgMarks)
        assertEquals(0L, playlistRepository.getPlaylistById("due").lastUpdateDate)
    }

    @Test
    fun scheduledRefreshKeepsExistingContentWhenParsedChannelsAreEmpty() = runTest {
        val duePlaylist = remotePlaylist(id = "due")
        val playlistRepository =
            FakePlaylistRepository(
                duePlaylist,
                duePlaylists = listOf(duePlaylist),
            )
        val channelRepository = FakePlaylistChannelRepository()
        val syncStateRepository = FakePlaylistSyncStateRepository()
        val useCase =
            UpdateRemotePlaylistChannelsUseCaseImpl(
                playlistRepository = playlistRepository,
                contentUpdater = PlaylistContentUpdater(
                    channelRepository,
                    FakeChannelFavoriteRepository()
                ),
                syncStateRepository = syncStateRepository,
            )

        useCase()

        assertEquals(listOf("load:due"), channelRepository.operations)
        assertEquals(0, syncStateRepository.epgMarks)
        assertEquals(0L, playlistRepository.getPlaylistById("due").lastUpdateDate)
    }

    @Test
    fun playlistRepositoryReturnsOnlyDueRemotePlaylists() = runTest {
        val now = 10.hours.inWholeMilliseconds
        val repository =
            PlaylistRepositoryImpl(
                FakePlaylistLocalDataSource(
                    listOf(
                        entity(id = "due", type = PlaylistType.REMOTE, updatePeriod = 1L),
                        entity(
                            id = "not-due",
                            type = PlaylistType.REMOTE,
                            updatePeriod = 1L,
                            lastUpdateDate = now - 1.hours.inWholeMilliseconds,
                        ),
                        entity(id = "disabled", type = PlaylistType.REMOTE, updatePeriod = 0L),
                        entity(id = "local", type = PlaylistType.LOCAL, updatePeriod = 1L),
                    ),
                ),
            )

        val duePlaylistIds = repository.getDueRemotePlaylists(nowMillis = now).map { it.id }

        assertEquals(listOf("due"), duePlaylistIds)
    }

    @Test
    fun selectPlaylistUseCaseUsesTargetedSelection() = runTest {
        val playlistRepository = FakePlaylistRepository(
            remotePlaylist(id = "old", isSelected = true),
            remotePlaylist(id = "new"),
        )
        val useCase = SelectPlaylistUseCaseImpl(playlistRepository = playlistRepository)

        useCase(playlistId = "new")

        assertEquals("new", playlistRepository.selectedPlaylistId)
        assertEquals(0, playlistRepository.getAllPlaylistsCalls)
        assertEquals(0, playlistRepository.savePlaylistsCalls)
    }

    @Test
    fun selectedPlaylistProviderUsesLocalSelectedIdQuery() = runTest {
        val localDataSource =
            FakePlaylistLocalDataSource(
                listOf(
                    entity(id = "old", type = PlaylistType.REMOTE, updatePeriod = 1L),
                    entity(id = "selected", type = PlaylistType.REMOTE, updatePeriod = 1L),
                ),
                selectedPlaylistId = "selected",
            )
        val provider = SelectedPlaylistProviderImpl(local = localDataSource)

        assertEquals("selected", provider.getSelectedPlaylistId())
        assertEquals(1, localDataSource.selectedPlaylistIdCalls)
        assertEquals(0, localDataSource.allPlaylistsCalls)
    }

    @Test
    fun deleteSelectedPlaylistReassignsSelectionAndDeletesRelatedContent() = runTest {
        val selectedPlaylist = remotePlaylist(id = "selected", isSelected = true)
        val nextPlaylist = remotePlaylist(id = "next")
        val playlistRepository = FakePlaylistRepository(selectedPlaylist, nextPlaylist)
        val channelRepository = FakePlaylistChannelRepository()
        val favoriteRepository = FakeChannelFavoriteRepository()
        val useCase =
            DeletePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
                playlistChannelRepository = channelRepository,
                channelFavoriteRepository = favoriteRepository,
            )

        useCase(selectedPlaylist)

        assertEquals(listOf("delete:selected"), channelRepository.operations)
        assertEquals(listOf("selected"), favoriteRepository.deletedPlaylists)
        assertTrue(playlistRepository.getPlaylistById("next").isSelected)
    }
}

private class FakePlaylistLocalDataSource(
    private val playlists: List<PlaylistEntity>,
    private val selectedPlaylistId: String? = null,
) : PlaylistLocalDataSource {
    var allPlaylistsCalls = 0
    var selectedPlaylistIdCalls = 0

    override suspend fun getPlaylistById(id: String): PlaylistEntity =
        playlists.first { playlist -> playlist.id == id }

    override fun observePlaylists(): Flow<List<PlaylistEntity>> =
        flowOf(playlists)

    override suspend fun getAllPlaylists(): List<PlaylistEntity> {
        allPlaylistsCalls += 1
        return playlists
    }

    override suspend fun getSelectedPlaylistId(): String? {
        selectedPlaylistIdCalls += 1
        return selectedPlaylistId
    }

    override suspend fun getRemotePlaylistsWithUpdatePeriod(
        playlistType: String,
        noUpdatePeriod: Long,
    ): List<PlaylistEntity> =
        playlists.filter { playlist ->
            playlist.playlistType == playlistType && playlist.updatePeriod != noUpdatePeriod
        }

    override suspend fun deletePlaylist(id: String) = Unit

    override suspend fun savePlaylists(playlists: List<PlaylistEntity>) = Unit

    override suspend fun savePlaylist(playlist: PlaylistEntity) = Unit

    override suspend fun selectPlaylist(id: String) = Unit
}

private class FakePlaylistRepository(
    vararg initialPlaylists: Playlist,
    private val duePlaylists: List<Playlist>? = null,
) : PlaylistRepository {
    private val playlists = initialPlaylists.associateBy { playlist -> playlist.id }.toMutableMap()
    var getAllPlaylistsCalls = 0
    var savePlaylistsCalls = 0
    var selectedPlaylistId: String? =
        initialPlaylists.firstOrNull { playlist -> playlist.isSelected }?.id

    override suspend fun getPlaylistById(id: String): Playlist =
        requireNotNull(playlists[id])

    override fun observePlaylists(): Flow<List<Playlist>> =
        flowOf(playlists.values.toList())

    override suspend fun getAllPlaylists(): List<Playlist> {
        getAllPlaylistsCalls += 1
        return playlists.values.toList()
    }

    override suspend fun getDueRemotePlaylists(nowMillis: Long): List<Playlist> =
        duePlaylists ?: emptyList()

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlists.remove(playlist.id)
    }

    override suspend fun savePlaylists(playlists: List<Playlist>) {
        savePlaylistsCalls += 1
        playlists.forEach { playlist -> savePlaylist(playlist) }
    }

    override suspend fun savePlaylist(playlist: Playlist) {
        playlists[playlist.id] = playlist
    }

    override suspend fun selectPlaylist(id: String) {
        selectedPlaylistId = id
        playlists.keys.toList().forEach { playlistId ->
            playlists[playlistId] =
                requireNotNull(playlists[playlistId]).copy(isSelected = playlistId == id)
        }
    }
}

private class FakePlaylistChannelRepository : PlaylistChannelRepository {
    val operations = mutableListOf<String>()
    val loadedChannels = mutableMapOf<String, List<PlaylistChannel>>()

    override suspend fun savePlaylistChannels(channels: List<PlaylistChannel>) {
        operations += "save:${channels.firstOrNull()?.parentListId.orEmpty()}"
    }

    override suspend fun loadPlaylistChannels(source: PlaylistChannelSource): List<PlaylistChannel> {
        operations += "load:${source.parentListId}"
        return loadedChannels[source.parentListId].orEmpty()
    }

    override suspend fun loadPlaylistGroups(): List<String> = emptyList()

    override suspend fun loadPlaylistGroupCounts(): Map<String, Int> = emptyMap()

    override suspend fun loadPlaylistChannelsCount(): Int = 0

    override suspend fun loadPlaylistGroupChannelsCount(group: String): Int = 0

    override suspend fun loadChannelsById(): List<PlaylistChannel> = emptyList()

    override suspend fun loadAllChannels(): List<PlaylistChannel> = emptyList()

    override suspend fun loadPlaylistChannelsByUrls(urls: List<String>): List<PlaylistChannel> =
        emptyList()

    override suspend fun loadPlaylistGroupChannels(group: String): List<PlaylistChannel> =
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
        channelName: String,
        channelUrl: String,
        favoriteType: String,
    ) = Unit

    override suspend fun deleteChannelFromFavorite(channelUrl: String) = Unit

    override suspend fun loadSelectedFavoriteChannels(): List<FavoriteChannel> = emptyList()

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

private class FakePlaylistSyncStateRepository : PlaylistSyncStateRepository {
    var epgMarks = 0

    override suspend fun markChannelsEpgInfoUpdateRequired() {
        epgMarks += 1
    }
}

private fun remotePlaylist(
    id: String,
    name: String = "Playlist",
    source: String = "https://example.com/list.m3u",
    updatePeriod: Long = 1L,
    isSelected: Boolean = false,
) = Playlist(
    id = id,
    playlistName = name,
    playlistSource = source,
    playlistType = PlaylistType.REMOTE,
    updatePeriod = updatePeriod,
    isSelected = isSelected,
)

private fun localPlaylist(
    id: String,
    name: String = "Playlist",
    source: String = "file.m3u",
    isSelected: Boolean = false,
) = Playlist(
    id = id,
    playlistName = name,
    playlistSource = source,
    playlistType = PlaylistType.LOCAL,
    updatePeriod = 0L,
    isSelected = isSelected,
)

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

private fun entity(
    id: String,
    type: PlaylistType,
    updatePeriod: Long,
    lastUpdateDate: Long = 0L,
) = PlaylistEntity(
    id = id,
    playlistName = id,
    playlistSource = "source",
    playlistType = type.name,
    lastUpdateDate = lastUpdateDate,
    updatePeriod = updatePeriod,
    isSelected = false,
)
