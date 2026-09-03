package com.mvproject.tinyiptvkmp.playlist

import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.DeletePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.MarkChannelsEpgInfoUpdateRequiredUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ReplacePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetRemotePlaylistsToRefreshUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.PlaylistContentCoordinator
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistLastUpdateDateUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCase
import kotlin.time.Clock

internal class DefaultPlaylistContentCoordinator(
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val getRemotePlaylistsToRefreshUseCase: GetRemotePlaylistsToRefreshUseCase,
    private val updatePlaylistLastUpdateDateUseCase: UpdatePlaylistLastUpdateDateUseCase,
    private val replacePlaylistContentUseCase: ReplacePlaylistContentUseCase,
    private val deletePlaylistContentUseCase: DeletePlaylistContentUseCase,
    private val markChannelsEpgInfoUpdateRequiredUseCase: MarkChannelsEpgInfoUpdateRequiredUseCase,
) : PlaylistContentCoordinator {

    override suspend fun createPlaylistWithContent(playlist: Playlist) {
        val savedPlaylist = createPlaylistUseCase(playlist = playlist)
        replaceContentAndUpdateSyncState(
            playlist = savedPlaylist,
            clearExistingContentBeforeLoading = true,
        )
    }

    override suspend fun updatePlaylistWithContent(playlist: Playlist) {
        val result = updatePlaylistUseCase(playlist = playlist)
        if (!result.contentRefreshRequired) return

        replaceContentAndUpdateSyncState(
            playlist = result.playlist,
            clearExistingContentBeforeLoading = true,
        )
    }

    override suspend fun deletePlaylistWithContent(playlist: Playlist) {
        deletePlaylistUseCase(playlist = playlist)
        deletePlaylistContentUseCase(playlistId = playlist.id)
    }

    override suspend fun refreshRemotePlaylistContent() {
        val currentDate = Clock.System.now().toEpochMilliseconds()
        var isEpgInfoUpdateRequired = false

        getRemotePlaylistsToRefreshUseCase(nowMillis = currentDate).forEach { playlist ->
            val contentReplaced =
                replacePlaylistContentUseCase.replaceRemotePlaylistContent(
                    playlistId = playlist.id,
                    source = playlist.playlistSource,
                    clearExistingContentBeforeLoading = false,
                )

            if (contentReplaced) {
                updatePlaylistLastUpdateDateUseCase(
                    playlistId = playlist.id,
                    lastUpdateDate = currentDate,
                )
                isEpgInfoUpdateRequired = true
            }
        }

        if (isEpgInfoUpdateRequired) {
            markChannelsEpgInfoUpdateRequiredUseCase()
        }
    }

    private suspend fun replaceContentAndUpdateSyncState(
        playlist: Playlist,
        clearExistingContentBeforeLoading: Boolean,
    ) {
        val contentReplaced = replaceContent(
            playlist = playlist,
            clearExistingContentBeforeLoading = clearExistingContentBeforeLoading,
        )

        if (!contentReplaced) return

        markChannelsEpgInfoUpdateRequiredUseCase()
        if (playlist.playlistType == PlaylistType.REMOTE) {
            updatePlaylistLastUpdateDateUseCase(playlistId = playlist.id)
        }
    }

    private suspend fun replaceContent(
        playlist: Playlist,
        clearExistingContentBeforeLoading: Boolean,
    ): Boolean =
        when (playlist.playlistType) {
            PlaylistType.LOCAL ->
                replacePlaylistContentUseCase.replaceLocalPlaylistContent(
                    playlistId = playlist.id,
                    source = playlist.playlistSource,
                    clearExistingContentBeforeLoading = clearExistingContentBeforeLoading,
                )

            PlaylistType.REMOTE ->
                replacePlaylistContentUseCase.replaceRemotePlaylistContent(
                    playlistId = playlist.id,
                    source = playlist.playlistSource,
                    clearExistingContentBeforeLoading = clearExistingContentBeforeLoading,
                )
        }
}
