package com.mvproject.tinyiptvkmp

import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.DeletePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.MarkChannelsEpgInfoUpdateRequiredUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ReplacePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetRemotePlaylistsToRefreshUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistLastUpdateDateUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistResult
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RootViewModelTest {
    @Test
    fun createPlaylistReplacesContentAndMarksEpgWhenContentWasReplaced() = runTest {
        val createPlaylistUseCase = FakeCreatePlaylistUseCase()
        val replacePlaylistContentUseCase =
            FakeReplacePlaylistContentUseCase(contentReplaced = true)
        val updateLastUpdateDateUseCase = FakeUpdatePlaylistLastUpdateDateUseCase()
        val markEpgUseCase = FakeMarkChannelsEpgInfoUpdateRequiredUseCase()
        val viewModel =
            rootViewModel(
                createPlaylistUseCase = createPlaylistUseCase,
                replacePlaylistContentUseCase = replacePlaylistContentUseCase,
                updatePlaylistLastUpdateDateUseCase = updateLastUpdateDateUseCase,
                markChannelsEpgInfoUpdateRequiredUseCase = markEpgUseCase,
            )

        viewModel.createPlaylistWithContent(remotePlaylist(id = "playlist-1"))

        assertEquals(listOf("playlist-1"), createPlaylistUseCase.createdPlaylistIds)
        assertEquals(listOf("remote:playlist-1:true"), replacePlaylistContentUseCase.replacements)
        assertEquals(listOf("playlist-1"), updateLastUpdateDateUseCase.updatedPlaylistIds)
        assertEquals(1, markEpgUseCase.markCount)
    }

    @Test
    fun createLocalPlaylistUsesLocalContentReplacement() = runTest {
        val replacePlaylistContentUseCase =
            FakeReplacePlaylistContentUseCase(contentReplaced = true)
        val updateLastUpdateDateUseCase = FakeUpdatePlaylistLastUpdateDateUseCase()
        val viewModel =
            rootViewModel(
                replacePlaylistContentUseCase = replacePlaylistContentUseCase,
                updatePlaylistLastUpdateDateUseCase = updateLastUpdateDateUseCase,
            )

        viewModel.createPlaylistWithContent(localPlaylist(id = "playlist-1"))

        assertEquals(listOf("local:playlist-1:true"), replacePlaylistContentUseCase.replacements)
        assertEquals(emptyList(), updateLastUpdateDateUseCase.updatedPlaylistIds)
    }

    @Test
    fun updatePlaylistSkipsContentWhenMetadataDoesNotRequireRefresh() = runTest {
        val replacePlaylistContentUseCase =
            FakeReplacePlaylistContentUseCase(contentReplaced = true)
        val markEpgUseCase = FakeMarkChannelsEpgInfoUpdateRequiredUseCase()
        val viewModel =
            rootViewModel(
                updatePlaylistUseCase = FakeUpdatePlaylistUseCase(
                    result = UpdatePlaylistResult(
                        playlist = remotePlaylist(id = "playlist-1"),
                        contentRefreshRequired = false,
                    ),
                ),
                replacePlaylistContentUseCase = replacePlaylistContentUseCase,
                markChannelsEpgInfoUpdateRequiredUseCase = markEpgUseCase,
            )

        viewModel.updatePlaylistWithContent(remotePlaylist(id = "playlist-1"))

        assertEquals(emptyList(), replacePlaylistContentUseCase.replacements)
        assertEquals(0, markEpgUseCase.markCount)
    }

    @Test
    fun updatePlaylistReplacesContentOnlyWhenMetadataRequestsRefresh() = runTest {
        val replacePlaylistContentUseCase =
            FakeReplacePlaylistContentUseCase(contentReplaced = true)
        val updateLastUpdateDateUseCase = FakeUpdatePlaylistLastUpdateDateUseCase()
        val markEpgUseCase = FakeMarkChannelsEpgInfoUpdateRequiredUseCase()
        val viewModel =
            rootViewModel(
                updatePlaylistUseCase = FakeUpdatePlaylistUseCase(
                    result = UpdatePlaylistResult(
                        playlist = remotePlaylist(id = "playlist-1"),
                        contentRefreshRequired = true,
                    ),
                ),
                replacePlaylistContentUseCase = replacePlaylistContentUseCase,
                updatePlaylistLastUpdateDateUseCase = updateLastUpdateDateUseCase,
                markChannelsEpgInfoUpdateRequiredUseCase = markEpgUseCase,
            )

        viewModel.updatePlaylistWithContent(remotePlaylist(id = "playlist-1"))

        assertEquals(listOf("remote:playlist-1:true"), replacePlaylistContentUseCase.replacements)
        assertEquals(listOf("playlist-1"), updateLastUpdateDateUseCase.updatedPlaylistIds)
        assertEquals(1, markEpgUseCase.markCount)
    }

    @Test
    fun deletePlaylistDeletesMetadataAndContent() = runTest {
        val deletePlaylistUseCase = FakeDeletePlaylistUseCase()
        val deletePlaylistContentUseCase = FakeDeletePlaylistContentUseCase()
        val viewModel =
            rootViewModel(
                deletePlaylistUseCase = deletePlaylistUseCase,
                deletePlaylistContentUseCase = deletePlaylistContentUseCase,
            )

        viewModel.deletePlaylistWithContent(remotePlaylist(id = "playlist-1"))

        assertEquals(listOf("playlist-1"), deletePlaylistUseCase.deletedPlaylistIds)
        assertEquals(listOf("playlist-1"), deletePlaylistContentUseCase.deletedPlaylistIds)
    }

    @Test
    fun refreshRemotePlaylistContentMarksEpgAfterSuccessfulReplacements() = runTest {
        val replacePlaylistContentUseCase =
            FakeReplacePlaylistContentUseCase(contentReplaced = true)
        val updateLastUpdateDateUseCase = FakeUpdatePlaylistLastUpdateDateUseCase()
        val markEpgUseCase = FakeMarkChannelsEpgInfoUpdateRequiredUseCase()
        val viewModel =
            rootViewModel(
                getRemotePlaylistsToRefreshUseCase = FakeGetRemotePlaylistsToRefreshUseCase(
                    playlists = listOf(remotePlaylist(id = "playlist-1")),
                ),
                replacePlaylistContentUseCase = replacePlaylistContentUseCase,
                updatePlaylistLastUpdateDateUseCase = updateLastUpdateDateUseCase,
                markChannelsEpgInfoUpdateRequiredUseCase = markEpgUseCase,
            )

        viewModel.refreshRemotePlaylistContent()

        assertEquals(listOf("remote:playlist-1:false"), replacePlaylistContentUseCase.replacements)
        assertEquals(listOf("playlist-1"), updateLastUpdateDateUseCase.updatedPlaylistIds)
        assertEquals(1, markEpgUseCase.markCount)
    }
}

