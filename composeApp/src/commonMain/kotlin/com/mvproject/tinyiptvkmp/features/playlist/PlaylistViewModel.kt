/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.mvi.MVI
import com.mvproject.tinyiptvkmp.core.common.mvi.mvi
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SavePlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistContract.UiAction
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistContract.UiEffect
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistContract.UiState
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlaylistViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val savePlaylistUseCase: SavePlaylistUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    private val args = savedStateHandle.toRoute<AppRoutes.PlaylistDetail>()

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
                    isEdit = playlist.id != LONG_VALUE_ZERO,
                    lastUpdateDate = playlist.lastUpdateDate,
                    updatePeriod = playlist.updatePeriod.toInt(),
                )
            }
        }
    }

    override fun onAction(uiAction: UiAction) = when (uiAction) {
        UiAction.SavePlaylist -> savePlaylist()
        is UiAction.SetLocalUri -> setLocalPlaylistUri(
            name = uiAction.name,
            uri = uiAction.uri
        )

        is UiAction.SetRemoteUrl -> setRemotePlaylistUrl(url = uiAction.url)
        is UiAction.SetTitle -> setPlaylistTitle(title = uiAction.title)
        is UiAction.SetUpdatePeriod -> setPlaylistUpdatePeriod(type = uiAction.period)
        UiAction.UpdatePlaylist -> updatePlaylist()
        UiAction.NavigateBack -> viewModelScope.postUiEffect(UiEffect.NavigateBack)
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

    private fun savePlaylist() {
        updateUiState {
            copy(
                selectedId = Random.nextLong(),
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
                    Logger.e("testing saveOrUpdatePlayList isUpdate=$isUpdate, failure ${it.localizedMessage}")
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
