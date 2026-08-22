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
import com.mvproject.tinyiptvkmp.core.common.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.common.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SavePlaylistUseCase
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PlaylistViewModel(
    args: AppRoutes.PlaylistDetail,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val savePlaylistUseCase: SavePlaylistUseCase,
) : ViewModel(),
    KoinComponent,
    MviCore<PlaylistUiState, PlaylistUiAction, PlaylistUiEffect> by mviCore(PlaylistUiState()) {

    private val logger by injectLogger()

    init {
        initPlaylist(playlistId = args.id)
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

        is PlaylistUiAction.SetRemoteUrl -> setRemotePlaylistUrl(url = uiAction.url)
        is PlaylistUiAction.SetTitle -> setPlaylistTitle(title = uiAction.title)
        is PlaylistUiAction.SetUpdatePeriod -> setPlaylistUpdatePeriod(type = uiAction.period)
        PlaylistUiAction.UpdatePlaylist -> updatePlaylist()
        PlaylistUiAction.NavigateBack -> viewModelScope.postUiEffect(PlaylistUiEffect.OnNavigateBack)
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
        updateUiState {
            copy(isSaving = true)
        }
        saveOrUpdatePlayList(isUpdate = true)
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun savePlaylist() {
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
                    savePlaylistUseCase(
                        playlist = playlist,
                        isUpdate = isUpdate
                    )
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
    val isEdit: Boolean = false,
    val isComplete: Boolean = false,
) {
    val isReadyToSave: Boolean
        get() = playlistName.isNotBlank() && playlistSource.isNotBlank()

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
    data class SetUpdatePeriod(val period: Int) : PlaylistUiAction
    data object SavePlaylist : PlaylistUiAction
    data object UpdatePlaylist : PlaylistUiAction
    data object NavigateBack : PlaylistUiAction
}

sealed interface PlaylistUiEffect {
    data object OnNavigateBack : PlaylistUiEffect
}