private fun rootViewModel(
    createPlaylistUseCase: CreatePlaylistUseCase = FakeCreatePlaylistUseCase(),
    updatePlaylistUseCase: UpdatePlaylistUseCase = FakeUpdatePlaylistUseCase(),
    deletePlaylistUseCase: DeletePlaylistUseCase = FakeDeletePlaylistUseCase(),
    getRemotePlaylistsToRefreshUseCase: GetRemotePlaylistsToRefreshUseCase =
        FakeGetRemotePlaylistsToRefreshUseCase(),
    updatePlaylistLastUpdateDateUseCase: UpdatePlaylistLastUpdateDateUseCase =
        FakeUpdatePlaylistLastUpdateDateUseCase(),
    replacePlaylistContentUseCase: ReplacePlaylistContentUseCase = FakeReplacePlaylistContentUseCase(),
    deletePlaylistContentUseCase: DeletePlaylistContentUseCase = FakeDeletePlaylistContentUseCase(),
    markChannelsEpgInfoUpdateRequiredUseCase: MarkChannelsEpgInfoUpdateRequiredUseCase =
        FakeMarkChannelsEpgInfoUpdateRequiredUseCase(),
) = RootViewModel(
    createPlaylistUseCase = createPlaylistUseCase,
    updatePlaylistUseCase = updatePlaylistUseCase,
    deletePlaylistUseCase = deletePlaylistUseCase,
    getRemotePlaylistsToRefreshUseCase = getRemotePlaylistsToRefreshUseCase,
    updatePlaylistLastUpdateDateUseCase = updatePlaylistLastUpdateDateUseCase,
    replacePlaylistContentUseCase = replacePlaylistContentUseCase,
    deletePlaylistContentUseCase = deletePlaylistContentUseCase,
    markChannelsEpgInfoUpdateRequiredUseCase = markChannelsEpgInfoUpdateRequiredUseCase,
)

