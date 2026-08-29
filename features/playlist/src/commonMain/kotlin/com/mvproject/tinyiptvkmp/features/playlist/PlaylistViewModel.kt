/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.foundation.common.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.model.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.CreatePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdatePlaylistUseCase
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import okio.use
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class PlaylistDetailArgs(
    val playlistId: String,
)

class PlaylistViewModel(
    @InjectedParam args: PlaylistDetailArgs,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
) : ViewModel(),
    KoinComponent,
    MviCore<PlaylistUiState, PlaylistUiAction, PlaylistUiEffect> by mviCore(PlaylistUiState()) {

    private val logger by injectLogger()

    init {
        initPlaylist(playlistId = args.playlistId)
    }

    private fun initPlaylist(playlistId: String) {
        viewModelScope.launch {
            val playlist = getPlaylistUseCase(playlistId = playlistId)

            updateUiState {
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
    }

    override fun onAction(uiAction: PlaylistUiAction) = when (uiAction) {
        PlaylistUiAction.SavePlaylist -> savePlaylist()
        is PlaylistUiAction.SetLocalUri -> setLocalPlaylistUri(
            name = uiAction.name,
            uri = uiAction.uri
        )

        is PlaylistUiAction.ImportLocalFile -> importLocalPlaylistFile(file = uiAction.file)
        is PlaylistUiAction.SetRemoteUrl -> setRemotePlaylistUrl(url = uiAction.url)
        is PlaylistUiAction.SetTitle -> setPlaylistTitle(title = uiAction.title)
        is PlaylistUiAction.SetUpdatePeriod -> setPlaylistUpdatePeriod(type = uiAction.period)
        PlaylistUiAction.UpdatePlaylist -> updatePlaylist()
        PlaylistUiAction.NavigateBack -> viewModelScope.postUiEffect(PlaylistUiEffect.OnNavigateBack)
    }

    private fun importLocalPlaylistFile(file: PlatformFile) {
        updateUiState {
            copy(isImportingLocalFile = true)
        }

        viewModelScope.launch {
            runCatching {
                file.copyToTemporaryPlaylistFile()
            }.onSuccess { uri ->
                setLocalPlaylistUri(
                    name = file.name,
                    uri = uri,
                )
            }.onFailure {
                logger.e(it) { "Failed to import local playlist file ${file.name}: ${it.message}" }
            }

            updateUiState {
                copy(isImportingLocalFile = false)
            }
        }
    }

    private fun setLocalPlaylistUri(name: String, uri: String) {
        updateUiState {
            copy(
                playlistName = name,
                playlistSource = uri,
                playlistType = PlaylistType.LOCAL,
            )
        }
    }

    private fun setRemotePlaylistUrl(url: String) {
        updateUiState {
            copy(
                playlistSource = url,
                playlistType = PlaylistType.REMOTE,
            )
        }
    }

    private fun setPlaylistUpdatePeriod(type: Int) {
        updateUiState {
            copy(
                isComplete = false,
                updatePeriod = type
            )
        }
    }

    private fun setPlaylistTitle(title: String) {
        updateUiState {
            copy(playlistName = title)
        }
    }

    private fun updatePlaylist() {
        if (!uiState.value.isReadyToSave) return

        updateUiState {
            copy(isSaving = true)
        }
        saveOrUpdatePlayList(isUpdate = true)
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun savePlaylist() {
        if (!uiState.value.isReadyToSave) return

        updateUiState {
            copy(
                selectedId = uiState.value.selectedId.ifEmpty { Uuid.random().toString() },
                isSaving = true
            )
        }
        saveOrUpdatePlayList()
    }

    private fun saveOrUpdatePlayList(isUpdate: Boolean = false) {
        viewModelScope.launch {
            val playlist = uiState.value.toPlaylist()

            val result =
                runCatching {
                    if (isUpdate) {
                        updatePlaylistUseCase(playlist = playlist)
                    } else {
                        createPlaylistUseCase(playlist = playlist)
                    }
                }.onFailure {
                    logger.e(it) { "testing saveOrUpdatePlayList isUpdate=$isUpdate, failure ${it.message}" }
                }

            updateUiState {
                copy(
                    isComplete = result.isSuccess,
                    isSaving = false
                )
            }
        }
    }
}

@Immutable
data class PlaylistUiState(
    val selectedId: String = String.empty,
    val playlistName: String = String.empty,
    val playlistSource: String = String.empty,
    val playlistType: PlaylistType = PlaylistType.REMOTE,
    val updatePeriod: Int = UpdatePeriod.NO_UPDATE.value,
    val lastUpdateDate: Long = LONG_VALUE_ZERO,
    val isSaving: Boolean = false,
    val isImportingLocalFile: Boolean = false,
    val isEdit: Boolean = false,
    val isComplete: Boolean = false,
) {
    val isReadyToSave: Boolean
        get() = playlistName.isNotBlank() &&
                playlistSource.isNotBlank() &&
                !isSaving &&
                !isImportingLocalFile

    fun toPlaylist() =
        with(this) {
            Playlist(
                id = selectedId,
                playlistName = playlistName,
                playlistSource = playlistSource,
                playlistType = playlistType,
                lastUpdateDate = lastUpdateDate,
                updatePeriod = updatePeriod.toLong(),
            )
        }
}

sealed interface PlaylistUiAction {
    data class SetTitle(val title: String) : PlaylistUiAction
    data class SetRemoteUrl(val url: String) : PlaylistUiAction
    data class SetLocalUri(val name: String, val uri: String) : PlaylistUiAction
    data class ImportLocalFile(val file: PlatformFile) : PlaylistUiAction
    data class SetUpdatePeriod(val period: Int) : PlaylistUiAction
    data object SavePlaylist : PlaylistUiAction
    data object UpdatePlaylist : PlaylistUiAction
    data object NavigateBack : PlaylistUiAction
}

sealed interface PlaylistUiEffect {
    data object OnNavigateBack : PlaylistUiEffect
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
