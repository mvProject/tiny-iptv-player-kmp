package com.mvproject.tinyiptvkmp.features.playlist.api

import com.mvproject.tinyiptvkmp.features.playlist.api.data.local.PlaylistLocalDataSource
import com.mvproject.tinyiptvkmp.features.playlist.api.data.repository.PlaylistRepositoryImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistLastUpdateDateUseCaseImpl
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCaseImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class PlaylistUseCaseTest {
    @Test
    fun createFirstPlaylistSelectsItAndReturnsSavedPlaylist() = runTest {
        val playlistRepository = FakePlaylistRepository()
        val useCase =
            CreatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
            )

        val savedPlaylist = useCase(remotePlaylist(id = "playlist-1"))

        assertTrue(savedPlaylist.isSelected)
        assertEquals(0L, savedPlaylist.lastUpdateDate)
        assertEquals(savedPlaylist, playlistRepository.getPlaylistById("playlist-1"))
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
        val useCase =
            UpdatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
            )

        val result =
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
        assertFalse(result.contentRefreshRequired)
    }

    @Test
    fun remoteNameAndPeriodUpdateDoesNotRequestContentRefresh() = runTest {
        val existing =
            remotePlaylist(
                id = "playlist-1",
                name = "Old",
                source = "https://old.example/list.m3u",
                updatePeriod = 1L,
                isSelected = true,
            )
        val playlistRepository = FakePlaylistRepository(existing)
        val useCase =
            UpdatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
            )

        val result =
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
        assertFalse(result.contentRefreshRequired)
    }

    @Test
    fun remoteUrlUpdateRequestsContentRefresh() = runTest {
        val playlistRepository =
            FakePlaylistRepository(
                remotePlaylist(
                    id = "playlist-1",
                    source = "https://old.example/list.m3u",
                    isSelected = true,
                ),
            )
        val useCase =
            UpdatePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
            )

        val result =
            useCase(
                remotePlaylist(
                    id = "playlist-1",
                    source = "https://new.example/list.m3u",
                ),
            )

        val updatedPlaylist = playlistRepository.getPlaylistById("playlist-1")
        assertEquals("https://new.example/list.m3u", updatedPlaylist.playlistSource)
        assertTrue(updatedPlaylist.isSelected)
        assertEquals(updatedPlaylist, result.playlist)
        assertTrue(result.contentRefreshRequired)
    }

    @Test
    fun playlistRepositoryReturnsRemotePlaylistsToRefresh() = runTest {
        val now = 10.hours.inWholeMilliseconds
        val repository =
            PlaylistRepositoryImpl(
                FakePlaylistLocalDataSource(
                    listOf(
                        entity(id = "refresh", type = PlaylistType.REMOTE, updatePeriod = 1L),
                        entity(
                            id = "fresh",
                            type = PlaylistType.REMOTE,
                            updatePeriod = 1L,
                            lastUpdateDate = now - 1.hours.inWholeMilliseconds,
                        ),
                        entity(id = "disabled", type = PlaylistType.REMOTE, updatePeriod = 0L),
                        entity(id = "local", type = PlaylistType.LOCAL, updatePeriod = 1L),
                    ),
                ),
            )

        val playlistIds = repository.getRemotePlaylistsToRefresh(nowMillis = now).map { it.id }

        assertEquals(listOf("refresh"), playlistIds)
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
    fun playlistRepositoryUsesLocalSelectedIdQuery() = runTest {
        val localDataSource =
            FakePlaylistLocalDataSource(
                listOf(
                    entity(id = "old", type = PlaylistType.REMOTE, updatePeriod = 1L),
                    entity(id = "selected", type = PlaylistType.REMOTE, updatePeriod = 1L),
                ),
                selectedPlaylistId = "selected",
            )
        val repository = PlaylistRepositoryImpl(localDataSource = localDataSource)

        assertEquals("selected", repository.getSelectedPlaylistId())
        assertEquals(1, localDataSource.selectedPlaylistIdCalls)
        assertEquals(0, localDataSource.allPlaylistsCalls)
    }

    @Test
    fun deleteSelectedPlaylistReassignsSelectionAndDeletesOnlyMetadata() = runTest {
        val selectedPlaylist = remotePlaylist(id = "selected", isSelected = true)
        val nextPlaylist = remotePlaylist(id = "next")
        val playlistRepository = FakePlaylistRepository(selectedPlaylist, nextPlaylist)
        val useCase =
            DeletePlaylistUseCaseImpl(
                playlistRepository = playlistRepository,
            )

        useCase(selectedPlaylist)

        assertTrue(playlistRepository.getPlaylistById("next").isSelected)
    }

    @Test
    fun updateLastUpdateDateUseCaseUpdatesOnlyTimestamp() = runTest {
        val playlistRepository = FakePlaylistRepository(remotePlaylist(id = "playlist-1"))
        val useCase =
            UpdatePlaylistLastUpdateDateUseCaseImpl(
                playlistRepository = playlistRepository,
            )

        useCase(playlistId = "playlist-1", lastUpdateDate = 123L)

        assertEquals(123L, playlistRepository.getPlaylistById("playlist-1").lastUpdateDate)
    }
}

private class FakePlaylistLocalDataSource(
    private val playlists: List<Playlist>,
    private val selectedPlaylistId: String? = null,
) : PlaylistLocalDataSource {
    var allPlaylistsCalls = 0
    var selectedPlaylistIdCalls = 0

    override suspend fun getPlaylistById(id: String): Playlist =
        playlists.first { playlist -> playlist.id == id }

    override fun observePlaylists(): Flow<List<Playlist>> =
        flowOf(playlists)

    override suspend fun getAllPlaylists(): List<Playlist> {
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
    ): List<Playlist> =
        playlists.filter { playlist ->
            playlist.playlistType == PlaylistType.valueOf(playlistType) && playlist.updatePeriod != noUpdatePeriod
        }

    override suspend fun deletePlaylist(id: String) = Unit

    override suspend fun savePlaylists(playlists: List<Playlist>) = Unit

    override suspend fun savePlaylist(playlist: Playlist) = Unit

    override suspend fun selectPlaylist(id: String) = Unit
}

private class FakePlaylistRepository(
    vararg initialPlaylists: Playlist,
    private val playlistsToRefresh: List<Playlist>? = null,
) : PlaylistRepository {
    private val playlists = initialPlaylists.associateBy { playlist -> playlist.id }.toMutableMap()
    var getAllPlaylistsCalls = 0
    var savePlaylistsCalls = 0
    var selectedPlaylistId: String? =
        initialPlaylists.firstOrNull { playlist -> playlist.isSelected }?.id

    override suspend fun getPlaylistById(id: String): Playlist =
        requireNotNull(playlists[id])

    override suspend fun getSelectedPlaylistId(): String? = selectedPlaylistId

    override fun observePlaylists(): Flow<List<Playlist>> =
        flowOf(playlists.values.toList())

    override suspend fun getAllPlaylists(): List<Playlist> {
        getAllPlaylistsCalls += 1
        return playlists.values.toList()
    }

    override suspend fun getRemotePlaylistsToRefresh(nowMillis: Long): List<Playlist> =
        playlistsToRefresh ?: emptyList()

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

private fun entity(
    id: String,
    type: PlaylistType,
    updatePeriod: Long,
    lastUpdateDate: Long = 0L,
) = Playlist(
    id = id,
    playlistName = id,
    playlistSource = "source",
    playlistType = PlaylistType.valueOf(type.name),
    lastUpdateDate = lastUpdateDate,
    updatePeriod = updatePeriod,
    isSelected = false,
)