private class FakeCreatePlaylistUseCase : CreatePlaylistUseCase {
    val createdPlaylistIds = mutableListOf<String>()

    override suspend fun invoke(playlist: Playlist): Playlist {
        createdPlaylistIds += playlist.id
        return playlist
    }
}

private class FakeUpdatePlaylistUseCase(
    private val result: UpdatePlaylistResult = UpdatePlaylistResult(
        playlist = remotePlaylist(id = "playlist-1"),
        contentRefreshRequired = false,
    ),
) : UpdatePlaylistUseCase {
    override suspend fun invoke(playlist: Playlist): UpdatePlaylistResult = result
}

private class FakeDeletePlaylistUseCase : DeletePlaylistUseCase {
    val deletedPlaylistIds = mutableListOf<String>()

    override suspend fun invoke(playlist: Playlist) {
        deletedPlaylistIds += playlist.id
    }
}

private class FakeGetRemotePlaylistsToRefreshUseCase(
    private val playlists: List<Playlist> = emptyList(),
) : GetRemotePlaylistsToRefreshUseCase {
    override suspend fun invoke(nowMillis: Long): List<Playlist> = playlists
}

private class FakeUpdatePlaylistLastUpdateDateUseCase : UpdatePlaylistLastUpdateDateUseCase {
    val updatedPlaylistIds = mutableListOf<String>()

    override suspend fun invoke(playlistId: String, lastUpdateDate: Long) {
        updatedPlaylistIds += playlistId
    }
}

private class FakeReplacePlaylistContentUseCase(
    private val contentReplaced: Boolean = false,
) : ReplacePlaylistContentUseCase {
    val replacements = mutableListOf<String>()

    override suspend fun replaceLocalPlaylistContent(
        playlistId: String,
        source: String,
        clearExistingContentBeforeLoading: Boolean,
    ): Boolean {
        replacements += "local:$playlistId:$clearExistingContentBeforeLoading"
        return contentReplaced
    }

    override suspend fun replaceRemotePlaylistContent(
        playlistId: String,
        source: String,
        clearExistingContentBeforeLoading: Boolean,
    ): Boolean {
        replacements += "remote:$playlistId:$clearExistingContentBeforeLoading"
        return contentReplaced
    }
}

private class FakeDeletePlaylistContentUseCase : DeletePlaylistContentUseCase {
    val deletedPlaylistIds = mutableListOf<String>()

    override suspend fun invoke(playlistId: String) {
        deletedPlaylistIds += playlistId
    }
}

private class FakeMarkChannelsEpgInfoUpdateRequiredUseCase :
    MarkChannelsEpgInfoUpdateRequiredUseCase {
    var markCount = 0

    override suspend fun invoke() {
        markCount += 1
    }
}

private fun remotePlaylist(
    id: String,
) = Playlist(
    id = id,
    playlistName = "Playlist",
    playlistSource = "https://example.com/list.m3u",
    playlistType = PlaylistType.REMOTE,
    updatePeriod = 1L,
)

private fun localPlaylist(
    id: String,
) = Playlist(
    id = id,
    playlistName = "Playlist",
    playlistSource = "/tmp/list.m3u",
    playlistType = PlaylistType.LOCAL,
    updatePeriod = 0L,
)
