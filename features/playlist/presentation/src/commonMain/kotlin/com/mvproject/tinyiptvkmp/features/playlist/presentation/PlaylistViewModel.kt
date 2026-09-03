/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist.presentation

import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.core.base.mvi.runCatchingSuspend
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.PlaylistContentCoordinator
import com.mvproject.tinyiptvkmp.features.playlist.presentation.nav.PlaylistNavigator
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import okio.use
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class PlaylistViewModel(
    @InjectedParam args: PlaylistDetailArgs,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val playlistContentCoordinator: PlaylistContentCoordinator,
) : MviViewModel<PlaylistState, PlaylistAction, Nothing>() {

    private val navigator: PlaylistNavigator by inject()
    private val id = args.playlistId

    override fun createStore() = createStore(
        initialState = PlaylistState(),
        invokeOnStart = { loadPlaylist(playlistId = id) },
    )

    override fun onIntent(intent: PlaylistAction) {
        when (intent) {
            is PlaylistAction.ImportLocalFile -> launch { importLocalPlaylistFile(file = intent.file) }
            PlaylistAction.NavigateBack -> launch { navigator.navigateUp() }
            PlaylistAction.SavePlaylist -> launch { savePlaylist() }
            is PlaylistAction.SetLocalUri -> setLocalPlaylistUri(
                name = intent.name,
                uri = intent.uri
            )

            is PlaylistAction.SetRemoteUrl -> setRemotePlaylistUrl(url = intent.url)
            is PlaylistAction.SetTitle -> setPlaylistTitle(title = intent.title)
            is PlaylistAction.SetUpdatePeriod -> setPlaylistUpdatePeriod(type = intent.period)
            PlaylistAction.UpdatePlaylist -> launch { updatePlaylist() }
        }
    }

    private suspend fun loadPlaylist(playlistId: String) {
        val playlist = getPlaylistUseCase(playlistId = playlistId)
        setState {
            copy(
                selectedId = playlist.id,
                playlistName = playlist.playlistName,
                playlistSource = playlist.playlistSource,
                playlistType = playlist.playlistType,
                isEdit = playlist.id.isNotBlank(),
                lastUpdateDate = playlist.lastUpdateDate,
                updatePeriod = playlist.updatePeriod.toInt(),
            )
        }
    }

    private suspend fun importLocalPlaylistFile(file: PlatformFile) {
        setState { copy(isImportingLocalFile = true) }

        runCatchingSuspend {
            file.copyToTemporaryPlaylistFile()
        }.onSuccess { uri ->
            setLocalPlaylistUri(
                name = file.name,
                uri = uri,
            )
        }.onFailure {
            logger.e(it) { "Failed to import local playlist file ${file.name}: ${it.message}" }
        }

        setState { copy(isImportingLocalFile = false) }
    }

    private fun setLocalPlaylistUri(name: String, uri: String) {
        setState {
            copy(
                playlistName = name,
                playlistSource = uri,
                playlistType = PlaylistType.LOCAL,
            )
        }
    }

    private fun setRemotePlaylistUrl(url: String) {
        setState {
            copy(
                playlistSource = url,
                playlistType = PlaylistType.REMOTE,
            )
        }
    }

    private fun setPlaylistUpdatePeriod(type: Int) {
        setState {
            copy(
                isComplete = false,
                updatePeriod = type
            )
        }
    }

    private fun setPlaylistTitle(title: String) {
        setState {
            copy(playlistName = title)
        }
    }

    private suspend fun updatePlaylist() {
        if (!state.value.isReadyToSave) return

        setState {
            copy(isSaving = true)
        }
        runCatchingSuspend {
            playlistContentCoordinator.updatePlaylistWithContent(state.value.toPlaylist())
        }.onSuccess {
            handleSaveCompleted()
        }.onFailure { throwable ->
            handleSaveFailure(throwable = throwable)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun savePlaylist() {
        if (!state.value.isReadyToSave) return

        val id = state.value.selectedId.ifEmpty { Uuid.random().toString() }
        setState {
            copy(
                selectedId = id,
                isSaving = true
            )
        }
        runCatchingSuspend {
            playlistContentCoordinator.createPlaylistWithContent(state.value.toPlaylist())
        }.onSuccess {
            handleSaveCompleted()
        }.onFailure { throwable ->
            handleSaveFailure(throwable = throwable)
        }
    }

    private fun handleSaveCompleted() {
        setState { copy(isComplete = true, isSaving = false) }
    }

    private fun handleSaveFailure(throwable: Throwable) {
        logger.e(throwable) { "Failed to save playlist" }
        setState { copy(isComplete = false, isSaving = false) }
    }
}

@OptIn(ExperimentalUuidApi::class)
private suspend fun PlatformFile.copyToTemporaryPlaylistFile(): String =
    withContext(Dispatchers.IO) {
        val target = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "tinyiptv-${Uuid.random()}-$name"
        val sourcePath = existingSystemPathOrNull()

        FileSystem.SYSTEM.sink(target).buffer().use { sink ->
            if (sourcePath != null) {
                FileSystem.SYSTEM.source(sourcePath).buffer().use { source ->
                    sink.writeAll(source)
                }
            } else {
                sink.write(readBytes())
            }
        }

        target.toString()
    }

private fun PlatformFile.existingSystemPathOrNull(): Path? =
    path
        ?.let { value -> runCatching { value.toPath() }.getOrNull() }
        ?.takeIf { candidate ->
            runCatching { FileSystem.SYSTEM.exists(candidate) }.getOrDefault(false)
        }
