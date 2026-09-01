package com.mvproject.tinyiptvkmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.runCatchingSuspend
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.DeletePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.MarkChannelsEpgInfoUpdateRequiredUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ReplacePlaylistContentUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.DeletePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetRemotePlaylistsToRefreshUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistLastUpdateDateUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCase
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.time.Clock

class RootViewModel(
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val getRemotePlaylistsToRefreshUseCase: GetRemotePlaylistsToRefreshUseCase,
    private val updatePlaylistLastUpdateDateUseCase: UpdatePlaylistLastUpdateDateUseCase,
    private val replacePlaylistContentUseCase: ReplacePlaylistContentUseCase,
    private val deletePlaylistContentUseCase: DeletePlaylistContentUseCase,
    private val markChannelsEpgInfoUpdateRequiredUseCase: MarkChannelsEpgInfoUpdateRequiredUseCase,
) : ViewModel(), KoinComponent {
    private val logger by injectLogger()

    fun refreshRemotePlaylistContentOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingSuspend {
                refreshRemotePlaylistContent()
            }.onFailure { throwable ->
                logger.e(throwable) { "Failed to refresh remote playlist content" }
            }
        }
    }

    suspend fun createPlaylistWithContent(playlist: Playlist) {
        val savedPlaylist = createPlaylistUseCase(playlist = playlist)
        replaceContentAndUpdateSyncState(
            playlist = savedPlaylist,
            clearExistingContentBeforeLoading = true,
        )
    }

    suspend fun updatePlaylistWithContent(playlist: Playlist) {
        val result = updatePlaylistUseCase(playlist = playlist)
        if (!result.contentRefreshRequired) return

        replaceContentAndUpdateSyncState(
            playlist = result.playlist,
            clearExistingContentBeforeLoading = true,
        )
    }

    suspend fun deletePlaylistWithContent(playlist: Playlist) {
        deletePlaylistUseCase(playlist = playlist)
        deletePlaylistContentUseCase(playlistId = playlist.id)
    }

    suspend fun refreshRemotePlaylistContent() {
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
